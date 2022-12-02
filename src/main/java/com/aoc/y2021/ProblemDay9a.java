package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProblemDay9a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<List<Integer>> field = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            field.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                field.get(field.size() - 1).add(Integer.parseInt(line.substring(i, i+1)));
            }
        }

        int risk = 0;
        for (int row = 0; row < field.size(); row++) {
            for (int col = 0; col < field.get(row).size(); col++) {
                final int val = field.get(row).get(col);
                final Integer top = row - 1 >= 0 ? field.get(row - 1).get(col) : null;
                final Integer bot = row + 1 < field.size() ? field.get(row + 1).get(col) : null;
                final Integer lf = col - 1 >= 0 ? field.get(row).get(col - 1) : null;
                final Integer rt = col + 1 < field.get(row).size() ? field.get(row).get(col + 1) : null;

                boolean isDecreasing = top == null || top > val;
                isDecreasing &= bot == null || bot > val;
                isDecreasing &= lf == null || lf > val;
                isDecreasing &= rt == null || rt > val;

                if (isDecreasing) {
                    risk += val + 1;
                }
            }
        }

        return risk;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day9.txt"));
        return scanner;
    }

}
