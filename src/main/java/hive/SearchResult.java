package main.java.hive;

import java.util.Stack;
import main.java.utilities.Move;

public record SearchResult(Move move, int score, Stack<Move> moveHistory) {}
