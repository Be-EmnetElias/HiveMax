package main.java;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import main.utilities.Move;
import main.utilities.MoveGenerator;
import main.utilities.SearchEvaluateThread;

public class HiveEvaluator {
    
    public static void main(String[] args){
        
        // for(int i=0; i<10000; i++){
        //     SearchEvaluateThread t = new SearchEvaluateThread(new Board(), i);
        //     t.start();
        // // Board board = new Board();
        // // Random rand = new Random();
        // // ArrayList<Move> currentLegalMoves = new ArrayList<>(MoveGenerator.getCurrentLegalMoves(board, board.IS_WHITE_TURN));
        // // int randomMoveIndex = rand.nextInt(currentLegalMoves.size());
        // // Move randomMove = currentLegalMoves.get(randomMoveIndex);
        // // board.makeMove(randomMove);
        // // System.out.println("Made random move: " + randomMove + " and found " + MoveGenerator.getCurrentLegalMoves(board, true).size() + " more moves");
        // }


        int depth = 4;
        long startTime = System.currentTimeMillis();
        long nodes = nodeCount(depth, new Board());
        long endTime = System.currentTimeMillis();
        System.out.println("DEPTH: " + depth + " NODES: " + nodes);
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

    }

    public static long nodeCount(int depth, Board board){
        if(depth == 0) return 1;

        long nodes = 0;
        HashSet<Move> moves = MoveGenerator.getCurrentLegalMoves(board, board.IS_WHITE_TURN);

        for(Move move: moves){
            board.makeMove(move);
            nodes += nodeCount(depth - 1, board);
            board.undoMove(move);
        }

        return nodes;
        
    }
}
