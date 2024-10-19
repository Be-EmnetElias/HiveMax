package main.java.hive;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import main.java.Board;
import main.java.utilities.BoardUtil;
import main.java.utilities.Move;
import main.java.utilities.PieceType;

public class HiveEvaluator {
    
// = = == == == === === WEIGHTS === === == == == = = 

    public static int 

    WEAK_COUNT_WEIGHT = 1, 
    
    
    MATERIAL_COUNT_PAWN_WEIGHT = 100, 
    
    CENTER_PAWN_COUNT_WEIGHT = 1,
    
    KING_PAWN_SHIELD_WEIGHT = 1,
    
    ISOLATED_PAWN_WEIGHT = -1,
    
    DOUBLE_PAWN_WEIGHT = -1,
    
    RANK_PASSED_PAWN_WEIGHT = 1,
    
    PASS_PAWN_WEIGHT = 1,
    
    RANK_PASS_PAWN_WEIGHT = 1,

    BLOCKED_PAWN_WEIGHT = -1,
    
    BLOCKED_PASSED_PAWN_WEIGHT = -1,
    
    BACKWARD_PAWN_WEIGHT = -1,


    
    MATERIAL_COUNT_KNIGHT_WEIGHT = 200,
    
    KNIGHT_MOBILITY_WEIGHT = 1,
    
    KNIGHT_ON_OUTPOST_WEIGHT = 1,
    
    KNIGHT_ON_CENTER_WEIGHT = 1,
    
    KNIGHT_ON_OUTER_EDGE_1_WEIGHT = 1,
    
    KNIGHT_ON_OUTER_EDGE_2_WEIGHT = 1,
    
    KNIGHT_ON_OUTER_EDGE_3_WEIGHT = 1,
    
    KNIGHT_SUPPORTED_BY_PAWN_WEIGHT = 1,
    

    MATERIAL_COUNT_BISHOP_WEIGHT = 300,
    
    BISHOP_MOBILITY_WEIGHT = 1,

    BISHOP_ON_LARGE_DIAGONAL_WEIGHT = 1,

    BISHOP_PAIR_WEIGHT = 1,

    
    MATERIAL_COUNT_ROOK_WEIGHT = 500,
    
    ROOK_MOBILITY_WEIGHT = 1,

    ROOK_BEHIND_PASS_PAWN = 1,

    ROOK_ON_CLOSED_FILE = -1,

    ROOK_ON_OPEN_FILE = 1,

    ROOK_ON_SEMI_OPEN_FILE = 1,

    ROOKS_CONNECTED_WEIGHT = 1,


    
    MATERIAL_COUNT_QUEEN_WEIGHT = 900,
    
    QUEEN_MOBILITY_WEIGHT = 1,

    
    KING_CASTLED_WEIGHT = 1,

    KING_ATTACKED_VALUE_WEIGHT = -1,

    KING_DEFENDED_VALUE_WEIGHT = 1;

// = = == == == === === ======= === === == == == = = 

    public static boolean showLogs = true;

    public HiveEvaluator(){}

    public static void Log(String msg){

    }

    public static int Evaluate(Board board, HashSet<Move> currentLegalMoves, HashSet<Move> enemyPsuedoLegalMoves,  boolean isWhite){
        return EvaluateTeam(board, currentLegalMoves, enemyPsuedoLegalMoves, isWhite) - EvaluateTeam(board, enemyPsuedoLegalMoves, currentLegalMoves, !isWhite);
    }
    private static int EvaluateTeam(Board board, HashSet<Move> currentLegalMoves, HashSet<Move> enemyPsuedoLegalMoves,  boolean isWhite){
        if(currentLegalMoves.isEmpty()){
            return Integer.MIN_VALUE;
        }

        long[] enemyBoards = BoardUtil.getTeamBoards(board, !isWhite);
        long[] teamBoards = BoardUtil.getTeamBoards(board, isWhite);
        long team = BoardUtil.getTeamBoard(board, isWhite);

        long pawns = teamBoards[0];
        long knights = teamBoards[1];
        long bishops = teamBoards[2];
        long rooks = teamBoards[3];
        long queens = teamBoards[4];
        long king = teamBoards[5];

        long enemyPawns = enemyBoards[0];

        EnumMap<PieceType, HashSet<Move>> movesPieceType = new EnumMap<>(PieceType.class);
        for(Move move : currentLegalMoves){
            if(!movesPieceType.containsKey(move.pieceType())){
                movesPieceType.put(move.pieceType(), (new HashSet<Move>(){{add(move);}}));
            }else{
                HashSet<Move> moves = movesPieceType.get(move.pieceType());
                moves.add(move);
                movesPieceType.put(move.pieceType(), moves);
            }
        }


        int score = 0;

        //GENERAL INFORMATION
        // score += weakCount(currentLegalMoves, enemyBoards)                                              *WEAK_COUNT_WEIGHT;

        //PAWN INFORMATION
        score += materialCount(pawns)                                                                   *MATERIAL_COUNT_PAWN_WEIGHT;
        // score += centerPawnCount(pawns)                                                                 *CENTER_PAWN_COUNT_WEIGHT;
        // score += kingPawnShieldCount(pawns,king)                                                        *KING_PAWN_SHIELD_WEIGHT;
        // score += isolatedPawnCount(pawns)                                                               *ISOLATED_PAWN_WEIGHT;
        // score += doubledPawnCount(pawns)                                                                *DOUBLE_PAWN_WEIGHT;
        // score += passPawnCount(pawns, enemyPawns, isWhite)                                              *PASS_PAWN_WEIGHT;
        // score += rankPassPawnCount()                                                                    *RANK_PASS_PAWN_WEIGHT;
        // score += backwardPawnCount()                                                                    *BACKWARD_PAWN_WEIGHT;
        // score += blockedPawnCount()                                                                     *BLOCKED_PAWN_WEIGHT;
        // score += blockedPassPawnCount()                                                                 *BLOCKED_PASSED_PAWN_WEIGHT;

        //KNIGHT INFORMATION
        score += materialCount(knights)                                                                 *MATERIAL_COUNT_KNIGHT_WEIGHT;
        // score += mobility(isWhite ? PieceType.WHITE_KNIGHT: PieceType.BLACK_KNIGHT, movesPieceType)     *KNIGHT_MOBILITY_WEIGHT;
        // score += knightOnOutpostCount(knights, enemyPawns)                                              *KNIGHT_ON_OUTPOST_WEIGHT;
        // score += knightOnCenterCount(knights)                                                           *KNIGHT_ON_CENTER_WEIGHT;
        // score += knightOnOuterEdge1Count(knights)                                                       *KNIGHT_ON_OUTER_EDGE_1_WEIGHT;
        // score += knightOnOuterEdge2Count(knights)                                                       *KNIGHT_ON_OUTER_EDGE_2_WEIGHT;
        // score += knightOnOuterEdge3Count(knights)                                                       *KNIGHT_ON_OUTER_EDGE_3_WEIGHT;
        // score += knightSupportedByPawnCount(knights, pawns)                                             *KNIGHT_SUPPORTED_BY_PAWN_WEIGHT;

        //BISHOP INFORMATION
        score += materialCount(bishops)                                                                 *MATERIAL_COUNT_BISHOP_WEIGHT;
        // score += mobility(isWhite ? PieceType.WHITE_BISHOP: PieceType.BLACK_BISHOP, movesPieceType)     *BISHOP_MOBILITY_WEIGHT;
        // score += bishopOnLargeDiagonalsCount(bishops)                                                   *BISHOP_ON_LARGE_DIAGONAL_WEIGHT;
        // score += hasBishopPair(bishops)                                                                 *BISHOP_PAIR_WEIGHT;

        //ROOK INFORMATION
        score += materialCount(rooks)                                                                   *MATERIAL_COUNT_ROOK_WEIGHT;
        // score += mobility(isWhite ? PieceType.WHITE_ROOK: PieceType.BLACK_ROOK, movesPieceType)         *ROOK_MOBILITY_WEIGHT;
        // score += rookBehindPassPawnCount()                                                              *ROOK_BEHIND_PASS_PAWN;
        // score += rookOnClosedFileCount(rooks, pawns, enemyPawns)                                        *ROOK_ON_CLOSED_FILE;
        // score += rookOnOpenFileCount(rooks,team)                                                        *ROOK_ON_OPEN_FILE;
        // score += rookOnSemiOpenFileCount(rooks,pawns)                                                   *ROOK_ON_SEMI_OPEN_FILE;
        // score += rooksConnected(rooks,team)                                                             *ROOKS_CONNECTED_WEIGHT;

        //QUEEN INFORMATION
        score += materialCount(queens)                                                                  *MATERIAL_COUNT_QUEEN_WEIGHT;
        // score += mobility(isWhite ? PieceType.WHITE_QUEEN: PieceType.BLACK_QUEEN, movesPieceType)       *QUEEN_MOBILITY_WEIGHT;

        //KING INFORMATION
        score += kingCastled(isWhite, board)                                                            *KING_CASTLED_WEIGHT;
        // score += kingAttackedValue()                                                                    *KING_ATTACKED_VALUE_WEIGHT;
        // score += kingDefendedValue()                                                                    *KING_DEFENDED_VALUE_WEIGHT;

        

        return score;
    }

    // ========== MASKS =============

    static long CENTER_MASK = (1L << 27) | (1L << 28) | (1L << 35) | (1L << 36);

    static long OUTER_EDGE_1 = 0x0000001818000000L;

    static long OUTER_EDGE_2 = 0x00003C24243C0000L;

    static long OUTER_EDGE_3 = 0xFF818181818181FFL;

    static long LARGE_NEGATIVE_DIAGONAL = 0x8040201008040201L;

    static long LARGE_POSITIVE_DIAGONAL = 0x0102040810204080L;


    //------------- general information ----------------

    // returns the number of squares that cannot be protected by this team
    private static int weakCount(HashSet<Move> currentLegalMoves, long[] enemies){
        HashSet<Integer> protectedSquares = new HashSet<>();
        HashSet<Integer> currentSquares = new HashSet<>();
        int enemySquares = 0;
        for(Move move: currentLegalMoves){
            protectedSquares.add(move.toSquare());
            currentSquares.add(move.fromSquare());
        }
        for(long enemy: enemies){
            enemySquares += Long.bitCount(enemy);
        }
        return 64 - currentSquares.size() - enemySquares - protectedSquares.size();
    }

    // returns the amount of this piece
    private static int materialCount(long piece){
        return Long.bitCount(piece);
    }

    // returns the amount of squares this piece can move to
    private static int mobility(PieceType type, EnumMap<PieceType, HashSet<Move>> movesPieceType){
        return movesPieceType.get(type) != null ? movesPieceType.get(type).size() : 0;
    }

    //============= pawn information =============

    // returns the count of pawns in the center squares
    private static int centerPawnCount(long pawns){
        return Long.bitCount(pawns & CENTER_MASK);

    }
    
    // returns the count of pawns immediately surrounding th king
    private static int kingPawnShieldCount(long pawns, long king){
        int kingSquare = Long.numberOfTrailingZeros(king); // Find the position of the king
        int count = 0;

        // Define the relative positions of the squares adjacent to the king
        int[] adjacentOffsets = {-9, -8, -7, -1, 1, 7, 8, 9};

        for (int offset : adjacentOffsets) {
            int adjacentSquare = kingSquare + offset;

            // Check if the adjacent square is a valid square on the board
            if (adjacentSquare >= 0 && adjacentSquare < 64) {
                long adjacentMask = 1L << adjacentSquare;

                // Check if the adjacent square is occupied by a pawn
                if ((pawns & adjacentMask) != 0) {
                    count++;
                }
            }
        }

        return count;

    }

    // returns the count of isolated pawns (TODO: idk what counts as isolated)
    private static int isolatedPawnCount(long pawns){
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if ((pawns & (1L << i)) != 0) { // If there is a pawn at this square
                int file = i % 8;
                long adjacentFiles = 0L;
                if (file > 0) adjacentFiles |= (pawns >> 1) & 0x7F7F7F7F7F7F7F7FL;
                if (file < 7) adjacentFiles |= (pawns << 1) & 0xFEFEFEFEFEFEFEFEL;
                if (adjacentFiles == 0) {
                    count++;
                }
            }
        }
        return count;

    }
    
    // returns the count of doubled pawns, 
    // doubled pawns are 2 or more pawns on the same file
    private static int doubledPawnCount(long pawns){
        int count = 0;
        for (int file = 0; file < 8; file++) {
            long fileMask = 0x0101010101010101L << file;
            long pawnsOnFile = pawns & fileMask;
            int numPawnsOnFile = Long.bitCount(pawnsOnFile);
            if (numPawnsOnFile > 1) {
                count += (numPawnsOnFile - 1);
            }
        }
        return count;

    }

    // returns the count of pass pawns
    // pass pawn has no enemy pawns on its column and the neighboring columns
    private static int passPawnCount(long pawns, long enemyPawns, boolean isWhite){
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if ((pawns & (1L << i)) != 0) { // If there is a pawn at this square
                int file = i % 8;
                long fileMask = 0x0101010101010101L << file;
                long adjacentFiles = 0L;
                if (file > 0) adjacentFiles |= fileMask >> 1;
                if (file < 7) adjacentFiles |= fileMask << 1;
                long blockingFiles = fileMask | adjacentFiles;

                // Only consider squares in front of the pawn
                if (isWhite) {
                    blockingFiles &= ~((1L << i) - 1);
                } else {
                    blockingFiles &= -1L << i;
                }

                if ((enemyPawns & blockingFiles) == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    // todo
    private static int rankPassPawnCount(){
        return 0;
    }

    // todo
    private static int backwardPawnCount(){
        return 0;
    }

    // todo
    private static int blockedPawnCount(){
        return 0;
    }

    // todo
    private static int blockedPassPawnCount(){
        return 0;
    }

    //=========== knight information ============

    // returns the count of knights on an outpost
    // an outpost is a square that is not being attacked by enemy pawns
    // todo: 
    private static int knightOnOutpostCount(long knights, long enemyPawns){
        return 0;
    }

    // todo:
    // return the count of knights that are protected by a friendly pawn
    private static int knightSupportedByPawnCount(long knights, long pawns){
        return 0;
    }

    // returns the count of knights on the 4 center squares
    private static int knightOnCenterCount(long knights){
        return Long.bitCount(knights & CENTER_MASK);
    }

    // returns the count of knights on the closest ring around the center
    private static int knightOnOuterEdge1Count(long knights){
        return Long.bitCount(knights & OUTER_EDGE_1);
    }

    // returns the count of knights on the medium ring around the board
    private static int knightOnOuterEdge2Count(long knights){
        return Long.bitCount(knights & OUTER_EDGE_2);
    }

    // returns the count of knights the outer most edge of the board
    private static int knightOnOuterEdge3Count(long knights){
        return Long.bitCount(knights & OUTER_EDGE_3);
    }

    //--------------- bishop information ----------------
    
    // returns the count of bishops on large diagonals
    private static int bishopOnLargeDiagonalsCount(long bishops){
        return Long.bitCount(bishops & (LARGE_NEGATIVE_DIAGONAL | LARGE_POSITIVE_DIAGONAL));
    }

    // returns 1 if there are two or more bishop
    private static int hasBishopPair(long bishops){
        return Long.bitCount(bishops) > 1 ? 1 : 0;
    }

    //--------------- rook information ----------------

    // returns the count of rooks behind a pass pawn
    private static int rookBehindPassPawnCount(){
        return 0;
    }


    private static int rookOnOpenFileCount(long rooks, long board){
        return 0;
    }

    private static int rookOnSemiOpenFileCount(long rooks, long pawns){
        return 0;
    }

    private static int rookOnClosedFileCount(long rooks, long pawns, long enemyPawns){
        return 0;
    }

    private static int rooksConnected(long rooks, long board){
        return 0;
    }



    //--------------- queen information ----------------

    //--------------- king information ----------------
    
    // return 1 if this team's king has castled
    private static int kingCastled(boolean isWhite, Board board){
        return (isWhite && board.WHITE_CASTLED) || (!isWhite && board.BLACK_CASTLED) ? 1 : 0;
    }


}
