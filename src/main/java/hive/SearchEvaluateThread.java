package main.java.hive;

import java.util.concurrent.Callable;

import main.java.board.Board;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import main.java.utilities.*;
import java.util.*;


public class SearchEvaluateThread implements Callable<SearchResult>{
    
    private Board board;
    private Move move;
    private int depth;
    private boolean isWhite;

    
    public SearchEvaluateThread(Board board, Move move, int depth, boolean isWhite){
        this.board = new Board(board);
        this.move = move;
        this.depth = depth;
        this.isWhite = isWhite;

    }

    @Override
    public SearchResult call(){
        this.board.makeMove(this.move);
        this.depth = depth - 1;
        return SearchAndEvaluate(board, this.move, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, !isWhite);
    }

    public SearchResult SearchAndEvaluate(Board board, Move currMove, int depth, int alpha, int beta, boolean isWhite){
        List<Move> legalMoves = MoveGenerator.getCurrentLegalMoves(board);
        
        if(depth <= 0 || legalMoves.isEmpty()){
            Stack<Move> moveHistory = new Stack<>();
            return new SearchResult(this.move, HiveEvaluator.Evaluate(board), moveHistory);
        }
        
        if(isWhite){
            SearchResult bestMove = new SearchResult(null, Integer.MIN_VALUE, null);

            for(Move move : legalMoves){
                board.makeMove(move);
                SearchResult sr = SearchAndEvaluate(board, move, depth - 1, alpha, beta, false);
                
                if(sr.score() > bestMove.score()){
                    bestMove = sr;
                    sr.moveHistory().push(move);
                }
                alpha = Math.max(alpha, sr.score());
                
                board.undoMove(move);

                if(beta <= alpha){
                    break;
                }

            }

            return new SearchResult(this.move, bestMove.score(), bestMove.moveHistory());

        }else{

            SearchResult bestMove = new SearchResult(null, Integer.MAX_VALUE, null);

            for(Move move : legalMoves){
                board.makeMove(move);

                SearchResult sr = SearchAndEvaluate(board, move, depth - 1, alpha, beta, true);

                if(sr.score() < bestMove.score()){
                    bestMove = sr;
                    sr.moveHistory().push(move);

                }
                beta = Math.min(beta, sr.score());

                board.undoMove(move);

                if(beta <= alpha){
                    break;
                }
            }

            return new SearchResult(this.move, bestMove.score(), bestMove.moveHistory());

        }


    }

    

    
}
