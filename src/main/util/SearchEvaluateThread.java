package main.util;

import main.java.Board;

public class SearchEvaluateThread extends Thread {
    
    Board board;
    int threadNumber;

    public SearchEvaluateThread(Board board, int num){
        this.board = board;
        this.threadNumber = num;
    }

    @Override
    public void run(){
        System.out.println("Thread Number # " + threadNumber + " found " + MoveGenerator.getCurrentLegalMoves(board, board.IS_WHITE_TURN).size() + " moves");
    }
}
