package main.java;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.utilities.*;

import java.util.EnumMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class HiveGUI extends JPanel{
    
    static Board board;
    static MoveGenerator moveGenerator;
    static ChessPanel chessPanel;
    static DragPanel dragPanel;
    static InfoPanel infoPanel;

    public static void main(String[] args) throws IOException{

        board = new Board();
        // board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");

        JFrame frame = new JFrame();

        frame.setUndecorated(true);
        frame.setSize(1300, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        ImageIcon HIVE_MAX_ICON = new ImageIcon("src\\main\\assets\\HIVE_MAX_LOGO.png");
        frame.setIconImage(HIVE_MAX_ICON.getImage());

        chessPanel = new ChessPanel(board);
        chessPanel.setBounds(0,0, 800, 800);

        

        infoPanel = new InfoPanel(board, chessPanel);
        infoPanel.setBounds(800,600, 500, 200);

        dragPanel = new DragPanel(board, chessPanel, infoPanel);
        dragPanel.setBounds(800, 0, 200, 600);

        JButton clearButton = new JButton("Clear Board");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // chessPanel.board.setBoard("8/8/8/8/8/8/8/8 w - - 0 0");
                chessPanel.repaint();

            
            }
        });


        
        frame.setLayout(null);
        frame.add(chessPanel);
        frame.add(dragPanel);
        frame.add(infoPanel);

        

        frame.setVisible(true);
    }

}

class InfoPanel extends JPanel{

    Board board;
    ChessPanel chessPanel;
    JTextArea infoArea;
    JTextField fenInput;

    public InfoPanel(Board board, ChessPanel chessPanel){
        this.board = board;
        this.chessPanel = chessPanel;
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(800, 400));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        infoArea = new JTextArea();
        infoArea.setText(board.getFenString());

        fenInput = new JTextField();
        fenInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, fenInput.getPreferredSize().height));

        JButton applyFenButton = new JButton("Set Board");
        applyFenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fen = fenInput.getText();
                board.setBoard(board, fen);
                chessPanel.repaint();
            }
        });

        this.add(infoArea);
        this.add(fenInput);
        this.add(applyFenButton);


    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        infoArea.setText(board.getFenString());


    }
}

class DragPanel extends JPanel{

    public static BufferedImage allPieces;
    public static EnumMap<PieceType,Image> pieceImages = new EnumMap<>(PieceType.class);
    public static final String PIECES_PATH = "src\\main\\assets\\pieces\\pieces.png";
    public Board board;
    public ChessPanel chessPanel;
    public InfoPanel infoPanel;

    public PieceType[][] pieceLayout = {
        {PieceType.WHITE_PAWN, PieceType.BLACK_PAWN},
        {PieceType.WHITE_KNIGHT, PieceType.BLACK_KNIGHT},
        {PieceType.WHITE_BISHOP, PieceType.BLACK_BISHOP},
        {PieceType.WHITE_ROOK, PieceType.BLACK_ROOK},
        {PieceType.WHITE_QUEEN, PieceType.BLACK_QUEEN},
        {PieceType.WHITE_KING, PieceType.BLACK_KING},
    };

    public PieceType selected = PieceType.EMPTY;

    public DragPanel(Board board, ChessPanel chessPanel, InfoPanel infoPanel) throws IOException{
        this.board = board;
        this.chessPanel = chessPanel;
        this.infoPanel = infoPanel;

        this.setBackground(Color.GREEN);
        allPieces = ImageIO.read(new File(PIECES_PATH));
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
        this.setPreferredSize(new Dimension(300,800));
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int y = 0;
        int x = 0;
        for(Map.Entry<PieceType, Image> entry : pieceImages.entrySet()){
            g.drawImage(entry.getValue(), x, y, null);
            y += 100;
            if(y == 600){
                y = 0;
                x = 100;
            }
        }
    }

    private class ClickListener extends MouseAdapter{

        public void mousePressed(MouseEvent e){
            // System.out.println("Mouse pressed:" + e.getX() + ", " + e.getY());
            if(e.getX() > 200 || e.getY() > 600){
                selected = PieceType.EMPTY;

            }else{
                selected = pieceLayout[e.getY()/100][e.getX()/100];
            }
        }

        public void mouseReleased(MouseEvent e){
            int x = 7 - (Math.abs(e.getX()) / 100);
            if(e.getX() < 0){
                int y = Math.abs(e.getY()) / 100;
                int square = BoardUtil.rowColToSquare(y, x);

                switch(selected){
                    case BLACK_BISHOP:
                        board.BLACK_BISHOPS = BoardUtil.setBit(board.BLACK_BISHOPS, square);
                        break;
                    case BLACK_KING:
                        board.BLACK_KINGS = BoardUtil.setBit(board.BLACK_KINGS, square);
                        break;
                    case BLACK_KNIGHT:
                        board.BLACK_KNIGHTS = BoardUtil.setBit(board.BLACK_KNIGHTS, square);
                        break;
                    case BLACK_PAWN:
                        board.BLACK_PAWNS = BoardUtil.setBit(board.BLACK_PAWNS, square);
                        break;
                    case BLACK_QUEEN:
                        board.BLACK_QUEENS = BoardUtil.setBit(board.BLACK_QUEENS, square);
                        break;
                    case BLACK_ROOK:
                        board.BLACK_ROOKS = BoardUtil.setBit(board.BLACK_ROOKS, square);
                        break;
                    case WHITE_BISHOP:
                        board.WHITE_BISHOPS = BoardUtil.setBit(board.WHITE_BISHOPS, square);
                        break;
                    case WHITE_KING:
                        board.WHITE_KINGS = BoardUtil.setBit(board.WHITE_KINGS, square);
                        break;
                    case WHITE_KNIGHT:
                        board.WHITE_KNIGHTS = BoardUtil.setBit(board.WHITE_KNIGHTS, square);
                        break;
                    case WHITE_PAWN:
                        board.WHITE_PAWNS = BoardUtil.setBit(board.WHITE_PAWNS, square);
                        break;
                    case WHITE_QUEEN:
                        board.WHITE_QUEENS = BoardUtil.setBit(board.WHITE_QUEENS, square);
                        break;
                    case WHITE_ROOK:
                        board.WHITE_ROOKS = BoardUtil.setBit(board.WHITE_ROOKS, square);
                        break;
                    default:
                        break;
                    
                }
                chessPanel.repaint();
                infoPanel.repaint();
            }
            
        }
    }

    private class DragListener extends MouseMotionAdapter{

        public void mouseDragged(MouseEvent e){
        }
    }
}

class ChessPanel extends JPanel{

    public Board board;
    public HashSet<Move> legalMoves = new HashSet<>();

    public static Image boardImage;
    public static BufferedImage allPieces;
    public static EnumMap<PieceType,Image> pieceImages = new EnumMap<>(PieceType.class);
    public static final String BG_PATH = "src\\main\\assets\\boards\\bg_brown.png";
    public static final String PIECES_PATH = "src\\main\\assets\\pieces\\pieces.png";

    public PieceType selectedPiece = PieceType.EMPTY;
    public int[] dragCoor = new int[]{-1, -1};
    public HashSet<Move> moveHints = new HashSet<>();
    public HashSet<Integer> dangerSquares = new HashSet<>();

    int toSquare = -1;
    int fromSquare = -1;
    int depth = 4;
    
    public Move bestMove = null;

    public ChessPanel(Board board) throws IOException{
        this.board = board;
        legalMoves = MoveGenerator.getCurrentLegalMoves(board);
        bestMove = HiveEvaluator.bestMove(board, legalMoves, depth, board.IS_WHITE_TURN);

        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);

        boardImage = ImageIO.read(new File(BG_PATH));
        allPieces = ImageIO.read(new File(PIECES_PATH));
        boardImage = boardImage.getScaledInstance(800, 800, BufferedImage.SCALE_SMOOTH);
        

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

    private class ClickListener extends MouseAdapter{

        public void mousePressed(MouseEvent e){
            fromSquare = BoardUtil.rowColToSquare(e.getY()/100, e.getX()/100);
            selectedPiece = BoardUtil.getPieceTypeAtSquare(board, fromSquare);
            HashSet<Move> hints = new HashSet<>();

            for(Move move: legalMoves){
                if(move.fromSquare() == fromSquare){
                    hints.add(move);
                }
            }

            moveHints = hints;

            if(selectedPiece == PieceType.WHITE_KING){
                long[] boards = BoardUtil.getTeamBoardsWithoutKing(board, true);
                long enemyBoard = 0L;
                for(long b: boards){
                    enemyBoard |= b;
                }
                HashSet<Move> dangerMoves = MoveGenerator.getPsuedoLegalMoves(board, BoardUtil.getTeamBoards(board, false), enemyBoard , false, true, BoardUtil.NULL_PINNED_PIECES, BoardUtil.NULL_DANGER_SQUARES, BoardUtil.NULL_CAPTURE_MASK, BoardUtil.NULL_PUSH_MASK);
                for(Move m : dangerMoves){
                    dangerSquares.add(m.toSquare());
                }

            }else{
                dangerSquares.clear();

            }

            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            selectedPiece = PieceType.EMPTY;
            toSquare = BoardUtil.rowColToSquare(e.getY()/100, e.getX()/100);

            for(Move move: legalMoves){
                if(move.toSquare() == toSquare && move.fromSquare() == fromSquare){
                    board.makeMove(move);
                    legalMoves = MoveGenerator.getCurrentLegalMoves(board);
                    bestMove = HiveEvaluator.bestMove(board, legalMoves, depth, board.IS_WHITE_TURN);
                    System.out.println("BeST MOVE: " + bestMove);
                    break;
                }
            }
            fromSquare = -1;
            toSquare = -1;
            moveHints = new HashSet<>();
            dangerSquares.clear();



            repaint();
        }
    }

    private class DragListener extends MouseMotionAdapter{

        public void mouseMoved(MouseEvent e){
            dragCoor[0] = e.getX();
            dragCoor[1] = e.getY();

            repaint();
        }

        public void mouseDragged(MouseEvent e){
            dragCoor[0] = e.getX();
            dragCoor[1] = e.getY();

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        g.drawImage(boardImage, 0, 0, 800, 800, null);

        // drag highlight
        g.setColor(new Color(255,255,255,100));
        g.fillRect(dragCoor[0] / 100 * 100, dragCoor[1] / 100 * 100,100,100);

        for(Move move : moveHints){
            int moveHintX = (move.toSquare() % 8) * 100;
            int moveHintY = (move.toSquare() / 8) * 100;

            g.setColor(new Color(250,175,2)); // yellow 
            g.fillRect(moveHintX,moveHintY, 100,100);
        }

        g.setColor(new Color(255,0,0,100));
        
        // draw danger squares
        for(int square : dangerSquares){
            int moveHintX = (square % 8) * 100;
            int moveHintY = (square / 8) * 100;
            g.fillRect(moveHintX,moveHintY, 100,100);

        }

        if(bestMove != null){
            g.setColor(new Color(0,255,0,100));
            g.fillRect((bestMove.fromSquare() % 8) * 100,(bestMove.fromSquare() / 8) * 100, 100,100);
            g.fillRect((bestMove.toSquare() % 8) * 100,(bestMove.toSquare() / 8) * 100, 100,100);


        }

        //draw all pieces
        g.setColor(Color.WHITE);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int square = row * 8 + col;
                g.drawString((row*8 + col) + "", col*100,row*100 + 100);

                if(square != fromSquare){
                    PieceType current = BoardUtil.getPieceTypeAtSquare(board, square);
                    g.drawImage(pieceImages.get(current), col * 100, row * 100, null);
                } 
            }
        }

        if(selectedPiece != PieceType.EMPTY){
            g.drawImage(pieceImages.get(selectedPiece), dragCoor[0] - 50, dragCoor[1] - 50 , null);

        }
    }
}

