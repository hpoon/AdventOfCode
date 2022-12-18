package com.aoc;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class FloydWarshall {

    public static <T> Map<Pair<Graph.GraphNode<T>, Graph.GraphNode<T>>, Integer> shortestPaths(Graph<T> graph) {
        Map<Pair<Graph.GraphNode<T>, Graph.GraphNode<T>>, Integer> nodePairsToDistance = new HashMap<>();
        for (Graph.GraphNode<T> node : graph.getNodes()) {
            Graph.GraphNode<T> t1 = node;
            nodePairsToDistance.put(ImmutablePair.of(t1, t1), 0);
            for (Graph.GraphEdge<T> edge : graph.getConnections(node)) {
                Graph.GraphNode<T> t2 = edge.getConnection();
                nodePairsToDistance.put(ImmutablePair.of(t1, t2), edge.getWeight());
                nodePairsToDistance.put(ImmutablePair.of(t2, t2), 0);
            }
        }

        for (Graph.GraphNode<T> n1 : graph.getNodes()) {
            for (Graph.GraphNode<T> n2 : graph.getNodes()) {
                for (Graph.GraphNode<T> n3 : graph.getNodes()) {
                    int n2n3 = nodePairsToDistance.getOrDefault(ImmutablePair.of(n2, n3), Integer.MAX_VALUE / 2);
                    int n2n1 = nodePairsToDistance.getOrDefault(ImmutablePair.of(n2, n1), Integer.MAX_VALUE / 2);
                    int n1n3 = nodePairsToDistance.getOrDefault(ImmutablePair.of(n1, n3), Integer.MAX_VALUE / 2);
                    if (n2n1 + n1n3 < n2n3) {
                        nodePairsToDistance.put(ImmutablePair.of(n2, n3), n2n1 + n1n3);
                    }
                }
            }
        }
        return nodePairsToDistance;
    }

}
