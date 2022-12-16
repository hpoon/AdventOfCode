package com.aoc.y2021;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay13a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        int maxX = 0;
        int maxY = 0;
        final List<Pair<Integer, Integer>> points = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            final List<Integer> point = Arrays.stream(line.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            final int x = point.get(0);
            final int y = point.get(1);
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
            points.add(ImmutablePair.of(x, y));
        }

        String field[][] = new String[maxY+1][maxX+1];
        for (String[] strings : field) {
            Arrays.fill(strings, ".");
        }
        for (final Pair<Integer, Integer> p : points) {
            field[p.getValue()][p.getKey()] = "#";
        }

        final String[] fold = scanner.nextLine().split("=");
        final String axis = fold[0].substring(fold[0].length() - 1);
        final int line = Integer.parseInt(fold[1]);

        if (axis.equals("x")) {
            final String[][] newField = new String[field.length][line];
            for (int y = 0; y < newField.length; y++) {
                newField[y] = Arrays.copyOfRange(field[y], 0, line);
            }
            for (int y = 0; y < field.length; y++) {
                for (int x = line + 1; x < field[y].length; x++) {
                    final String value = field[y][x];
                    if (value.equals("#")) {
                        newField[y][line - (x - line)] = value;
                    }
                }
            }

            field = newField;
        }

        int visible = 0;
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[y][x].equals("#")) {
                    visible++;
                }
            }
        }
        return visible;
    }

}
