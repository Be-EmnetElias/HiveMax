package test.java.MoveGeneratorTests;

import main.java.*;
import main.util.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
