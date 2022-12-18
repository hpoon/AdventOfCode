package com.aoc;

import com.google.common.collect.ImmutableSet;
import lombok.Value;

import java.util.*;

public class Graph<T> {

    private final Map<GraphNode<T>, Set<GraphEdge<T>>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public Set<GraphNode<T>> getNodes() {
        return adjacencyList.keySet();
    }

    public int size() {
        return getNodes().size();
    }

    public void addConnection(GraphNode<T> origin, GraphNode<T> destination, int weight) {
        Set<GraphEdge<T>> originConnections = adjacencyList.getOrDefault(origin, new HashSet<>());
        originConnections.add(new GraphEdge<>(destination, weight));
        adjacencyList.put(origin, originConnections);
    }

    public Set<GraphEdge<T>> getConnections(GraphNode<T> origin) {
        return adjacencyList.getOrDefault(origin, ImmutableSet.of());
    }

    @Value
    public static class GraphNode<T> {
        private final T value;

        public GraphNode(T value) {
            this.value = value;
        }
    }

    @Value
    public static class GraphEdge<T> {
        private final GraphNode<T> connection;
        private final int weight;
    }
}
