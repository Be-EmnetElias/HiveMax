package main.java.hive;

import main.java.board.Board;
import main.java.board.BoardUtil;
import main.java.move.Move;
import main.java.move.MoveGenerator;
import java.util.*;

/**
 * TODO:
 * A tournament between different HiveWeights
 * 
 * The first round create random weights and have them play each other
 * 
 * On every new round, create more HiveWeights derived from the previous winners
 * 
 */
public class HiveTournament {
    

    private Game[] CURRENT_GAMES;
    public boolean showLogs = true;

    public void Log(String msg){
        if(showLogs){
            System.out.println("[HiveTournament] " + msg);
        }
    }
    public HiveTournament(int initialPlayerCount){
        this.CURRENT_GAMES = new Game[initialPlayerCount / 2];
        for(int i=0; i<CURRENT_GAMES.length; i++){
            Game newGame = new Game(new Board(), HiveEvaluator.SUBJECT_YMIR_WEIGHTS, HiveEvaluator.SUBJECT_YMIR_WEIGHTS);
            this.CURRENT_GAMES[i] = newGame;

        }
    }

    public void Start(){
        Log("Starting Tournament " + CURRENT_GAMES.length + " GAMES");
        boolean done = true;
        ArrayList<HiveWeights> winners = new ArrayList<>();
        do {
            done = true;
            for(Game game : this.CURRENT_GAMES){

                if(!BoardUtil.isGameOver(game.board())){
                    done = false;
                    if(game.board().IS_WHITE_TURN){
                        Move move = HiveSearch.bestMove(
                            new Board(game.board()),
                            game.player1(),
                            MoveGenerator.getCurrentLegalMoves(game.board()),
                            true
                        );
                        game.board().makeMove(move);
                    }else{
                        Move move = HiveSearch.bestMove(
                            new Board(game.board()),
                            game.player2(),
                            MoveGenerator.getCurrentLegalMoves(game.board()),
                            false
                        );
                        game.board().makeMove(move);
    
                    }
                    game.board().incr();

                    if(BoardUtil.isGameOver(game.board())){
                        if(game.board().IS_WHITE_TURN){
                            winners.add(game.player2());
                        }else{
                            winners.add(game.player1());
                        }
                    }
                }
            }
        }while(!done);

        Log("Winners: " + winners.size());
        Update(winners);

    }

    //todo
    public void Update(ArrayList<HiveWeights> winners){
        
    }

    //todo
    public void Mutate(){

    }

    private record Game(Board board, HiveWeights player1, HiveWeights player2) {}
    
}
