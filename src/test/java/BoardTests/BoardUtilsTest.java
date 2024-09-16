package test.java.BoardTests;

import main.java.*;
import main.utilities.*;

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
        long pushMask = 0L;
        for(int i=0; i<64; i+=9){
            pushMask = BoardUtil.setBit(pushMask, i);
        }
        int captureMask = 36;
        assertTrue(BoardUtil.squareValidInCaptureAndPushMasks(36, captureMask, pushMask));
    }

    @Test
    public void squareValidInCaptureAndPushMasks_CaptureMaskOnly_EqualsTrue(){
        long pushMask = 0L;
        for(int i=0; i<64; i+=9){
            pushMask = BoardUtil.setBit(pushMask, i);
        }
        int captureMask = 41;
        assertTrue(BoardUtil.squareValidInCaptureAndPushMasks(41, captureMask, pushMask));

    }

    @Test
    public void squareValidInCaptureAndPushMasks_PushMaskOnly_EqualsTrue(){
        long pushMask = 0L;
        for(int i=0; i<64; i+=9){
            pushMask = BoardUtil.setBit(pushMask, i);
        }
        int captureMask = 41;
        assertTrue(BoardUtil.squareValidInCaptureAndPushMasks(9, captureMask, pushMask));
    }

    @Test
    public void squareValidInCaptureAndPushMasks_NeitherMask_EqualsFalse(){
        long pushMask = 0L;
        for(int i=0; i<64; i+=9){
            pushMask = BoardUtil.setBit(pushMask, i);
        }
        int captureMask = 27;
        assertFalse(BoardUtil.squareValidInCaptureAndPushMasks(60, captureMask, pushMask));
    }

    @Test
    public void isValidPinDirection_NullMap_EqualsTrue(){
        assertEquals(true, false);

    }

    @Test
    public void isValidPinDirection_ContainsDirection_EqualsTrue(){
        assertEquals(true, false);

    }

    @Test
    public void isValidPinDirection_DoesNotContainDirection_EqualsFalse(){
        assertEquals(true, false);
    }

    @Test
    public void canKingSideCastle_EqualsTrue(){

    }

    // @Test
    // public void canKingSideCastle_InvalidCasltingRights_EqualsFalse(){

    // }

    // @Test
    // public void canKingSideCastle_F_File_Danger_EqualsFalse(){

    // }

    
    // @Test
    // public void canKingSideCastle_F_File_Blocked_EqualsFalse(){

    // }

    // @Test
    // public void canKingSideCastle_G_File_Danger_EqualsFalse(){
        
    // }
    
    // @Test
    // public void canKingSideCastle_G_File_Blocked_EqualsFalse(){

    // }

   
}
