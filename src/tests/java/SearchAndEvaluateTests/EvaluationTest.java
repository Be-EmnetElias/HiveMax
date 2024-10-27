package tests.java.SearchAndEvaluateTests;

import org.junit.jupiter.api.Test;

import java.util.*;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.hive.HiveEvaluator;
import main.java.hive.HiveSearch;
import main.java.move.Move;
import main.java.move.MoveGenerator;

public class EvaluationTest {
    // 

    @Test
    public void testAvoidCheckMate(){
        Board board = new Board();

        List<Move> legalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, false, false, true);
        List<Move> pLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, true, false, true);

        // MoveGenerator.showLogs = true;
        HiveSearch.showLogs = true;
        HiveEvaluator.showLogs = true;

        // long rank = 0b1111111100000000000000000000000000000000000000000000000000000000L;
        // System.out.println("RANK:" + Long.toHexString(rank));
        // BoardUtil.printLong(rank >>> 16);
        // BoardUtil.printLong(board.WHITE_PAWNS);
        BoardUtil.printBoard(board);

       

        int score = HiveEvaluator.EvaluateTeam(board,pLegalMoves, legalMoves,false);
        // Move move = HiveSearch.bestMove(board,legalMoves, 3, true);
        // System.out.println("WHITE");
        // System.out.println("Best move: " + move);
        // System.out.println("SCORE: " + score);
        // System.out.println("BLACK");

        // Move move = HiveSearch.bestMove(board,legalMoves,true);
    }
}
