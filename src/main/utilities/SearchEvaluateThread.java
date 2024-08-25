package main.utilities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
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
        Random rand = new Random();
        ArrayList<Move> currentLegalMoves = new ArrayList<>(MoveGenerator.getCurrentLegalMoves(board, board.IS_WHITE_TURN));
        int randomMoveIndex = rand.nextInt(currentLegalMoves.size());
        Move randomMove = currentLegalMoves.get(randomMoveIndex);
        board.makeMove(randomMove);

        System.out.println("Thread # " + threadNumber + " made random move: " + randomMove + " and found " + MoveGenerator.getCurrentLegalMoves(board, true).size() + " more moves");
    }
}
