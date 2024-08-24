package main.java;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
import main.util.*;


public class HiveGUI extends JPanel{
    
    static Board board;
    static MoveGenerator moveGenerator;
    static ChessPanel chessPanel;
    static DragPanel dragPanel;
    static InfoPanel infoPanel;

    public static void main(String[] args) throws IOException{

        board = new Board(" 8/8/8/8/8/8/8/8 w KQkq - 0 0");
        // board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");

        BoardUtil.printBoard(board);

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
        infoArea.setText(board.getFenString(board));

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
        infoArea.setText(board.getFenString(board));


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
            // System.out.println("Mouse dragging:" + e.getX() + ", " + e.getY());
        }
    }
}

class ChessPanel extends JPanel{

    public Board board;

    public static Image boardImage;
    public static BufferedImage allPieces;
    public static EnumMap<PieceType,Image> pieceImages = new EnumMap<>(PieceType.class);
    public static final String BG_PATH = "src\\main\\assets\\boards\\bg_brown.png";
    public static final String PIECES_PATH = "src\\main\\assets\\pieces\\pieces.png";
    int toSquare = -1;
    int fromSquare = -1;

    public ChessPanel(Board board) throws IOException{
        this.board = board;
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
            if(fromSquare == -1){
                fromSquare = BoardUtil.rowColToSquare(e.getY()/100, e.getX()/100);
            }else{
                toSquare = BoardUtil.rowColToSquare(e.getY()/100, e.getX()/100);
                PieceType from = BoardUtil.getPieceTypeAtSquare(board, fromSquare);
                PieceType to = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                String moveType = (BoardUtil.isOccupiedByFriendly(toSquare, BoardUtil.getTeamBoard(board, true)) || to == PieceType.EMPTY) ? "MoveType.DEFAULT" : " MoveType.CAPTURE";
                System.out.println("new Move(" + fromSquare + "," + toSquare + "," + "PieceType." + from + "," + "PieceType." +to + ", PieceType.EMPTY," + moveType + ")");
                fromSquare = -1;
                toSquare = -1;
            }
        }
    }

    private class DragListener extends MouseMotionAdapter{

        public void mouseDragged(MouseEvent e){
            // System.out.println("Mouse dragging:" + e.getX() + ", " + e.getY());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        g.drawImage(boardImage, 0, 0, 800, 800, null);

        //draw all pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int square = row * 8 + col;
                PieceType current = BoardUtil.getPieceTypeAtSquare(board, square);
                g.setColor(Color.WHITE);
                g.drawString((row*8 + col) + "", col*100,row*100 + 100);
                g.drawImage(pieceImages.get(current), col * 100, row * 100, null);
                
            }
        }
    }
}

