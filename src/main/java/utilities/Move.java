package main.java.utilities;
import java.io.*;

/**
 * fromSquare
 * toSquare
 * pieceType
 * capturedPieceType
 * promotionPieceType
 * moveType
 */
public record Move(
    int fromSquare,
    int toSquare,   
    PieceType pieceType,   
    PieceType capturedPieceType,
    PieceType promotionPieceType,
    MoveType moveType 
) implements Serializable{

    @Override
    public String toString(){
        return pieceType + " " + moveType + " from " + fromSquare + " to " + toSquare + (capturedPieceType == PieceType.EMPTY ? "" : " capturing " + capturedPieceType) + "\n";
    }

    
}

