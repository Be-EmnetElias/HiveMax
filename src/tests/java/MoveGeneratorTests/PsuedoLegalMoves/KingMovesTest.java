package tests.java.MoveGeneratorTests.PsuedoLegalMoves;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import main.java.Board;
import main.java.utilities.BoardUtil;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;
import main.java.utilities.MoveType;
import main.java.utilities.PieceType;

public class KingMovesTest {
    public HashSet<Move> GenerateKingMoves(Board board, boolean isWhite){
        board.IS_WHITE_TURN = isWhite;

        long whiteKings = board.WHITE_KINGS;
        long whiteBoard = BoardUtil.getTeamBoard(board, true);

        long blackKings = board.BLACK_KINGS;
        long blackBoard = BoardUtil.getTeamBoard(board, false);

        return MoveGenerator.getKingMoves(
            board,
            Long.numberOfTrailingZeros(isWhite ? whiteKings : blackKings), 
            isWhite ? whiteBoard : blackBoard, 
            new HashSet<Integer>(),
            isWhite
        );
    }

    @Test
    public void KingMoves_Default(){
        Board board = new Board("8/8/8/4K3/8/8/8/ b - - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();

        expectedMoves.add(new Move(28,21,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,20,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,19,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,27,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));

        assertEquals(expectedMoves, GenerateKingMoves(board, true));


    }

    @Test
    public void KingMoves_CastleBothSides(){
        Board board = new Board("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 0");
        HashSet<Move> expectedWhiteMoves = new HashSet<>();
        expectedWhiteMoves.add(new Move(60,52,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,51,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,53,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,61,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,59,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,62,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));
        expectedWhiteMoves.add(new Move(60,58,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        HashSet<Move> expectedBlackMoves = new HashSet<>();
        expectedBlackMoves.add(new Move(4,12,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,11,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,13,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,5,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,3,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,6,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));
        expectedBlackMoves.add(new Move(4,2,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        assertEquals(expectedWhiteMoves, GenerateKingMoves(board, true));
        assertEquals(expectedBlackMoves, GenerateKingMoves(board, false));

    }

    @Test
    public void KingMoves_Blocked_CannotCastleQueenSide(){
        HashSet<Move> expectedWhiteMoves = new HashSet<>();
        HashSet<Move> expectedBlackMoves = new HashSet<>();

        // d file blocked
        expectedWhiteMoves.add(new Move(60,51,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,52,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,53,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,61,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,62,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        expectedBlackMoves.add(new Move(4,12,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,11,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,13,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,5,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,6,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));
        assertEquals(expectedWhiteMoves, GenerateKingMoves(new Board("r2pk2r/8/8/8/8/8/8/R2PK2R b KQkq - 0 0"), true));
        assertEquals(expectedBlackMoves, GenerateKingMoves(new Board("r2pk2r/8/8/8/8/8/8/R2PK2R b KQkq - 0 0"), false));


        // c file blocked
        expectedWhiteMoves = new HashSet<>();
        expectedBlackMoves = new HashSet<>();

        expectedWhiteMoves.add(new Move(60,59,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,51,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,52,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,53,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,61,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,62,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        expectedBlackMoves.add(new Move(4,3,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,11,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,12,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,5,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,13,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,6,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));
        assertEquals(expectedWhiteMoves, GenerateKingMoves(new Board("r1p1k2r/8/8/8/8/8/8/R1P1K2R b KQkq - 0 0"), true));
        assertEquals(expectedBlackMoves, GenerateKingMoves(new Board("r1p1k2r/8/8/8/8/8/8/R1P1K2R b KQkq - 0 0"), false));
        
        // b file blocked, same set of moves as c file
        assertEquals(expectedWhiteMoves, GenerateKingMoves(new Board("rp2k2r/8/8/8/8/8/8/RP2K2R b KQkq - 0 0"), true));
        assertEquals(expectedBlackMoves, GenerateKingMoves(new Board("rp2k2r/8/8/8/8/8/8/RP2K2R b KQkq - 0 0"), false));
    }

    @Test
    public void KingMoves_Blocked_CannotCastleKingSide(){
        HashSet<Move> expectedWhiteMoves = new HashSet<>();
        HashSet<Move> expectedBlackMoves = new HashSet<>();
        //f file blocked  r3kp1r/8/8/8/8/8/8/R3KP1R b KQkq - 0 0

        expectedWhiteMoves.add(new Move(60,53,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,52,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,51,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,59,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,58,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        expectedBlackMoves.add(new Move(4,13,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,12,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,11,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,3,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,2,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        assertEquals(expectedWhiteMoves, GenerateKingMoves(new Board("r3kp1r/8/8/8/8/8/8/R3KP1R b KQkq - 0 0"), true));
        assertEquals(expectedBlackMoves, GenerateKingMoves(new Board("r3kp1r/8/8/8/8/8/8/R3KP1R b KQkq - 0 0"), false));
        //g file blocked r3k1pr/8/8/8/8/8/8/R3K1PR b KQkq - 0 0
        
        expectedWhiteMoves = new HashSet<>();
        expectedBlackMoves = new HashSet<>();

        expectedWhiteMoves.add(new Move(60,61,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,53,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,52,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,51,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,59,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedWhiteMoves.add(new Move(60,58,PieceType.WHITE_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        expectedBlackMoves.add(new Move(4,5,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,13,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,12,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,11,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,3,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedBlackMoves.add(new Move(4,2,PieceType.BLACK_KING,PieceType.EMPTY, PieceType.EMPTY,MoveType.CASTLE));

        assertEquals(expectedWhiteMoves, GenerateKingMoves(new Board("r3k1pr/8/8/8/8/8/8/R3K1PR b KQkq - 0 0"), true));
        assertEquals(expectedBlackMoves, GenerateKingMoves(new Board("r3k1pr/8/8/8/8/8/8/R3K1PR b KQkq - 0 0"), false));
    }
}


