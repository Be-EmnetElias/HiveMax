package main.java.hive;

import java.util.HashSet;
import main.java.Board;
import main.java.utilities.*;


public class HiveSearch {
    
    public static double CHECKMATE = 100, PROMOTION = 50, CASTLE = 30, CAPTURE = 25, CHECK = 10, DEFAULT = 0;

    

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

        HashSet<Move> moves = MoveGenerator.getCurrentLegalMoves(originalBoard);
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

        HashSet<Move> moves = MoveGenerator.getCurrentLegalMoves(board);

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

    public static Move bestMove(Board board, HashSet<Move> currentLegalMoves, int depth, boolean isWhite) {
        int maxScore = Integer.MIN_VALUE;
        Move bestMove = null;

        System.out.println("[HiveSearch] Searching Board with " + currentLegalMoves.size() + " current legal moves:");
        for(Move move : currentLegalMoves){
            System.out.println("             Searching " + move);
            board.makeMove(move);
            HashSet<Move> nextLegalMoves = MoveGenerator.getCurrentLegalMoves(board, !isWhite);
            HashSet<Move> nextEnemyLegalMoves = MoveGenerator.getCurrentLegalMoves(board, isWhite);

            int score = -1 * negamax(board, nextLegalMoves, nextEnemyLegalMoves, depth - 1, !isWhite, isWhite);

            if(score >= maxScore){
                bestMove = move;
            }
            board.undoMove(move);
        }

        return bestMove;
    }

    private static int negamax(Board board, HashSet<Move> currentLegalMoves, HashSet<Move> enemyLegalMoves, int depth, boolean isWhite, boolean originallyWhite){

        if(depth <= 0){
            return HiveEvaluator.Evaluate(board, currentLegalMoves, originallyWhite) - HiveEvaluator.Evaluate(board, enemyLegalMoves, !originallyWhite);

            // return SearchCapturesChecks(board, currentLegalMoves, enemyLegalMoves, isWhite, originallyWhite, 1);
        }

        int maxScore = Integer.MIN_VALUE;

        for(Move move : currentLegalMoves){
           
            board.makeMove(move);

            HashSet<Move> nextLegalMoves = MoveGenerator.getCurrentLegalMoves(board, !isWhite);
            HashSet<Move> nextEnemyLegalMoves = MoveGenerator.getCurrentLegalMoves(board, isWhite);

            int score = -1 * negamax(board, nextLegalMoves, nextEnemyLegalMoves, depth - 1, !isWhite, originallyWhite);
            board.undoMove(move);

            if(score > maxScore){
                maxScore = score;
            }
            
        }
        return maxScore;
    }

    private static int SearchCapturesChecks(Board board, HashSet<Move> currentLegalMoves, HashSet<Move> enemyLegalMoves, boolean isWhite, boolean originallyWhite, int depth){
        HashSet<Move> captureAndCheckOnly = new HashSet<>();

        for(Move move : currentLegalMoves){
            if(move.moveType() == MoveType.CHECK || move.moveType() == MoveType.CAPTURE){
                captureAndCheckOnly.add(move);
            }
        }
        
        if(captureAndCheckOnly.isEmpty()){
            return HiveEvaluator.Evaluate(board, currentLegalMoves, originallyWhite) - HiveEvaluator.Evaluate(board, enemyLegalMoves, !originallyWhite);
        }

        int maxScore = Integer.MIN_VALUE;


        for(Move move : captureAndCheckOnly){
            board.makeMove(move);

            HashSet<Move> nextLegalMoves = MoveGenerator.getCurrentLegalMoves(board, !isWhite);
            HashSet<Move> nextEnemyLegalMoves = MoveGenerator.getCurrentLegalMoves(board, isWhite);
            int score = -1 * SearchCapturesChecks(board, nextLegalMoves, nextEnemyLegalMoves, !isWhite, originallyWhite, depth + 1);
            board.undoMove(move);

            if(score >= maxScore){
                maxScore = score;
            }
        }

        return maxScore;
    }
    
    



}
