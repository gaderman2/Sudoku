package net.htlgrieskirchen.pos3.sudoku;


import java.io.File;

public class Main {
    public static void main(String[] args) {
        SudokuSolver ss = new SudokuSolver();
        int[][] input = ss.readSudoku(new File("1_sudoku_level1.csv"));
        
        System.out.println(">--- ORIGINAL ---");
        printSudoku(input);
        int[][] output = ss.solveSudoku(input);
        System.out.println(">--- SOLUTION ---");
        printSudoku(output);
        System.out.println(">----------------");
        System.out.println("SOLVED    = " + ss.checkSudoku(output));
        System.out.println(">----------------");
        System.out.println("Benchmark: " + ss.benchmark(input) + " ms");
    }

    private static void printSudoku(int[][] sudoku){
        for(int i = 0; i < sudoku.length; i++) {
            for (int j : sudoku[i]) System.out.print(j + " | ");
            System.out.println();
        }
    }
}
