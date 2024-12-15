package com.aoc;

import com.google.common.collect.ImmutableSet;
import lombok.Value;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Originally made as a way to practice graph related problems, but as more years of AOC passed, its problems has
 * outscaled the ability of this class, and it was much easier to just use something like JGraphT to do things.
 *
 * @param <T>
 */
@Deprecated()
public class Graph<T> {

    private final Map<GraphNode<T>, Set<GraphEdge<T>>> adjacencyList;
    private final MultiKeyMap<T, Integer> adjacencyMatrix;

    public Graph() {
        adjacencyList = new HashMap<>();
        adjacencyMatrix = new MultiKeyMap<>();
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
        adjacencyMatrix.put(origin.getValue(), destination.getValue(), weight);
    }

    public Set<GraphEdge<T>> getConnections(GraphNode<T> origin) {
        return adjacencyList.getOrDefault(origin, ImmutableSet.of());
    }

    public int getAdjacentConnectionWeight(GraphNode<T> origin, GraphNode<T> destination) {
        return Optional.ofNullable(adjacencyMatrix.get(origin.getValue(), destination.getValue()))
                .orElseThrow(() -> new RuntimeException(
                        String.format("Connection does not exist between %s and %s",
                                origin.getValue(), destination.getValue())));
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
