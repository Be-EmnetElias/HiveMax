package main.util;

public record Move(
    int fromSquare,
    int toSquare,   
    PieceType pieceType,   
    PieceType capturedPieceType,
    PieceType promotionName,
    MoveType moveType 
){

    @Override
    public String toString(){
        return pieceType + " " + moveType + " from " + fromSquare + " to " + toSquare + (capturedPieceType == PieceType.EMPTY ? "" : " capturing " + capturedPieceType) + "\n";
    }
}

