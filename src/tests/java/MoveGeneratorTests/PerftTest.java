package tests.java.MoveGeneratorTests;

import main.java.*;
import main.utilities.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

public class PerftTest {
    
    Board board;
    MoveGenerator moveGenerator;

    @BeforeEach
    public void setup(){
        this.board = new Board(); 
        this.moveGenerator = new MoveGenerator();
    }

    @Test
    public void test(){
        
    }

}
