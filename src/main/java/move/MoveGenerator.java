package main.java.move;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.board.PieceType;

public class MoveGenerator{

    public static boolean showLogs = false;

    public MoveGenerator(){}
    
    public static void Log(String msg){
        if(showLogs){
            System.out.println("[Move Generator] " + msg);
        }
    }

    public static List<Move> getCurrentLegalMoves(Board board){
        return getCurrentLegalMoves(board, board.IS_WHITE_TURN);
    }

    /** 
     * * To get current legal moves:
     * * (1) calculate enemy attacking pseudo legal moves
     * * (2) extract the danger squares and determine if king in single or double check
     * * (3) 
     * *        (a) If neither, return psuedo legal moves accordingly
     * *        (b) If single check, calculate capture and push mask and return according psuedo legal moves
     * *        (c) If double check, return king moves accordingly to danger squares
     * @param isWhite
     * @return List<Move>
     */
    public static List<Move> getCurrentLegalMoves(Board board, boolean isWhite){
        Log("=== Current Legal Moves for " + (isWhite ? "WHITE":"BLACK") + " ===");
        int kingPosition = BoardUtil.getKingSquare(board, isWhite);
        HashSet<Integer> dangerSquares = new HashSet<>();
        boolean singleCheck = false;
        boolean doubleCheck = false;
        int captureMask = BoardUtil.NULL_CAPTURE_MASK;
        long pushMask = BoardUtil.NULL_PUSH_MASK;
        PieceType checkingPiece = PieceType.EMPTY;
        
        // get enemy psuedo legal moves
        List<Move> enemyPsuedoLegalMoves = getEnemyPsuedoLegalMoves(board, isWhite, true, false);

        Log("\tDanger Squares: ");

        //todo
        // always add enemy king squares

        // extract danger squares and determine if single or double check
        // king is in single check if the enemy move attacks the king
        // king is in double check if a different enemy move is also attacking the king
        for(Move enemyMove : enemyPsuedoLegalMoves){
            int toSquare = enemyMove.toSquare();
            Log("\t\t" + toSquare + " by " + enemyMove.pieceType());
            dangerSquares.add(toSquare);
            if(!doubleCheck && (toSquare == kingPosition)){

                if(!singleCheck){
                    singleCheck = true;
                    captureMask = enemyMove.fromSquare();
                    checkingPiece = enemyMove.pieceType();
                }else{
                    if(captureMask != enemyMove.fromSquare()){
                        doubleCheck = true;
                    }
                }
            }
        }

        // return moves accordingly
        if(doubleCheck){
            List<Move> kingMoves = getKingMoves(board, kingPosition, BoardUtil.getTeamBoard(board, isWhite), dangerSquares, isWhite);
            Log("\tKing in double check " + kingMoves.size() + " MOVES");
            return kingMoves;
        }else{
        
            //calculate pinned pieces
            long[] enemySlidingPieces = isWhite ? new long[]{board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS} : new long[]{board. WHITE_BISHOPS, board.WHITE_ROOKS, board.WHITE_QUEENS};
            HashMap<Integer, Integer> pinnedPieces = calculatePinnedPieces(BoardUtil.getTeamBoard(board, isWhite), BoardUtil.getTeamBoard(board, !isWhite), enemySlidingPieces, kingPosition, isWhite);
            
            if(singleCheck && BoardUtil.canPieceSlide(checkingPiece)){
                Log("\tKing in single check by sliding enemy piece");
                //calculate push masks, which are the squares from the capture mask to the kingPosition
                pushMask = 0L;
                int dy = (kingPosition / 8) - (captureMask / 8);
                int dx = (kingPosition % 8) - (captureMask % 8);
                if (dy > 1) dy = 1;
                if (dy < 0) dy = -1;
                if(dx > 1) dx = 1;
                if (dx < 0) dx = -1;
                int displacement = dx + (dy == 1 ? 8:0) + (dy == -1 ? -8:0);
                int current = captureMask + displacement;

                while(current != kingPosition){
                    pushMask = BoardUtil.setBit(pushMask, current);
                    current += displacement;
                }

            }

            List<Move> legalMoves = getPsuedoLegalMoves(board, BoardUtil.getTeamBoards(board, isWhite), BoardUtil.getTeamBoard(board, !isWhite), isWhite, false, false, pinnedPieces, dangerSquares, captureMask, pushMask); 

            Log(legalMoves.size() + " MOVES");
            Log(legalMoves.toString());
            return legalMoves;
            
        }
    }

    /**
     * todo: what exactly are psuedo legal moves
     * sometimes we want to calculate danger squares for a king, which means that pieces being protected cannot be captured, hence 'kingremoved'
     *      -- for example if a knight is protecting a queen, an enemy king cannot capture this queen, so the move 'knight takes friendly queen' is 'valid' move when looking for danger squares
     * pawns can be tricky, because when calculating danger squares, we ignore pawn default moves and all pawn capture moves are 'valid' when looking for danger squares
     * then sometimes we want
     * 
     * Returns the psuedo legal moves of this teams enemy
     * @param board
     * @param isWhite
     * @param attacksOnly
     * @return
     */
    public static List<Move> getEnemyPsuedoLegalMoves(Board board, boolean isWhite, boolean attacksOnly, boolean allPawnMoves){
        HashMap<Integer, Integer> emptyPinnedPieces = new HashMap<>();
        HashSet<Integer> dangerSquares = new HashSet<>();
        int captureMask = BoardUtil.NULL_CAPTURE_MASK;
        long pushMask = BoardUtil.NULL_PUSH_MASK;
        long[] teamBoards = BoardUtil.getTeamBoardsWithoutKing(board, isWhite);
        long teamBoardWithoutKing = 0L;
        for(long b: teamBoards){
            teamBoardWithoutKing |= b;
        }

        return getPsuedoLegalMoves(board, BoardUtil.getTeamBoards(board, !isWhite), teamBoardWithoutKing, !isWhite, attacksOnly, allPawnMoves, emptyPinnedPieces, dangerSquares, captureMask, pushMask);
    }


    public static List<Move> getPsuedoLegalMoves(Board board, long[] team, long enemyBoard, boolean isWhite, boolean lookingForDangerSquares, boolean allPawnMoves, HashMap<Integer, Integer> pinnedPieces, HashSet<Integer> dangerSquares, int captureMask, long pushMask){
        List<Move> psuedoLegalMoves = new ArrayList<>();
        long teamBoard = BoardUtil.getTeamBoard(board, isWhite);
        psuedoLegalMoves.addAll(getPawnMoves(board, team[0], teamBoard, enemyBoard, isWhite, lookingForDangerSquares, allPawnMoves, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getKnightMoves(board, team[1], teamBoard, isWhite, lookingForDangerSquares, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getSlidingPieceMoves(board, (isWhite ? PieceType.WHITE_BISHOP : PieceType.BLACK_BISHOP), team[2], teamBoard, enemyBoard, isWhite, lookingForDangerSquares, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getSlidingPieceMoves(board, isWhite ? PieceType.WHITE_ROOK : PieceType.BLACK_ROOK, team[3], teamBoard, enemyBoard, isWhite, lookingForDangerSquares, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getSlidingPieceMoves(board, isWhite ? PieceType.WHITE_QUEEN : PieceType.BLACK_QUEEN, team[4], teamBoard, enemyBoard, isWhite, lookingForDangerSquares, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getKingMoves(board, Long.numberOfTrailingZeros(team[5]), teamBoard, dangerSquares, isWhite));

        return psuedoLegalMoves;
    }

    /** 
     * Calculates the pieces pinned to this color's king:
     * (1) For each enemy sliding piece, start traversing towards the king
     * (2) If friendly is found and the next piece is our king, this friendly is pinned
     * (3) If friendly is found and the next piece is friendly, no pin
     * (4) If enemy is found, no pin
     * @return HashMap<Integer, Integer> key: piece square, value: pinned direction 
     */
    public static HashMap<Integer, Integer> calculatePinnedPieces(long teamBoard, long enemyBoard, long[] enemySlidingPieces, int kingPosition, boolean isWhite){
        HashMap<Integer, Integer> pinnedPieces = new HashMap<>();

        //bishops, rooks, queens
        for(int i=0; i<3; i++){
            long enemySlidingPiece = enemySlidingPieces[i];
            int[] directions = (i == 0) ? BoardUtil.BISHOP_DISPLACEMENTS : (i == 1) ? BoardUtil.ROOK_DISPLACEMENTS : BoardUtil.QUEEN_DISPLACEMENTS;

            while(enemySlidingPiece != 0){
                int enemySquare = Long.numberOfTrailingZeros(enemySlidingPiece);
                
                for(int dir: directions){
                    int firstFriendlySquare = -1;
                    int square = kingPosition;

                    while(true){
                        int currentFile = square % 8;
                        square += dir;
                        int nextFile = square % 8;

                        // Check for board boundaries
                        if (square < 0 || square >= 64 || Math.abs(nextFile - currentFile) > 1) {
                            firstFriendlySquare = -1;
                            break;
                        }

                        long bit = 1L << square;

                        if ((teamBoard & bit) != 0) {
                            if (firstFriendlySquare == -1) {
                                firstFriendlySquare = square;
                            } else {
                                // Found a second friendly piece, so the first is not pinned
                                firstFriendlySquare = -1;
                                break;
                            }
                        }

                        //enemy piece found
                        if((enemyBoard & bit) != 0 && square != enemySquare){
                            firstFriendlySquare = -1;
                            break;
                        }

                        if (square == enemySquare) {
                            break;
                        }
                    }
                
                    if (firstFriendlySquare != -1) {
                        // The piece at firstFriendlySquare is pinned in direction `dir`
                        pinnedPieces.put(firstFriendlySquare, dir);
                    }
                }

                enemySlidingPiece &= enemySlidingPiece - 1;
            }
        }
        
        return pinnedPieces;
    }

    public static List<Move> getPawnMoves(Board board, long pawns, long teamBoard, long enemyBoard, boolean isWhite, boolean capturesOnly, boolean allPawnMoves, int captureMask, long pushMask, HashMap<Integer, Integer> pinnedPieces){
        List<Move> pawnMoves = new ArrayList<>();

        int[] displacements = isWhite ? BoardUtil.WHITE_PAWN_DISPLACEMENTS : BoardUtil.BLACK_PAWN_DISPLACEMENTS;
        PieceType pieceType = isWhite ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN;

        // for each pawn
        while(pawns != 0){

            int fromSquare = Long.numberOfTrailingZeros(pawns);
            int fromFile = fromSquare % 8;
            int fromRank = fromSquare / 8;

            // for each displacement
            for(int displacement: displacements){

                int absDisplacement = Math.abs(displacement);

                int toSquare = fromSquare + displacement;
                int toFile = toSquare % 8;
                int toRank = toSquare / 8;

                // make sure pawns on the left most file don't spill over into the right column
                boolean overflowed = Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2;
                // must be on squares 0 through 63
                boolean validSquare = BoardUtil.checkValidSquare(toSquare);
                // must be either not pinned, or pinned and in the pinned direction
                boolean validPinDirection = BoardUtil.isValidPinDirection(fromSquare, displacement, pinnedPieces);
                // must have no masks, or in either mask
                boolean validInCaptureAndPushMasks = BoardUtil.squareValidInCaptureAndPushMasks(toSquare, captureMask, pushMask);

                boolean validPrereqs = !overflowed && validSquare && validPinDirection && validInCaptureAndPushMasks;
                
                if(allPawnMoves){
                    if( (absDisplacement%2 != 0) && !overflowed && validSquare){
                        PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                        // if(BoardUtil.isSameTeam(capturedPieceType, pieceType)){
                        //     capturedPieceType = PieceType.EMPTY;
                        // }
                        pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, MoveType.CAPTURE));
                    }
                }               
                // when only looking for captures, immediately return attacking move if not overflowing and valid square
                else if(capturesOnly)
                {
                    if( (absDisplacement%2 != 0) && !overflowed && validSquare){
                        PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                        // if(BoardUtil.isSameTeam(capturedPieceType, pieceType)){
                        //     capturedPieceType = PieceType.EMPTY;
                        // }
                        pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, MoveType.CAPTURE));
                    }
                }
                else if(validPrereqs){
                    
                    PieceType jumpedOver = BoardUtil.getPieceTypeAtSquare(board, toSquare + (isWhite ? 8 : -8));
                    PieceType enpassantCapturedPieceType = BoardUtil.getPieceTypeAtSquare(board, board.ENPASSANT_SQUARE + (isWhite ? 8 : -8));
                    PieceType defaultCapturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                    
                    boolean defaultValid = (absDisplacement == 8) && (defaultCapturedPieceType == PieceType.EMPTY);
                    boolean doubleJumpValid = absDisplacement == 16 && (isWhite ? fromSquare > 47 : fromSquare < 16) && defaultCapturedPieceType == PieceType.EMPTY && jumpedOver == PieceType.EMPTY;

                    boolean captureValid = (absDisplacement%2 != 0) && BoardUtil.isOccupiedByEnemy(toSquare, enemyBoard);
                    boolean enpassantValid = (absDisplacement%2 != 0) && (toSquare == board.ENPASSANT_SQUARE) && !captureValid && !BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
                    boolean promotionValid = isWhite ? toRank == 0 : toRank == 7;

                    PieceType capturedPieceType = enpassantValid ? enpassantCapturedPieceType : defaultCapturedPieceType;
                    MoveType moveType = promotionValid ? MoveType.PROMOTION : enpassantValid ? MoveType.ENPASSANT : captureValid ? MoveType.CAPTURE : MoveType.DEFAULT;

                    boolean validMove = defaultValid || doubleJumpValid || captureValid || enpassantValid;

                    if(validMove){
                        if(promotionValid){
                            pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, isWhite ? PieceType.WHITE_QUEEN : PieceType.BLACK_QUEEN, moveType));
                            pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, isWhite ? PieceType.WHITE_ROOK : PieceType.BLACK_ROOK, moveType));
                            pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, isWhite ? PieceType.WHITE_BISHOP : PieceType.BLACK_BISHOP, moveType));
                            pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, isWhite ? PieceType.WHITE_KNIGHT : PieceType.BLACK_KNIGHT, moveType));
                        }else{
                            pawnMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, moveType));
                        }
                    }
                }

            }

            pawns &= pawns - 1; 
        }

        return pawnMoves;
    }

    public static List<Move> getKnightMoves(Board board, long knights, long teamBoard, boolean isWhite, boolean lookingForDangerSquares,  int captureMask, long pushMask, HashMap<Integer, Integer> pinnedPieces){
        List<Move> knightMoves = new ArrayList<>();

        while(knights != 0){

            int fromSquare = Long.numberOfTrailingZeros(knights);
            int fromFile = fromSquare % 8;
            int fromRank = fromSquare /8;

            for(int displacement : BoardUtil.KNIGHT_DISPLACEMENTS){

                int toSquare = fromSquare + displacement;
                int toFile = toSquare % 8;
                int toRank = toSquare / 8;

                PieceType pieceType = isWhite ? PieceType.WHITE_KNIGHT : PieceType.BLACK_KNIGHT;
                PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                // if(lookingForDangerSquares && BoardUtil.isSameTeam(capturedPieceType, pieceType)){
                //     capturedPieceType = PieceType.EMPTY;
                // }
                MoveType moveType = capturedPieceType == PieceType.EMPTY ? MoveType.DEFAULT : MoveType.CAPTURE;

                boolean overflowed = Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2;
                boolean validSquare = BoardUtil.checkValidSquare(toSquare);
                boolean validEnemy = lookingForDangerSquares || !BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
                boolean validPinDirection = BoardUtil.isValidPinDirection(fromSquare, displacement, pinnedPieces);
                boolean validInCaptureAndPushMasks = BoardUtil.squareValidInCaptureAndPushMasks(toSquare, captureMask, pushMask);

                boolean validPrereqs = !overflowed && validSquare && validEnemy && validPinDirection && validInCaptureAndPushMasks;

                if(validPrereqs){
                    knightMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, moveType));
                }
            }

            knights &= knights - 1;
        }

        return knightMoves;
    }

    public static List<Move> getSlidingPieceMoves(Board board, PieceType pieceType, long slidingPiece, long teamBoard, long enemyBoard, boolean isWhite, boolean lookingForDangerSquares, int captureMask, long pushMask, HashMap<Integer, Integer> pinnedPieces){
        List<Move> moves = new ArrayList<>();
        
        int[] displacements = BoardUtil.SLIDING_DISPLACEMENTS.get(pieceType);

        while(slidingPiece != 0){

            int fromSquare = Long.numberOfTrailingZeros(slidingPiece);

            for(int displacement : displacements){
                if(BoardUtil.isValidPinDirection(fromSquare, displacement, pinnedPieces)){
                    int toSquare = fromSquare;

                    while(true){
                        int fromFile = toSquare % 8;
                        toSquare += displacement;

                        PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                        if(lookingForDangerSquares && (capturedPieceType == PieceType.WHITE_KING || capturedPieceType == PieceType.BLACK_KING)){
                            capturedPieceType = PieceType.EMPTY;
                        }
                        MoveType moveType = capturedPieceType == PieceType.EMPTY ? MoveType.DEFAULT : MoveType.CAPTURE;
                        boolean validOverflow = Math.abs(fromFile - (toSquare % 8)) < 2;
                        boolean validSquare = BoardUtil.checkValidSquare(toSquare);
                        boolean emptySquare = capturedPieceType == PieceType.EMPTY;
                        boolean containsFriendly = BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
                        boolean containsEnemy = BoardUtil.isOccupiedByEnemy(toSquare,enemyBoard);
                        boolean validInCaptureAndPushMasks = BoardUtil.squareValidInCaptureAndPushMasks(toSquare, captureMask, pushMask);

                        // make sure we don't capture friendlies
                        // this must be after checking that we aren't capturing a king and empty square
                        if(BoardUtil.isSameTeam(capturedPieceType, pieceType)){
                            capturedPieceType = PieceType.EMPTY;
                        }

                        if(validSquare && validOverflow && validInCaptureAndPushMasks && (emptySquare || containsEnemy || (containsFriendly && lookingForDangerSquares))){
                            moves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, moveType));
                        }

                        if(containsFriendly || containsEnemy || !validSquare || !validOverflow){
                            break;
                        }
                    }
                }
            }

            slidingPiece &= slidingPiece -1;
        }
        
        return moves;
    }

    public static List<Move> getKingMoves(Board board, int kingPosition, long teamBoard, HashSet<Integer> dangerSquares, boolean isWhite){
        List<Move> kingMoves = new ArrayList<>();
        PieceType pieceType = isWhite ? PieceType.WHITE_KING : PieceType.BLACK_KING;

        boolean inCheck = dangerSquares.contains(kingPosition);

        int fromSquare = kingPosition;
        int fromFile = fromSquare % 8;
        int fromRank = fromSquare /8;

        for(int displacement : BoardUtil.QUEEN_DISPLACEMENTS){

            int toSquare = fromSquare + displacement;
            int toFile = toSquare % 8;
            int toRank = toSquare / 8;

            PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
            MoveType moveType = capturedPieceType == PieceType.EMPTY ? MoveType.DEFAULT : MoveType.CAPTURE;
            boolean toSquareInCheck = dangerSquares.contains(toSquare);
            boolean overflowed = Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2;
            boolean validSquare = BoardUtil.checkValidSquare(toSquare);
            boolean validEnemy = !BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
            boolean validPrereqs = !overflowed && validSquare && validEnemy && !toSquareInCheck;

            if(validPrereqs){
                kingMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, moveType));
            }
        }

        if(!inCheck){
            if(BoardUtil.canKingSideCastle(board, isWhite, dangerSquares)){
                kingMoves.add(new Move(fromSquare, fromSquare + 2, pieceType, PieceType.EMPTY, PieceType.EMPTY, MoveType.CASTLE));
            }
    
            if(BoardUtil.canQueenSideCastle(board, isWhite, dangerSquares)){
                kingMoves.add(new Move(fromSquare, fromSquare - 2, pieceType, PieceType.EMPTY, PieceType.EMPTY, MoveType.CASTLE));
            }
    
        }

        return kingMoves;
    }


}