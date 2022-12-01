
package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay25a implements ProblemDay<Integer> {

    private Scanner scanner;

    @Override
    public Integer solve() {
        final List<List<Character>> map = new ArrayList<>();
        while (scanner.hasNextLine()) {
            map.add(scanner.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        }

        int steps = 0;
        int moves = 0;
        do {
            moves = moveEast(map);
            moves += moveSouth(map);
            steps++;
        } while (moves > 0);

        print(map);

        return steps;
    }

    private void print(final List<List<Character>> map) {
        for (final List<Character> row : map) {
            for (final char c : row) {
                System.out.print(c);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private int moveEast(final List<List<Character>> map) {
        int moves = 0;
        for (int row = 0; row < map.size(); row++) {
            final List<Character> line = map.get(row);
            final List<Character> newLine = new ArrayList<>(line);
            for (int col = line.size() - 1; col >= 0; col--) {
                final char cell = line.get(col);
                final int nextCol = (col >= line.size() - 1) ? 0 : col + 1;
                final boolean hasForwardMovement = line.get(nextCol).equals('.');
                if (cell == '>' && hasForwardMovement) {
                    newLine.set(nextCol, cell);
                    newLine.set(col, '.');
                    moves++;
                }
            }
            map.set(row, newLine);
        }
        return moves;
    }

    private int moveSouth(final List<List<Character>> map) {
        int moves = 0;
        for (int col = 0; col < map.get(0).size(); col++) {
            int finalCol = col;
            final List<Character> line = map.stream().map(row -> row.get(finalCol)).collect(Collectors.toList());
            final List<Character> newLine = new ArrayList<>(line);
            for (int row = line.size() - 1; row >= 0; row--) {
                final char cell = line.get(row);
                final int nextRow = (row >= line.size() - 1) ? 0 : row + 1;
                final boolean hasForwardMovement = line.get(nextRow).equals('.');
                if (cell == 'v' && hasForwardMovement) {
                    newLine.set(nextRow, cell);
                    newLine.set(row, '.');
                    moves++;
                }
            }
            IntStream.range(0, map.size()).forEach(row -> map.get(row).set(finalCol, newLine.get(row)));
        }
        return moves;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day25.txt"));
        return scanner;
    }
}
