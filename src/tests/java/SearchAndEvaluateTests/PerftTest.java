package tests.java.SearchAndEvaluateTests;
import main.java.board.Board;
import main.java.hive.HiveSearch;
import main.java.move.PerftInfo;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;


public class PerftTest {

    
    Board board;

    PerftInfo[] PerftPosition = new PerftInfo[]{
        new PerftInfo(1, 0, 0, 0, 0, 0, 0, 0, 0),
        new PerftInfo(20, 0, 0, 0, 0, 0, 0, 0, 0),
        new PerftInfo(400, 0, 0, 0, 0, 0, 0, 0, 0),
        new PerftInfo(8902, 34, 0, 0, 0, 12, 0, 0, 0),
        new PerftInfo(197_281, 1576, 0, 0, 0, 469, 0, 0, 8),
        new PerftInfo(4_865_609, 82719, 258, 0, 0, 27351, 6, 0, 347),
        new PerftInfo(119_060_324, 2_812_008,5248, 0, 0, 809_099, 329, 46, 10_828),
        // new PerftInfo(3_195_901_860, 108_329_926, 319_617, 883_453, 0,	33_103_848,	18_026,	1628, 435_767)
        // new PerftInfo(84,998,978,956	3,523,740,106	7,187,977	23,605,205	0	968,981,593	847,039	147,215	9,852,036);
        // new PerftInfo(2,439,530,234,167	125,208,536,153	319,496,827	1,784,356,000	17,334,376	36,095,901,903	37,101,713	5,547,231	400,191,963);
    };
 
    @BeforeEach
    public void setupBoard(){
        board = new Board();
    }

    @Test
    public void PerftDepth0(){
        assertEquals(PerftPosition[0].nodes(), HiveSearch.nodeCount(0, 0, new Board()).nodes());
    }

    @Test
    public void PerftDepth1(){
        assertEquals(PerftPosition[1].nodes(), HiveSearch.nodeCount(1, 1, new Board()).nodes());
    }

    @Test
    public void PerftDepth2(){
        assertEquals(PerftPosition[2].nodes(), HiveSearch.nodeCount(2, 2, new Board()).nodes());
    }

    @Test
    public void PerftDepth3(){
        assertEquals(PerftPosition[3].nodes(), HiveSearch.nodeCount(3, 3, new Board()).nodes());
    }

    @Test
    public void PerftDepth4(){
        assertEquals(PerftPosition[4].nodes(), HiveSearch.nodeCount(4, 4, new Board()).nodes());
        
    }

    @Test
    public void PerftDepth5(){
        assertEquals(PerftPosition[5].nodes(), HiveSearch.nodeCount(5, 5, new Board()).nodes());
    }

    @Test
    public void PerftDepth6(){
        assertEquals(PerftPosition[6].nodes(), HiveSearch.nodeCount(6, 6, new Board()).nodes());
        // System.out.println(HiveEvaluator.nodeCount(3, 3, new Board("rnbqkbnr/pppp1ppp/8/3Pp3/8/8/PPP1PPPP/RNBQKBNR b KQkq -  0 0")));
    }

    // @Test
    // public void PerftDepth7(){
    //     assertEquals(PerftPosition[7].nodes(), HiveSearch.nodeCount(7, 7, new Board()).nodes());
    //     // System.out.println(HiveEvaluator.nodeCount(7, 7, new Board()));
    // }
}
