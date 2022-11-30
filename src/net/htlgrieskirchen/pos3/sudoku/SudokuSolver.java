package net.htlgrieskirchen.pos3.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/* Please enter here an answer to task four between the tags:
 * <answerTask4>
 *    Hier sollte die Antwort auf die Aufgabe 4 stehen.
 * </answerTask4>
 */

public class SudokuSolver implements ISodukoSolver {

    public SudokuSolver() {
        //initialize if necessary
    }

    @Override
    public final int[][] readSudoku(File file) {
        int[][] sudoku = new int[9][9];
        for (int i = 0; i < sudoku.length; i++)
            for (int j = 0; j < sudoku.length; j++) sudoku[i][j] = -1;

        try {
            Stream<String> lineStream = Files.lines(file.toPath());
            lineStream.forEach(x -> {
                int i = 0;
                while (sudoku[i][0] != -1) i++;

                //x.replace(" ", "");
                String[] parts = x.split(";");

                for (int j = 0; j < parts.length; j++) {
                    sudoku[i][j] = Integer.parseInt(parts[j]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sudoku;
    }

    @Override
    public boolean checkSudoku(int[][] rawSudoku) {
        List<Integer> numberList = new ArrayList<>();
        for (int[] ints : rawSudoku) {
            for (int j : ints) numberList.add(j);

            for (int k = 1; k < 10; k++) if (!numberList.contains(k)) return false;
            numberList.clear();
        }

        //numberList.clear();
        for (int j = 0; j < rawSudoku.length; j++) {
            for (int[] ints : rawSudoku) numberList.add(ints[j]);

            for (int i = 1; i < 10; i++) if (!numberList.contains(i)) return false;
            numberList.clear();
        }

        //numberList.clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                for (int k = i * 3; k < 3 + 3 * i; k++) {
                    for (int l = j * 3; l < 3 + 3 * j; l++) {
                        numberList.add(rawSudoku[k][l]);
                    }
                }

                for (int k = 1; k < 10; k++) if (!numberList.contains(k)) return false;
                numberList.clear();
            }
        }

        return true;
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        int[][][] entries = new int[9][9][9];

        //Step 1
        for (int i = 0; i < rawSudoku.length; i++)
            for (int j = 0; j < rawSudoku[i].length; j++) {
                if (rawSudoku[i][j] != 0) continue;

                for (int k = 1; k < 10; k++) entries[i][j][k - 1] = k;
            }

        boolean repeat = true;
        while(repeat) {

            //Step 2
            for (int i = 0; i < rawSudoku.length; i++)
                for (int j = 0; j < rawSudoku[i].length; j++) {
                    for (int k = 0; k < entries[i][j].length; k++) {
                        if (entries[i][j][k] == 0) continue;

                        if (checkIfRemove(rawSudoku, i, j, entries[i][j][k])){
                            entries[i][j][k] = 0;
                        }
                    }
                }

            //Step 3
            for (int i = 0; i < rawSudoku.length; i++)
                for (int j = 0; j < rawSudoku[i].length; j++) {
                    int number = 0;
                    for (int k = 0; k < entries[i][j].length; k++) {
                        if (entries[i][j][k] == 0) continue;

                        if (number != 0) {
                            number = 0;
                            break;
                        }

                        number = entries[i][j][k];
                    }
                    if (number != 0){
                        rawSudoku[i][j] = number;
                        //System.out.println(i + " | " + j + " -> " + number);
                    }
                }

            //Step 4
            repeat = false;
            for (int i = 0; i < rawSudoku.length; i++)
                for (int j = 0; j < rawSudoku[i].length; j++) {
                    if (rawSudoku[i][j] == 0) {
                        repeat = true;
                        break;
                    }
                    if(repeat) break;
                }

        }

        return rawSudoku;
    }

    private boolean checkIfRemove(int[][] rawSudoku, int x, int y, int number) {
        //System.out.println("Check: " + x + " | " + y + " : " + number);
        for (int j = 0; j < rawSudoku.length; j++)
            if (rawSudoku[x][j] == number) return true;

        for (int i = 0; i < rawSudoku.length; i++)
            if (rawSudoku[i][y] == number) return true;

        for (int i = x / 3 * 3; i < 3 + (x / 3) * 3; i++)
            for (int j = y / 3 * 3; j < 3 + (y / 3) * 3; j++) {
                //System.out.println( i + " | " + j);
                if (rawSudoku[i][j] == number) return true;
            }

        return false;
    }

    public long benchmark(int[][] rawSudoku){
        long startTime = System.currentTimeMillis();

        for(int i = 0; i < 10; i++){
            int[][] input = readSudoku(new File("1_sudoku_level1.csv"));

            int[][] output = solveSudoku(input);
            checkSudoku(output);
        }

        return System.currentTimeMillis() - startTime;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        // implement this method
        return new int[0][0]; // delete this line!
    }

    // add helper methods here if necessary
}
