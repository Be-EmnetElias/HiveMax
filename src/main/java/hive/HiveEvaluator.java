package main.java.hive;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.board.PieceType;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import main.java.move.MoveType;

// todo: when training, will need mulitple evalutors with different weights, need to make this class NON-STATIC, or keep the functions static and import weights instead of using fields
public class HiveEvaluator {
    
// = = == == == === === WEIGHTS === === == == == = = 

    public static int 

    WEAK_COUNT_WEIGHT = 1, 
    
    
    MATERIAL_COUNT_PAWN_WEIGHT = 100, 
    
    CENTER_PAWN_COUNT_WEIGHT = 25,
    
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

    public static boolean showLogs = false;

    public HiveEvaluator(){}

    public static void Log(String msg){
        if(showLogs){
            System.out.println("[ Hive Evaluate ] " + msg + "\n");
        }
    }

    /**
     * Returns a static evaluation of this board by subtracting blacks score from whites score
     * @param board
     * @param isWhite
     * @return
     */
    public static int Evaluate(Board board){

        //todo load weights?

        // when evaluating, look at the psuedo legal moves for better information
        // for example getting all pawn moves including captures, instead of only the legal ones
        List<Move> whiteMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, false, false, true);
        List<Move> blackMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, true, false, true);
        int whiteScore = EvaluateTeam(board, whiteMoves, blackMoves, true);
        int blackScore = EvaluateTeam(board, blackMoves, whiteMoves, false);

        return whiteScore - blackScore;
    }
    public static int EvaluateTeam(Board board, List<Move> currentLegalMoves, List<Move> enemyPsuedoLegalMoves,  boolean isWhite){

        // todo: check end game conditions
        // if no legal moves left, determine if checkmate (-infinity score) or stalemate(0 score)
        // check for insufficient material draw(0 score)
        // check for 50 move rule
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

        EnumMap<PieceType, List<Move>> movesPieceType = movesPerPieceType(currentLegalMoves);
        EnumMap<PieceType, List<Move>> enemyMovesPieceType = movesPerPieceType(enemyPsuedoLegalMoves);

        long passPawns = passPawns(pawns, enemyPawns, isWhite);
        long squaresAttackedByFriendlyPawns = squaresAttackedByPawns(movesPieceType, isWhite ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN);
        long squaresNotAttackedEnemyPawns = ~squaresAttackedByPawns(enemyMovesPieceType, isWhite ? PieceType.BLACK_PAWN : PieceType.WHITE_PAWN);


        int score = 0;

        //GENERAL INFORMATION
        score += weakCount(currentLegalMoves, enemyBoards)                                              *WEAK_COUNT_WEIGHT;

        //PAWN INFORMATION
        score += materialCount(pawns)                                                                   *MATERIAL_COUNT_PAWN_WEIGHT;
        score += centerPawnCount(pawns)                                                                 *CENTER_PAWN_COUNT_WEIGHT;
        score += kingPawnShieldCount(pawns,king)                                                        *KING_PAWN_SHIELD_WEIGHT;
        score += isolatedPawnCount(pawns)                                                               *ISOLATED_PAWN_WEIGHT;
        score += doubledPawnCount(pawns)                                                                *DOUBLE_PAWN_WEIGHT;
        score += passPawnCount(pawns, enemyPawns, isWhite)                                              *PASS_PAWN_WEIGHT;
        score += rankPassPawnCount()                                                                    *RANK_PASS_PAWN_WEIGHT;
        score += backwardPawnCount()                                                                    *BACKWARD_PAWN_WEIGHT;
        score += blockedPawnCount()                                                                     *BLOCKED_PAWN_WEIGHT;
        score += blockedPassPawnCount()                                                                 *BLOCKED_PASSED_PAWN_WEIGHT;

        //KNIGHT INFORMATION
        score += materialCount(knights)                                                                 *MATERIAL_COUNT_KNIGHT_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_KNIGHT: PieceType.BLACK_KNIGHT, movesPieceType)     *KNIGHT_MOBILITY_WEIGHT;
        score += knightOnOutpostCount(knights, squaresNotAttackedEnemyPawns)                            *KNIGHT_ON_OUTPOST_WEIGHT;
        score += knightSupportedByPawnCount(knights, squaresAttackedByFriendlyPawns)                    *KNIGHT_SUPPORTED_BY_PAWN_WEIGHT;
        score += knightOnCenterCount(knights)                                                           *KNIGHT_ON_CENTER_WEIGHT;
        score += knightOnOuterEdge1Count(knights)                                                       *KNIGHT_ON_OUTER_EDGE_1_WEIGHT;
        score += knightOnOuterEdge2Count(knights)                                                       *KNIGHT_ON_OUTER_EDGE_2_WEIGHT;
        score += knightOnOuterEdge3Count(knights)                                                       *KNIGHT_ON_OUTER_EDGE_3_WEIGHT;

        //BISHOP INFORMATION
        score += materialCount(bishops)                                                                 *MATERIAL_COUNT_BISHOP_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_BISHOP: PieceType.BLACK_BISHOP, movesPieceType)     *BISHOP_MOBILITY_WEIGHT;
        score += bishopOnLargeDiagonalsCount(bishops)                                                   *BISHOP_ON_LARGE_DIAGONAL_WEIGHT;
        score += hasBishopPair(bishops)                                                                 *BISHOP_PAIR_WEIGHT;

        //ROOK INFORMATION
        score += materialCount(rooks)                                                                   *MATERIAL_COUNT_ROOK_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_ROOK: PieceType.BLACK_ROOK, movesPieceType)         *ROOK_MOBILITY_WEIGHT;
        score += rookBehindPassPawnCount()                                                              *ROOK_BEHIND_PASS_PAWN;
        score += rookOnClosedFileCount(rooks, pawns, enemyPawns)                                        *ROOK_ON_CLOSED_FILE;
        score += rookOnOpenFileCount(rooks,team)                                                        *ROOK_ON_OPEN_FILE;
        score += rookOnSemiOpenFileCount(rooks,pawns)                                                   *ROOK_ON_SEMI_OPEN_FILE;
        score += rooksConnected(rooks,team)                                                             *ROOKS_CONNECTED_WEIGHT;

        //QUEEN INFORMATION
        score += materialCount(queens)                                                                  *MATERIAL_COUNT_QUEEN_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_QUEEN: PieceType.BLACK_QUEEN, movesPieceType)       *QUEEN_MOBILITY_WEIGHT;

        //KING INFORMATION
        score += kingCastled(isWhite, board)                                                            *KING_CASTLED_WEIGHT;
        // score += kingAttackedValue()                                                                    *KING_ATTACKED_VALUE_WEIGHT;
        // score += kingDefendedValue()                                                                    *KING_DEFENDED_VALUE_WEIGHT;

        

        return score;
    }

    // ========== MASKS =============

    static long CENTER_MASK = (1L << 27) | (1L << 28) | (1L << 35) | (1L << 36);

    static long FILE_MASK = 0xff00000000000000L; //left most rank

    static long RANK_MASK = 0xff00000000000000L; //rank 0

    static long OUTER_EDGE_1 = 0x0000001818000000L;

    static long OUTER_EDGE_2 = 0x00003C24243C0000L;

    static long OUTER_EDGE_3 = 0xFF818181818181FFL;

    static long LARGE_NEGATIVE_DIAGONAL = 0x8040201008040201L;

    static long LARGE_POSITIVE_DIAGONAL = 0x0102040810204080L;


    //------------- helpers --------------

    // returns a long of pass pawns
    // pass pawns have no enemy pawns in front of it and on adjacent files
    private static long passPawns(long pawns, long enemyPawns, boolean isWhite){
        long passPawns = 0L;

        while(pawns != 0){
            int pawnSquare = Long.numberOfTrailingZeros(pawns);
            long pawnFile = FILE_MASK << (pawnSquare / 8);
            long pawnRank = RANK_MASK << (pawnSquare % 8);


            // long enemyPawnSameFile = enemyPawns
            pawns &= pawns - 1;
            break;
        }

        return passPawns;
    }
    // returns a map with key/value -> PieceType/List<Move> from a list of moves
    private static EnumMap<PieceType, List<Move>> movesPerPieceType(List<Move> legalMoves){
        EnumMap<PieceType, List<Move>> movesPieceType = new EnumMap<>(PieceType.class);
        for(Move move : legalMoves){
            if(!movesPieceType.containsKey(move.pieceType())){
                List<Move> moves = new ArrayList<>();
                moves.add(move);
                movesPieceType.put(move.pieceType(), moves);
            }else{
                List<Move> moves = movesPieceType.get(move.pieceType());
                moves.add(move);
                movesPieceType.put(move.pieceType(), moves);
            }
        }
        return movesPieceType;
    }

    // returns a binary long of which squares are not being attacked by this team's pawns
    private static long squaresAttackedByPawns(EnumMap<PieceType, List<Move>> enemyMovesPieceType, PieceType pawn){
        long squaresAttackedPawns = 0L;
        if(enemyMovesPieceType.keySet().contains(pawn)){
            for(Move move : enemyMovesPieceType.get(pawn)){
                if(move.moveType() == MoveType.CAPTURE){
                    squaresAttackedPawns |= (1L << move.toSquare());
                }
            }
        }
        return squaresAttackedPawns;
    }
    
    
    //------------- general information ----------------

    // returns the number of squares that cannot be protected by this team
    private static int weakCount(List<Move> currentLegalMoves, long[] enemies){
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
    private static int mobility(PieceType type, EnumMap<PieceType, List<Move>> movesPieceType){
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

    //todo
    // returns the count of isolated pawns (TODO: idk what counts as isolated)
    private static int isolatedPawnCount(long pawns){
        return 0;

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

    //todo
    // returns the count of pass pawns
    // a pass pawn has no enemy pawns on the ranks in front of its file and the 2 adjacent files
    private static int passPawnCount(long pawns, long enemyPawns, boolean isWhite){
        // int count = 0;
        // for (int i = 0; i < 64; i++) {
        //     if ((pawns & (1L << i)) != 0) { // If there is a pawn at this square
        //         int file = i % 8;
        //         long fileMask = 0x0101010101010101L << file;
        //         long adjacentFiles = 0L;
        //         if (file > 0) adjacentFiles |= fileMask >> 1;
        //         if (file < 7) adjacentFiles |= fileMask << 1;
        //         long blockingFiles = fileMask | adjacentFiles;

        //         // Only consider squares in front of the pawn
        //         if (isWhite) {
        //             blockingFiles &= ~((1L << i) - 1);
        //         } else {
        //             blockingFiles &= -1L << i;
        //         }

        //         if ((enemyPawns & blockingFiles) == 0) {
        //             count++;
        //         }
        //     }
        // }
        // return count;
        return 0;
    }

    // todo
    // returns the combined ranks of all pass pawns
    private static int rankPassPawnCount(){
        return 0;
    }

    // todo
    // returns the count of backward pawns
    // a backward pawn has no friendly pawns on the ranks behind it
    private static int backwardPawnCount(){
        return 0;
    }

    // todo
    // returns the count of blocked pawns
    // a blocked pawn has a non-pawn piece on the ranks in front of it
    private static int blockedPawnCount(){
        return 0;
    }

    // todo
    // returns the count of blocked pass pawns
    // reuturns the count of pass pawns that are blocked
    private static int blockedPassPawnCount(){
        return 0;
    }

    //=========== KNIGHT INFORMATION ============

    // returns the count of knights on an outpost
    // an outpost is a square that is not being attacked by enemy pawns
    private static int knightOnOutpostCount(long knights, long squaresNotAttackedEnemyPawns){
        return Long.bitCount(knights & squaresNotAttackedEnemyPawns);
    }

    // return the count of knights that are protected by a friendly pawn
    private static int knightSupportedByPawnCount(long knights, long squaresAttackedByFriendlyPawns){
        return Long.bitCount(knights & squaresAttackedByFriendlyPawns);
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

    //--------------- BISHOP INFORMATION ----------------
    
    // returns the count of bishops on large diagonals
    private static int bishopOnLargeDiagonalsCount(long bishops){
        return Long.bitCount(bishops & (LARGE_NEGATIVE_DIAGONAL | LARGE_POSITIVE_DIAGONAL));
    }

    // returns 1 if there are two or more bishop
    private static int hasBishopPair(long bishops){
        return Long.bitCount(bishops) > 1 ? 1 : 0;
    }

    //--------------- ROOK INFORMATION ----------------

    // returns the count of rooks behind a pass pawn
    //todo
    private static int rookBehindPassPawnCount(){
        return 0;
    }

    //todo
    private static int rookOnOpenFileCount(long rooks, long board){
        return 0;
    }

    //todo
    private static int rookOnSemiOpenFileCount(long rooks, long pawns){
        return 0;
    }


    //todo
    private static int rookOnClosedFileCount(long rooks, long pawns, long enemyPawns){
        return 0;
    }

    //todo
    // returns 1 if the rooks are on the same file or same rank, 0 otherwise
    private static int rooksConnected(long rooks, long board){
       return 0;
    }



    //--------------- queen information ----------------
    //todo

    //--------------- king information ----------------
    
    // return 1 if this team's king has castled
    private static int kingCastled(boolean isWhite, Board board){
        return (isWhite && board.WHITE_CASTLED) || (!isWhite && board.BLACK_CASTLED) ? 1 : 0;
    }


}
