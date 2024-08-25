package tests.java.MoveGeneratorTests.PsuedoLegalMoves;

import main.java.*;
import main.utilities.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BishopRookQueenTest {
    
    public HashSet<Move> GenerateSlidingPieceMove(Board board, boolean isWhite, PieceType piece, long slidingPieces){
        board.IS_WHITE_TURN = isWhite;

        long whiteBoard = BoardUtil.getTeamBoard(board, true);

        long blackBoard = BoardUtil.getTeamBoard(board, false);

        return MoveGenerator.getSlidingPieceMoves(
            board,
            piece,
            slidingPieces,
            isWhite ? whiteBoard : blackBoard,
            isWhite ? blackBoard : whiteBoard,
            isWhite,
            BoardUtil.NULL_CAPTURE_MASK,
            BoardUtil.NULL_PUSH_MASK,
            null
        );
    }

    @Test
    public void QueenMoves_Default(){
        Board board = new Board("8/8/8/4Q3/8/8/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,27,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,26,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,25,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,24,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,20,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,30,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,31,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,20,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,12,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,4,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,44,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,52,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,60,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,19,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,10,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,1,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,21,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,14,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,7,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,46,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,55,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,42,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,49,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,56,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));

        assertEquals(expectedMoves, GenerateSlidingPieceMove(board, true, PieceType.WHITE_QUEEN, board.WHITE_QUEENS));

    }

    @Test
    public void QueenMoves_PathBlockedByFriendly(){
        Board board = new Board("8/2P1P1P1/8/2P1Q1P1/8/2P1P1P1/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();

        expectedMoves.add(new Move(28,20,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,21,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,27,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,19,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));

        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_QUEEN,board.WHITE_QUEENS));

    }

    @Test
    public void QueenMoves_Capture(){
        Board board = new Board("8/2p1p1p1/8/2p1Q1p1/8/2p1p1p1/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,20,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,12,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,21,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,14,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,30,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,46,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,44,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,42,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,27,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,26,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,19,PieceType.WHITE_QUEEN,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,10,PieceType.WHITE_QUEEN,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));

        assertEquals(expectedMoves, GenerateSlidingPieceMove(board, true, PieceType.WHITE_QUEEN, board.WHITE_QUEENS));

    }

    @Test
    public void RookMoves_Default(){
        Board board = new Board("8/8/8/4R3/8/8/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,20,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,12,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,4,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,30,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,31,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,44,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,52,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,60,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,27,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,26,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,25,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,24,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_ROOK,board.WHITE_ROOKS));
    }

    @Test
    public void RookMoves_PathBlockedByFriendly(){
        Board board = new Board("8/4P3/8/2P1R1P1/8/4P3/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,20,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,27,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_ROOK,board.WHITE_ROOKS));

    }

    @Test
    public void RookMoves_Capture(){
        Board board = new Board("8/4p3/8/2p1R1p1/8/4p3/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,20,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,12,PieceType.WHITE_ROOK,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,29,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,30,PieceType.WHITE_ROOK,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,36,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,44,PieceType.WHITE_ROOK,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,27,PieceType.WHITE_ROOK,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,26,PieceType.WHITE_ROOK,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_ROOK,board.WHITE_ROOKS));
    
    }

    @Test
    public void BishopMoves_Default(){
        Board board = new Board("8/8/8/4B3/8/8/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,21,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,14,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,7,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,46,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,55,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,42,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,49,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,56,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,19,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,10,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,1,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_BISHOP,board.WHITE_BISHOPS));

    }

    @Test
    public void BishopMoves_PathBlockedByFriendly(){
        Board board = new Board("8/2P3P1/8/4B3/8/2P3P1/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,21,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,19,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_BISHOP,board.WHITE_BISHOPS));

    }

    @Test
    public void BishopMoves_Capture(){
        Board board = new Board("8/2p3p1/8/4B3/8/2p3p1/8/ b KQkq - 0 0");
        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(new Move(28,19,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,10,PieceType.WHITE_BISHOP,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,21,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,14,PieceType.WHITE_BISHOP,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,37,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,46,PieceType.WHITE_BISHOP,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));
        expectedMoves.add(new Move(28,35,PieceType.WHITE_BISHOP,PieceType.EMPTY, PieceType.EMPTY,MoveType.DEFAULT));
        expectedMoves.add(new Move(28,42,PieceType.WHITE_BISHOP,PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE));

        assertEquals(expectedMoves, GenerateSlidingPieceMove(board,true,PieceType.WHITE_BISHOP,board.WHITE_BISHOPS));

    }
  
}
