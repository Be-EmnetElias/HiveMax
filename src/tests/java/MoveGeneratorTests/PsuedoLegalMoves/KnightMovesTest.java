package tests.java.MoveGeneratorTests.PsuedoLegalMoves;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.board.PieceType;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import main.java.move.MoveType;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KnightMovesTest {
    
    public List<Move> GenerateKnightMoves(Board board, boolean isWhite){
        board.IS_WHITE_TURN = isWhite;

        long whiteKnights = board.WHITE_KNIGHTS;
        long whiteBoard = BoardUtil.getTeamBoard(board, true);

        long blackKnights = board.BLACK_KNIGHTS;
        long blackBoard = BoardUtil.getTeamBoard(board, false);

        return MoveGenerator.getKnightMoves(
            board,
            isWhite ? whiteKnights : blackKnights, 
            isWhite ? whiteBoard : blackBoard, 
            isWhite, 
            false,
            BoardUtil.NULL_CAPTURE_MASK, 
            BoardUtil.NULL_PUSH_MASK, 
            null
        );
    }

    @Test
    public void KnightMoves_Default(){
        Board board = new Board("8/8/8/3nN3/8/8/8/8 w - - 0 1");

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(28, 11, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedMoves.add(new Move(28, 13, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));

        expectedMoves.add(new Move(28, 22, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedMoves.add(new Move(28, 18, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));

        expectedMoves.add(new Move(28, 38, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedMoves.add(new Move(28, 34, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));

        expectedMoves.add(new Move(28, 45, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));
        expectedMoves.add(new Move(28, 43, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT));

        assertTrue(expectedMoves.containsAll(GenerateKnightMoves(board, true)));


    }

    @Test
    public void KnightMoves_BlockedByFriendly_NoMoves(){
        Board board = new Board("8/3P1B2/2R3R1/4N3/2B3Q1/3P1K2/8/8 w - - 0 1");
        assertTrue(GenerateKnightMoves(board, true).isEmpty());
    }

    @Test
    public void KnightMoves_Captures(){
        Board board = new Board("8/2pPpB2/1kK2bP1/3Nn3/1qP2pR1/2pQrP2/8/ b KQkq - 0 0");

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(27, 10, PieceType.WHITE_KNIGHT, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(27, 12, PieceType.WHITE_KNIGHT, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));

        expectedMoves.add(new Move(27, 21, PieceType.WHITE_KNIGHT, PieceType.BLACK_BISHOP, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(27, 37, PieceType.WHITE_KNIGHT, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));

        expectedMoves.add(new Move(27, 44, PieceType.WHITE_KNIGHT, PieceType.BLACK_ROOK, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(27, 42, PieceType.WHITE_KNIGHT, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));

        expectedMoves.add(new Move(27, 33, PieceType.WHITE_KNIGHT, PieceType.BLACK_QUEEN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(27, 17, PieceType.WHITE_KNIGHT, PieceType.BLACK_KING, PieceType.EMPTY, MoveType.CAPTURE));

        assertTrue(expectedMoves.containsAll(GenerateKnightMoves(board, true)));

    }
}
