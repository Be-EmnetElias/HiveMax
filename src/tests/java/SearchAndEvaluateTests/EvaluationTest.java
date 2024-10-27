package tests.java.SearchAndEvaluateTests;

import org.junit.jupiter.api.Test;

import java.util.*;
import main.java.hive.HiveEvaluator;
import main.java.hive.HiveSearch;
import main.java.utilities.Board;
import main.java.utilities.BoardUtil;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;

public class EvaluationTest {
    // 
    @Test
    public void testAvoidCheckMate(){
        Board board = new Board("7k/8/1p6/8/1PN5/8/8/7K w - -  0 0");

        List<Move> legalMoves = MoveGenerator.getCurrentLegalMoves(board, true);
        List<Move> pLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, true, false, true);

        // MoveGenerator.showLogs = true;
        HiveSearch.showLogs = true;
        // HiveEvaluator.showLogs = true;

        // int score = HiveEvaluator.Evaluate(board, legalMoves, pLegalMoves,true);
        // Move move = HiveSearch.bestMove(board,legalMoves, 3, true);
        // System.out.println("WHITE");
        // System.out.println("Best move: " + move);
        // System.out.println("SCORE: " + score);
        // System.out.println("BLACK");

        Move move = HiveSearch.bestMove(board,legalMoves,true);
        BoardUtil.printBoard(board);
    }
}
