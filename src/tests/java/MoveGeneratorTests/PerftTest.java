package tests.java.MoveGeneratorTests;

import main.java.*;
import main.utilities.*;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

record PerftInfo(long nodes, long captures, long enpassants, long castles, long promotions, long checks, long discoveryChecks, long doubleChecks, long checkmates) {}

public class PerftTest {

    
    Board board;

    PerftInfo[] PerftPosition = new PerftInfo[]{
        new PerftInfo(1, 0, 0, 0, 0, 0, 0, 0, 0),
        new PerftInfo(20, 0, 0, 0, 0, 0, 0, 0, 0),
        new PerftInfo(400, 0, 0, 0, 0, 0, 0, 0, 0),
        new PerftInfo(8902, 34, 0, 0, 0, 12, 0, 0, 0),
        new PerftInfo(197_281, 1576, 0, 0, 0, 469, 0, 0, 8),
        // new PerftInfo(4_865_609, 82719, 258, 0, 0, 27351, 6, 0, 347),
        // new PerftInfo(119_060_324, 2_812_008,5248, 0, 0, 809_099, 329, 46, 10_828),
        // 3,195,901,860	108,329,926	319,617	883,453	0	33,103,848	18,026	1628	435,767
        // 84,998,978,956	3,523,740,106	7,187,977	23,605,205	0	968,981,593	847,039	147,215	9,852,036
        // 2,439,530,234,167	125,208,536,153	319,496,827	1,784,356,000	17,334,376	36,095,901,903	37,101,713	5,547,231	400,191,963
    };

    public PerftInfo nodeCount(int depth, Board board){
        if(depth == 0) return new PerftInfo(1,0,0,0,0,0,0,0,0);

        long nodes = 0;
        long captures = 0;
        long enpassants = 0;
        long castles = 0;
        long promotions = 0;
        long checks = 0;
        long discoveryChecks = 0;
        long doubleChecks = 0;
        long checkmates = 0;

        HashSet<Move> moves = MoveGenerator.getCurrentLegalMoves(board);
        TreeMap<String, Long> nodeStrings = new TreeMap<>();
        for(Move move: moves){
            switch(move.moveType()){
                case CAPTURE:       captures += 1;      break;
                case CASTLE:        checks += 1;        break;
                case CHECK:         checks += 1;        break;
                case ENPASSANT:     enpassants += 1;    break;
                case PROMOTION:     promotions += 1;    break;
                default: break;
            }
            board.makeMove(move);
            PerftInfo newInfo = nodeCount(depth - 1, board);
            nodes += newInfo.nodes();
            captures += newInfo.captures();
            enpassants += newInfo.enpassants();
            castles += newInfo.castles();
            promotions += newInfo.promotions();
            checks += newInfo.checks();
            discoveryChecks += newInfo.discoveryChecks();
            doubleChecks += newInfo.doubleChecks();
            checkmates += newInfo.checkmates();

            board.undoMove(move);
            
            if(depth == 2){
                nodeStrings.put(BoardUtil.squareToString(move.fromSquare()) + BoardUtil.squareToString(move.toSquare()), newInfo.nodes());
                // System.out.println(BoardUtil.squareToString(move.fromSquare()) + " : " + newInfo.nodes());

            }
        }

        if(depth == 2){
            for(Map.Entry<String, Long> entry : nodeStrings.entrySet()){
                String startPos = entry.getKey();
                long count = entry.getValue();

                System.out.println(startPos + " : " + count);
            }
        }
        return new PerftInfo(nodes, captures, enpassants, castles, promotions, checks, discoveryChecks, doubleChecks, checkmates);
        
    }

    
    @BeforeEach
    public void setupBoard(){
        board = new Board();
    }

    @Test
    public void PerftDepth0(){
        // for(int i=0; i<PerftPosition.length; i++){
            // assertEquals(12435, nodeCount(3, new Board("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3  0 0")).nodes());
            assertEquals(529, nodeCount(2, new Board("rnbqkbnr/1ppppppp/p7/8/3P4/8/PPP1PPPP/RNBQKBNR w KQkq -  0 0")).nodes());
        
            // }
    }

    // @Test
    // public void PerftDepth1(){
    //     System.out.println("Perft info: " + nodeCount(3, board));
    // }

    // @Test
    // public void PerftDepth2(){
       
    // }

    // @Test
    // public void PerftDepth3(){
       
    // }

    // @Test
    // public void PerftDepth4(){
        
    // }

    // @Test
    // public void PerftDepth5(){
 
    // }
}
