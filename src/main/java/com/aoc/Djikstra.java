package com.aoc;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

public class Djikstra {

    public static <T> List<Graph.GraphNode<T>> shortestPaths(Graph<T> graph,
                                                             Graph.GraphNode<T> origin,
                                                             Graph.GraphNode<T> destination,
                                                             Function<Graph.GraphEdge<T>, Integer> weightFunction) {
        Map<Graph.GraphNode<T>, Integer> distances = new HashMap<>();
        Queue<Pair<Graph.GraphNode<T>, Integer>> priority = new PriorityQueue<>(Map.Entry.comparingByValue());
        Map<Graph.GraphNode<T>, Graph.GraphNode<T>> nodeToParent = new HashMap<>();
        distances.put(origin, 0);
        Graph.GraphNode<T> current = origin;
        while (current != null) {
            Set<Graph.GraphEdge<T>> edges = graph.getConnections(current);
            int weightToCurrent = distances.getOrDefault(current, 0);
            for (Graph.GraphEdge<T> edge : edges) {
                int currWeight = weightToCurrent + weightFunction.apply(edge);
                if (distances.containsKey(edge.getConnection())) {
                    int currentSmallestWeight = distances.get(edge.getConnection());
                    if (currWeight < currentSmallestWeight) {
                        priority.remove(ImmutablePair.of(edge.getConnection(), distances.get(edge.getConnection())));
                        distances.put(edge.getConnection(), currWeight);
                        priority.add(ImmutablePair.of(edge.getConnection(), currWeight));
                        nodeToParent.put(edge.getConnection(), current);
                    }
                } else {
                    distances.put(edge.getConnection(), currWeight);
                    priority.add(ImmutablePair.of(edge.getConnection(), currWeight));
                    nodeToParent.put(edge.getConnection(), current);
                }
            }
            current = !priority.isEmpty() ? priority.poll().getLeft() : null;
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
