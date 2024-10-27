package tests.java.SearchAndEvaluateTests;


import org.junit.jupiter.api.Test;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.hive.HiveSearch;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import java.util.*;

public class CheckMateTest {
    
    //todo: test simple position has correct evaluation
    @Test
    public void testAvoidCheckMate(){
        Board board = new Board("3pk3/3p4/8/8/8/1B6/4Q3/4K3 b KQkq -  0 0");

        List<Move> legalMoves = MoveGenerator.getCurrentLegalMoves(board, true);
        List<Move> pLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, true, false, true);

        // MoveGenerator.showLogs = true;
        HiveSearch.showLogs = true;


        // int score = HiveEvaluator.Evaluate(board, legalMoves, pLegalMoves,true);
        // Move move = HiveSearch.bestMove(board,legalMoves, 3, true);
        // System.out.println("WHITE");
        // System.out.println("Best move: " + move);
        // System.out.println("SCORE: " + score);
        // System.out.println("BLACK");
        
        HiveSearch.DEPTH = 2;
        Move move = HiveSearch.bestMove(board,pLegalMoves,false);
        BoardUtil.printBoard(board);
    }

    @Test
    public void testCheckmate(){
    }

   
}
