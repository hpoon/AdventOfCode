package com.aoc.y2021;

import com.aoc.ProblemDay;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay4b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<Integer> numbers = Arrays.stream(scanner.nextLine().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final List<BingoBoard> boards = new ArrayList<>();
        while (scanner.hasNextLine()) {
            boards.add(new BingoBoard(scanner));
        }

        BingoBoard lastWin = null;
        int lastNum = -1;
        for (int number : numbers) {
            final Set<BingoBoard> toRemove = new HashSet<>();
            for (final BingoBoard board : boards) {
                board.markPosition(number);
                if (board.hasWon()) {
                    toRemove.add(board);
                    lastWin = board;
                    lastNum = number;
                }
            }

            if (!toRemove.isEmpty()) {
                toRemove.forEach(boards::remove);
            }
        }

        return lastWin.sumUnmarkedNumbers() * lastNum;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day4.txt"));
        return scanner;
    }

    private static class BingoBoard {

        private List<List<Pair<Integer, Boolean>>> board = new ArrayList<>();

        public BingoBoard(final Scanner scanner) {
            scanner.nextLine();
            for (int i = 0; i < 5; i++) {
                final String[] array = scanner.nextLine().split(" ");
                final List<Pair<Integer, Boolean>> row = Arrays.stream(array)
                        .filter(str -> !str.isEmpty())
                        .map(Integer::parseInt).map(num -> new Pair<>(num, false))
                        .collect(Collectors.toList());
                board.add(row);
            }
        }

        private void markPosition(final int number) {
            for (int row = 0; row < board.size(); row++) {
                for (int col = 0; col < board.get(row).size(); col++) {
                    final Pair<Integer, Boolean> pair = board.get(row).get(col);
                    if (pair.getKey() == number) {
                        board.get(row).set(col, new Pair<>(pair.getKey(), true));
                    }
                }
            }
        }

        private boolean hasWon() {
            for (int row = 0; row < board.size(); row++) {
                int completed = 0;
                for (int col = 0; col < board.get(row).size(); col++) {
                    final Pair<Integer, Boolean> pair = board.get(row).get(col);
                    if (pair.getValue()) {
                        completed++;
                    } else {
                        break;
                    }

                    if (completed == 5) {
                        return true;
                    }
                }
            }

            for (int row = 0; row < board.size(); row++) {
                int completed = 0;
                for (int col = 0; col < board.get(row).size(); col++) {
                    final Pair<Integer, Boolean> pair = board.get(col).get(row);
                    if (pair.getValue()) {
                        completed++;
                    } else {
                        break;
                    }

                    if (completed == 5) {
                        return true;
                    }
                }
            }

            return false;
        }

        private int sumUnmarkedNumbers() {
            int sum = 0;
            for (int row = 0; row < board.size(); row++) {
                int completed = 0;
                for (int col = 0; col < board.get(row).size(); col++) {
                    final Pair<Integer, Boolean> pair = board.get(row).get(col);
                    if (!pair.getValue()) {
                        sum += pair.getKey();
                    }
                }
            }
            return sum;
        }
    }
}
