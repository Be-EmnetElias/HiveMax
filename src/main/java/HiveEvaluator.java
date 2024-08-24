package main.java;

import java.util.ArrayList;

import main.util.SearchEvaluateThread;

public class HiveEvaluator {
    
    public static void main(String[] args){
        for(int i=0; i<1000; i++){
            SearchEvaluateThread t = new SearchEvaluateThread(new Board(), i);
            t.start();
        }

        System.out.println("done, found");

    }
}
