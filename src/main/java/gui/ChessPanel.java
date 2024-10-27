package main.java.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.JPanel;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.board.PieceType;
import main.java.move.Move;
import main.java.move.MoveType;
import main.java.network.Client;
import main.java.network.GameState;

import java.util.List;

public class ChessPanel extends JPanel{

    public boolean isWhite = true;
    public boolean isMyTurn = false;
    public Board board;
    public List<Move> legalMoves;
    public List<Move> psuedoLegalMoves;

    public static Image boardImage;
    public static BufferedImage allPieces;
    public static EnumMap<PieceType,Image> pieceImages = new EnumMap<>(PieceType.class);
    public static final String BG_PATH = "src\\main\\assets\\boards\\bg_brown.png";
    public static final String PIECES_PATH = "src\\main\\assets\\pieces\\pieces.png";

    public PieceType selectedPiece = PieceType.EMPTY;
    public int[] dragCoor = new int[]{-1, -1};
    public int[] recentMove = new int[]{-1, -1};
    public List<Move> moveHints = new ArrayList<>();
    public List<Move> nextMoveHints = new ArrayList<>();


    public boolean sendMoveToClient = false;
    public boolean singlePlayer = false;
    public Client client;
    public SinglePlayerServer singlePlayerServer;
    HashMap<MoveType, File> moveTypeSoundMap;


    int toSquare = -1;
    int fromSquare = -1;

    public Move bestMove = null;

    public ChessPanel(Board board) throws IOException{
        this.board = board;
        this.legalMoves =  new ArrayList<>(); 
        this.psuedoLegalMoves = new ArrayList<>();

        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);

        boardImage = ImageIO.read(new File(BG_PATH));
        allPieces = ImageIO.read(new File(PIECES_PATH));
        boardImage = boardImage.getScaledInstance(800, 800, BufferedImage.SCALE_SMOOTH);
        
        this.moveTypeSoundMap = new HashMap<>();
        moveTypeSoundMap.put(MoveType.DEFAULT, new File("src\\main\\assets\\sounds\\DEFAULT.wav"));
        moveTypeSoundMap.put(MoveType.CAPTURE, new File("src\\main\\assets\\sounds\\CAPTURE.wav"));
        moveTypeSoundMap.put(MoveType.CASTLE, new File("src\\main\\assets\\sounds\\CASTLE.wav"));
        moveTypeSoundMap.put(MoveType.CHECK, new File("src\\main\\assets\\sounds\\CHECK.wav"));
        moveTypeSoundMap.put(MoveType.PROMOTION, new File("src\\main\\assets\\sounds\\PROMOTION.wav"));


        // Slice the allPieces image into individual piece images
        PieceType[] pieceLetters = new PieceType[]{
            PieceType.WHITE_KING, PieceType.WHITE_QUEEN, PieceType.WHITE_BISHOP, PieceType.WHITE_KNIGHT, PieceType.WHITE_ROOK, PieceType.WHITE_PAWN,
            PieceType.BLACK_KING, PieceType.BLACK_QUEEN, PieceType.BLACK_BISHOP, PieceType.BLACK_KNIGHT, PieceType.BLACK_ROOK, PieceType.BLACK_PAWN
        };
        int ind = 0;
        for (int y = 0; y < 400; y += 200) {
            for (int x = 0; x < 1200; x += 200) {
                pieceImages.put(pieceLetters[ind],allPieces.getSubimage(x, y, 200, 200).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH));
                ind++;
                
            }
        }
    }

    public void updateChessPanel(GameState gameState){
        this.board = gameState.board;
        this.isWhite = gameState.isWhite;
        this.isMyTurn = board.IS_WHITE_TURN == isWhite;
        this.legalMoves = gameState.currentLegalMoves;
        this.psuedoLegalMoves = gameState.enemyPsuedoLegalMoves;
        if(gameState.recentMove != null){
            this.recentMove[0] = gameState.recentMove.fromSquare();
            this.recentMove[1] = gameState.recentMove.toSquare();
            playSound(gameState.recentMove.moveType());

        }
        repaint();
    }

    public void resetChessPanel(){
        fromSquare = -1;
        toSquare = -1;
        moveHints = new ArrayList<>();
        nextMoveHints = new ArrayList<>();
    }
    
    public void playSound(MoveType moveType) {
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(moveTypeSoundMap.get(moveType));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class ClickListener extends MouseAdapter{

        @Override
        public void mousePressed(MouseEvent e){
            fromSquare = BoardUtil.rowColToSquare(e.getY()/100, e.getX()/100, isWhite);
            selectedPiece = BoardUtil.getPieceTypeAtSquare(board, fromSquare);
            List<Move> hints = new ArrayList<>();
            

            if(isMyTurn){
                for(Move move: legalMoves){
                    if(move.fromSquare() == fromSquare){
                        hints.add(move);
                    }
                }
                moveHints = hints;
            }else{
                for(Move move: psuedoLegalMoves){
                    if(move.fromSquare() == fromSquare){
                        hints.add(move);
                    }
                }
                nextMoveHints = hints;
            }

            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
            selectedPiece = PieceType.EMPTY;
            toSquare = BoardUtil.rowColToSquare(e.getY()/100, e.getX()/100, isWhite);

            if(isMyTurn){
                for(Move move: legalMoves){
                    if(move.toSquare() == toSquare && move.fromSquare() == fromSquare){
                        if(sendMoveToClient){
                            client.sendMessage(move);
                        }
                        if(singlePlayer){
                            singlePlayerServer.update(move);
                        }
                        break;
                    }
                }
            }

            resetChessPanel();
            repaint();
        }
    }

    private class DragListener extends MouseMotionAdapter{

        @Override
        public void mouseMoved(MouseEvent e){
            dragCoor[0] = e.getX();
            dragCoor[1] = e.getY();

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e){
            dragCoor[0] = e.getX();
            dragCoor[1] = e.getY();

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // board
        g.drawImage(boardImage, 0, 0, 800, 800, null);


        // recent move
        g.setColor(new Color(155, 252, 114,100));
        if(!this.isWhite){
            g.fillRect(700 - (recentMove[0] % 8 * 100), 700 - (recentMove[0] / 8 * 100),100,100);
            g.fillRect(700 - (recentMove[1] % 8 * 100), 700 - (recentMove[1] / 8 * 100),100,100);

        }else{
            g.fillRect(recentMove[0] % 8 * 100, recentMove[0] / 8 * 100,100,100);
            g.fillRect(recentMove[1] % 8 * 100, recentMove[1] / 8 * 100,100,100);
        }


        // drag highlight
        g.setColor(new Color(255,255,255,100));
        g.fillRect(dragCoor[0] / 100 * 100, dragCoor[1] / 100 * 100,100,100);

        // move hints
        for(Move move : moveHints){
            int moveHintX = (move.toSquare() % 8) * 100;
            int moveHintY = (move.toSquare() / 8) * 100;

            if(!this.isWhite){
                moveHintX = 700 - (move.toSquare() % 8) * 100;
                moveHintY = 700 - (move.toSquare() / 8) * 100;
            }

            g.setColor(new Color(250,175,2)); // yellow 
            g.fillRect(moveHintX,moveHintY, 100,100);
        }

        // move hints
        for(Move move : nextMoveHints){
            int moveHintX = (move.toSquare() % 8) * 100;
            int moveHintY = (move.toSquare() / 8) * 100;

            if(!this.isWhite){
                moveHintX = 700 - (move.toSquare() % 8) * 100;
                moveHintY = 700 - (move.toSquare() / 8) * 100;
            }

            g.setColor(new Color(220,220,220)); // grey 
            g.fillRect(moveHintX,moveHintY, 100,100);
        }

        // if(bestMove != null){
        //     g.setColor(new Color(0,255,0,100));
        //     g.fillRect((bestMove.fromSquare() % 8) * 100,(bestMove.fromSquare() / 8) * 100, 100,100);
        //     g.fillRect((bestMove.toSquare() % 8) * 100,(bestMove.toSquare() / 8) * 100, 100,100);
        // }

        //draw all pieces and square numbers
        g.setColor(Color.WHITE);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int square = BoardUtil.rowColToSquare(row,col, this.isWhite);
                g.drawString(square + "", col*100,row*100 + 100);

                if(square != fromSquare){
                    PieceType current = BoardUtil.getPieceTypeAtSquare(board, square);
                    g.drawImage(pieceImages.get(current), col * 100, row * 100, null);
                } 
            }
        }

        // draws the selected piece
        if(selectedPiece != PieceType.EMPTY){
            g.drawImage(pieceImages.get(selectedPiece), dragCoor[0] - 50, dragCoor[1] - 50 , null);
        }
    }

}

