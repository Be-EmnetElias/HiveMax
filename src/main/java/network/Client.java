package main.java.network;

import java.io.*;
import java.net.Socket;
import javax.swing.JPanel;
import java.util.*;

import main.java.Board;
import main.java.gui.ChessPanel;
import main.java.utilities.BoardUtil;
import main.java.utilities.Move;

public class Client implements Runnable {
    
    private Socket client;
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
            client = new Socket("localhost", 9999);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            Object inMessage;
            while((inMessage = in.readObject()) != null){

                if(inMessage instanceof UUID){
                    this.clientId = (UUID)inMessage;
                    System.out.println("\n[Client] Connected to server, ID: " + clientId);
                }

                else if(inMessage instanceof GameState){
                    System.out.println("\n[Client] Recieved " + inMessage);
                    GameState gameState = (GameState) inMessage;
                    gameState.isWhite = gameState.whitePlayer.equals(clientId);
                    chessPanel.updateChessPanel(gameState);
                }

                else{
                    System.out.println("\n[Client] Recieved unknown object");
                }
                
            }
        } catch (IOException | ClassNotFoundException e){
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
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        System.out.println("[Client] Closing client");

        }catch (IOException e){

        }
    }
    

}
