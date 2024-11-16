package tests.java.SearchAndEvaluateTests;

import org.junit.jupiter.api.Test;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.board.PieceType;
import main.java.hive.HiveHash;
import main.java.hive.HiveSearch;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import main.java.move.MoveType;

import static org.junit.Assert.assertEquals;

import java.util.*;

public class HiveHashTest {
    
    @Test
    public void PositionHasSameHash(){
        Board board_1 = new Board();
        Board board_2 = new Board();

        Move move_1 = new Move(51, 35, PieceType.WHITE_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT);
        Move move_2 = new Move(11, 27, PieceType.BLACK_PAWN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT);
        Move move_3 = new Move(57, 42, PieceType.WHITE_KNIGHT, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT);
    
        board_1.makeMove(move_1);
        board_1.makeMove(move_2);
        board_1.makeMove(move_3);

        long hash_1 = HiveHash.getHash(board_1);

        board_2.makeMove(move_3);
        board_2.makeMove(move_2);
        board_2.makeMove(move_1);

        long hash_2 = HiveHash.getHash(board_2);

        assertEquals(hash_1, hash_2);
    
    }
}
