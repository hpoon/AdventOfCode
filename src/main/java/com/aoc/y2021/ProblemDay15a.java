package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProblemDay15a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        // Recursion relation:
        // risk[y][x] = min(risk[y-1][x], risk[y][x-1]);

        final List<List<Integer>> field = new ArrayList<>();
        while (scanner.hasNextLine()) {
            field.add(scanner.nextLine().chars().map(i -> i - 48).boxed().collect(Collectors.toList()));
        }

        final List<List<Integer>> totalRisk = field.stream()
                .map(row -> row.stream().map(i -> Integer.MAX_VALUE).collect(Collectors.toList()))
                .collect(Collectors.toList());
        totalRisk.get(0).set(0, 0);
        final Queue<Pair<Integer, Integer>> bfs = new LinkedList<>();
        final Pair<Integer, Integer> start = ImmutablePair.of(0, 0);
        bfs.add(start);
        while (!bfs.isEmpty()) {
            final Pair<Integer, Integer> p = bfs.poll();
            final int x = p.getKey();
            final int y = p.getValue();
            final int r = field.get(y).get(x);

            if (x > 0 || y > 0) {
                if (totalRisk.get(y).get(x) < Integer.MAX_VALUE) {
                    continue;
                }
                totalRisk.get(y).set(x, Math.min(
                        y - 1 >= 0 ? totalRisk.get(y - 1).get(x) + r : Integer.MAX_VALUE,
                        x - 1 >= 0 ? totalRisk.get(y).get(x - 1) + r : Integer.MAX_VALUE));
            }
            if (y + 1 < field.size()) {
                bfs.add(ImmutablePair.of(x, y + 1));
            }
            if (x + 1 < field.get(y).size()) {
                bfs.add(ImmutablePair.of(x + 1, y));
            }
        }

        return totalRisk.get(totalRisk.size() - 1).get(totalRisk.get(0).size() - 1);
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day15.txt"));
        return scanner;
    }
}
