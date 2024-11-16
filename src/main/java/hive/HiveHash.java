package main.java.hive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import main.java.board.*;
import main.java.move.Move;
import java.util.Random;



/**
 * Zobrist Hashing <p>
 * 
 * Provides functionality to create a hash from a {@link main.java.board.Board} <p>
 * 
 * Keeps a look up table key::value => hash::<T> <p>
 * 
 * Allows multithreaded access to the table <p>
 */
public class HiveHash {
    
    /**
     * key::value => hashcode :: best move? legal moves? evaluation score?
     */
    public static ConcurrentHashMap<Long, Integer> HIVE_HASH_TABLE = new ConcurrentHashMap<>();
    
    /**
     *      Index Mapping: <p>
     *          
     *      0    -  63  : WHITE_PAWN at square 0 - 63 <p>
     *      64   -  127 : WHITE_KNIGHT at square 0 - 63 <p>
     *      ...  -  767 : ............................. <p>
     *      768  -  831 : ENPASSANT at square 0 - 63 <p>
     *      832  -  835 : CASTLING RIGHTS KQkq <p>
     *      836         : IS_WHITE_TURN <p>
     *      
     */
    private static int[] PIECE_HASH = new int[837];
    
    /**
     * Creates psuedorandom numbers for <p>
     *      each piece at each square (12 pieces x 64 squares) <p>
     *      side to move is white <p>
     *      castling rights <p>
     *      file of the enpassant square <p>
     * 
     *      for a total of 781 random numbers
     */
    public HiveHash(){

        long seed = 334561345786735675L;
        Random random = new Random(seed);
        
        for(int i=0; i<PIECE_HASH.length; i++){
            PIECE_HASH[i] = random.nextInt();
        }


    }

    /**
     * Creates a hash from this board by xoring each value
     * @param board
     * @return
     */
    public static long getHash(Board board){
        long hash = 0L;

        long[] boards = BoardUtil.getAllBoards(board);
        // pieces
        for(int i=0; i<boards.length; i++){
            int startingIndex = i * 64;
            long pieceBoard = boards[i];
            
            while(pieceBoard != 0){
                int square = Long.numberOfTrailingZeros(pieceBoard);
                hash ^= PIECE_HASH[startingIndex + square];
                pieceBoard &= pieceBoard - 1;
            }

        }

        // enpassant square
        int nextIndex = 12 * 64;
        int enpassantSquare = Long.numberOfTrailingZeros(board.ENPASSANT_SQUARE);
        hash ^= PIECE_HASH[nextIndex + enpassantSquare];

        // castling rights
        nextIndex += 64;
        if((board.CASTLING_RIGHTS & 8) != 0 ){
            hash ^= PIECE_HASH[nextIndex];
        }

        if((board.CASTLING_RIGHTS & 2) != 0 ){
            hash ^= PIECE_HASH[nextIndex + 1];
        }

        if((board.CASTLING_RIGHTS & 4) != 0 ){
            hash ^= PIECE_HASH[nextIndex + 2];
        }

        if((board.CASTLING_RIGHTS & 1) != 0 ){
            hash ^= PIECE_HASH[nextIndex + 3];
        }

        // white turn
        nextIndex += 4;
        if(board.IS_WHITE_TURN){
            hash ^= PIECE_HASH[nextIndex];
        }


        return hash;

    }

    public static boolean containsHash(long hashcode){
        return HIVE_HASH_TABLE.contains(hashcode);
    }

    public static int getHashValue(long hashcode){
        return HIVE_HASH_TABLE.get(hashcode);
    }

    public static void putHashValue(long hashcode, int value){
        HIVE_HASH_TABLE.put(hashcode, value);
    }
}
