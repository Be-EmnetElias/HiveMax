package tests.java.MoveGeneratorTests.LegalMovesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import java.util.List;

import main.java.hive.HiveSearch;
import main.java.utilities.Board;
import main.java.utilities.BoardUtil;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;
import main.java.utilities.MoveType;
import main.java.utilities.PieceType;

public class LegalMovesTest {
    
    @Test
    public void testKingCannotCaptureProtectedEnemy(){
        Board board = new Board("3pk3/3p4/8/8/8/1B6/4Q3/4K3 b KQkq -  0 0");
        Move move = new Move(53, 13,PieceType.WHITE_QUEEN, PieceType.EMPTY, PieceType.EMPTY, MoveType.DEFAULT);

        // board.makeMove(move);

        // MoveGenerator.showLogs = true;
        List<Move> clm = MoveGenerator.getCurrentLegalMoves(board, false);
        List<Move> eplm = MoveGenerator.getEnemyPsuedoLegalMoves(board, false, false, true);

        System.out.println(clm);

        BoardUtil.printBoard(board);
        
    }

    @Test
    public void testDoubleCheck(){
        
    }
}
