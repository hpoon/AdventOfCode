package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProblemDay13b implements ProblemDay<String> {

    private Scanner scanner;

    public String solve() {
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

        while (scanner.hasNextLine()) {
            final String[] fold = scanner.nextLine().split("=");
            final String axis = fold[0].substring(fold[0].length() - 1);
            final int line = Integer.parseInt(fold[1]);

            final String[][] newField;
            if (axis.equals("x")) {
                newField = new String[field.length][line];
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
            } else {
                newField = new String[line][field[0].length];
                for (int y = 0; y < newField.length; y++) {
                    newField[y] = Arrays.copyOfRange(field[y], 0, field[y].length);
                }
                for (int y = line + 1; y < field.length; y++) {
                    for (int x = 0; x < field[y].length; x++) {
                        final String value = field[y][x];
                        if (value.equals("#")) {
                            newField[line - (y - line)][x] = value;
                        }
                    }
                }
            }
            field = newField;
        }

        StringBuilder msg = new StringBuilder();
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                msg.append(field[y][x]);
            }
            msg.append("\n");
        }
        return msg.toString();
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day13.txt"));
        return scanner;
    }

}