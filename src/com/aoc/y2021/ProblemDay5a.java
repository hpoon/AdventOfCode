package com.aoc.y2021;

import com.aoc.ProblemDay;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProblemDay5a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> lines = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;
        while (scanner.hasNextLine()) {
            final String[] pointsArray = scanner.nextLine().split(" -> ");
            final String[] p1Str = pointsArray[0].split(",");
            final String[] p2Str = pointsArray[1].split(",");
            final Pair<Integer, Integer> p1 = new Pair<>(Integer.parseInt(p1Str[0]), Integer.parseInt(p1Str[1]));
            final Pair<Integer, Integer> p2 = new Pair<>(Integer.parseInt(p2Str[0]), Integer.parseInt(p2Str[1]));
            if (Objects.equals(p1.getKey(), p2.getKey()) || Objects.equals(p1.getValue(), p2.getValue())) {
                lines.add(new Pair<>(p1, p2));
                if (p1.getKey() > maxX) {
                    maxX = p1.getKey();
                }
                if (p1.getValue() > maxY) {
                    maxY = p1.getValue();
                }
                if (p2.getKey() > maxX) {
                    maxX = p2.getKey();
                }
                if (p2.getValue() > maxY) {
                    maxY = p2.getValue();
                }
            }
        }

        final int canvas[][] = new int[maxY + 1][maxX + 1];
        for (final Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> line : lines) {
            drawLine(line, canvas);
        }

        return countOverlaps(canvas);
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day5.txt"));
        return scanner;
    }

    private void drawLine(final Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> line, int[][] canvas) {
        final Pair<Integer, Integer> p1 = line.getKey();
        final Pair<Integer, Integer> p2 = line.getValue();
        final boolean isHorizontal = !p1.getKey().equals(p2.getKey());

        if (isHorizontal) {
            for (int x = Math.min(p1.getKey(), p2.getKey()); x <= Math.max(p1.getKey(), p2.getKey()); x++) {
                canvas[p1.getValue()][x]++;
            }
        } else {
            for (int y = Math.min(p1.getValue(), p2.getValue()); y <= Math.max(p1.getValue(), p2.getValue()); y++) {
                canvas[y][p1.getKey()]++;
            }
        }
    }

    private int countOverlaps(int[][] canvas) {
        int overlaps = 0;
        for (int y = 0; y < canvas.length; y++) {
            for (int x = 0; x < canvas[y].length; x++) {
                if (canvas[y][x] > 1) {
                    overlaps++;
                }
            }
        }
        return overlaps;
    }
}
