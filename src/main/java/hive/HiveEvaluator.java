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
    
    
    MATERIAL_COUNT_PAWN_WEIGHT = 1, 
    
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


    
    MATERIAL_COUNT_KNIGHT_WEIGHT = 1,
    
    KNIGHT_MOBILITY_WEIGHT = 1,
    
    KNIGHT_ON_OUTPOST_WEIGHT = 1,
    
    KNIGHT_ON_CENTER_WEIGHT = 1,
    
    KNIGHT_ON_OUTER_EDGE_1_WEIGHT = 1,
    
    KNIGHT_ON_OUTER_EDGE_2_WEIGHT = 1,
    
    KNIGHT_ON_OUTER_EDGE_3_WEIGHT = 1,
    
    KNIGHT_SUPPORTED_BY_PAWN_WEIGHT = 1,
    

    MATERIAL_COUNT_BISHOP_WEIGHT = 1,
    
    BISHOP_MOBILITY_WEIGHT = 1,

    BISHOP_ON_LARGE_DIAGONAL_WEIGHT = 1,

    BISHOP_PAIR_WEIGHT = 1,

    
    MATERIAL_COUNT_ROOK_WEIGHT = 1,
    
    ROOK_MOBILITY_WEIGHT = 1,

    ROOK_BEHIND_PASS_PAWN = 1,

    ROOK_ON_CLOSED_FILE = -1,

    ROOK_ON_OPEN_FILE = 1,

    ROOK_ON_SEMI_OPEN_FILE = 1,

    ROOKS_CONNECTED_WEIGHT = 1,


    
    MATERIAL_COUNT_QUEEN_WEIGHT = 1,
    
    QUEEN_MOBILITY_WEIGHT = 1,

    
    KING_CASTLED_WEIGHT = 1,

    KING_ATTACKED_VALUE_WEIGHT = -1,

    KING_DEFENDED_VALUE_WEIGHT = 1;

// = = == == == === === ======= === === == == == = = 

    public HiveEvaluator(){}

    public static int Evaluate(Board board, HashSet<Move> currentLegalMoves, boolean isWhite){
        if(currentLegalMoves.isEmpty()){
            return Integer.MIN_VALUE;
        }

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
        score += weakCount(currentLegalMoves, BoardUtil.getTeamBoards(board, !isWhite))                                             *WEAK_COUNT_WEIGHT;

        // PAWNS
        score += materialCount(isWhite ? board.WHITE_PAWNS : board.BLACK_PAWNS)                                                     *MATERIAL_COUNT_PAWN_WEIGHT;

        // KNIGHTS
        score += materialCount(isWhite ? board.WHITE_KNIGHTS : board.BLACK_KNIGHTS)                                                 *MATERIAL_COUNT_KNIGHT_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN, movesPieceType)                                    *KNIGHT_MOBILITY_WEIGHT;

        // BISHOPS
        score += materialCount(isWhite ? board.WHITE_BISHOPS : board.BLACK_BISHOPS)                                                 *MATERIAL_COUNT_BISHOP_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_BISHOP : PieceType.BLACK_BISHOP, movesPieceType)                                *BISHOP_MOBILITY_WEIGHT;

        // ROOKS
        score += materialCount(isWhite ? board.WHITE_ROOKS : board.BLACK_ROOKS)                                                     *MATERIAL_COUNT_ROOK_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_ROOK : PieceType.BLACK_ROOK, movesPieceType)                                    *ROOK_MOBILITY_WEIGHT;

        // QUEENS
        score += materialCount(isWhite ? board.WHITE_QUEENS : board.BLACK_QUEENS)                                                   *MATERIAL_COUNT_QUEEN_WEIGHT;
        score += mobility(isWhite ? PieceType.WHITE_QUEEN : PieceType.BLACK_QUEEN, movesPieceType)                                  *QUEEN_MOBILITY_WEIGHT;

        // KINGS

        return score;
    }

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

    private static int materialCount(long piece){
        return Long.bitCount(piece);
    }

    private static int mobility(PieceType type, EnumMap<PieceType, HashSet<Move>> movesPieceType){
        return movesPieceType.get(type) != null ? movesPieceType.get(type).size() : 0;
    }
}
