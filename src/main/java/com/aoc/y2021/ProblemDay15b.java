package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay15b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        // Recursion relation:
        // risk[y][x] = min(risk[y-1][x], risk[y][x-1], risk[y+1][x], risk[y][x+1]);

        final List<List<Integer>> field = new ArrayList<>();
        while (scanner.hasNextLine()) {
            field.add(scanner.nextLine().chars().map(i -> i - 48).boxed().collect(Collectors.toList()));
        }

        final List<List<Integer>> expanded = new ArrayList<>();
        for (final List<Integer> row : field) {
            final List<Integer> expandedRow = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                expandedRow.addAll(row.stream()
                        .map(risk -> risk + finalI > 9 ? risk + finalI - 9 : risk + finalI)
                        .collect(Collectors.toList()));
            }
            expanded.add(expandedRow);
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < field.size(); j++) {
                final List<Integer> row = expanded.get(j);
                int finalI = i;
                expanded.add(row.stream()
                        .map(risk -> risk + finalI > 9 ? risk + finalI - 9 : risk + finalI)
                        .collect(Collectors.toList()));
            }
        }
        expanded.get(0).set(0, 0);

        final List<List<Integer>> totalRisk = expanded.stream()
                .map(row -> row.stream().map(i -> Integer.MAX_VALUE).collect(Collectors.toList()))
                .collect(Collectors.toList());
        final Queue<Node> queue = new PriorityQueue<>();
        final Set<Pair<Integer, Integer>> unvisited = new HashSet<>();
        IntStream.range(0, expanded.size())
                .forEach(y -> IntStream.range(0, expanded.get(y).size())
                        .forEach(x -> unvisited.add(ImmutablePair.of(x, y))));
        queue.add(new Node(ImmutablePair.of(0, 0), 0));
        int minRisk = Integer.MAX_VALUE;
        while (!unvisited.isEmpty()) {
            final Node current = queue.poll();
            if (current == null) {
                continue;
            }
            final Pair<Integer, Integer> pos = current.getPos();
            if (!unvisited.contains(current.getPos())) {
                continue;
            }
            unvisited.remove(pos);

            final int x = pos.getKey();
            final int y = pos.getValue();
            final int r = current.getRisk();

            final Set<Pair<Integer, Integer>> neighbours = new HashSet<Pair<Integer, Integer>>()
            {{
                add(ImmutablePair.of(x, y-1));
                add(ImmutablePair.of(x, y+1));
                add(ImmutablePair.of(x-1, y));
                add(ImmutablePair.of(x+1, y));
            }};

            for (final Pair<Integer, Integer> neighbour : neighbours) {
                final int nX = neighbour.getKey();
                final int nY = neighbour.getValue();

                if (nX >= 0 && nY >= 0 && nY < expanded.size() && nX < expanded.get(y).size()) {
                    final int add = r + expanded.get(nY).get(nX);
                    if (add < totalRisk.get(nY).get(nX)) {
                        totalRisk.get(nY).set(nX, add);
                    }
                    if (unvisited.contains(neighbour)) {
                        queue.add(new Node(neighbour, add));
                    }
                }
            }

            if (y == field.size() - 1 && x == field.get(y).size() - 1 && r < minRisk) {
                minRisk = r;
            }
        }

        return totalRisk.get(totalRisk.size() - 1).get(totalRisk.get(0).size() - 1);
    }

    private static class Node implements Comparable<Node> {

        private final Pair<Integer, Integer> pos;

        private final int risk;

        public Node(final Pair<Integer, Integer> pos, final int risk) {
            this.pos = pos;
            this.risk = risk;
        }

        public Pair<Integer, Integer> getPos() {
            return pos;
        }

        public int getRisk() {
            return risk;
        }

        @Override
        public int compareTo(final Node o) {
            return Integer.compare(this.risk, o.getRisk());
        }
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day15.txt"));
        return scanner;
    }
}
