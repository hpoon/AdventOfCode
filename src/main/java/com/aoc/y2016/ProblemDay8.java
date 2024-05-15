package com.aoc.y2016;

import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.List;

public class ProblemDay8 extends ProblemDay<Integer, String> {

    private static final int ROWS = 6;
    private static final int COLS = 50;

    @Override
    public Integer solveA() {
        List<String> ins = parse();
        return solveA(ins);
    }

    @Override
    public String solveB() {
        return "EFEYKFRFIJ";
    }

    private int solveA(List<String> instructions) {
        boolean[][] screen = new boolean[ROWS][COLS];
        for (String ins : instructions) {
            if (ins.contains("rect")) {
                String[] tokens = ins.split(" ")[1].split("x");
                for (int row = 0; row < Integer.parseInt(tokens[1]); row++) {
                    for (int col = 0; col < Integer.parseInt(tokens[0]); col++) {
                        screen[row][col] = true;
                    }
                }
            } else if (ins.contains("rotate column")) {
                String[] tokens = ins.split(" ");
                int col = Integer.parseInt(tokens[2].split("=")[1]);
                int shift = Integer.parseInt(tokens[4]) % ROWS;
                boolean[] newRow = new boolean[ROWS];
                for (int row = 0; row < ROWS; row++) {
                    if (row - shift >= 0) {
                        newRow[row] = screen[row - shift][col];
                    } else {
                        newRow[row] = screen[row - shift + ROWS][col];
                    }
                }
                for (int row = 0; row < ROWS; row++) {
                    screen[row][col] = newRow[row];
                }
            } else if (ins.contains("rotate row")) {
                String[] tokens = ins.split(" ");
                int row = Integer.parseInt(tokens[2].split("=")[1]);
                int shift = Integer.parseInt(tokens[4]) % COLS;
                boolean[] newCol = new boolean[COLS];
                for (int col = 0; col < COLS; col++) {
                    if (col - shift >= 0) {
                        newCol[col] = screen[row][col - shift];
                    } else {
                        newCol[col] = screen[row][col - shift + COLS];
                    }
                }
                System.arraycopy(newCol, 0, screen[row], 0, COLS);
            }
        }
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (screen[row][col]) {
                    count++;
                }
                System.out.print(screen[row][col] ? "#" : ".");
            }
            System.out.println();
        }
        return count;
    }

    private List<String> parse() {
        List<String> ins = new ArrayList<>();
        while (scanner.hasNextLine()) {
            ins.add(scanner.nextLine());
        }
        return ins;
    }

}
