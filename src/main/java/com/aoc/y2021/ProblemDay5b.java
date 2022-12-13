package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProblemDay5b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> lines = new ArrayList<>();
        final List<LineType> types = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;
        while (scanner.hasNextLine()) {
            final String[] pointsArray = scanner.nextLine().split(" -> ");
            final String[] p1Str = pointsArray[0].split(",");
            final String[] p2Str = pointsArray[1].split(",");
            final Pair<Integer, Integer> p1 = ImmutablePair.of(Integer.parseInt(p1Str[0]), Integer.parseInt(p1Str[1]));
            final Pair<Integer, Integer> p2 = ImmutablePair.of(Integer.parseInt(p2Str[0]), Integer.parseInt(p2Str[1]));
            final LineType type;
            if (Objects.equals(p1.getKey(), p2.getKey())) {
                type = LineType.VERTICAL;
            } else if (Objects.equals(p1.getValue(), p2.getValue())) {
                type = LineType.HORIZONTAL;
            } else if (isDiagonal(p1, p2)) {
                type = LineType.DIAGONAL;
            } else {
                throw new RuntimeException("bad line");
            }

            lines.add(ImmutablePair.of(p1, p2));
            types.add(type);
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

        final int[][] canvas = new int[maxY + 1][maxX + 1];
        for (int i = 0; i < lines.size(); i++) {
            drawLine(lines.get(i), canvas, types.get(i));
        }

        return countOverlaps(canvas);
    }

    private boolean isDiagonal(final Pair<Integer, Integer> p1, final Pair<Integer, Integer> p2) {
        return Math.abs(p1.getKey() - p2.getKey()) == Math.abs(p1.getValue() - p2.getValue());
    }

    private void drawLine(final Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> line, int[][] canvas, LineType type) {
        final Pair<Integer, Integer> p1 = line.getKey();
        final Pair<Integer, Integer> p2 = line.getValue();

        if (type == LineType.HORIZONTAL) {
            for (int x = Math.min(p1.getKey(), p2.getKey()); x <= Math.max(p1.getKey(), p2.getKey()); x++) {
                canvas[p1.getValue()][x]++;
            }
        } else if (type == LineType.VERTICAL) {
            for (int y = Math.min(p1.getValue(), p2.getValue()); y <= Math.max(p1.getValue(), p2.getValue()); y++) {
                canvas[y][p1.getKey()]++;
            }
        } else {
            int x;
            int endX;
            int y;
            int endY;
            if (p1.getKey() < p2.getKey()) {
                x = p1.getKey();
                y = p1.getValue();
                endX = p2.getKey();
                endY = p2.getValue();
            } else {
                x = p2.getKey();
                y = p2.getValue();
                endX = p1.getKey();
                endY = p1.getValue();
            }

            while (x <= endX) {
                canvas[y][x]++;
                x++;
                y = endY > y ? y + 1 : y - 1;
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

    private enum LineType {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL
    }
}
