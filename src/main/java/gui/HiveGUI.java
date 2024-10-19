package main.java.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.java.utilities.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import main.java.Board;
import main.java.hive.HiveSearch;
import main.java.network.Client;
import main.java.network.GameState;


public class HiveGUI extends JPanel {


    Client client;
    boolean playingOnline = false;
    SinglePlayerServer singlePlayerServer;

    public static void main(String[] args) throws IOException{
        new HiveGUI();        
    }

    public HiveGUI() throws IOException {
        Board board = new Board();
        
        JFrame frame = new JFrame();

        // frame.setUndecorated(true);
        frame.setSize(915, 840);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.DARK_GRAY);

        
        ImageIcon HIVE_MAX_ICON = new ImageIcon("src\\main\\assets\\HIVE_MAX_LOGO.png");
        frame.setIconImage(HIVE_MAX_ICON.getImage());

        ChessPanel chessPanel = new ChessPanel(board);
        client = new Client(chessPanel);
        chessPanel.client = client;
        chessPanel.setBounds(100,0, 800, 800);

        

        InfoPanel infoPanel = new InfoPanel(board, chessPanel);
        infoPanel.setBounds(900,600, 500, 200);

        DragPanel dragPanel = new DragPanel(board, chessPanel, infoPanel);
        dragPanel.setBounds(900, 0, 200, 600);

        JButton playOnlineButton = new JButton(("Play Online"));
        playOnlineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playingOnline = !playingOnline;
                
                if(playingOnline){
                    System.out.println("Searching for server...");
                    Thread thread = new Thread(client);
                    thread.start();
                    chessPanel.sendMoveToClient = true;
                    playOnlineButton.setText("Leave");
                }else{
                    client.shutdown();
                    chessPanel.sendMoveToClient = false;
                    playOnlineButton.setText("Online");

                }
                revalidate();
            }
        });
        
        playOnlineButton.setBounds(0, 0, 100, 100);

        JButton singlePlayerButton = new JButton("AI");
        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting single player");
                singlePlayerServer = new SinglePlayerServer(chessPanel);
                chessPanel.singlePlayer = true;
                chessPanel.singlePlayerServer = singlePlayerServer;
                if(client.client != null && !client.client.isClosed()){
                    client.shutdown();
                }
                chessPanel.sendMoveToClient = false;
                playOnlineButton.setText("Online");
            }
        });
        
        singlePlayerButton.setBounds(0, 100, 100, 100);

        JButton customButton = new JButton("[Test Mode]");
        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setSize(1300, 840);
                frame.add(dragPanel);
                frame.add(infoPanel);
                revalidate();
            }
        });
        
        customButton.setBounds(0, 200, 100, 100);

        frame.add(playOnlineButton);
        frame.add(singlePlayerButton);
        frame.add(customButton);


        frame.setLayout(null);
        frame.add(chessPanel);
        

        

        frame.setVisible(true);
    }
}

class SinglePlayerServer {

    ChessPanel chessPanel;
    GameState gameState;
    boolean computerIsWhite;
    boolean isWhite;
    ExecutorService executorService;
    int DEPTH = 3;

    public SinglePlayerServer(ChessPanel chessPanel){
        Random rand = new Random();
        this.gameState = new GameState(null, null);
        this.isWhite = rand.nextBoolean();
        this.gameState.isWhite = this.isWhite;
        this.computerIsWhite = !this.isWhite;
        this.chessPanel = chessPanel;
        chessPanel.updateChessPanel(gameState);
        executorService = Executors.newCachedThreadPool();

        if(!this.isWhite){
            makeComputerMove();
        }
    }

    public void update(Move move){
        this.gameState.update(move);
        this.chessPanel.updateChessPanel(gameState);
        if(this.gameState.board.IS_WHITE_TURN == this.computerIsWhite){
            makeComputerMove();
        }
    }

    public void makeComputerMove(){
        Future<Move> bestMove = executorService.submit(() -> {
            return HiveSearch.bestMove(
                new Board(this.gameState.board),
                this.gameState.currentLegalMoves,
                DEPTH,
                this.computerIsWhite
            );
        });

        executorService.execute(() -> {
            try {
                Move move = bestMove.get();
                update(move);
            } catch (Exception e) {
                
            }
        });
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
            System.out.println("Mouse pressed:" + e.getX() + ", " + e.getY());
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
                int square = BoardUtil.rowColToSquare(y, x, true);

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

