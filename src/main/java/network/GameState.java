package main.java.network;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;
import main.java.Board;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;

public class GameState implements Serializable{
    public Board board;
    public HashSet<Move> currentLegalMoves, enemyPsuedoLegalMoves;
    public UUID whitePlayer, blackPlayer;
    public boolean isWhite;
    public Move recentMove;

    public boolean whiteWon = false;
    public boolean blackWon = false;

    public GameState(UUID whiteId, UUID blackId){
        this.whitePlayer = whiteId;
        this.blackPlayer = blackId;

        this.board = new Board();
        this.currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
        this.enemyPsuedoLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, board.IS_WHITE_TURN, false);
    }

    public void update(Move move){
        this.board.makeMove(move);
        this.recentMove = move;
        this.currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
        this.enemyPsuedoLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, !board.IS_WHITE_TURN, false);

        if(currentLegalMoves.size() == 0){
            if(board.IS_WHITE_TURN){
                System.out.println("BLACK WINS");
            }else{
                System.out.println("WHITE WINS");
            }
        }

    }
}
