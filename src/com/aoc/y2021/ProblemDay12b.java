
package com.aoc.y2021;

import com.aoc.ProblemDay;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class ProblemDay12b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final Map<String, Set<String>> graph = new HashMap<>();
        while (scanner.hasNextLine()) {
            final String[] line = scanner.nextLine().split("-");
            final String n1 = line[0];
            final String n2 = line[1];

            final Set<String> n1Connections = graph.getOrDefault(n1, new HashSet<>());
            n1Connections.add(n2);

            final Set<String> n2Connections = graph.getOrDefault(n2, new HashSet<>());
            n2Connections.add(n1);

            graph.put(n1, n1Connections);
            graph.put(n2, n2Connections);
        }

        final Pair<String, Map<String, Integer>> start = new Pair<>("start", new HashMap<>());
        final Stack<Pair<String, Map<String, Integer>>> aStar = new Stack<>();
        aStar.add(start);
        int paths = 0;
        while (!aStar.isEmpty()) {
            final Pair<String, Map<String, Integer>> p = aStar.pop();
            final String node = p.getKey();
            final Map<String, Integer> cameFrom = p.getValue();
            if (node.equals("end")) {
                paths++;
            } else {
                final Set<String> conns = graph.get(node);
                for (final String conn : conns) {
                    final Map<String, Integer> cameFromUpdate = new HashMap<>(cameFrom);
                    if (conn.equals("start")) {
                        continue;
                    }
                    if (cameFrom.containsKey(node) && cameFrom.values().stream().anyMatch(v -> v >= 2)) {
                        continue;
                    }
                    if (node.toLowerCase().equals(node)) {
                        cameFromUpdate.put(node, cameFromUpdate.getOrDefault(node, 0) + 1);
                    }
                    aStar.add(new Pair<>(conn, cameFromUpdate));
                }
            }
        }
        return paths;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day12.txt"));
        return scanner;
    }

}
