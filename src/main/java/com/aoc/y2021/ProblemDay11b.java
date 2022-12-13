package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;

public class ProblemDay11b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final List<List<Integer>> field = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            field.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                field.get(field.size() - 1).add(Integer.parseInt(line.substring(i, i+1)));
            }
        }

        int flashes = 0;
        for (int i = 0; i < 500; i++) {
            final Set<Pair<Integer, Integer>> flashed = new HashSet<>();
            for (int row = 0; row < field.size(); row++) {
                for (int col = 0; col < field.get(row).size(); col++) {
                    final int value = field.get(row).get(col);
                    if (flashed.contains(ImmutablePair.of(row, col))) {
                        continue;
                    }
                    if (value >= 9) {
                        final Queue<Pair<Integer, Integer>> bfs = new LinkedList<>();
                        bfs.add(ImmutablePair.of(row, col));
                        while (!bfs.isEmpty()) {
                            final Pair<Integer, Integer> p = bfs.poll();
                            final int r = p.getKey();
                            final int c = p.getValue();
                            final int v = field.get(r).get(c);
                            if (flashed.contains(ImmutablePair.of(r, c))) {
                                continue;
                            }
                            if (v >= 9) {
                                flashed.add(p);
                                addIfPossible(r - 1, c - 1, field, flashed, bfs);
                                addIfPossible(r - 1, c, field, flashed, bfs);
                                addIfPossible(r - 1, c + 1, field, flashed, bfs);
                                addIfPossible(r, c - 1, field, flashed, bfs);
                                addIfPossible(r, c + 1, field, flashed, bfs);
                                addIfPossible(r + 1, c - 1, field, flashed, bfs);
                                addIfPossible(r + 1, c, field, flashed, bfs);
                                addIfPossible(r + 1, c + 1, field, flashed, bfs);
                            } else {
                                field.get(r).set(c, v + 1);
                                if (v + 1 >= 9) {
                                    addIfPossible(r, c, field, flashed, bfs);
                                }
                            }
                        }
                    }
                }
            }

            for (int row = 0; row < field.size(); row++) {
                for (int col = 0; col < field.get(row).size(); col++) {
                    final int value = field.get(row).get(col);
                    field.get(row).set(col, value + 1);
                }
            }

            for (final Pair<Integer, Integer> p : flashed) {
                field.get(p.getKey()).set(p.getValue(), 0);
            }

            flashes += flashed.size();

            if (flashed.size() == field.size() * field.get(0).size()) {
                return i + 1;
            }
        }

        return 0;
    }

    private void addIfPossible(final int row,
                               final int col,
                               final List<List<Integer>> field,
                               final Set<Pair<Integer, Integer>> flashed,
                               final Queue<Pair<Integer, Integer>> bfs) {
        final Pair<Integer, Integer> pair = ImmutablePair.of(row, col);
        boolean shouldAdd = row >= 0 && row < field.size();
        shouldAdd = shouldAdd && col >= 0 && col < field.get(row).size();
        shouldAdd = shouldAdd && !flashed.contains(pair);
        if (shouldAdd) {
            bfs.add(pair);
        }
    }

}
