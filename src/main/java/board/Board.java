package main.java.board;

import java.util.HashMap;
import java.util.Map;

import main.java.move.Move;
import main.java.move.MoveType;

import java.io.*;


public class Board implements Serializable{
    
    public long WHITE_PAWNS, WHITE_KNIGHTS, WHITE_BISHOPS, WHITE_ROOKS, WHITE_QUEENS, WHITE_KINGS;

    public long BLACK_PAWNS, BLACK_KNIGHTS, BLACK_BISHOPS, BLACK_ROOKS, BLACK_QUEENS, BLACK_KINGS;

    public int ENPASSANT_SQUARE;

    public int CASTLING_RIGHTS;

    private int PREVIOUS_CASTLING_RIGHTS;

    public boolean IS_WHITE_TURN;

    public boolean WHITE_CASTLED, BLACK_CASTLED;


    // todo: make board class only fields? refactor to move the below methods to BoardUtil

    public Board(){
        setBoard(this, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
    }

    public Board(String fenPosition){
        setBoard(this, fenPosition);
    }

    public Board(
        long WHITE_PAWNS, 
        long WHITE_KNIGHTS, 
        long WHITE_BISHOPS, 
        long WHITE_ROOKS, 
        long WHITE_QUEENS, 
        long WHITE_KINGS,
        long BLACK_PAWNS, 
        long BLACK_KNIGHTS, 
        long BLACK_BISHOPS, 
        long BLACK_ROOKS, 
        long BLACK_QUEENS, 
        long BLACK_KINGS,
        int ENPASSANT_SQUARE,
        int CASTLING_RIGHTS,
        boolean IS_WHITE_TURN
    ){
        this.WHITE_PAWNS = WHITE_PAWNS; 
        this.WHITE_KNIGHTS = WHITE_KNIGHTS; 
        this.WHITE_BISHOPS = WHITE_BISHOPS; 
        this.WHITE_ROOKS = WHITE_ROOKS; 
        this.WHITE_QUEENS = WHITE_QUEENS; 
        this.WHITE_KINGS = WHITE_KINGS;
        this.BLACK_PAWNS = BLACK_PAWNS; 
        this.BLACK_KNIGHTS = BLACK_KNIGHTS; 
        this.BLACK_BISHOPS = BLACK_BISHOPS; 
        this.BLACK_ROOKS = BLACK_ROOKS; 
        this.BLACK_QUEENS = BLACK_QUEENS; 
        this.BLACK_KINGS = BLACK_KINGS;
        this.ENPASSANT_SQUARE = ENPASSANT_SQUARE;
        this.CASTLING_RIGHTS = CASTLING_RIGHTS;
        this.IS_WHITE_TURN = IS_WHITE_TURN;    
    }

    public Board(Board other){
        this.WHITE_PAWNS = other.WHITE_PAWNS; 
        this.WHITE_KNIGHTS = other.WHITE_KNIGHTS; 
        this.WHITE_BISHOPS = other.WHITE_BISHOPS; 
        this.WHITE_ROOKS = other.WHITE_ROOKS; 
        this.WHITE_QUEENS = other.WHITE_QUEENS; 
        this.WHITE_KINGS = other.WHITE_KINGS;
        this.BLACK_PAWNS = other.BLACK_PAWNS; 
        this.BLACK_KNIGHTS = other.BLACK_KNIGHTS; 
        this.BLACK_BISHOPS = other.BLACK_BISHOPS; 
        this.BLACK_ROOKS = other.BLACK_ROOKS; 
        this.BLACK_QUEENS = other.BLACK_QUEENS; 
        this.BLACK_KINGS = other.BLACK_KINGS;
        this.ENPASSANT_SQUARE = other.ENPASSANT_SQUARE;
        this.CASTLING_RIGHTS = other.CASTLING_RIGHTS;
        this.IS_WHITE_TURN = other.IS_WHITE_TURN;   
    }

    public void setBoard(Board board, String fenposition){

        if(fenposition.equals("")){
            setBoard(board, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
            return;
        }
        
        String[] boardInformation = fenposition.split("\\s+");

        String position = boardInformation[0];
        String turn = boardInformation[1];
        String castleRights = boardInformation[2];
        String enpassantSquare = boardInformation[3];
        // String halfmoves = boardInformation[4];

        // Set turn
        board.IS_WHITE_TURN = turn.equals("w");

        //Init all bitboards to empty
        board.WHITE_PAWNS = 0L; 
        board.WHITE_BISHOPS = 0L; 
        board.WHITE_KNIGHTS = 0L;
        board.WHITE_ROOKS = 0L; 
        board.WHITE_QUEENS = 0L; 
        board.WHITE_KINGS = 0L; 
        board.BLACK_PAWNS = 0L; 
        board.BLACK_BISHOPS = 0L; 
        board.BLACK_KNIGHTS = 0L; 
        board.BLACK_ROOKS = 0L; 
        board.BLACK_QUEENS = 0L;
        board.BLACK_KINGS = 0L;

        board.ENPASSANT_SQUARE = -1;
        board.CASTLING_RIGHTS = 0;

        int row = 0;  // Start at the top row
        int col = 0;  // Start at the first column

        //Set piece position
        for (char c : position.toCharArray()) {
            int currentSquare = row * 8 + col;
            switch (c) {
                case 'P': board.WHITE_PAWNS = BoardUtil.setBit(board.WHITE_PAWNS, currentSquare);   break;
                case 'N': board.WHITE_KNIGHTS = BoardUtil.setBit(board.WHITE_KNIGHTS, currentSquare); break;
                case 'B': board.WHITE_BISHOPS = BoardUtil.setBit(board.WHITE_BISHOPS, currentSquare); break;
                case 'R': board.WHITE_ROOKS = BoardUtil.setBit(board.WHITE_ROOKS, currentSquare);   break;
                case 'Q': board.WHITE_QUEENS = BoardUtil.setBit(board.WHITE_QUEENS, currentSquare);  break;
                case 'K': board.WHITE_KINGS = BoardUtil.setBit(board.WHITE_KINGS, currentSquare);   break;
                case 'p': board.BLACK_PAWNS = BoardUtil.setBit(board.BLACK_PAWNS, currentSquare);   break;
                case 'n': board.BLACK_KNIGHTS = BoardUtil.setBit(board.BLACK_KNIGHTS, currentSquare); break;
                case 'b': board.BLACK_BISHOPS = BoardUtil.setBit(board.BLACK_BISHOPS, currentSquare); break;
                case 'r': board.BLACK_ROOKS = BoardUtil.setBit(board.BLACK_ROOKS, currentSquare);   break;
                case 'q': board.BLACK_QUEENS = BoardUtil.setBit(board.BLACK_QUEENS, currentSquare);  break;
                case 'k': board.BLACK_KINGS = BoardUtil.setBit(board.BLACK_KINGS, currentSquare);   break;
                case '/': row++; col = -1; break;  // Move to the next row
                default: col += Character.getNumericValue(c) - 1; break;  // Skip empty squares
            }

            col++;
        }

        // Set enpassant square
        if (!enpassantSquare.equals("-")) {
            int epCol = enpassantSquare.charAt(0) - 'a';
            int epRow = 7 - (enpassantSquare.charAt(1) - '1');
            board.ENPASSANT_SQUARE = epRow * 8 + epCol;
        }

        // Set castling rights
        if (castleRights.contains("K")) board.CASTLING_RIGHTS |= 8;  // White King-side
        if (castleRights.contains("Q")) board.CASTLING_RIGHTS |= 4;  // White Queen-side
        if (castleRights.contains("k")) board.CASTLING_RIGHTS |= 2;  // Black King-side
        if (castleRights.contains("q")) board.CASTLING_RIGHTS |= 1;  // Black Queen-side
    }

    public String getFenString(){
        String fenPosition = "";

        int emptyPieces = 0;
        for(int i=0; i<64; i++){
            if(i%8 == 0 && i > 0){
                if(emptyPieces > 0) fenPosition += emptyPieces;
                if(i < 63) fenPosition += "/";
                
                emptyPieces = 0;
            }

            PieceType curr = BoardUtil.getPieceTypeAtSquare(this, i);
            if(curr != PieceType.EMPTY){
                if(emptyPieces > 0){
                    fenPosition += "" + emptyPieces;
                    emptyPieces = 0;
                }

                fenPosition += BoardUtil.pieceTypeToString(curr);


            }else{
                emptyPieces += 1;
            }
            
        }

        fenPosition += " " + (this.IS_WHITE_TURN ? "w":"b");
        fenPosition += " KQkq ";
        String enpasantSquare = (this.ENPASSANT_SQUARE == -1) ? "- ":"" + (char)('a' + this.ENPASSANT_SQUARE%8) + (8-this.ENPASSANT_SQUARE/8) + " ";
        fenPosition += enpasantSquare;
        fenPosition += " 0 0";

        return fenPosition;
    }

    /**
     * Assumes this move is valid
     * Almost every move can be made by simply replacing the target location with the current piece, and the previous location to empty
     * Enpassant: In addition to the default move, also empty the enpassant square +|- 1
     * Promotion: In addition to the default move, remove the piece, add the new promoted piece
     * Castle: In addition to the default move, apply a default move to the corresponding rook to +|- 1 from the king
     */
    public void makeMove(Move move){
        this.PREVIOUS_CASTLING_RIGHTS = CASTLING_RIGHTS;
        int fromSquare = move.fromSquare();
        int toSquare = move.toSquare();
        PieceType pieceType = move.pieceType();
        PieceType capturedPieceType = move.capturedPieceType();
        PieceType promotedPieceType = move.promotionPieceType();
        MoveType moveType = move.moveType();

        HashMap<PieceType, Long> boardsMap = makeBoardMap();

        // move the piece type
        long pieceBoard = boardsMap.get(pieceType);
        pieceBoard = BoardUtil.clearBit(pieceBoard, fromSquare);
        pieceBoard = BoardUtil.setBit(pieceBoard, toSquare);
        boardsMap.put(pieceType, pieceBoard);

        // clear the capture type
        if(capturedPieceType != PieceType.EMPTY){
            long capturedBoard = boardsMap.get(capturedPieceType);
            capturedBoard = BoardUtil.clearBit(capturedBoard, toSquare);
            boardsMap.put(capturedPieceType, capturedBoard);
        }

        // handle enpassant
        if(moveType == MoveType.ENPASSANT){
            long capturedBoard = boardsMap.get(capturedPieceType);
            capturedBoard = BoardUtil.clearBit(capturedBoard, toSquare + (IS_WHITE_TURN ? 8 : -8));
            boardsMap.put(capturedPieceType, capturedBoard);
            ENPASSANT_SQUARE = -1;
        }

        // handle castling
        if(moveType == MoveType.CASTLE){
            switch(toSquare){
                case 62:
                    // white king side castle
                    long newRooks1 = BoardUtil.setBit(BoardUtil.clearBit(WHITE_ROOKS,63), 61);
                    boardsMap.put(PieceType.WHITE_ROOK, newRooks1);
                    WHITE_CASTLED = true;
                    break;
                case 58:
                    // white queen side castle
                    long newRooks2 = BoardUtil.setBit(BoardUtil.clearBit(WHITE_ROOKS,56), 59);
                    boardsMap.put(PieceType.WHITE_ROOK, newRooks2);
                    WHITE_CASTLED = true;

                    break;
                case 6:
                    // black king side castle
                    long newRooks3 = BoardUtil.setBit(BoardUtil.clearBit(BLACK_ROOKS,7), 5);
                    boardsMap.put(PieceType.BLACK_ROOK, newRooks3);
                    BLACK_CASTLED = true;

                    break;
                case 2:
                    // black queen side castle
                    long newRooks4 = BoardUtil.setBit(BoardUtil.clearBit(BLACK_ROOKS,0), 3);
                    boardsMap.put(PieceType.BLACK_ROOK, newRooks4);
                    BLACK_CASTLED = true;
                    break;
            }
        }
        
        // handle promotion
        if(moveType == MoveType.PROMOTION){
            pieceBoard = BoardUtil.clearBit(pieceBoard, toSquare);
            long promotedBoard = boardsMap.get(promotedPieceType);
            promotedBoard = BoardUtil.setBit(promotedBoard, toSquare);

            boardsMap.put(pieceType, pieceBoard);
            boardsMap.put(promotedPieceType, promotedBoard);
        }

        // update enpassant
        if((pieceType == PieceType.WHITE_PAWN || pieceType == PieceType.BLACK_PAWN) && Math.abs(fromSquare - toSquare) == 16 ){
            ENPASSANT_SQUARE = toSquare + (IS_WHITE_TURN ? 8 : -8);
        }else{
            ENPASSANT_SQUARE = -1;
        }

        // update castling rights
        if(pieceType == PieceType.WHITE_KING){
            CASTLING_RIGHTS &= 3;
        }

        if(pieceType == PieceType.BLACK_KING){
            CASTLING_RIGHTS &= 12;
        }

        if(pieceType == PieceType.WHITE_ROOK){
            if(fromSquare == 63){
                CASTLING_RIGHTS &= 7;
            }

            if(fromSquare == 56){
                CASTLING_RIGHTS &= 11;
            }
        }

        if(pieceType == PieceType.BLACK_ROOK){
            if(fromSquare == 7){
                CASTLING_RIGHTS &= 13;
            }

            if(fromSquare == 0){
                CASTLING_RIGHTS &= 14;
            }
        }

        // update piece boards
        for(Map.Entry<PieceType, Long> entry : boardsMap.entrySet()){
            PieceType piece = entry.getKey();
            long pieceTypeBoard = entry.getValue();
            
            switch(piece){
                case BLACK_BISHOP:
                    this.BLACK_BISHOPS = pieceTypeBoard;
                    break;
                case BLACK_KING:
                    this.BLACK_KINGS = pieceTypeBoard;
                    break;
                case BLACK_KNIGHT:
                    this.BLACK_KNIGHTS = pieceTypeBoard;
                    break;
                case BLACK_PAWN:
                    this.BLACK_PAWNS = pieceTypeBoard;
                    break;
                case BLACK_QUEEN:
                    this.BLACK_QUEENS = pieceTypeBoard;
                    break;
                case BLACK_ROOK:
                    this.BLACK_ROOKS = pieceTypeBoard;
                    break;
                case WHITE_BISHOP:
                    this.WHITE_BISHOPS = pieceTypeBoard;
                    break;
                case WHITE_KING:
                    this.WHITE_KINGS = pieceTypeBoard;
                    break;
                case WHITE_KNIGHT:
                    this.WHITE_KNIGHTS = pieceTypeBoard;
                    break;
                case WHITE_PAWN:
                    this.WHITE_PAWNS = pieceTypeBoard;
                    break;
                case WHITE_QUEEN:
                    this.WHITE_QUEENS = pieceTypeBoard;
                    break;
                case WHITE_ROOK:
                    this.WHITE_ROOKS = pieceTypeBoard;
                    break;
                default:
                    break;
                
            }
        }

        // update turn
        this.IS_WHITE_TURN = !BoardUtil.getPieceTypeTeam(pieceType);
        
    }

    /**
     * Almost every move can be undone by simply replacing the target location with the captured piece, and the previous location to the current piece
     * Enpassant: Instead, replace the enpassant square with empty, enpassant +/- 8 with the captured piece, and the previous with the current piece
     * Promotion: In addition to the default move, remove the piece, add the new promoted piece
     * Castle: In addition, return the rooks to their spot and update castling rights 
     */
    public void undoMove(Move move){
        this.CASTLING_RIGHTS = PREVIOUS_CASTLING_RIGHTS;
        this.PREVIOUS_CASTLING_RIGHTS = -1; //todo move this to the move type
        int fromSquare = move.fromSquare();
        int toSquare = move.toSquare();
        PieceType pieceType = move.pieceType();
        PieceType capturedPieceType = move.capturedPieceType();
        PieceType promotedPieceType = move.promotionPieceType();
        MoveType moveType = move.moveType();

        HashMap<PieceType, Long> boardsMap = makeBoardMap();

        ENPASSANT_SQUARE = -1;
        // move the piece type
        long pieceBoard = boardsMap.get(pieceType);
        pieceBoard = BoardUtil.clearBit(pieceBoard, toSquare);
        pieceBoard = BoardUtil.setBit(pieceBoard, fromSquare);
        boardsMap.put(pieceType, pieceBoard);

        // clear the capture type
        if(capturedPieceType != PieceType.EMPTY && moveType != MoveType.ENPASSANT){
            long capturedBoard = boardsMap.get(capturedPieceType);
            capturedBoard = BoardUtil.setBit(capturedBoard, toSquare);
            boardsMap.put(capturedPieceType, capturedBoard);
        }

        // handle enpassant
        if(moveType == MoveType.ENPASSANT){
            long capturedBoard = boardsMap.get(capturedPieceType);
            capturedBoard = BoardUtil.setBit(capturedBoard, toSquare + (IS_WHITE_TURN ? -8 : 8));
            boardsMap.put(capturedPieceType, capturedBoard);
            ENPASSANT_SQUARE = toSquare;
        }

        // handle castling
        if(moveType == MoveType.CASTLE){
            switch(toSquare){
                case 62:
                    // white king side castle
                    long newRooks1 = BoardUtil.setBit(BoardUtil.clearBit(WHITE_ROOKS,61), 63);
                    boardsMap.put(PieceType.WHITE_ROOK, newRooks1);
                    WHITE_CASTLED = false;
                    
                    break;
                case 58:
                    // white queen side castle
                    long newRooks2 = BoardUtil.setBit(BoardUtil.clearBit(WHITE_ROOKS,59), 56);
                    boardsMap.put(PieceType.WHITE_ROOK, newRooks2);
                    WHITE_CASTLED = false;
                    
                    break;
                case 6:
                    // black king side castle
                    long newRooks3 = BoardUtil.setBit(BoardUtil.clearBit(BLACK_ROOKS,5), 7);
                    boardsMap.put(PieceType.BLACK_ROOK, newRooks3);
                    BLACK_CASTLED = false;
                    break;
                case 2:
                    // black queen side castle
                    long newRooks4 = BoardUtil.setBit(BoardUtil.clearBit(BLACK_ROOKS,3), 0);
                    boardsMap.put(PieceType.BLACK_ROOK, newRooks4);
                    BLACK_CASTLED = false;
                    
                    break;
            }
        }
        
        // handle promotion
        if(moveType == MoveType.PROMOTION){
            pieceBoard = BoardUtil.setBit(pieceBoard, fromSquare);
            long promotedBoard = boardsMap.get(promotedPieceType);
            promotedBoard = BoardUtil.clearBit(promotedBoard, toSquare);

            boardsMap.put(pieceType, pieceBoard);
            boardsMap.put(promotedPieceType, promotedBoard);
        }

        // update enpassant
        //todo: idk

        // update piece boards
        for(Map.Entry<PieceType, Long> entry : boardsMap.entrySet()){
            PieceType piece = entry.getKey();
            long pieceTypeBoard = entry.getValue();
            
            switch(piece){
                case BLACK_BISHOP:
                    this.BLACK_BISHOPS = pieceTypeBoard;
                    break;
                case BLACK_KING:
                    this.BLACK_KINGS = pieceTypeBoard;
                    break;
                case BLACK_KNIGHT:
                    this.BLACK_KNIGHTS = pieceTypeBoard;
                    break;
                case BLACK_PAWN:
                    this.BLACK_PAWNS = pieceTypeBoard;
                    break;
                case BLACK_QUEEN:
                    this.BLACK_QUEENS = pieceTypeBoard;
                    break;
                case BLACK_ROOK:
                    this.BLACK_ROOKS = pieceTypeBoard;
                    break;
                case WHITE_BISHOP:
                    this.WHITE_BISHOPS = pieceTypeBoard;
                    break;
                case WHITE_KING:
                    this.WHITE_KINGS = pieceTypeBoard;
                    break;
                case WHITE_KNIGHT:
                    this.WHITE_KNIGHTS = pieceTypeBoard;
                    break;
                case WHITE_PAWN:
                    this.WHITE_PAWNS = pieceTypeBoard;
                    break;
                case WHITE_QUEEN:
                    this.WHITE_QUEENS = pieceTypeBoard;
                    break;
                case WHITE_ROOK:
                    this.WHITE_ROOKS = pieceTypeBoard;
                    break;
                default:
                    break;
                
            }
        }

        // update turn
        this.IS_WHITE_TURN = !this.IS_WHITE_TURN;
    }

    public HashMap<PieceType, Long> makeBoardMap(){
        return new HashMap<>(){{
            put(PieceType.WHITE_PAWN, WHITE_PAWNS);
            put(PieceType.WHITE_KNIGHT, WHITE_KNIGHTS);
            put(PieceType.WHITE_BISHOP, WHITE_BISHOPS);
            put(PieceType.WHITE_ROOK, WHITE_ROOKS);
            put(PieceType.WHITE_QUEEN, WHITE_QUEENS);
            put(PieceType.WHITE_KING, WHITE_KINGS);
            put(PieceType.BLACK_PAWN, BLACK_PAWNS);
            put(PieceType.BLACK_KNIGHT, BLACK_KNIGHTS);
            put(PieceType.BLACK_BISHOP, BLACK_BISHOPS);
            put(PieceType.BLACK_ROOK, BLACK_ROOKS);
            put(PieceType.BLACK_QUEEN, BLACK_QUEENS);
            put(PieceType.BLACK_KING, BLACK_KINGS);
    
        }};
    }


    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }

        if(obj.getClass() != this.getClass()){
            return false;
        }

        final Board other = (Board)obj;

        return( 
            this.WHITE_PAWNS == other.WHITE_PAWNS && 
            this.WHITE_KNIGHTS == other.WHITE_KNIGHTS && 
            this.WHITE_BISHOPS == other.WHITE_BISHOPS && 
            this.WHITE_ROOKS == other.WHITE_ROOKS && 
            this.WHITE_QUEENS == other.WHITE_QUEENS && 
            this.WHITE_KINGS == other.WHITE_KINGS && 
            this.BLACK_PAWNS == other.BLACK_PAWNS && 
            this.BLACK_KNIGHTS == other.BLACK_KNIGHTS && 
            this.BLACK_BISHOPS == other.BLACK_BISHOPS && 
            this.BLACK_ROOKS == other.BLACK_ROOKS && 
            this.BLACK_QUEENS == other.BLACK_QUEENS && 
            this.BLACK_KINGS == other.BLACK_KINGS && 
            this.ENPASSANT_SQUARE == other.ENPASSANT_SQUARE &&
            this.CASTLING_RIGHTS == other.CASTLING_RIGHTS && 
            this.IS_WHITE_TURN == other.IS_WHITE_TURN
        );
    }








}
