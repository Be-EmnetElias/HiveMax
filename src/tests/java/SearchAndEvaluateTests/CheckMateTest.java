package tests.java.SearchAndEvaluateTests;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import main.java.Board;
import main.java.hive.HiveEvaluator;
import main.java.hive.HiveSearch;
import main.java.utilities.*;

public class CheckMateTest {
    
    //todo: test simple position has correct evaluation
    @Test
    public void testAvoidCheckMate(){
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/3B1Q2/PPP1P1PP/RNBQKBNR w KQkq -  0 0");
        HashSet<Move> legalMoves = MoveGenerator.getCurrentLegalMoves(board);
        HashSet<Move> pLegalMoves = MoveGenerator.getCurrentLegalMoves(board, !board.IS_WHITE_TURN);

        System.out.println(legalMoves);
        System.out.println(pLegalMoves);

        int score = HiveEvaluator.Evaluate(board, legalMoves, pLegalMoves,true);
        Move move = HiveSearch.bestMove(board,legalMoves, 3, true);
        System.out.println("WHITE");
        System.out.println("Best move: " + move);
        System.out.println("SCORE: " + score);
        System.out.println("BLACK");
        // score = HiveEvaluator.Evaluate(board, pLegalMoves, legalMoves,false);
        // move = HiveSearch.bestMove(board,pLegalMoves, 3, false);
        // System.out.println("Best move: " + move);
        // System.out.println("SCORE: " + score);
        // BoardUtil.printBoard(board);
    }

    @Test
    public void testCheckmate(){
        
    }
}
