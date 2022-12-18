package com.aoc;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class TopologicalSortTest {

    private static final Graph.GraphNode<String> R = new Graph.GraphNode<>("R");
    private static final Graph.GraphNode<String> S = new Graph.GraphNode<>("S");
    private static final Graph.GraphNode<String> T = new Graph.GraphNode<>("T");
    private static final Graph.GraphNode<String> X = new Graph.GraphNode<>("X");
    private static final Graph.GraphNode<String> Y = new Graph.GraphNode<>("Y");
    private static final Graph.GraphNode<String> Z = new Graph.GraphNode<>("Z");

    private Graph<String> graph;

    @BeforeEach
    public void before() {
        graph = new Graph<>();
        graph.addConnection(R, S, 5);
        graph.addConnection(R, T, 3);
        graph.addConnection(S, X, 6);
        graph.addConnection(S, T, 2);
        graph.addConnection(T, X, 7);
        graph.addConnection(T, Y, 4);
        graph.addConnection(X, Y, -1);
        graph.addConnection(X, Z, 1);
        graph.addConnection(Y, Z, -2);
    }

    @Test
    public void testLongestPath() {
        Map<Graph.GraphNode<String>, Integer> actual = TopologicalSort.nodeToLongestPaths(
                graph, S, Graph.GraphEdge::getWeight);
        Map<Graph.GraphNode<String>, Integer> expected = ImmutableMap.of(
                S, 0,
                T, 2,
                X, 9,
                Y, 8,
                Z, 10);
        assertThat(actual).isEqualTo(expected);
    }

}
