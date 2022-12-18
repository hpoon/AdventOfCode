package com.aoc;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class DjikstraTest {

    private static final Graph.GraphNode<String> START = new Graph.GraphNode<>("S");
    private static final Graph.GraphNode<String> A = new Graph.GraphNode<>("A");
    private static final Graph.GraphNode<String> B = new Graph.GraphNode<>("B");
    private static final Graph.GraphNode<String> C = new Graph.GraphNode<>("C");
    private static final Graph.GraphNode<String> D = new Graph.GraphNode<>("D");

    private Graph<String> graph;

    @BeforeEach
    public void before() {
        graph = new Graph<>();
        graph.addConnection(START, A, 10);
        graph.addConnection(START, C, 6);
        graph.addConnection(A, START, 10);
        graph.addConnection(A, B, 2);
        graph.addConnection(A, C, 3);
        graph.addConnection(B, A, 2);
        graph.addConnection(B, C, 10);
        graph.addConnection(B, D, 7);
        graph.addConnection(C, START, 6);
        graph.addConnection(C, A, 3);
        graph.addConnection(C, B, 10);
        graph.addConnection(C, D, 5);
        graph.addConnection(D, B, 7);
        graph.addConnection(D, C, 5);
    }

    @Test
    public void testShortestPath1() {
        List<Graph.GraphNode<String>> path = Djikstra.shortestPaths(
                graph, START, D, Graph.GraphEdge::getWeight);
        assertThat(path).isEqualTo(ImmutableList.of(START, C, D));
    }

    @Test
    public void testShortestPath2() {
        List<Graph.GraphNode<String>> path = Djikstra.shortestPaths(
                graph, START, B, Graph.GraphEdge::getWeight);
        assertThat(path).isEqualTo(ImmutableList.of(START, C, A, B));
    }

}
