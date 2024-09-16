package main.java;

import main.utilities.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import main.utilities.BoardUtil;
import main.utilities.Move;
import main.utilities.MoveGenerator;
import main.utilities.PerftInfo;


public class HiveEvaluator {
    
    public static double CHECKMATE = 100, PROMOTION = 50, CASTLE = 30, CAPTURE = 25, CHECK = 10, DEFAULT = 0;

    public static HashMap<PieceType, Integer> PIECE_WEIGHTS = new HashMap<>(){{
        put(PieceType.EMPTY, 0);
        put(PieceType.WHITE_KING, 0);
        put(PieceType.BLACK_KING, 0);



        put(PieceType.WHITE_PAWN, 100);
        put(PieceType.WHITE_KNIGHT, 200);
        put(PieceType.WHITE_BISHOP, 300);
        put(PieceType.WHITE_ROOK, 500);
        put(PieceType.WHITE_QUEEN, 900);

        put(PieceType.BLACK_PAWN, 100);
        put(PieceType.BLACK_KNIGHT, 200);
        put(PieceType.BLACK_BISHOP, 300);
        put(PieceType.BLACK_ROOK, 500);
        put(PieceType.BLACK_QUEEN, 900);

    }};

    public HiveEvaluator(){}

    public static PerftInfo nodeCount(int start, int depth, Board originalBoard){
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

        HashSet<Move> moves = MoveGenerator.getCurrentLegalMoves(originalBoard);
        long total = moves.stream().parallel()
            .map(move -> {
                Board board = new Board(originalBoard); //todo should be a copy of original board
                board.makeMove(move);
                PerftInfo info = nodeCount(depth - 1, board);
                System.out.println(BoardUtil.squareToString(move.fromSquare()) + "" + BoardUtil.squareToString(move.toSquare()) + " : " + info.nodes());
                return info.nodes();
            })
            .reduce(0L, (x,y) -> x+y);
        System.out.println(total);
        return new PerftInfo(total, captures, enpassants, castles, promotions, checks, discoveryChecks, doubleChecks, checkmates);

        // for(Move move: moves){
        //     switch(move.moveType()){
        //         case CAPTURE:       captures += 1;      break;
        //         case CASTLE:        checks += 1;        break;
        //         case CHECK:         checks += 1;        break;
        //         case ENPASSANT:     enpassants += 1;    break;
        //         case PROMOTION:     promotions += 1;    break;
        //         default: break;
        //     }
        //     originalBoard.makeMove(move);

        //     PerftInfo newInfo = nodeCount(depth - 1, originalBoard);
            
        //     nodes += newInfo.nodes();
        //     captures += newInfo.captures();
        //     enpassants += newInfo.enpassants();
        //     castles += newInfo.castles();
        //     promotions += newInfo.promotions();
        //     checks += newInfo.checks();
        //     discoveryChecks += newInfo.discoveryChecks();
        //     doubleChecks += newInfo.doubleChecks();
        //     checkmates += newInfo.checkmates();

        //     originalBoard.undoMove(move);
    
            
        //     String moveString = BoardUtil.squareToString(move.fromSquare()) + "" + BoardUtil.squareToString(move.toSquare());
        //     System.out.println(moveString + " : " + newInfo.nodes());
        // }

        // return new PerftInfo(nodes, captures, enpassants, castles, promotions, checks, discoveryChecks, doubleChecks, checkmates);
        
        
    }

    public static PerftInfo nodeCount(int depth, Board board){
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

    private static double getMovePriority(Move move){
        double result = 0.0;
        switch(move.moveType()){
            case CAPTURE:
                result += CAPTURE;
                break;
            case CASTLE:
                result += CASTLE;
                break;
            case CHECK:
                result += CHECK;
                break;
            case ENPASSANT:
                result += CAPTURE;
                break;
            case PROMOTION:
                result += PROMOTION;
                break;
            default:
                break;
            
        }

        return result;
    }


    public static List<Move> orderMoves(HashSet<Move> moves){
        List<Move> result = new ArrayList<Move>(moves);
        result.sort((move1,move2) -> Double.compare(getMovePriority(move2), getMovePriority(move1)));
        return result;
    }

    public static Move bestMove(Board board, HashSet<Move> currentLegalMoves, int depth, boolean isWhite) {
        double maxScore = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        List<Move> orderedMoves = orderMoves(currentLegalMoves);
        for (Move move : orderedMoves) {
            board.makeMove(move);
            List<Move> nextLegalMoves = orderMoves(MoveGenerator.getCurrentLegalMoves(board, !isWhite));
            List<Move> nextEnemyLegalMoves = orderMoves(MoveGenerator.getCurrentLegalMoves(board, isWhite));
            double score = -negamax(board, nextLegalMoves,nextEnemyLegalMoves, depth - 1, !isWhite,-beta,-alpha);  // Negate the score
            board.undoMove(move);

    
            if (score > maxScore) {
                maxScore = score;
                bestMove = move;
            }

            alpha = Math.max(alpha, score);
            if(alpha >= beta){
                break;
            }
        }

        return bestMove;
    }
    
    private static double negamax(Board board, List<Move> currentLegalMoves,List<Move> enemyLegalMoves, int depth, boolean COLOR, double alpha, double beta) {
        //if (depth <= 0) return SearchCapturesChecks(currentLegalMoves, enemyLegalMoves, COLOR, alpha, beta);
        if(depth <= 0) return staticEvaluation(currentLegalMoves,COLOR) - staticEvaluation(enemyLegalMoves, !COLOR);
    
        double maxScore = Double.NEGATIVE_INFINITY;
        
    
        for (Move move : currentLegalMoves) {
            board.makeMove(move);
            List<Move> nextLegalMoves = orderMoves(MoveGenerator.getCurrentLegalMoves(board, !COLOR));
            List<Move> nextEnemyLegalMoves = orderMoves(MoveGenerator.getCurrentLegalMoves(board, COLOR));
            double score = -negamax(board, nextLegalMoves, nextEnemyLegalMoves, depth - 1, !COLOR, -beta, -alpha);  // Negate the score
            board.undoMove(move);
    
            if (score > maxScore) {
                maxScore = score;
            }

            alpha = Math.max(alpha,score);
            if(alpha >= beta) break;
        }
    
        return maxScore;
    }

    private static double staticEvaluation(List<Move> legalMoves, boolean isWhite){
        if(legalMoves.isEmpty()){
            return Double.NEGATIVE_INFINITY;
        }else{
            int score = 0;
            for(Move move : legalMoves){
                score += PIECE_WEIGHTS.get(move.pieceType());
            }
            return score;
        }
    }



}
