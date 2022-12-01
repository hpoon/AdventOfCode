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

public class ProblemDay12a implements ProblemDay<Integer> {

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

        final Pair<String, Set<String>> start = new Pair<>("start", new HashSet<>());
        final Stack<Pair<String, Set<String>>> aStar = new Stack<>();
        aStar.add(start);
        int paths = 0;
        while (!aStar.isEmpty()) {
            final Pair<String, Set<String>> p = aStar.pop();
            final String node = p.getKey();
            final Set<String> cameFrom = p.getValue();
            if (node.equals("end")) {
                paths++;
            } else {
                final Set<String> conns = graph.get(node);
                for (final String conn : conns) {
                    final Set<String> cameFromUpdate = new HashSet<>(cameFrom);
                    if (cameFrom.contains(conn)) {
                        continue;
                    }
                    if (node.toLowerCase().equals(node)) {
                        cameFromUpdate.add(node);
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
