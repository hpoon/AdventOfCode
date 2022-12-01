package com.aoc.y2021;

import com.aoc.ProblemDay;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class ProblemDay9b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<List<Integer>> field = new ArrayList<>();
        final List<List<Boolean>> visited = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            field.add(new ArrayList<>());
            visited.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                field.get(field.size() - 1).add(Integer.parseInt(line.substring(i, i+1)));
                visited.get(visited.size() - 1).add(false);
            }
        }

        final PriorityQueue<Integer> basins = new PriorityQueue<>(Comparator.reverseOrder());
        for (int row = 0; row < field.size(); row++) {
            for (int col = 0; col < field.get(row).size(); col++) {
                if (visited.get(row).get(col)) {
                    continue;
                }

                final int val = field.get(row).get(col);

                if (val != 9) {
                    final Queue<Pair<Integer, Integer>> bfs = new LinkedList<>();
                    bfs.add(new Pair<>(row, col));
                    int size = 0;
                    while (!bfs.isEmpty()) {
                        final Pair<Integer, Integer> p = bfs.poll();
                        final int r = p.getKey();
                        final int c = p.getValue();

                        if (visited.get(r).get(c)) {
                            continue;
                        }

                        final int v = field.get(r).get(c);
                        if (v != 9) {
                            if (r - 1 >= 0 && !visited.get(r - 1).get(c)) {
                                bfs.add(new Pair<>(r - 1, c));
                            }
                            if (r + 1 < field.size() && !visited.get(r + 1).get(c)) {
                                bfs.add(new Pair<>(r + 1, c));
                            }
                            if (c - 1 >= 0 && !visited.get(r).get(c - 1)) {
                                bfs.add(new Pair<>(r, c - 1));
                            }
                            if (c + 1 < field.get(r).size() && !visited.get(r).get(c + 1)) {
                                bfs.add(new Pair<>(r, c + 1));
                            }
                            size ++;
                        }

                        visited.get(r).set(c, true);
                    }
                    basins.add(size);
                }

                visited.get(row).set(col, true);
            }
        }

        return basins.poll() * basins.poll() * basins.poll();
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day9.txt"));
        return scanner;
    }

}
