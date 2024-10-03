package main.java.network;

import main.java.Board;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;

import java.util.HashSet;
import java.util.UUID;
import java.io.Serializable;

public class GameState implements Serializable{
    public Board board;
    public HashSet<Move> currentLegalMoves, enemyPsuedoLegalMoves;
    public UUID whitePlayer, blackPlayer;
    public boolean isWhite;

    public GameState(UUID whiteId, UUID blackId){
        this.whitePlayer = whiteId;
        this.blackPlayer = blackId;

        this.board = new Board();
        this.currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
        this.enemyPsuedoLegalMoves = MoveGenerator.getCurrentLegalMoves(board, !board.IS_WHITE_TURN);
    }

    public void update(Move move){
        this.board.makeMove(move);
        this.currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
        this.enemyPsuedoLegalMoves = MoveGenerator.getCurrentLegalMoves(board, !board.IS_WHITE_TURN);

    }
}
