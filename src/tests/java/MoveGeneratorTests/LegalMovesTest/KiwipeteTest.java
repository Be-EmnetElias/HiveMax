package tests.java.MoveGeneratorTests.LegalMovesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.board.Board;
import main.java.hive.HiveSearch;
import main.java.move.PerftInfo;

public class KiwipeteTest {
    Board board;

    PerftInfo[] PerftPosition = new PerftInfo[]{
        new PerftInfo(48, 8, 0, 2, 0, 0, 0, 0, 0),
        new PerftInfo(2039, 351, 1, 91, 0, 3, 0, 0, 0),
        new PerftInfo(97862, 17102, 45, 3162, 0, 993, 0, 0, 1),
        new PerftInfo(4085603, 7571630, 1929, 128013, 15172, 25523, 42, 6, 43),
        new PerftInfo(193690690, 35043416, 73365, 4993637, 8392, 3309887, 19883, 2637, 30171),
        // new PerftInfo(8031647685, 1558445089, 3577504, 184513607, 56627920, 92238050, 568417, 54948, 360003)

    };
 
    @BeforeEach
    public void setupBoard(){
        board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
    }

    @Test
    public void PerftDepth1(){
        assertEquals(PerftPosition[0].nodes(), HiveSearch.nodeCount(1, 1, board).nodes());
    }

    @Test
    public void PerftDepth2(){
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
