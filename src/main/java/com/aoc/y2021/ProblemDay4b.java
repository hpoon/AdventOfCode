package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay4b extends ProblemDay<Integer> {

    @Override
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

    private static class BingoBoard {

        private List<List<Pair<Integer, Boolean>>> board = new ArrayList<>();

        public BingoBoard(final Scanner scanner) {
            scanner.nextLine();
            for (int i = 0; i < 5; i++) {
                final String[] array = scanner.nextLine().split(" ");
                final List<Pair<Integer, Boolean>> row = Arrays.stream(array)
                        .filter(str -> !str.isEmpty())
                        .map(Integer::parseInt).map(num -> ImmutablePair.of(num, false))
                        .collect(Collectors.toList());
                board.add(row);
            }
        }

        private void markPosition(final int number) {
            for (int row = 0; row < board.size(); row++) {
                for (int col = 0; col < board.get(row).size(); col++) {
                    final Pair<Integer, Boolean> pair = board.get(row).get(col);
                    if (pair.getKey() == number) {
                        board.get(row).set(col, ImmutablePair.of(pair.getKey(), true));
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
