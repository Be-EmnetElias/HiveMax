package test.java.BoardTests;

import main.java.*;
import main.util.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class BoardUtilsTest {
    
    Board board;

    @BeforeEach
    public void setup(){
        this.board = new Board();
    }

    @Test
    public void GetKingSquare_BLACK_KING_EqualsFour(){
        assertEquals(4, BoardUtil.getKingSquare(board, false));
    }

    @Test
    public void GetKingSquare_WHITE_KING_EqualsSixty(){
        assertEquals(60, BoardUtil.getKingSquare(board, true));
    }

    @Test
    public void IsOccupiedByFriendly_EqualsTrue(){
        assertTrue(BoardUtil.isOccupiedByFriendly(0, board.BLACK_ROOKS));
    }

    @Test
    public void IsOccupiedByFriendly_EqualsFalse(){
        assertFalse(BoardUtil.isOccupiedByFriendly(1, board.BLACK_ROOKS));
    }

    @Test
    public void CheckValidSquare_EqualsTrue(){
        assertTrue(BoardUtil.checkValidSquare(30));
    }

    @Test
    public void CheckValidSquare_LessThanZero_EqualsFalse(){
        assertFalse(BoardUtil.checkValidSquare(-1));
    }

    @Test
    public void CheckValidSquare_GreaterThanSixtyThree_EqualsFalse(){
        assertFalse(BoardUtil.checkValidSquare(64));
    }

    @Test
    public void squareValidInCaptureAndPushMasks_BothMasks_EqualsTrue(){
        assertEquals(true, false);
    }

    @Test
    public void squareValidInCaptureAndPushMasks_CaptureMaskOnly_EqualsFalse(){
        assertEquals(true, false);
    }

    @Test
    public void squareValidInCaptureAndPushMasks_PushMaskOnly_EqualsFalse(){
        assertEquals(true, false);
    }

    @Test
    public void squareValidInCaptureAndPushMasks_NeitherMask_EqualsFalse(){
        assertEquals(true, false);
    }

    // valid pin, pinned map is null

    // valid pin, correct direction

    // invalid pin, incorrect direction
}
