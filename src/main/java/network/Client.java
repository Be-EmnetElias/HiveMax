package main.java.network;

import java.io.*;
import java.net.Socket;
import java.util.*;
import main.java.gui.ChessPanel;

public class Client implements Runnable {
    
    public Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public UUID clientId;
    public boolean isWhite;
    public ChessPanel chessPanel;

    public Client(ChessPanel chessPanel){
        this.chessPanel = chessPanel;
    }

    @Override
    public void run(){
        try{
            client = new Socket("localhost",9999); //73.140.79.98
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            Object inMessage;
            while((inMessage = in.readObject()) != null){

                if(inMessage instanceof UUID uuid){
                    this.clientId = uuid;
                    System.out.println("\n[Client] Connected to server, ID: " + clientId);
                }

                else if(inMessage instanceof GameState gameState){
                    System.out.println("\n[Client] Recieved " + inMessage);
                    gameState.isWhite = gameState.whitePlayer.equals(clientId);
                    chessPanel.updateChessPanel(gameState);
                }

                else{
                    System.out.println("\n[Client] Recieved unknown object");
                }
                
            }
        } catch (IOException | ClassNotFoundException e){
            System.out.println("[Client] Error: " + e);
            shutdown();
        }
    }

    public void sendMessage(Object message){
        try {
            out.writeObject(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void shutdown(){
        try{
            if(in != null && out != null){
                in.close();
                out.close();
            }
            if(client != null && !client.isClosed()){
                client.close();
            }
            System.out.println("[Client] Closing client");

        }catch (IOException e){
            System.out.println("[Client] Could not close client?");
        }
    }
    

}
