package main.java.network;

import java.io.Serializable;
import java.util.UUID;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.hive.HiveHash;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import main.java.move.MoveType;

import java.util.List;

public class GameState implements Serializable{
    public Board board;
    public List<Move> currentLegalMoves, enemyPsuedoLegalMoves;
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
        this.enemyPsuedoLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, board.IS_WHITE_TURN, false, true);
    }

    public void update(Move move){
        this.board.makeMove(move);
        this.recentMove = move;
        this.currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
        this.enemyPsuedoLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, !board.IS_WHITE_TURN, false, true);

        if(currentLegalMoves.isEmpty()){
            for(Move emove : enemyPsuedoLegalMoves){
                if(emove.moveType() == MoveType.CAPTURE && BoardUtil.isSquareOnBoard(emove.toSquare(), isWhite ? board.WHITE_KINGS : board.BLACK_KINGS)){
                    return;
                }
            }
        }
    }

    public void update(Board board){
        this.board = board;
        this.currentLegalMoves = MoveGenerator.getCurrentLegalMoves(board);
        this.enemyPsuedoLegalMoves = MoveGenerator.getEnemyPsuedoLegalMoves(board, !board.IS_WHITE_TURN, false, true);

        if(currentLegalMoves.isEmpty()){
            for(Move emove : enemyPsuedoLegalMoves){
                if(emove.moveType() == MoveType.CAPTURE && BoardUtil.isSquareOnBoard(emove.toSquare(), isWhite ? board.WHITE_KINGS : board.BLACK_KINGS)){

                }
            }

        }

    }

}
