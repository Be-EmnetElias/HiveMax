package main.java;

import java.util.HashMap;
import java.util.HashSet;

import main.util.*;

import main.java.*;


public class Board {
    
    public long WHITE_PAWNS, WHITE_KNIGHTS, WHITE_BISHOPS, WHITE_ROOKS, WHITE_QUEENS, WHITE_KINGS;

    public long BLACK_PAWNS, BLACK_KNIGHTS, BLACK_BISHOPS, BLACK_ROOKS, BLACK_QUEENS, BLACK_KINGS;

    public int ENPASSANT_SQUARE;

    public int CASTLING_RIGHTS;

    public boolean IS_WHITE_TURN;



    

    public Board(){
        setBoard(this, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
    }

    public Board(String fenPosition){
        setBoard(this, fenPosition);
    }

    public void setBoard(Board board, String fenposition){
        
        String[] boardInformation = fenposition.split("\\s+");

        String position = boardInformation[0];
        String turn = boardInformation[1];
        String castleRights = boardInformation[2];
        String enpassantSquare = boardInformation[3];
        // String halfmoves = boardInformation[4];

        // Set turn
        board.IS_WHITE_TURN = turn.equals("w");

        //Init all bitboards to empty
        board.WHITE_PAWNS = 0L; 
        board.WHITE_BISHOPS = 0L; 
        board.WHITE_KNIGHTS = 0L;
        board.WHITE_ROOKS = 0L; 
        board.WHITE_QUEENS = 0L; 
        board.WHITE_KINGS = 0L; 
        board.BLACK_PAWNS = 0L; 
        board.BLACK_BISHOPS = 0L; 
        board.BLACK_KNIGHTS = 0L; 
        board.BLACK_ROOKS = 0L; 
        board.BLACK_QUEENS = 0L;
        board.BLACK_KINGS = 0L;

        board.ENPASSANT_SQUARE = -1;
        board.CASTLING_RIGHTS = 0;

        int row = 0;  // Start at the top row
        int col = 0;  // Start at the first column

        //Set piece position
        for (char c : position.toCharArray()) {
            int currentSquare = row * 8 + col;
            switch (c) {
                case 'P': board.WHITE_PAWNS = BoardUtil.setBit(board.WHITE_PAWNS, currentSquare);   break;
                case 'N': board.WHITE_KNIGHTS = BoardUtil.setBit(board.WHITE_KNIGHTS, currentSquare); break;
                case 'B': board.WHITE_BISHOPS = BoardUtil.setBit(board.WHITE_BISHOPS, currentSquare); break;
                case 'R': board.WHITE_ROOKS = BoardUtil.setBit(board.WHITE_ROOKS, currentSquare);   break;
                case 'Q': board.WHITE_QUEENS = BoardUtil.setBit(board.WHITE_QUEENS, currentSquare);  break;
                case 'K': board.WHITE_KINGS = BoardUtil.setBit(board.WHITE_KINGS, currentSquare);   break;
                case 'p': board.BLACK_PAWNS = BoardUtil.setBit(board.BLACK_PAWNS, currentSquare);   break;
                case 'n': board.BLACK_KNIGHTS = BoardUtil.setBit(board.BLACK_KNIGHTS, currentSquare); break;
                case 'b': board.BLACK_BISHOPS = BoardUtil.setBit(board.BLACK_BISHOPS, currentSquare); break;
                case 'r': board.BLACK_ROOKS = BoardUtil.setBit(board.BLACK_ROOKS, currentSquare);   break;
                case 'q': board.BLACK_QUEENS = BoardUtil.setBit(board.BLACK_QUEENS, currentSquare);  break;
                case 'k': board.BLACK_KINGS = BoardUtil.setBit(board.BLACK_KINGS, currentSquare);   break;
                case '/': row++; col = -1; break;  // Move to the next row
                default: col += Character.getNumericValue(c) - 1; break;  // Skip empty squares
            }

            col++;
        }

        // Set enpassant square
        if (!enpassantSquare.equals("-")) {
            int epCol = enpassantSquare.charAt(0) - 'a';
            int epRow = 7 - (enpassantSquare.charAt(1) - '1');
            board.ENPASSANT_SQUARE = epRow * 8 + epCol;
        }

        // Set castling rights
        if (castleRights.contains("K")) board.CASTLING_RIGHTS |= 1;  // White King-side
        if (castleRights.contains("Q")) board.CASTLING_RIGHTS |= 2;  // White Queen-side
        if (castleRights.contains("k")) board.CASTLING_RIGHTS |= 4;  // Black King-side
        if (castleRights.contains("q")) board.CASTLING_RIGHTS |= 8;  // Black Queen-side
    }

    public String getFenString(Board board){
        String fenPosition = "";

        int emptyPieces = 0;
        for(int i=0; i<64; i++){
            if(i%8 == 0 && i > 0){
                if(emptyPieces > 0) fenPosition += emptyPieces;
                if(i < 63) fenPosition += "/";
                
                emptyPieces = 0;
            }

            PieceType curr = BoardUtil.getPieceTypeAtSquare(board, i);
            if(curr != PieceType.EMPTY){
                if(emptyPieces > 0){
                    fenPosition += "" + emptyPieces;
                    emptyPieces = 0;
                }

                fenPosition += BoardUtil.pieceTypeToString(curr);


            }else{
                emptyPieces += 1;
            }
            
        }

        fenPosition += " " + (board.IS_WHITE_TURN ? "w":"b");
        fenPosition += " KQkq";
        fenPosition += " -";
        fenPosition += " 0 0";

        return fenPosition;
    }


    








}
