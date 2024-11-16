package main.java.hive;

import java.util.EnumMap;
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
    
    public static boolean showLogs = false;

    public static HiveWeights SUBJECT_YMIR_WEIGHTS = new HiveWeights(
        -1,
        100,
        1,
        1,
        -1,
        -1,
        1,
        1,
        -1,
        -1,
        -1,
        200,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        300,
        1,
        1,
        1,
        500,
        0,
        1,
        -1,
        1,
        1,
        1,
        900,
        1,
        1,
        0,
        0 
    );

    /**
     * Returns a static evaluation of this board by subtracting blacks score from whites score
     * @param board
     * @param isWhite
     * @return
     */
    public static int Evaluate(Board board, HiveWeights weights){

        //todo load weights?

        // when evaluating, look at the psuedo legal moves for better information
        // for example getting all pawn moves including captures, instead of only the legal ones
        List<Move> whiteMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, false, false, true);
        List<Move> blackMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, true, false, true);
        int whiteScore = EvaluateTeam(board, weights, whiteMoves, blackMoves, true);
        int blackScore = EvaluateTeam(board, weights, blackMoves, whiteMoves, false);

        return whiteScore - blackScore;
    }
    
    public static int EvaluateTeam(Board board, HiveWeights weights, List<Move> currentLegalMoves, List<Move> enemyPsuedoLegalMoves,  boolean isWhite){

        int WEAK_COUNT_WEIGHT                 = weights.WEAK_COUNT_WEIGHT(); 
        int MATERIAL_COUNT_PAWN_WEIGHT        = weights.MATERIAL_COUNT_PAWN_WEIGHT(); 
        int CENTER_PAWN_COUNT_WEIGHT          = weights.CENTER_PAWN_COUNT_WEIGHT();
        int KING_PAWN_SHIELD_WEIGHT           = weights.KING_PAWN_SHIELD_WEIGHT();
        int ISOLATED_PAWN_WEIGHT              = weights.ISOLATED_PAWN_WEIGHT();
        int DOUBLE_PAWN_WEIGHT                = weights.DOUBLE_PAWN_WEIGHT();
        int RANK_PASSED_PAWN_WEIGHT           = weights.RANK_PASSED_PAWN_WEIGHT();
        int PASS_PAWN_WEIGHT                  = weights.PASS_PAWN_WEIGHT();
        int BLOCKED_PAWN_WEIGHT               = weights.BLOCKED_PAWN_WEIGHT();
        int BLOCKED_PASSED_PAWN_WEIGHT        = weights.BLOCKED_PASSED_PAWN_WEIGHT();
        int BACKWARD_PAWN_WEIGHT              = weights.BACKWARD_PAWN_WEIGHT();
        int MATERIAL_COUNT_KNIGHT_WEIGHT      = weights.MATERIAL_COUNT_KNIGHT_WEIGHT();
        int KNIGHT_MOBILITY_WEIGHT            = weights.KNIGHT_MOBILITY_WEIGHT();
        int KNIGHT_ON_OUTPOST_WEIGHT          = weights.KNIGHT_ON_OUTPOST_WEIGHT();
        int KNIGHT_ON_CENTER_WEIGHT           = weights.KNIGHT_ON_CENTER_WEIGHT();
        int KNIGHT_ON_OUTER_EDGE_1_WEIGHT     = weights.KNIGHT_ON_OUTER_EDGE_1_WEIGHT();
        int KNIGHT_ON_OUTER_EDGE_2_WEIGHT     = weights.KNIGHT_ON_OUTER_EDGE_2_WEIGHT();
        int KNIGHT_ON_OUTER_EDGE_3_WEIGHT     = weights.KNIGHT_ON_OUTER_EDGE_3_WEIGHT();
        int KNIGHT_SUPPORTED_BY_PAWN_WEIGHT   = weights.KNIGHT_SUPPORTED_BY_PAWN_WEIGHT();
        int MATERIAL_COUNT_BISHOP_WEIGHT      = weights.MATERIAL_COUNT_BISHOP_WEIGHT();
        int BISHOP_MOBILITY_WEIGHT            = weights.BISHOP_MOBILITY_WEIGHT();
        int BISHOP_ON_LARGE_DIAGONAL_WEIGHT   = weights.BISHOP_ON_LARGE_DIAGONAL_WEIGHT();
        int BISHOP_PAIR_WEIGHT                = weights.BISHOP_PAIR_WEIGHT();
        int MATERIAL_COUNT_ROOK_WEIGHT        = weights.MATERIAL_COUNT_ROOK_WEIGHT();
        int ROOK_MOBILITY_WEIGHT              = weights.ROOK_MOBILITY_WEIGHT();
        int ROOK_BEHIND_PASS_PAWN             = weights.ROOK_BEHIND_PASS_PAWN();
        int ROOK_ON_CLOSED_FILE               = weights.ROOK_ON_CLOSED_FILE();
        int ROOK_ON_OPEN_FILE                 = weights.ROOK_ON_OPEN_FILE();
        int ROOK_ON_SEMI_OPEN_FILE            = weights.ROOK_ON_SEMI_OPEN_FILE();
        int ROOKS_CONNECTED_WEIGHT            = weights.ROOKS_CONNECTED_WEIGHT();
        int MATERIAL_COUNT_QUEEN_WEIGHT       = weights.MATERIAL_COUNT_QUEEN_WEIGHT();
        int QUEEN_MOBILITY_WEIGHT             = weights.QUEEN_MOBILITY_WEIGHT();
        int KING_CASTLED_WEIGHT               = weights.KING_CASTLED_WEIGHT();
        int KING_ATTACKED_VALUE_WEIGHT        = weights.KING_ATTACKED_VALUE_WEIGHT();
        int KING_DEFENDED_VALUE_WEIGHT        = weights.KING_DEFENDED_VALUE_WEIGHT();

        // todo: check end game conditions
        // if no legal moves left, determine if checkmate (-infinity score) or stalemate(0 score)
        // check for insufficient material draw(0 score)
        // check for 50 move rule
        if(currentLegalMoves.isEmpty()){
            for(Move move : enemyPsuedoLegalMoves){
                if(move.moveType() == MoveType.CAPTURE && BoardUtil.isSquareOnBoard(move.toSquare(), isWhite ? board.WHITE_KINGS : board.BLACK_KINGS)){
                    return Integer.MIN_VALUE;
                }
            }
            return 0;
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
        score += passPawnCount(passPawns)                                                               *PASS_PAWN_WEIGHT;
        score += rankPassPawnCount(passPawns, isWhite)                                                  *RANK_PASSED_PAWN_WEIGHT;
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

    static long FILE_MASK = 0x101010101010101L; //left most rank

    static long RANK_MASK = 0xff00000000000000L; //rank 0

    static long OUTER_EDGE_1 = 0x0000001818000000L;

    static long OUTER_EDGE_2 = 0x00003C24243C0000L;

    static long OUTER_EDGE_3 = 0xFF818181818181FFL;

    static long LARGE_NEGATIVE_DIAGONAL = 0x8040201008040201L;

    static long LARGE_POSITIVE_DIAGONAL = 0x0102040810204080L;


    //------------- helpers --------------

    public static long shiftFile(long file, int amount){
        if(amount >= 0){
            return file << amount;
        }else{
            return file >> Math.abs(amount);
        }
    }

    public static long shiftRank(long rank, int amount){
        if(amount >= 0){
            return rank >>> ((7-amount) * 8); 

        }else{
            return rank << ((7-Math.abs(amount)) * 8); 

        }
    }

    public static long shiftRank(long rank, int amount, boolean isWhite){
        return isWhite ? ~(RANK_MASK >> ((7 - amount) * 8)) : RANK_MASK >> ((6-amount) * 8);

    }

    // returns a long of pass pawns
    // pass pawns have no enemy pawns in front of it and on adjacent files
    private static long passPawns(long pawns, long enemyPawns, boolean isWhite){
        long passPawns = 0L;

        while(pawns != 0){
            int pawnSquare = Long.numberOfTrailingZeros(pawns);
            int pawnFile = (pawnSquare % 8);
            int pawnRank = (pawnSquare / 8);

            long pawnFileMask = shiftFile(FILE_MASK, pawnFile);
            long pawnRankMask = shiftRank(RANK_MASK, pawnRank, isWhite);
            long enemyPawnsAhead = enemyPawns & pawnRankMask;
       
            boolean enemyPawnSameFile = Long.bitCount(enemyPawnsAhead & pawnFileMask) > 0;
            boolean enemyPawnLeftFile = false;
            boolean enemyPawnRightFile = false;

            if(pawnFile > 0){
                enemyPawnLeftFile = Long.bitCount(enemyPawnsAhead & (pawnFileMask >> 1)) > 0;
            }

            if(pawnFile < 7){
                enemyPawnRightFile = Long.bitCount(enemyPawnsAhead & (pawnFileMask << 1)) > 0;
            }

            if(!enemyPawnLeftFile && !enemyPawnRightFile && !enemyPawnSameFile){
                passPawns = BoardUtil.setBit(passPawns, pawnSquare);
            }
            
            pawns &= pawns - 1;
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

    // returns the count of pass pawns
    private static int passPawnCount(long passPawns){
        return Long.bitCount(passPawns);
    }

    // returns the combined ranks of all pass pawns
    // a rank in this case will be how far 'up' this pawn has traveled
    // (basically the absolute distance traveled to account for both black/white pawns)
    private static int rankPassPawnCount(long passPawns, boolean isWhite){
        int score = 0;
        while(passPawns != 0){
            int pawnSquare = Long.numberOfTrailingZeros(passPawns);
            int pawnRank = (pawnSquare / 8);
            int actualRank = isWhite ? (6 - pawnRank) : pawnRank;
            score += actualRank;
            
            passPawns &= passPawns - 1;
        }

        return score;
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
    // returns the count of pass pawns that are blocked
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

    // returns the count of rooks behind a different pass pawn
    //todo
    private static int rookBehindPassPawnCount(){
        return 0;
    }

    //todo
    // returns the coutn of rooks on an open file
    // an open file has no enemy pieces
    private static int rookOnOpenFileCount(long rooks, long board){
        return 0;
    }

    //todo
    // returns the count of rooks on an semi-open file
    // a semi-open file has no enemy pawns
    private static int rookOnSemiOpenFileCount(long rooks, long pawns){
        return 0;
    }


    //todo
    // returns the count of rooks on a closed file
    // a close filed has a friendly pawn
    private static int rookOnClosedFileCount(long rooks, long pawns, long enemyPawns){
        return 0;
    }

    //todo
    // returns 1 if the rooks are on the same file or same rank with no other team pieces in between, 0 otherwise
    private static int rooksConnected(long rooks, long teamBoard){
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
