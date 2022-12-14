package com.aoc.y2021;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class ProblemDay12a extends ProblemDay<Integer> {

    @Override
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

        final Pair<String, Set<String>> start = ImmutablePair.of("start", new HashSet<>());
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
                    aStar.add(ImmutablePair.of(conn, cameFromUpdate));
                }
            }
        }
        return paths;
    }

}
