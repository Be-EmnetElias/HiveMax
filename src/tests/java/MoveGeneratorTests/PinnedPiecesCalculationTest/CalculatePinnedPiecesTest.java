package tests.java.MoveGeneratorTests.PinnedPiecesCalculationTest;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.*;
import main.java.utilities.BoardUtil;
import main.java.utilities.MoveGenerator;

public class CalculatePinnedPiecesTest {

    /*

        * All pawns pinned

        b  .  .  r  .  .  b  .
        .  .  .  .  .  .  .  .
        .  .  P  P  P  .  .  .
        r  .  P  K  P  .  .  r
        .  .  P  P  P  .  .  .
        .  .  .  .  .  .  .  .
        b  .  .  .  .  .  b  .
        .  .  .  r  .  .  .  .
        
    */
    @Test
    public void CalculatePinnedPieces_AllDirections(){
        Board board = new Board("b2r2b1/8/2PPP3/r1PKP2r/2PPP3/8/b5b1/3r4 w - - 0 1");

        long team = BoardUtil.getTeamBoard(board, true);
        long enemies = BoardUtil.getTeamBoard(board, false);

        HashMap<Integer, Integer> pinnedPieces = MoveGenerator.calculatePinnedPieces(team, enemies, new long[]{board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS}, BoardUtil.getKingSquare(board, true), true);
        HashMap<Integer, Integer> expectedPinnedPieces = new HashMap<>();

        expectedPinnedPieces.put(18, -9);
        expectedPinnedPieces.put(19, -8);
        expectedPinnedPieces.put(20, -7);
        expectedPinnedPieces.put(28, 1);
        expectedPinnedPieces.put(36, 9);
        expectedPinnedPieces.put(35, 8);
        expectedPinnedPieces.put(34, 7);
        expectedPinnedPieces.put(26, -1);


        assertEquals(expectedPinnedPieces, pinnedPieces);
    }

    /*

        * Not pinned in any direction due to friendly pieces blocking

        b  .  .  r  .  .  b  .
        .  P  P  P  P  P  .  .
        .  P  P  P  P  P  .  .
        r  P  P  K  P  P  .  r
        .  P  P  P  P  P  .  .
        .  P  P  P  P  P  .  .
        b  .  .  .  .  .  b .
        .  .  .  r  .  .  .  .
        
    */
    @Test
    public void CalculatePinnedPieces_FriendliesBlockingPin_NoDirections(){
        Board board = new Board("b2r2b1/1PPPPP2/1PPPPP2/rPPKPP1r/1PPPPP2/1PPPPP2/b5b1/3r4 w - - 0 1");


        long team = BoardUtil.getTeamBoard(board, true);
        long enemies = BoardUtil.getTeamBoard(board, false);
        long enemySlidingPieces = board.BLACK_BISHOPS | board.BLACK_ROOKS | board.BLACK_QUEENS;

        HashMap<Integer, Integer> pinnedPieces = MoveGenerator.calculatePinnedPieces(team, enemies, new long[]{board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS}, BoardUtil.getKingSquare(board, true), true);
        HashMap<Integer, Integer> expectedPinnedPieces = new HashMap<>();


        assertEquals(expectedPinnedPieces, pinnedPieces);

    }

    
    /*

        * Not pinned in any direction due to non-sliding enemy pieces blocking

        b  .  .  r  .  .  b  .
        .  k  k  k  k  k  .  .
        .  k  P  P  P  k  .  .
        r  k  P  K  P  k  .  r
        .  k  P  P  P  k  .  .
        .  k  k  k  k  k  .  .
        b  .  .  .  .  .  b .
        .  .  .  r  .  .  .  .
        
    */
    @Test
    public void CalculatePinnedPieces_NonSlidingEnemiesBlockingPin_NoDirections(){
        Board board = new Board("b2r2b1/1nnnnn2/1nPPPn2/rnPKPn1r/1nPPPn2/1nnnnn2/b5b1/3r4 w - - 0 1");

        long team = BoardUtil.getTeamBoard(board, true);
        long enemies = BoardUtil.getTeamBoard(board, false);
        long enemySlidingPieces = board.BLACK_BISHOPS | board.BLACK_ROOKS | board.BLACK_QUEENS;

        HashMap<Integer, Integer> pinnedPieces = MoveGenerator.calculatePinnedPieces(team, enemies, new long[]{board.BLACK_BISHOPS, board.BLACK_ROOKS, board.BLACK_QUEENS}, BoardUtil.getKingSquare(board, true), true);
        HashMap<Integer, Integer> expectedPinnedPieces = new HashMap<>();

        assertEquals(expectedPinnedPieces, pinnedPieces);
    }

    private void assertEquals(HashMap<Integer, Integer> expectedPinnedPieces, HashMap<Integer, Integer> pinnedPieces) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
