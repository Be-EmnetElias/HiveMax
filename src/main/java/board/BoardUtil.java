package main.java.board;

import java.util.HashMap;
import java.util.HashSet;

import main.java.hive.HiveHash;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import main.java.move.MoveType;

public class BoardUtil {
    
    public static final PieceType[] PIECE_TYPES = PieceType.values();

    public static final long NULL_PUSH_MASK = ~0L;

    public static final int NULL_CAPTURE_MASK = -1;

    public static final HashSet<Integer> NULL_DANGER_SQUARES = new HashSet<>();

    public static final HashMap<Integer, Integer> NULL_PINNED_PIECES = new HashMap<>();

    public static final int[] WHITE_PAWN_DISPLACEMENTS = new int[]{-16, -8, -7, -9};

    public static final int[] BLACK_PAWN_DISPLACEMENTS = new int[]{16, 8, 7, 9};

    public static final int[] KNIGHT_DISPLACEMENTS = new int[]{-17, -15, -10, -6, 6, 10, 15, 17};

    public static final int[] BISHOP_DISPLACEMENTS = new int[]{-9, -7, 7, 9};

    public static final int[] ROOK_DISPLACEMENTS = new int[]{-8, -1, 1, 8};

    public static final int[] QUEEN_DISPLACEMENTS = new int[]{-9, -8, -7, -1, 1, 7, 8, 9};

    public static final HashSet<PieceType> SLIDING_PIECES = new HashSet<>(){{
        add(PieceType.BLACK_BISHOP);
        add(PieceType.BLACK_QUEEN);
        add(PieceType.BLACK_ROOK);

        add(PieceType.WHITE_ROOK);
        add(PieceType.WHITE_BISHOP);
        add(PieceType.WHITE_QUEEN);

    }};
    public static final HashMap<PieceType, int[]> SLIDING_DISPLACEMENTS = new HashMap<PieceType, int[]>() {{
        put(PieceType.WHITE_BISHOP, BISHOP_DISPLACEMENTS);
        put(PieceType.BLACK_BISHOP, BISHOP_DISPLACEMENTS);
        put(PieceType.WHITE_ROOK, ROOK_DISPLACEMENTS);
        put(PieceType.BLACK_ROOK, ROOK_DISPLACEMENTS);
        put(PieceType.WHITE_QUEEN, QUEEN_DISPLACEMENTS);
        put(PieceType.BLACK_QUEEN, QUEEN_DISPLACEMENTS);
    }};

    public BoardUtil(){}

    //todo
    public static boolean isGameOver(Board board){
        return MoveGenerator.getCurrentLegalMoves(board).isEmpty() || board.TURNS > 50;
    }

    public static long[] getTeamBoards(Board board, boolean isWhite){
        if(isWhite){
            return new long[]{
                board.WHITE_PAWNS, board.WHITE_KNIGHTS, board.WHITE_BISHOPS, board.WHITE_ROOKS, board.WHITE_QUEENS, board.WHITE_KINGS
            };
        }else{
            return new long[]{
                board.BLACK_PAWNS, board.BLACK_KNIGHTS, board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS, board.BLACK_KINGS
            };
        }
    }

    public static long getTeamBoard(Board board, boolean isWhite){
        if(isWhite){
            return board.WHITE_PAWNS | board.WHITE_KNIGHTS | board.WHITE_BISHOPS | board.WHITE_ROOKS | board.WHITE_QUEENS | board.WHITE_KINGS;
        }else{
            return board.BLACK_PAWNS | board.BLACK_KNIGHTS | board.BLACK_BISHOPS | board.BLACK_ROOKS | board.BLACK_QUEENS | board.BLACK_KINGS;
        }
    }

    public static long[] getTeamBoardsWithoutKing(Board board, boolean isWhite){
        if(isWhite){
            return new long[]{
                board.WHITE_PAWNS, board.WHITE_KNIGHTS, board.WHITE_BISHOPS, board.WHITE_ROOKS, board.WHITE_QUEENS
            };
        }else{
            return new long[]{
                board.BLACK_PAWNS, board.BLACK_KNIGHTS, board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS
            };
        }
    }

    public static long[] getAllBoards(Board board){
        return new long[]{
            board.WHITE_PAWNS, board.WHITE_KNIGHTS, board.WHITE_BISHOPS, board.WHITE_ROOKS, board.WHITE_QUEENS, board.WHITE_KINGS,
            board.BLACK_PAWNS, board.BLACK_KNIGHTS, board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS, board.BLACK_KINGS
        };
    }

    public static long convertSquareToBoard(int square){
        return 1L << square;
    }

    public static long setBit(long board, int square){
        return board |= (1L << square);
    }

    public static long clearBit(long board, int square){
        return board &= ~(1L << square);
    }

    public static int getKingSquare(Board board, boolean isWhite){
        return Long.numberOfTrailingZeros(isWhite ? board.WHITE_KINGS : board.BLACK_KINGS);
    }

    public static int rowColToSquare(int row, int col, boolean isWhite){
        if(isWhite){
            return (row * 8) + col;
        }else{
            return 63 - ((row * 8) + col);
        }
    }

    public static PieceType getPieceTypeAtSquare(Board board, int square){
        long boardFromSquare = convertSquareToBoard(square);
        long[] allBoards = getAllBoards(board);

        for(int i=0; i<allBoards.length; i++){
            if((allBoards[i] & boardFromSquare) != 0){
                return PIECE_TYPES[i];
            }
        }
        
        return PieceType.EMPTY;
    }

    public static boolean isOccupiedByFriendly(int square, long teamPieces){
        long boardFromSquare = convertSquareToBoard(square);
        return (boardFromSquare & teamPieces) != 0; 
    }

    public static boolean isSameTeam(PieceType piece, PieceType other){
        if(piece == PieceType.EMPTY || other == PieceType.EMPTY){
            return false;
        }else{
            return getPieceTypeTeam(piece) == getPieceTypeTeam(other);
        }
    }

    public static boolean getPieceTypeTeam(PieceType piece){
        switch(piece){
            case BLACK_BISHOP:
                return false;
            case BLACK_KING:
                return false;
            case BLACK_KNIGHT:
                return false;
            case BLACK_PAWN:
                return false;
            case BLACK_QUEEN:
                return false;
            case BLACK_ROOK:
                return false;
            case WHITE_BISHOP:
                return true;
            case WHITE_KING:
                return true;
            case WHITE_KNIGHT:
                return true;
            case WHITE_PAWN:
                return true;
            case WHITE_QUEEN:
                return true;
            case WHITE_ROOK:
                return true;
            default:
                throw new IllegalArgumentException("Cannot get team of PieceType Empty!"); 
        }
    }
    /*
     * TODO: find how often this is used
     * Optimization: instead of comparing a square to long, if the piece at this square is already known, compare to PieceType instead
     */
    public static boolean isOccupiedByEnemy(int square, long enemyPieces){
        long boardFromSquare = convertSquareToBoard(square);
        return (boardFromSquare & enemyPieces) != 0; 
    }

    public static boolean checkValidSquare(int square){
        return square >= 0 && square < 64;
    }

    public static boolean isSquareOnBoard(int square, long board){
        long boardFromSquare = convertSquareToBoard(square);
        return (boardFromSquare & board) != 0; 
    }

    /** 
     * Checks if the piece at this square can move in this direction against the pinned pieces map
     * Returns true if the piece is pinned in this direction or if the piece isn't pinned at all, false otherwise
     * @param fromSquare
     * @param displacement
     * @param pinnedPieces
     * @return boolean
     */
    public static boolean isValidPinDirection(int fromSquare, int displacement, HashMap<Integer, Integer> pinnedPieces){

        if(pinnedPieces == null || pinnedPieces.isEmpty() || !pinnedPieces.containsKey(fromSquare)){
            return true;
        }

        int pinnedDisplacement = pinnedPieces.get(fromSquare);
        boolean pawnDoubleException = pinnedDisplacement * 2 == displacement ; // double jump displacement is in the same direction as a normal move
        return  (pinnedDisplacement == displacement) || pawnDoubleException;
    }

    public static boolean squareValidInCaptureAndPushMasks(int square, int captureMask, long pushMask){
        if(captureMask == NULL_CAPTURE_MASK && pushMask == NULL_PUSH_MASK){
            return true;
        }else if(captureMask == NULL_CAPTURE_MASK){
            return isSquareOnBoard(square, pushMask);
        }else if(pushMask == NULL_PUSH_MASK){
            return square == captureMask;
        }else{
            return square == captureMask || isSquareOnBoard(square, pushMask);
        }

    }
               
    public static boolean canKingSideCastle(Board board, boolean isWhite, HashSet<Integer> dangerSquares){
        if(isWhite){
            if((board.CASTLING_RIGHTS & 8) != 0 && getPieceTypeAtSquare(board, 61) == PieceType.EMPTY && getPieceTypeAtSquare(board, 62) == PieceType.EMPTY && !dangerSquares.contains(61) && !dangerSquares.contains(62)){
                return true;
            }
        }else{
            if((board.CASTLING_RIGHTS & 2) != 0 && getPieceTypeAtSquare(board, 5) == PieceType.EMPTY && getPieceTypeAtSquare(board, 6) == PieceType.EMPTY && !dangerSquares.contains(5) && !dangerSquares.contains(6)){
                return true;
            }
        }

        return false;
    }

    public static boolean canQueenSideCastle(Board board, boolean isWhite, HashSet<Integer> dangerSquares){
        if(isWhite){
            if((board.CASTLING_RIGHTS & 4) != 0 && getPieceTypeAtSquare(board, 59) == PieceType.EMPTY && getPieceTypeAtSquare(board, 58) == PieceType.EMPTY && getPieceTypeAtSquare(board, 57) == PieceType.EMPTY && !dangerSquares.contains(59) && !dangerSquares.contains(58) && !dangerSquares.contains(57)){
                return true;
            }
        }else{
            if((board.CASTLING_RIGHTS & 1) != 0 && getPieceTypeAtSquare(board, 3) == PieceType.EMPTY && getPieceTypeAtSquare(board, 2) == PieceType.EMPTY  && getPieceTypeAtSquare(board, 1) == PieceType.EMPTY&& !dangerSquares.contains(3) && !dangerSquares.contains(2) && !dangerSquares.contains(1)){
                return true;
            }
        }

        return false;
    }

    public static boolean canPieceSlide(PieceType pieceType){
        return SLIDING_PIECES.contains(pieceType);
    }

    public static String pieceTypeToString(PieceType piece){
        switch(piece){
            case BLACK_BISHOP:
                return "b";
            case BLACK_KING:
                return "k";
            case BLACK_KNIGHT:
                return "n";
            case BLACK_PAWN:
                return "p";
            case BLACK_QUEEN:
                return "q";
            case BLACK_ROOK:
                return "r";
            case WHITE_BISHOP:
                return "B";
            case WHITE_KING:
                return "K";
            case WHITE_KNIGHT:
                return "N";
            case WHITE_PAWN:
                return "P";
            case WHITE_QUEEN:
                return "Q";
            case WHITE_ROOK:
                return "R";
            default:
                return "";
            
        }
    }

    // long 64 bits 000000010101010.....
    // MSB = square 63, LSB = square 0
    public static void printLong(long board){
        String boardStr = "";
        for(int i=0; i <= 64 - Long.toBinaryString(board).length(); i++){
            boardStr += "0";
        }

        boardStr += Long.toBinaryString(board);
        
        for(int i = boardStr.length()-1; i>0; i--){
            if(i%8==0){
                System.out.println();
                System.out.println();

            }
            System.out.print(boardStr.charAt(i) + "  ");
        }

        System.out.println();
        System.out.println();

        System.out.println();

        

    }

    public static void printBoard(Board board){
        long hash = HiveHash.getHash(board);
        int enpassantPosition = board.ENPASSANT_SQUARE;
        String enpasantSquare = (enpassantPosition == -1) ? "NONE":"" + (char)('a' + enpassantPosition%8) + (8-enpassantPosition/8);
        for (int row = 0; row < 8; row++) {
            System.out.print(8-row + " | ");
            for (int col = 0; col < 8; col++) {
                long pos = 1L << (row * 8 + col);
    
                // System.out.printf("%3d", row * 8 + col);
                if ((board.WHITE_PAWNS & pos) != 0) { System.out.print("P   ");}
                else if ((board.WHITE_KNIGHTS & pos) != 0) { System.out.print("N   ");}
                else if ((board.WHITE_BISHOPS & pos) != 0) { System.out.print("B   ");}
                else if ((board.WHITE_ROOKS & pos) != 0) { System.out.print("R   ");}
                else if ((board.WHITE_QUEENS & pos) != 0) { System.out.print("Q   ");}
                else if ((board.WHITE_KINGS & pos) != 0) { System.out.print("K   ");}
                else if ((board.BLACK_PAWNS & pos) != 0) { System.out.print("p   ");}
                else if ((board.BLACK_KNIGHTS & pos) != 0) { System.out.print("n   ");}
                else if ((board.BLACK_BISHOPS & pos) != 0) { System.out.print("b   ");}
                else if ((board.BLACK_ROOKS & pos) != 0) { System.out.print("r   ");}
                else if ((board.BLACK_QUEENS & pos) != 0) { System.out.print("q   ");}
                else if ((board.BLACK_KINGS & pos) != 0) { System.out.print("k   ");}
                else{
                    System.out.print(".   ");
                }
            }
            if(row == 0) System.out.print("\t TURN: " + (board.IS_WHITE_TURN ? "WHITE":"BLACK"));
            if(row == 1) System.out.print("\t CASTLE RIGHTS: " + 
            ((board.CASTLING_RIGHTS & 8) != 0 ? "K":"") +
            ((board.CASTLING_RIGHTS & 4) != 0 ? "Q":"") +
            ((board.CASTLING_RIGHTS & 2) != 0 ? "k":"") + 
            ((board.CASTLING_RIGHTS & 1) != 0 ? "q ":" ") + 

            ((board.CASTLING_RIGHTS & 8) != 0 ? "1":"0") + 
            ((board.CASTLING_RIGHTS & 4) != 0 ? "1":"0") + 
            ((board.CASTLING_RIGHTS & 2) != 0 ? "1":"0") + 
            ((board.CASTLING_RIGHTS & 1) != 0 ? "1 ":"0 ") +

            board.CASTLING_RIGHTS
            );
            if(row == 2) System.out.print("\t AVAILABLE ENPASSANT CAPTURE: " + (enpasantSquare.equals("a0")?"NONE":enpasantSquare));

            if(row == 3) System.out.println("\t TOTAL MOVES: " + board.TURNS);

            if(row == 4) System.out.println("\t Zobrist Hash: " + hash);

            if(row != 7) System.out.println("\n  | ");

        }

        System.out.println("\n   - - - - - - - - - - - - - - - - ");
        System.out.print("    ");
        for(int i=0; i<8; i++){
            System.out.print((char)('a' + i) + "   ");
        }

        System.out.println("\n");
    }

    public static String squareToString(int square){
        return "" + (char)('a' + square%8) + (8-square/8);

    }

    public static PieceType stringToPieceType(String pieceType){
        switch(pieceType){
            case "BLACK_BISHOP":
                return PieceType.BLACK_BISHOP;
            case "BLACK_KING":
                return PieceType.BLACK_KING;
            case "BLACK_KNIGHT":
                return PieceType.BLACK_KNIGHT;
            case "BLACK_PAWN":
                return PieceType.BLACK_PAWN;
            case "BLACK_QUEEN":
                return PieceType.BLACK_QUEEN;
            case "BLACK_ROOK":
                return PieceType.BLACK_ROOK;
            case "WHITE_BISHOP":
                return PieceType.WHITE_BISHOP;
            case "WHITE_KING":
                return PieceType.WHITE_KING;
            case "WHITE_KNIGHT":
                return PieceType.WHITE_KNIGHT;
            case "WHITE_PAWN":
                return PieceType.WHITE_PAWN;
            case "WHITE_QUEEN":
                return PieceType.WHITE_QUEEN;
            case "WHITE_ROOK":
                return PieceType.WHITE_ROOK;
            default:
                return PieceType.EMPTY;
        }
    }
    
    public static MoveType stringToMoveType(String moveType){
        switch(moveType){
            case "CAPTURE":
                return MoveType.CAPTURE;
            case "CHECK":
                return MoveType.CHECK;
            case "CASTLE":
                return MoveType.CASTLE;
            case "PROMOTION":
                return MoveType.PROMOTION;
            case "ENPASSANT":
                return MoveType.ENPASSANT;
            default:
                return MoveType.DEFAULT; 
        }
    }

    public static Move rawStringToMove(String rawMove){
        String[] splitString = rawMove.split("\\s+");
        return new Move(
            Integer.parseInt(splitString[0]),
            Integer.parseInt(splitString[1]),
            stringToPieceType(splitString[2]),
            stringToPieceType(splitString[3]),
            stringToPieceType(splitString[4]),
            stringToMoveType(splitString[5])

        );
    }

   
}
