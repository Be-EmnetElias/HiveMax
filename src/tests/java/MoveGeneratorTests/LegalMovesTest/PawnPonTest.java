package tests.java.MoveGeneratorTests.LegalMovesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.board.Board;
import main.java.hive.HiveSearch;
import main.java.move.PerftInfo;

public class PawnPonTest {
    Board board;


    PerftInfo[] PerftPosition = new PerftInfo[]{
        new PerftInfo(14, 1, 0, 0, 0, 2, 0, 0, 0),
        new PerftInfo(191, 14, 0, 0, 0, 10, 0, 0, 0),
        new PerftInfo(2812, 209, 2, 0, 0, 267, 3, 0, 0),
        new PerftInfo(43238, 3348, 123, 0, 0, 1680, 106, 0, 17),
        new PerftInfo(674624, 52051, 1165, 0, 0, 52950, 1292, 3, 0)
    };
 
    @BeforeEach
    public void setupBoard(){
        board = new Board("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
    }

    @Test
    public void PerftDepth1(){
        assertEquals(PerftPosition[0].nodes(), HiveSearch.nodeCount(1, 1, board).nodes());
    }

    @Test
    public void PerftDepth2(){
        // HiveSearch.nodeCount(1, 1, new Board("8/2p5/3p4/KP5r/1R2Pp1k/8/6P1/8 b - e3 0 0"));
        assertEquals(PerftPosition[1].nodes(), HiveSearch.nodeCount(2, 2, board).nodes());
    }

    @Test
    public void PerftDepth3(){
        assertEquals(PerftPosition[2].nodes(), HiveSearch.nodeCount(3, 3, board).nodes());
    }

    @Test
    public void PerftDepth4(){
        assertEquals(PerftPosition[3].nodes(), HiveSearch.nodeCount(4, 4, board).nodes());
    }

    @Test
    public void PerftDepth5(){
        assertEquals(PerftPosition[4].nodes(), HiveSearch.nodeCount(5, 5, board).nodes());
    }

}

