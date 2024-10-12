package tests.java.SearchAndEvaluateTests;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import main.java.Board;
import main.java.hive.HiveEvaluator;
import main.java.hive.HiveSearch;
import main.java.utilities.*;

public class MaterialCountTest {
    
    //todo: test simple position has correct evaluation
    @Test
    public void testOne(){
        Board board = new Board("r3k2r/8/8/3Q4/8/8/8/R3K2R w KQkq -  0 0");
        HashSet<Move> legalMoves = MoveGenerator.getCurrentLegalMoves(board);
        HashSet<Move> pLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, !board.IS_WHITE_TURN, false);

        int score = HiveEvaluator.Evaluate(board, legalMoves, pLegalMoves,true);
        Move move = HiveSearch.bestMove(board,legalMoves, 4, true);
        System.out.println("Best move: " + move);
        System.out.println("SCORE: " + score);
        BoardUtil.printBoard(board);
    }
}
