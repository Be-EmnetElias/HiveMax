package main.utilities;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import main.java.Board;

public class MoveGenerator{


    public MoveGenerator(){}
    
    
    public static HashSet<Move> getCurrentLegalMoves(Board board){
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
     * @return HashSet<Move>
     */
    public static HashSet<Move> getCurrentLegalMoves(Board board, boolean isWhite){
        int kingPosition = BoardUtil.getKingSquare(board, isWhite);
        HashSet<Integer> dangerSquares = new HashSet<>();
        HashMap<Integer, Integer> emptyPinnedPieces = new HashMap<>();
        boolean singleCheck = false;
        boolean doubleCheck = false;
        int captureMask = BoardUtil.NULL_CAPTURE_MASK;
        long pushMask = BoardUtil.NULL_PUSH_MASK;

        // get enemy psuedo legal moves
        HashSet<Move> enemyPsuedoLegalMoves = getPsuedoLegalMoves(board, BoardUtil.getTeamBoards(board, !isWhite),!isWhite, true, emptyPinnedPieces, dangerSquares, captureMask, pushMask);

        // extract danger squares and determine if single or double check
        // king is in single check if the enemy move attacks the king
        // king is in double check if a different enemy move is also attacking the king
        for(Move enemyMove : enemyPsuedoLegalMoves){
            int toSquare = enemyMove.toSquare();
            dangerSquares.add(toSquare);

            if(!doubleCheck && (toSquare == kingPosition)){

                if(!singleCheck){
                    singleCheck = true;
                    captureMask = enemyMove.fromSquare();
                }else{
                    if(captureMask != enemyMove.fromSquare()){
                        doubleCheck = true;
                    }
                }
            }
        }

        // return moves accordingly
        if(doubleCheck){
            return getKingMoves(board, kingPosition, BoardUtil.getTeamBoard(board, isWhite), dangerSquares, isWhite);
        }else{
            //calculate pinned pieces
            long enemySlidingPieces = isWhite ? (board.BLACK_QUEENS | board.BLACK_ROOKS | board.BLACK_BISHOPS) : (board.WHITE_QUEENS | board.WHITE_ROOKS | board. WHITE_BISHOPS);
            HashMap<Integer, Integer> pinnedPieces = calculatePinnedPieces(BoardUtil.getTeamBoard(board, isWhite), BoardUtil.getTeamBoard(board, !isWhite), enemySlidingPieces, kingPosition, isWhite);
            
            System.out.println("PINNED PIECES");
            System.out.println(pinnedPieces);
            
            if(singleCheck){
                // System.out.println("CAPTURE: " + captureMask);
                // System.out.print("PUSH MASK: ");

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
                    // System.out.print(current + " ");
                    current += displacement;
                }
                // System.out.println();

            }

            
            

            return getPsuedoLegalMoves(board, BoardUtil.getTeamBoards(board, isWhite), isWhite, false, pinnedPieces, dangerSquares, captureMask, pushMask);
            
        }
    }

    public static HashSet<Move> getPsuedoLegalMoves(Board board, long[] team, boolean isWhite, boolean attacksOnly, HashMap<Integer, Integer> pinnedPieces, HashSet<Integer> dangerSquares, int captureMask, long pushMask){
        HashSet<Move> psuedoLegalMoves = new HashSet<>();
        long teamBoard = BoardUtil.getTeamBoard(board, isWhite);
        long enemyBoard = BoardUtil.getTeamBoard(board, !isWhite);
        psuedoLegalMoves.addAll(getPawnMoves(board, team[0], teamBoard, enemyBoard, isWhite, attacksOnly, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getKnightMoves(board, team[1], teamBoard, isWhite, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getSlidingPieceMoves(board, (isWhite ? PieceType.WHITE_BISHOP : PieceType.BLACK_BISHOP), team[2], teamBoard, enemyBoard, isWhite, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getSlidingPieceMoves(board, isWhite ? PieceType.WHITE_ROOK : PieceType.BLACK_ROOK, team[3], teamBoard, enemyBoard, isWhite, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getSlidingPieceMoves(board, isWhite ? PieceType.WHITE_QUEEN : PieceType.BLACK_QUEEN, team[4], teamBoard, enemyBoard, isWhite, captureMask, pushMask, pinnedPieces));
        psuedoLegalMoves.addAll(getKingMoves(board, Long.numberOfTrailingZeros(team[5]), teamBoard, dangerSquares, isWhite));

        return psuedoLegalMoves;
    }

    /** 
     * TODO: change this method to iterate from sliding pieces to king
     * ! currently if any sliding piece is found in any pinned direction it counts, which is incorrect, a bishop cannot attack like a rook
     * Calculates the pieces pinned to this color's king:
     * @return HashMap<Integer, Integer> key: piece square, value: pinned direction 
     */
    public static HashMap<Integer, Integer> calculatePinnedPieces(long teamBoard, long enemyBoard, long enemySlidingPieces, int kingPosition, boolean isWhite){
        HashMap<Integer, Integer> pinnedPieces = new HashMap<>();

        int[] directions = BoardUtil.QUEEN_DISPLACEMENTS;

        // System.out.println("PIECES THAT ARE PINNED TO " + (isWhite ? "WHITE_KING":"BLACK_KING" ) + " at " + kingPosition);
        for(int dir: directions){
            // System.out.println("\t DIRECTION: " + dir);
            int currentSquare = kingPosition + dir;

            int pieceSquareMaybePinned = -1;

            while(BoardUtil.checkValidSquare(currentSquare) && (Math.abs(((currentSquare - dir) % 8) - (currentSquare % 8)) < 2)){
                
                // 
                // System.out.println("\t\t SQUARE: " + currentSquare);

                // System.out.println("\t\t\t VALID ");

                if(BoardUtil.isOccupiedByFriendly(currentSquare, teamBoard)){
                    if(pieceSquareMaybePinned == -1){
                        // System.out.println("\t\t\t OCC BY FRIENDLY ");

                        pieceSquareMaybePinned = currentSquare;
                    }else{
                        // System.out.println("\t\t\t OCC BY FRIENDLY AGAIN, SAFE ");

                        break;
                    }
                }   
                
                if(BoardUtil.isOccupiedByEnemy(currentSquare, enemyBoard)){
                    // System.out.println("\t\t\t OCC BY ENEMY ");

                    if(BoardUtil.isSquareOnBoard(currentSquare, enemySlidingPieces) && pieceSquareMaybePinned != -1){
                        pinnedPieces.put(pieceSquareMaybePinned, dir);
                    }

                    break;
                }
                currentSquare += dir;
            }
        }
      
        
        return pinnedPieces;
    }

    public static HashSet<Move> getPawnMoves(Board board, long pawns, long teamBoard, long enemyBoard, boolean isWhite, boolean capturesOnly, int captureMask, long pushMask, HashMap<Integer, Integer> pinnedPieces){
        HashSet<Move> pawnMoves = new HashSet<>();

        int[] displacements = isWhite ? BoardUtil.WHITE_PAWN_DISPLACEMENTS : BoardUtil.BLACK_PAWN_DISPLACEMENTS;
        PieceType pieceType = isWhite ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN;

        while(pawns != 0){

            int fromSquare = Long.numberOfTrailingZeros(pawns);
            int fromFile = fromSquare % 8;
            int fromRank = fromSquare / 8;

            for(int displacement: displacements){

                int absDisplacement = Math.abs(displacement);

                int toSquare = fromSquare + displacement;
                int toFile = toSquare % 8;
                int toRank = toSquare / 8;

                boolean overflowed = Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2;
                boolean validSquare = BoardUtil.checkValidSquare(toSquare);
                boolean validPinDirection = BoardUtil.isValidPinDirection(fromSquare, displacement, pinnedPieces);
                boolean validInCaptureAndPushMasks = BoardUtil.squareValidInCaptureAndPushMasks(toSquare, captureMask, pushMask);

                boolean validPrereqs = !overflowed && validSquare && validPinDirection && validInCaptureAndPushMasks;
                
                
                if(validPrereqs){
                    
                    PieceType defaultCapturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                    PieceType jumpedOver = BoardUtil.getPieceTypeAtSquare(board, toSquare + (isWhite ? 8 : -8));
                    PieceType enpassantCapturedPieceType = BoardUtil.getPieceTypeAtSquare(board, board.ENPASSANT_SQUARE + (isWhite ? 8 : -8));
                    
                    boolean defaultValid = (absDisplacement == 8) && (defaultCapturedPieceType == PieceType.EMPTY);
                    boolean doubleJumpValid = absDisplacement == 16 && (isWhite ? fromSquare > 47 : fromSquare < 16) && defaultCapturedPieceType == PieceType.EMPTY && jumpedOver == PieceType.EMPTY;

                    boolean captureValid = (absDisplacement%2 != 0) && BoardUtil.isOccupiedByEnemy(toSquare, enemyBoard);
                    boolean enpassantValid = (absDisplacement%2 != 0) && (toSquare == board.ENPASSANT_SQUARE) && !captureValid && !BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
                    boolean promotionValid = isWhite ? toRank == 0 : toRank == 7;

                    PieceType capturedPieceType = enpassantValid ? enpassantCapturedPieceType : defaultCapturedPieceType;
                    MoveType moveType = promotionValid ? MoveType.PROMOTION : enpassantValid ? MoveType.ENPASSANT : captureValid ? MoveType.CAPTURE : MoveType.DEFAULT;

                    if(defaultValid || doubleJumpValid || captureValid || enpassantValid){
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

    public static HashSet<Move> getKnightMoves(Board board, long knights, long teamBoard, boolean isWhite, int captureMask, long pushMask, HashMap<Integer, Integer> pinnedPieces){
        HashSet<Move> knightMoves = new HashSet<>();

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
                MoveType moveType = capturedPieceType == PieceType.EMPTY ? MoveType.DEFAULT : MoveType.CAPTURE;

                boolean overflowed = Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2;
                boolean validSquare = BoardUtil.checkValidSquare(toSquare);
                boolean validEnemy = !BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
                boolean validPinDirection = BoardUtil.isValidPinDirection(toSquare, displacement, pinnedPieces);
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

    public static HashSet<Move> getSlidingPieceMoves(Board board, PieceType pieceType, long slidingPiece, long teamBoard, long enemyBoard, boolean isWhite, int captureMask, long pushMask, HashMap<Integer, Integer> pinnedPieces){
        HashSet<Move> moves = new HashSet<>();
        
        int[] displacements = BoardUtil.SLIDING_DISPLACEMENTS.get(pieceType);

        while(slidingPiece != 0){

            int fromSquare = Long.numberOfTrailingZeros(slidingPiece);

            for(int displacement : displacements){
                if(BoardUtil.isValidPinDirection(fromSquare, displacement, pinnedPieces)){
                    int toSquare = fromSquare;

                    while(true){
                        int toFile = toSquare % 8;
                        toSquare += displacement;

                        PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
                        MoveType moveType = capturedPieceType == PieceType.EMPTY ? MoveType.DEFAULT : MoveType.CAPTURE;
                        boolean validOverflow = Math.abs(toFile - (toSquare % 8)) < 2;
                        boolean validSquare = BoardUtil.checkValidSquare(toSquare);
                        boolean emptySquare = capturedPieceType == PieceType.EMPTY;
                        boolean containsFriendly = BoardUtil.isOccupiedByFriendly(toSquare, teamBoard);
                        boolean containsEnemy = BoardUtil.isOccupiedByEnemy(toSquare,enemyBoard);
                        boolean validInCaptureAndPushMasks = BoardUtil.squareValidInCaptureAndPushMasks(toSquare, captureMask, pushMask);
                        

                        if(validSquare && validOverflow && (emptySquare || containsEnemy) && validInCaptureAndPushMasks){
                            moves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, moveType));
                        }else{
                            break;
                        }

                        if(containsFriendly || containsEnemy){
                            break;
                        }
                    }
                }
            }

            slidingPiece &= slidingPiece -1;
        }
        
        return moves;
    }

    public static HashSet<Move> getKingMoves(Board board, int kingPosition, long teamBoard, HashSet<Integer> dangerSquares, boolean isWhite){
        HashSet<Move> kingMoves = new HashSet<>();
        PieceType pieceType = isWhite ? PieceType.WHITE_KING : PieceType.BLACK_KING;

        int fromSquare = kingPosition;
        int fromFile = fromSquare % 8;
        int fromRank = fromSquare /8;

        for(int displacement : BoardUtil.QUEEN_DISPLACEMENTS){

            int toSquare = fromSquare + displacement;
            int toFile = toSquare % 8;
            int toRank = toSquare / 8;

            PieceType capturedPieceType = BoardUtil.getPieceTypeAtSquare(board, toSquare);
            MoveType moveType = capturedPieceType == PieceType.EMPTY ? MoveType.DEFAULT : MoveType.CAPTURE;

            boolean overflowed = Math.abs(fromFile - toFile) > 2 || Math.abs(fromRank - toRank) > 2;
            boolean validSquare = BoardUtil.checkValidSquare(toSquare);
            boolean validEnemy = !BoardUtil.isOccupiedByFriendly(toSquare, teamBoard) && !dangerSquares.contains(toSquare);
            boolean validPrereqs = !overflowed && validSquare && validEnemy;

            if(validPrereqs){
                kingMoves.add(new Move(fromSquare, toSquare, pieceType, capturedPieceType, PieceType.EMPTY, moveType));
            }
        }

        if(BoardUtil.canKingSideCastle(board, isWhite, dangerSquares)){
            kingMoves.add(new Move(fromSquare, fromSquare + 2, pieceType, PieceType.EMPTY, PieceType.EMPTY, MoveType.CASTLE));
        }

        if(BoardUtil.canQueenSideCastle(board, isWhite, dangerSquares)){
            kingMoves.add(new Move(fromSquare, fromSquare - 2, pieceType, PieceType.EMPTY, PieceType.EMPTY, MoveType.CASTLE));
        }


        return kingMoves;
    }


}