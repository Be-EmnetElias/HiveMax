package test.java.MoveGeneratorTests.PsuedoLegalMoves;

import main.java.*;
import main.utilities.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PawnMovesTest {
    

    public HashSet<Move> GeneratePawnMoves(Board board, boolean isWhite){
        board.IS_WHITE_TURN = isWhite;
        long whitePawns = board.WHITE_PAWNS;
        long whiteBoard = BoardUtil.getTeamBoard(board, true);

        long blackPawns = board.BLACK_PAWNS;
        long blackBoard = BoardUtil.getTeamBoard(board, false);

        return MoveGenerator.getPawnMoves(
            board,
            isWhite ? whitePawns : blackPawns, 
            isWhite ? whiteBoard : blackBoard,
            isWhite ? blackBoard : whiteBoard, 
            isWhite, 
            false, 
            BoardUtil.NULL_CAPTURE_MASK, 
            BoardUtil.NULL_PUSH_MASK, 
            null
        );
    }

    @Test
    public void PawnMoves_DefaultAndDoubleJumpAndCapture(){
        Board board = new Board("8/4p3/3N1N2/8/8/3n1n2/4P3/8 w - - 0 1");
        
        HashSet<Move> expectedWhitePawnMoves = new HashSet<>();
        expectedWhitePawnMoves.add(new Move(52, 44, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedWhitePawnMoves.add(new Move(52, 36, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedWhitePawnMoves.add(new Move(52, 43, PieceType.WHITE_PAWN, PieceType.BLACK_KNIGHT, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhitePawnMoves.add(new Move(52, 45, PieceType.WHITE_PAWN, PieceType.BLACK_KNIGHT, PieceType.EMPTY, MoveType.CAPTURE));

        
        HashSet<Move> expectedBlackPawnMoves = new HashSet<>();
        expectedBlackPawnMoves.add(new Move(12, 20, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedBlackPawnMoves.add(new Move(12, 28, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedBlackPawnMoves.add(new Move(12, 21, PieceType.BLACK_PAWN, PieceType.WHITE_KNIGHT, PieceType.EMPTY, MoveType.CAPTURE));
        expectedBlackPawnMoves.add(new Move(12, 19, PieceType.BLACK_PAWN, PieceType.WHITE_KNIGHT, PieceType.EMPTY, MoveType.CAPTURE));

        assertEquals(expectedWhitePawnMoves, GeneratePawnMoves(board, true));
        assertEquals(expectedBlackPawnMoves, GeneratePawnMoves(board, false));

    }

    @Test
    public void PawnMoves_DoubleJumpBlocked(){
        Board board = new Board("8/6p1/8/6Q1/6q1/8/6P1/ b KQkq - 0 0");
        HashSet<Move> expectedWhiteMoves = new HashSet<>();
        expectedWhiteMoves.add(new Move(54,46,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        HashSet<Move> expectedBlackMoves = new HashSet<>();
        expectedBlackMoves.add(new Move(14,22,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        
        assertEquals(expectedWhiteMoves, GeneratePawnMoves(board,true));
        assertEquals(expectedBlackMoves, GeneratePawnMoves(board,false));

    
    }

    @Test
    public void PawnMoves_DefaultBlocked_CannotJumpOverPiece(){
        Board board = new Board("8/6p1/6P1/8/8/6p1/6P1/ b KQkq - 0 0");

        assertEquals(new HashSet<Move>(), GeneratePawnMoves(board, true));
        assertEquals(new HashSet<Move>(), GeneratePawnMoves(board, false));


    }

    @Test
    public void PawnMoves_DefaultAndEnpassant(){
        Board boardForWhite = new Board("8/8/8/6pP/8/8/8/8 w - g6 0 1");
        Board boardForBlack = new Board("8/8/8/8/6pP/8/8/8 b - h3 0 1");
        
        HashSet<Move> expectedWhitePawnMoves = new HashSet<>();
        expectedWhitePawnMoves.add(new Move(31, 22, PieceType.WHITE_PAWN, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.ENPASSANT));
        expectedWhitePawnMoves.add(new Move(31, 23, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));

        
        HashSet<Move> expectedBlackPawnMoves = new HashSet<>();
        expectedBlackPawnMoves.add(new Move(38, 47, PieceType.BLACK_PAWN, PieceType.WHITE_PAWN, PieceType.EMPTY, MoveType.ENPASSANT));
        expectedBlackPawnMoves.add(new Move(38, 46, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));


        assertEquals(expectedWhitePawnMoves, GeneratePawnMoves(boardForWhite, true));
        assertEquals(expectedBlackPawnMoves, GeneratePawnMoves(boardForBlack, false));
    }

    @Test
    public void PawnMoves_DefaultAndCaptureAndPromotion(){
        Board board = new Board("8/7P/8/8/8/8/7p/8 w - - 0 1");

        HashSet<Move> expectedWhitePawnMoves = new HashSet<>();
        expectedWhitePawnMoves.add(new Move(15, 7, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.WHITE_KNIGHT, MoveType.PROMOTION));
        expectedWhitePawnMoves.add(new Move(15, 7, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.WHITE_BISHOP, MoveType.PROMOTION));
        expectedWhitePawnMoves.add(new Move(15, 7, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.WHITE_ROOK, MoveType.PROMOTION));
        expectedWhitePawnMoves.add(new Move(15, 7, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.WHITE_QUEEN, MoveType.PROMOTION));

        HashSet<Move> expectedBlackPawnMoves = new HashSet<>();
        expectedBlackPawnMoves.add(new Move(55, 63, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.BLACK_KNIGHT, MoveType.PROMOTION));
        expectedBlackPawnMoves.add(new Move(55, 63, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.BLACK_BISHOP, MoveType.PROMOTION));
        expectedBlackPawnMoves.add(new Move(55, 63, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.BLACK_ROOK, MoveType.PROMOTION));
        expectedBlackPawnMoves.add(new Move(55, 63, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.BLACK_QUEEN, MoveType.PROMOTION));



        assertEquals(expectedWhitePawnMoves, GeneratePawnMoves(board, true));
        assertEquals(expectedBlackPawnMoves, GeneratePawnMoves(board, false));

    }

    


    




   
}
