package tests.java.BoardTests;
import main.java.*;
import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.board.PieceType;
import main.java.move.Move;
import main.java.move.MoveType;
import main.java.utilities.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
public class BoardMoveTest {
    Board board;

    @BeforeEach
    public void setup(){
        this.board = new Board();
    }

    @Test
    public void makeAndunmakeMove_PawnDoubleJump(){
        Board board = new Board("8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
        Board originalBoard = new Board("8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");

        Board expectedBoard = new Board("8/8/8/8/6P1/8/PPPPPP1P/RNBQKBNR b KQkq g3 0 0");
        Move move = new Move(54, 38, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT);

        board.makeMove(move);
        assertTrue(board.equals(expectedBoard));

        board.undoMove(move);
        
        assertTrue(board.equals(originalBoard));
     
    }

    @Test
    public void makeAndUndoMove_Capture(){
        Board board = new Board("8/8/8/8/8/5p2/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
        Board originalBoard = new Board("8/8/8/8/8/5p2/PPPPPPPP/RNBQKBNR w KQkq - 0 0");;
        Board expectedBoard = new Board("8/8/8/8/8/5P2/PPPPPP1P/RNBQKBNR b KQkq - 0 0");
        Move move = new Move(54, 45, PieceType.WHITE_PAWN, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.CAPTURE);

        board.makeMove(move);
        assertTrue(board.equals(expectedBoard));

        board.undoMove(move);
        assertTrue(board.equals(originalBoard));
     
    }

    @Test
    public void makeAndunmakeMove_Castle(){
        Board board = new Board("8/8/8/8/8/8/8/R3K2R w KQkq - 0 0");
        Board originalBoard = new Board("8/8/8/8/8/8/8/R3K2R w KQkq - 0 0");
        Board expectedBoard = new Board("8/8/8/8/8/8/8/R4RK1 b kq - 0 0");
        Move move = new Move(60, 62, PieceType.WHITE_KING, PieceType.EMPTY, PieceType.EMPTY, MoveType.CASTLE);
        
        BoardUtil.printBoard(board);
        board.makeMove(move);
        BoardUtil.printBoard(board);
        BoardUtil.printBoard(expectedBoard);

        assertTrue(board.equals(expectedBoard));
        board.undoMove(move);
        BoardUtil.printBoard(board);
        assertTrue(originalBoard.equals(board));
    }

    @Test
    public void makeAndunmakeMove_Promotion(){
        Board board = new Board("8/6P1/8/8/8/8/8/ w KQkq - 0 0");
        Board originalBoard = new Board("8/6P1/8/8/8/8/8/ w KQkq - 0 0");

        Board expectedBoard = new Board("6Q1/8/8/8/8/8/8/ b KQkq - 0 0");
        Move move = new Move(14, 6, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.WHITE_QUEEN, MoveType.PROMOTION);
        board.makeMove(move);

        assertTrue(board.equals(expectedBoard));

        board.undoMove(move);
        assertTrue(board.equals(originalBoard));
    }

    @Test
    public void makeAndunmakeMove_Enpassant(){
        Board board = new Board("8/8/8/5pP1/8/8/8/ w KQkq f6 0 0");
        Board originalBoard = new Board("8/8/8/5pP1/8/8/8/ w KQkq f6 0 0");

        Board expectedBoard = new Board("8/8/5P2/8/8/8/8/ b KQkq - 0 0");
        Move move = new Move(30, 21, PieceType.WHITE_PAWN, PieceType.BLACK_PAWN, PieceType.EMPTY, MoveType.ENPASSANT);

        board.makeMove(move);
        assertTrue(board.equals(expectedBoard));

        board.undoMove(move);

        assertTrue(originalBoard.equals(board));

    }


}
