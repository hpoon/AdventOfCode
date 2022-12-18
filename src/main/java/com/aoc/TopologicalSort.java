package com.aoc;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TopologicalSort {

    public static <T> Map<Graph.GraphNode<T>, Integer> nodeToLongestPaths(
            Graph<T> graph,
            Graph.GraphNode<T> origin,
            Function<Graph.GraphEdge<T>, Integer> weightFunction) {
        Stack<Graph.GraphNode<T>> stack = new Stack<>();
        Set<Graph.GraphNode<T>> visited = new HashSet<>();

        for (Graph.GraphNode<T> node : graph.getNodes()) {
            if (!visited.contains(node)) {
                topologicalSort(node, visited, stack, graph);
            }
        }

        Map<Graph.GraphNode<T>, Integer> distances = new HashMap<>();
        distances.put(origin, 0);

        while (!stack.isEmpty()) {
            Graph.GraphNode<T> node = stack.pop();
            if (distances.containsKey(node)) {
                final Set<Graph.GraphEdge<T>> edges = graph.getConnections(node);
                for (Graph.GraphEdge<T> edge : edges) {
                    int newDist = distances.get(node) + weightFunction.apply(edge);
                    if (distances.getOrDefault(edge.getConnection(), Integer.MIN_VALUE) < newDist) {
                        distances.put(edge.getConnection(), newDist);
                    }
                }
            }
        }
        return distances;
    }

    private static <T> void topologicalSort(Graph.GraphNode<T> node,
                                            Set<Graph.GraphNode<T>> visited,
                                            Stack<Graph.GraphNode<T>> stack,
                                            Graph<T> graph) {
        visited.add(node);

        // Recurse for all the vertices adjacent to this vertex
        final Set<Graph.GraphNode<T>> connections = graph.getConnections(node).stream()
                .map(Graph.GraphEdge::getConnection)
                .collect(Collectors.toSet());
        for (Graph.GraphNode<T> connection : connections) {
            if (!visited.contains(connection)) {
                topologicalSort(connection, visited, stack, graph);
            }
        }

        stack.push(node);
    }

}
