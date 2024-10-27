package main.java.hive;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.*;

import main.java.utilities.*;
import main.java.hive.SearchEvaluateThread;;

public class HiveSearch {
    
    public static double CHECKMATE = 100, PROMOTION = 50, CASTLE = 30, CAPTURE = 25, CHECK = 10, DEFAULT = 0;

    public static boolean showLogs = false;

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();//Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static int DEPTH = 5;

    public HiveSearch(){}

    public static PerftInfo nodeCount(int start, int depth, Board originalBoard){
        if(depth == 0) return new PerftInfo(1,0,0,0,0,0,0,0,0);


        long captures = 0;
        long enpassants = 0;
        long castles = 0;
        long promotions = 0;
        long checks = 0;
        long discoveryChecks = 0;
        long doubleChecks = 0;
        long checkmates = 0;

        List<Move> moves = MoveGenerator.getCurrentLegalMoves(originalBoard);
        long nodes = moves.stream().parallel()
            .map(move -> {
                Board board = new Board(originalBoard); //todo should be a copy of original board
                board.makeMove(move);
                PerftInfo info = nodeCount(depth - 1, board);
                return info.nodes();
            })
            .reduce(0L, (x,y) -> x+y);

        return new PerftInfo(nodes, captures, enpassants, castles, promotions, checks, discoveryChecks, doubleChecks, checkmates);
      
    }

    private static PerftInfo nodeCount(int depth, Board board){
        if(depth == 0) return new PerftInfo(1,0,0,0,0,0,0,0,0);

        long nodes = 0;
        long captures = 0;
        long enpassants = 0;
        long castles = 0;
        long promotions = 0;
        long checks = 0;
        long discoveryChecks = 0;
        long doubleChecks = 0;
        long checkmates = 0;

        List<Move> moves = MoveGenerator.getCurrentLegalMoves(board);

        for(Move move: moves){
            switch(move.moveType()){
                case CAPTURE:       captures += 1;      break;
                case CASTLE:        checks += 1;        break;
                case CHECK:         checks += 1;        break;
                case ENPASSANT:     enpassants += 1;    break;
                case PROMOTION:     promotions += 1;    break;
                default: break;
            }
            board.makeMove(move);

            PerftInfo newInfo = nodeCount(depth - 1, board);
            
            nodes += newInfo.nodes();
            captures += newInfo.captures();
            enpassants += newInfo.enpassants();
            castles += newInfo.castles();
            promotions += newInfo.promotions();
            checks += newInfo.checks();
            discoveryChecks += newInfo.discoveryChecks();
            doubleChecks += newInfo.doubleChecks();
            checkmates += newInfo.checkmates();

            board.undoMove(move);
    
            
            
        }



        return new PerftInfo(nodes, captures, enpassants, castles, promotions, checks, discoveryChecks, doubleChecks, checkmates);
        
    }

    public static Move bestMove(Board board, List<Move> currentLegalMoves, boolean isWhite) {
        
        //todo: sort legal moves first
        //todo: since the first layer of moves are all given to threads, cannot prune at this top level, which could save a lot of time
        Log("Searching " + currentLegalMoves.size() + " moves");
        List<SearchEvaluateThread> tasks = new ArrayList<>();
        for(Move move : currentLegalMoves){
            tasks.add(new SearchEvaluateThread(new Board(board), move, DEPTH, isWhite));
        }


        List<SearchResult> bestMovesSorted = new ArrayList<>();
        
        List<Future<SearchResult>> results;
        try {
            results = EXECUTOR.invokeAll(tasks);
            for (Future<SearchResult> result : results){
                SearchResult sr = result.get();
                bestMovesSorted.add(sr);
                Collections.sort(bestMovesSorted, (sr1, sr2) -> sr2.score() - sr1.score());
            }
        } catch (InterruptedException | ExecutionException e) {

        } 

        Log("======Best moves sorted=====");
        for(SearchResult sr : bestMovesSorted){
            Log("\t Score: " + sr.score() + " : " + sr.move());
            while(!sr.moveHistory().isEmpty()){
                Log("\t\t " + sr.moveHistory().pop());
            }
        }
        Move bestWhiteMove = bestMovesSorted.get(0).move();
        Move bestBlackMove = bestMovesSorted.get(Math.max(0, bestMovesSorted.size() - 1)).move();
        Move bestMove = isWhite ? bestWhiteMove : bestBlackMove;

        Log("Best Move " + bestMove);
        return bestMove;
    }

    private static void Log(String msg){
        if(showLogs){
            System.out.println("[ Hive Search ] " + msg);
        }
    }


}
