package tests.java.SearchAndEvaluateTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.board.Board;
import main.java.move.*;
import main.java.hive.HiveEvaluator;
import main.java.hive.HiveHash;
import main.java.hive.HiveSearch;
import main.java.hive.HiveWeights;

public class PerftTestEvaluation {

    Board board;
    HiveWeights weights;
    List<Move> currentLegalMoves;


    @BeforeEach
    public void setupBoard(){
        new HiveHash();
        board = new Board();
        weights =  HiveEvaluator.SUBJECT_YMIR_WEIGHTS;
        currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
    }

    @Test
    public void PerftDepth1(){
        HiveSearch.DEPTH = 1;
        HiveSearch.bestMove(board, weights, currentLegalMoves, true);
    }

    @Test
    public void PerftDepth2(){
        HiveSearch.DEPTH = 2;
        HiveSearch.bestMove(board, weights, currentLegalMoves, true);
    }

    @Test
    public void PerftDepth3(){
        HiveSearch.DEPTH = 3;
        HiveSearch.bestMove(board, weights, currentLegalMoves, true);
    }

    @Test
    public void PerftDepth4(){
        HiveSearch.DEPTH = 4;
        HiveSearch.bestMove(board, weights, currentLegalMoves, true);
        
    }

    @Test
    public void PerftDepth5(){
        HiveSearch.DEPTH = 5;
        HiveSearch.bestMove(board, weights, currentLegalMoves, true);
    }

    @Test
    public void PerftDepth6(){
        HiveSearch.DEPTH = 6;
        HiveSearch.bestMove(board, weights, currentLegalMoves, true);
    }

    // @Test
    // public void PerftDepth7(){
    //     assertEquals(PerftPosition[7].nodes(), HiveSearch.nodeCount(7, 7, new Board()).nodes());
    //     // System.out.println(HiveEvaluator.nodeCount(7, 7, new Board()));
    // }
}
