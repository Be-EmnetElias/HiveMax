package main.java.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.java.utilities.BoardUtil;
import main.java.utilities.Move;
import main.java.utilities.MoveGenerator;
import main.java.Board;

public class Server implements Runnable{


    private ArrayList<ConnectionHandler> connections; 
    private ArrayList<Game> activeGames;
    private ServerSocket server;
    private boolean shuttingDown;
    private ExecutorService threadPool;

    public Server(){
        this.connections = new ArrayList<>();
        this.activeGames = new ArrayList<>();
        this.shuttingDown = false;
    }

    public static void main(String[] args){
        Server server = new Server();
        server.run();
    }

    @Override
    public void run(){
        try{
            server = new ServerSocket(9999);
            threadPool = Executors.newCachedThreadPool();
            System.out.println("[Server] Started on port 9999");
            while(!shuttingDown){
                Socket client = server.accept();
                UUID clientId = UUID.randomUUID();
                ConnectionHandler handler = new ConnectionHandler(client, clientId);
                connections.add(handler);
                threadPool.execute(handler);


                System.out.println("[Server] Client connected, ID: " + clientId);
                System.out.println("[Server] Active Clients " + connections.size());

                matchmake();

                
            }

        } catch (IOException e) {
            shutdown();
        }
    }

    public void matchmake(){
        ConnectionHandler[] players = new ConnectionHandler[2];
        for(ConnectionHandler ch : connections){
            
            if(ch != null){

                if(players[0] != null){
                    System.out.println("[Server] Matchmaking, found 2 available clients");
                    players[1] = ch;
                    Game game = new Game(players);
                    activeGames.add(game);

                }else{
                    players[0] = ch;
                }
            }
        }

        // remove players from queue
        if(players[0] != null && players[1] != null ){
            connections.remove(players[0]);
            connections.remove(players[1]);
            System.out.println("[Server] Matchmaking complete, " + connections.size() + " active clients");
        }

        



    }
    
    public void handleMessage(Object message){
        for(Game g : activeGames){
            g.updateGame((Move)message);
        }
        // for(ConnectionHandler ch : connections){
        //     if(ch != null){
        //         ch.sendMessage(message);
        //     }
        // }

    }

    public void shutdown(){
        try {
            shuttingDown = true;
            threadPool.shutdown();
            if(!server.isClosed()){
                server.close();
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private class ConnectionHandler implements Runnable {

        private Socket client;
        private UUID clientId;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ConnectionHandler(Socket client, UUID id){
            this.client = client;
            this.clientId = id;
        }

        public void sendMessage(Object message){
            try {
                out.reset();
                out.writeObject(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            try{
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                sendMessage(clientId);

                Object message;
                while((message = in.readObject()) != null){
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                shutdown();
            }
        }

        public void shutdown(){
            
            try {
                in.close();
                out.close();
                if(!client.isClosed()){
                    client.close();
                    System.out.println("[Server] Closing client connection");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    private class Game {
        UUID gameId;
        ConnectionHandler white, black;
        GameState gameState;

        public Game(ConnectionHandler[] players){
            
            this.gameId = UUID.randomUUID();
            this.white = players[0];
            this.black = players[1];
            this.gameState = new GameState(white.clientId, black.clientId);

            white.sendMessage(this.gameState);
            black.sendMessage(this.gameState); 
        }

        public void updateGame(Move move){
            
            this.gameState.update(move);
            System.out.println("[Server] Updating game " + gameId + " with move: " + move);
            white.sendMessage(this.gameState);
            black.sendMessage(this.gameState); 

        }

    }
}