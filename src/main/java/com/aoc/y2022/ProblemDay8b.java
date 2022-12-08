package com.aoc.y2022;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProblemDay8b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        List<List<Integer>> grid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Integer> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                row.add(Character.getNumericValue(line.charAt(i)));
            }
            grid.add(row);
        }

        List<List<Pair<Integer, Integer>>> hCounts = new ArrayList<>();
        for (int row = 0; row < grid.size(); row++) {
            List<Pair<Integer, Integer>> rowCounts = new ArrayList<>();
            List<Integer> left = new ArrayList<>();
            List<Integer> right = new ArrayList<>(grid.get(row));
            for (int col = 0; col < grid.get(row).size(); col++) {
                int height = grid.get(row).get(col);
                right.remove(0);
                int leftCount = 0;
                for (int i = left.size() - 1; i >= 0; i--) {
                    leftCount++;
                    if (height <= left.get(i)) {
                        break;
                    }
                }
                int rightCount = 0;
                for (int i = 0; i < right.size(); i++) {
                    rightCount++;
                    if (height <= right.get(i)) {
                        break;
                    }
                }
                rowCounts.add(ImmutablePair.of(leftCount, rightCount));
                left.add(grid.get(row).get(col));
            }
            hCounts.add(rowCounts);
        }
        List<List<Pair<Integer, Integer>>> vCounts = new ArrayList<>();
        for (int col = 0; col < grid.get(0).size(); col++) {
            List<Integer> top = new ArrayList<>();
            List<Integer> bottom = new ArrayList<>();
            for (int row = 0; row < grid.size(); row++) {
                bottom.add(grid.get(row).get(col));
            }
            for (int row = 0; row < grid.size(); row++) {
                if (col == 0) {
                    vCounts.add(new ArrayList<>());
                }
                int height = grid.get(row).get(col);
                bottom.remove(0);
                int topCount = 0;
                for (int i = top.size() - 1; i >= 0; i--) {
                    topCount++;
                    if (height <= top.get(i)) {
                        break;
                    }
                }
                int bottomCount = 0;
                for (int i = 0; i < bottom.size(); i++) {
                    bottomCount++;
                    if (height <= bottom.get(i)) {
                        break;
                    }
                }
                vCounts.get(row).add(ImmutablePair.of(topCount, bottomCount));
                top.add(grid.get(row).get(col));
            }
        }

        int max = 0;
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.get(row).size(); col++) {
                Pair<Integer, Integer> hCount = hCounts.get(row).get(col);
                Pair<Integer, Integer> vCount = vCounts.get(row).get(col);
                int score = hCount.getLeft() * hCount.getRight() * vCount.getLeft() * vCount.getRight();
                if (score > max) {
                    max = score;
                }
            }
        }
        return max;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day8.txt"));
        return scanner;
    }

}
