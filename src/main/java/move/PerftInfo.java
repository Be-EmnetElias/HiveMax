package main.java.move;

public record PerftInfo(long nodes, long captures, long enpassants, long castles, long promotions, long checks, long discoveryChecks, long doubleChecks, long checkmates) {}