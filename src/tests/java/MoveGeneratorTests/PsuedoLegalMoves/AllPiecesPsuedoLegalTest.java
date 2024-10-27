package tests.java.MoveGeneratorTests.PsuedoLegalMoves;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import main.java.utilities.Board;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;
import main.java.utilities.MoveType;
import main.java.utilities.PieceType;
import java.util.*;

public class AllPiecesPsuedoLegalTest {
    
    @Test
    public void DefaultPosition_CurrentLegalMoves(){
        List<Move> expectedWhiteMoves = new ArrayList<>();
        List<Move> expectedBlackMoves = new ArrayList<>();

        Board board = new Board();

        expectedWhiteMoves.add(new Move(48,40,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(49,41,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(50,42,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,43,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,44,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(53,45,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(54,46,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(55,47,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(55,39,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(54,38,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(53,37,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,36,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,35,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(50,34,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(49,33,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(48,32,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(57,40,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(57,42,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(62,45,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(62,47,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));

        assertTrue(expectedWhiteMoves.containsAll(MoveGenerator.getCurrentLegalMoves(board, true)));

        expectedBlackMoves.add(new Move(8,16,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(9,17,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(10,18,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(11,19,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(12,20,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(13,21,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(14,22,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(15,23,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(15,31,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(14,30,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(13,29,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(12,28,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(11,27,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(10,26,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(9,25,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(8,24,PieceType.BLACK_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(1,16,PieceType.BLACK_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(1,18,PieceType.BLACK_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(6,21,PieceType.BLACK_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(6,23,PieceType.BLACK_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));

        assertTrue(expectedBlackMoves.containsAll(MoveGenerator.getCurrentLegalMoves(board, false)));
    }


    @Test
    public void Kiwipete_CurrentLegalMoves(){
        Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");

        List<Move> expectedWhiteMoves = new ArrayList<>();


        expectedWhiteMoves.add(new Move(48,32,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(48,40,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(49,41,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(42,32,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(42,57,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(42,59,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(42,25,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,58,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,44,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,37,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,30,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(51,23,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(27,20,PieceType.WHITE_PAWN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(27,19,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(28,43,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(28,34,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(28,18,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(28,11,PieceType.WHITE_KNIGHT,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(28,13,PieceType.WHITE_KNIGHT,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(28,22,PieceType.WHITE_KNIGHT,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(28,38,PieceType.WHITE_KNIGHT,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,43,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,34,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,25,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,16,PieceType.WHITE_BISHOP,PieceType.BLACK_BISHOP, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(52,59,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(52,61,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,59,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,61,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,37,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,29,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,21,PieceType.WHITE_QUEEN,PieceType.BLACK_KNIGHT, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(45,38,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,31,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,46,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,47,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(45,44,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(45,43,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(54,38,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(54,46,PieceType.WHITE_PAWN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(54,47,PieceType.WHITE_PAWN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedWhiteMoves.add(new Move(56,57,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(56,58,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(56,59,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(63,62,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(63,61,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,62,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));
        expectedWhiteMoves.add(new Move(60,58,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        assertTrue(expectedWhiteMoves.containsAll(MoveGenerator.getCurrentLegalMoves(board, true)));
    }
}
