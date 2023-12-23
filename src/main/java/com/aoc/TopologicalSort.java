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

    public static <T> Map<MatrixElement<T>, Integer> nodeToLongestPaths(
            Matrix<T> matrix,
            MatrixElement<T> origin,
            Function<MatrixElement<T>, Boolean> validElementFunction,
            Function<MatrixElement<T>, List<MatrixElement<T>>> eligibleStepsFunction) {
        Stack<MatrixElement<T>> stack = new Stack<>();
        Set<MatrixElement<T>> visited = new HashSet<>();

        for (int row = 0; row < matrix.height(); row++) {
            for (int col = 0; col < matrix.width(row); col++) {
                MatrixElement<T> element = matrix.getElement(row, col);
                if (!validElementFunction.apply(element)) {
                    continue;
                }
                if (!visited.contains(element)) {
                    topologicalSort(element, visited, stack, eligibleStepsFunction);
                }
            }
        }

        Map<MatrixElement<T>, Integer> distances = new HashMap<>();
        distances.put(origin, 0);

        while (!stack.isEmpty()) {
            MatrixElement<T> element = stack.pop();
            if (distances.containsKey(element)) {
                final List<MatrixElement<T>> steps = eligibleStepsFunction.apply(element);
                for (MatrixElement<T> step : steps) {
                    int newDist = distances.get(element) + 1;
                    if (distances.getOrDefault(step, Integer.MIN_VALUE) < newDist) {
                        distances.put(step, newDist);
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

    private static <T> void topologicalSort(MatrixElement<T> node,
                                            Set<MatrixElement<T>> visited,
                                            Stack<MatrixElement<T>> stack,
                                            Function<MatrixElement<T>, List<MatrixElement<T>>> eligibleStepsFunction) {
        visited.add(node);

        // Recurse for all the eligible next steps
        List<MatrixElement<T>> eligibleSteps = eligibleStepsFunction.apply(node);
        for (MatrixElement<T> steps : eligibleSteps) {
            if (!visited.contains(steps)) {
                topologicalSort(steps, visited, stack, eligibleStepsFunction);
            }
        }

        stack.push(node);
    }

}
