package main.java.hive;

public record HiveWeights(

        int WEAK_COUNT_WEIGHT,
        
        int MATERIAL_COUNT_PAWN_WEIGHT,
        
        int CENTER_PAWN_COUNT_WEIGHT,
        
        int KING_PAWN_SHIELD_WEIGHT,
        
        int ISOLATED_PAWN_WEIGHT,
        
        int DOUBLE_PAWN_WEIGHT,
        
        int RANK_PASSED_PAWN_WEIGHT,
        
        int PASS_PAWN_WEIGHT,

        int BLOCKED_PAWN_WEIGHT,
        
        int BLOCKED_PASSED_PAWN_WEIGHT,
        
        int BACKWARD_PAWN_WEIGHT,


        
        int MATERIAL_COUNT_KNIGHT_WEIGHT,
        
        int KNIGHT_MOBILITY_WEIGHT,
        
        int KNIGHT_ON_OUTPOST_WEIGHT,
        
        int KNIGHT_ON_CENTER_WEIGHT,
        
        int KNIGHT_ON_OUTER_EDGE_1_WEIGHT,
        
        int KNIGHT_ON_OUTER_EDGE_2_WEIGHT,
        
        int KNIGHT_ON_OUTER_EDGE_3_WEIGHT,
        
        int KNIGHT_SUPPORTED_BY_PAWN_WEIGHT,
        

        int MATERIAL_COUNT_BISHOP_WEIGHT,
        
        int BISHOP_MOBILITY_WEIGHT,

        int BISHOP_ON_LARGE_DIAGONAL_WEIGHT,

        int BISHOP_PAIR_WEIGHT,

        
        int MATERIAL_COUNT_ROOK_WEIGHT,
        
        int ROOK_MOBILITY_WEIGHT,

        int ROOK_BEHIND_PASS_PAWN,

        int ROOK_ON_CLOSED_FILE,

        int ROOK_ON_OPEN_FILE,

        int ROOK_ON_SEMI_OPEN_FILE,

        int ROOKS_CONNECTED_WEIGHT,


        
        int MATERIAL_COUNT_QUEEN_WEIGHT,
        
        int QUEEN_MOBILITY_WEIGHT,

        
        int KING_CASTLED_WEIGHT,

        int KING_ATTACKED_VALUE_WEIGHT,

        int KING_DEFENDED_VALUE_WEIGHT
) {


}
