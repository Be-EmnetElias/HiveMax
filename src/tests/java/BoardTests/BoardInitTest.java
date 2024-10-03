package tests.java.BoardTests;

import main.java.*;
import main.java.utilities.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;


public class BoardInitTest {
    
    Board board;

    @BeforeEach
    public void setup(){
        this.board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    // test initial piece layout

    @Test
    public void GetPieceTypeAtSquare_EqualsEmpty(){
        for(int i=16; i<=47; i++){
            assertEquals(PieceType.EMPTY, BoardUtil.getPieceTypeAtSquare(board, i));
        }
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsWhitePawn(){
        for(int i=48; i<=55; i++){
            assertEquals(PieceType.WHITE_PAWN, BoardUtil.getPieceTypeAtSquare(board, i));
        }
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsWhiteKnight(){
        assertEquals(PieceType.WHITE_KNIGHT, BoardUtil.getPieceTypeAtSquare(board, 57));
        assertEquals(PieceType.WHITE_KNIGHT, BoardUtil.getPieceTypeAtSquare(board, 62));
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsWhiteBishop(){
        assertEquals(PieceType.WHITE_BISHOP, BoardUtil.getPieceTypeAtSquare(board, 58));
        assertEquals(PieceType.WHITE_BISHOP, BoardUtil.getPieceTypeAtSquare(board, 61));
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsWhiteRook(){
        assertEquals(PieceType.WHITE_ROOK, BoardUtil.getPieceTypeAtSquare(board, 56));
        assertEquals(PieceType.WHITE_ROOK, BoardUtil.getPieceTypeAtSquare(board, 63));
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsWhiteQueen(){
        assertEquals(PieceType.WHITE_QUEEN, BoardUtil.getPieceTypeAtSquare(board, 59));
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsWhiteKing(){
        assertEquals(PieceType.WHITE_KING, BoardUtil.getPieceTypeAtSquare(board, 60));
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsBlackPawn(){
        for(int i=8; i<=15; i++){
            assertEquals(PieceType.BLACK_PAWN, BoardUtil.getPieceTypeAtSquare(board, i));
        }
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsBlackKnight(){
        assertEquals(PieceType.BLACK_KNIGHT, BoardUtil.getPieceTypeAtSquare(board, 1));
        assertEquals(PieceType.BLACK_KNIGHT, BoardUtil.getPieceTypeAtSquare(board, 6));

        
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsBlackBishop(){
        assertEquals(PieceType.BLACK_BISHOP, BoardUtil.getPieceTypeAtSquare(board, 2));
        assertEquals(PieceType.BLACK_BISHOP, BoardUtil.getPieceTypeAtSquare(board, 5));

        
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsBlackRook(){
        assertEquals(PieceType.BLACK_ROOK, BoardUtil.getPieceTypeAtSquare(board, 0));
        assertEquals(PieceType.BLACK_ROOK, BoardUtil.getPieceTypeAtSquare(board, 7));

        
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsBlackQueen(){
        assertEquals(PieceType.BLACK_QUEEN, BoardUtil.getPieceTypeAtSquare(board, 3));
        
    }

    @Test
    public void GetPieceTypeAtSquare_EqualsBlackKing(){
        assertEquals(PieceType.BLACK_KING, BoardUtil.getPieceTypeAtSquare(board, 4));
        
    }
  

}
