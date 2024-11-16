package main.java.hive;

import java.util.concurrent.Callable;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import java.util.*;


public class SearchEvaluateThread implements Callable<SearchResult>{
    
    private Board board;
    private Move move;
    private int depth;
    private boolean isWhite;
    private HiveWeights weights;
    
    public SearchEvaluateThread(Board board, HiveWeights weights, Move move, int depth, boolean isWhite){
        this.board = new Board(board);
        this.move = move;
        this.depth = depth;
        this.isWhite = isWhite;
        this.weights = weights;

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
            long hash = HiveHash.getHash(board);
            

            if(HiveHash.containsHash(hash)){
                System.out.println("seen hash");
                return new SearchResult(this.move, HiveHash.getHashValue(hash));
            }else{
                // System.out.println("new  hash, size; " + HiveHash.HIVE_HASH_TABLE.size());
                int score = HiveEvaluator.Evaluate(board, weights);
                HiveHash.putHashValue(hash, score);
                return new SearchResult(this.move, score);
            }
        }
        
        if(isWhite){
            SearchResult bestMove = new SearchResult(null, Integer.MIN_VALUE);

            for(Move move : legalMoves){
                board.makeMove(move);
                SearchResult sr = SearchAndEvaluate(board, move, depth - 1, alpha, beta, false);
                
                if(sr.score() > bestMove.score()){
                    bestMove = sr;
                }
                alpha = Math.max(alpha, sr.score());
                
                board.undoMove(move);

                if(beta <= alpha){
                    break;
                }

            }

            return new SearchResult(this.move, bestMove.score());

        }else{

            SearchResult bestMove = new SearchResult(null, Integer.MAX_VALUE);

            for(Move move : legalMoves){
                board.makeMove(move);

                SearchResult sr = SearchAndEvaluate(board, move, depth - 1, alpha, beta, true);

                if(sr.score() < bestMove.score()){
                    bestMove = sr;

                }
                beta = Math.min(beta, sr.score());

                board.undoMove(move);

                if(beta <= alpha){
                    break;
                }
            }

            return new SearchResult(this.move, bestMove.score());

        }


    }

    

    
}
