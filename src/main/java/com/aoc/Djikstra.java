package com.aoc;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Djikstra {

    public static <T> List<Graph.GraphNode<T>> shortestPaths(Graph<T> graph,
                                                             Graph.GraphNode<T> origin,
                                                             Graph.GraphNode<T> destination,
                                                             Function<Graph.GraphEdge<T>, Integer> weightFunction) {
        Map<Graph.GraphNode<T>, Integer> distances = new HashMap<>();
        Map<Graph.GraphNode<T>, Graph.GraphNode<T>> nodeToParent = new HashMap<>();
        distances.put(origin, 0);
        Graph.GraphNode<T> current = origin;
        Set<Graph.GraphNode<T>> visited = new HashSet<>();
        while (current != null) {
            visited.add(current);
            Set<Graph.GraphEdge<T>> edges = graph.getConnections(current);
            int weightToCurrent = distances.getOrDefault(current, 0);
            for (Graph.GraphEdge<T> edge : edges) {
                int currWeight = weightToCurrent + weightFunction.apply(edge);
                if (distances.containsKey(edge.getConnection())) {
                    int currentSmallestWeight = distances.get(edge.getConnection());
                    if (currWeight < currentSmallestWeight) {
                        distances.put(edge.getConnection(), currWeight);
                        nodeToParent.put(edge.getConnection(), current);
                    }
                } else {
                    distances.put(edge.getConnection(), currWeight);
                    nodeToParent.put(edge.getConnection(), current);
                }
            }
            Set<Graph.GraphNode<T>> connections = edges.stream()
                    .map(Graph.GraphEdge::getConnection)
                    .collect(Collectors.toSet());
            current = distances.entrySet().stream()
                    .filter(e -> connections.contains(e.getKey()))
                    .filter(e -> !visited.contains(e.getKey()))
                    .min(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

        List<Graph.GraphNode<T>> path = new ArrayList<>();
        current = destination;
        while (current != null) {
            path.add(current);
            current = nodeToParent.get(current);
        }
        Collections.reverse(path);
        return path.get(0).equals(origin) ? path : ImmutableList.of();
    }

}
