package com.aoc.y2022;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ProblemDay8a implements ProblemDay<Integer> {

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

        List<List<Pair<Integer, Integer>>> hMaxes = new ArrayList<>();
        for (int row = 0; row < grid.size(); row++) {
            List<Pair<Integer, Integer>> rowMaxes = new ArrayList<>();
            List<Integer> left = new ArrayList<>();
            List<Integer> right = new ArrayList<>(grid.get(row));
            for (int col = 0; col < grid.get(row).size(); col++) {
                right.remove(grid.get(row).get(col));
                int leftMax = left.isEmpty() ? -1 : Collections.max(left);
                int rightMax = right.isEmpty() ? -1 : Collections.max(right);
                rowMaxes.add(ImmutablePair.of(leftMax, rightMax));
                left.add(grid.get(row).get(col));
            }
            hMaxes.add(rowMaxes);
        }
        List<List<Pair<Integer, Integer>>> vMaxes = new ArrayList<>();
        for (int col = 0; col < grid.get(0).size(); col++) {
            List<Integer> top = new ArrayList<>();
            List<Integer> bottom = new ArrayList<>();
            for (int row = 0; row < grid.size(); row++) {
                bottom.add(grid.get(row).get(col));
            }
            for (int row = 0; row < grid.size(); row++) {
                if (col == 0) {
                    vMaxes.add(new ArrayList<>());
                }
                Collections.sort(bottom);
                bottom.remove(grid.get(row).get(col));
                int topMax = top.isEmpty() ? -1 : Collections.max(top);
                int bottomMax = bottom.isEmpty() ? -1 : Collections.max(bottom);
                vMaxes.get(row).add(ImmutablePair.of(topMax, bottomMax));
                top.add(grid.get(row).get(col));
                Collections.sort(top);
            }
        }

        int count = 0;
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.get(row).size(); col++) {
                Pair<Integer, Integer> hMax = hMaxes.get(row).get(col);
                Pair<Integer, Integer> vMax = vMaxes.get(row).get(col);
                int minHeight = Math.min(
                        Math.min(hMax.getLeft(), hMax.getRight()),
                        Math.min(vMax.getLeft(), vMax.getRight()));
                if (grid.get(row).get(col) > minHeight) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day8.txt"));
        return scanner;
    }

}
