package com.aoc.y2024;

import com.aoc.ProblemDay;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay5 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Graph<Integer, DefaultWeightedEdge> dependencyGraph = parseDependencies();
        List<List<Integer>> updates = parseUpdates();
        int sum = 0;
        for (List<Integer> update : updates) {
            if (isValidA(update, dependencyGraph)) {
                sum += update.get(update.size() / 2);
            }
        }
        return sum;
    }

    @Override
    public Integer solveB() {
        Graph<Integer, DefaultWeightedEdge> dependencyGraph = parseDependencies();
        List<List<Integer>> updates = parseUpdates();
        int sum = 0;
        for (List<Integer> update : updates) {
            if (isValidA(update, dependencyGraph)) {
                continue;
            }
            sum += correctB(update, dependencyGraph);
        }
        return sum;
    }

    private boolean isValidA(List<Integer> update, Graph<Integer, DefaultWeightedEdge> dependencyGraph) {
        for (int i = 1; i < update.size(); i++) {
            boolean found = dependencyGraph.containsEdge(update.get(i-1), update.get(i));
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private int correctB(List<Integer> update, Graph<Integer, DefaultWeightedEdge> dependencyGraph) {
        for (int u : update) {
            List<Integer> corrected = new ArrayList<>();
            Set<Integer> visited = new HashSet<>();
            correctB(u, corrected, visited, update, dependencyGraph);
            if (corrected.size() == update.size()) {
                return corrected.get(corrected.size() / 2);
            }
        }
        throw new RuntimeException("Shouldn't happen");
    }

    private boolean correctB(int node,
                             List<Integer> corrected,
                             Set<Integer> visited,
                             List<Integer> update,
                             Graph<Integer, DefaultWeightedEdge> dependencyGraph) {
        corrected.add(node);
        visited.add(node);
        if (corrected.size() == update.size()) {
            return true;
        }
        for (int u : update) {
            if (visited.contains(u)) {
                continue;
            }
            if (!dependencyGraph.containsEdge(node, u)) {
                continue;
            }
            if (correctB(u, corrected, visited, update, dependencyGraph)) {
                return true;
            };
            corrected.remove(corrected.size() - 1);
            visited.remove(u);
        }
        return false;
    }

    private Graph<Integer, DefaultWeightedEdge> parseDependencies() {
        Graph<Integer, DefaultWeightedEdge> graph = new SimpleDirectedGraph<>(DefaultWeightedEdge.class);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            String[] tokens = line.split("\\|");
            int node = Integer.parseInt(tokens[0]);
            int child = Integer.parseInt(tokens[1]);
            graph.addVertex(node);
            graph.addVertex(child);
            graph.addEdge(node, child);
        }
        return graph;
    }

    private List<List<Integer>> parseUpdates() {
        List<List<Integer>> updates = new ArrayList<>();
        while (scanner.hasNextLine()) {
            updates.add(Arrays.stream(scanner.nextLine().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
        }
        return updates;
    }

}
