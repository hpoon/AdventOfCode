package com.aoc.y2023;

import com.aoc.Graph;
import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.aoc.TopologicalSort;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Value;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class ProblemDay23 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Character> matrix = parse();
        return solveA(matrix);
    }

    @Override
    public Integer solveB() {
        Matrix<Character> matrix = parse();
        return solveB(matrix);
    }

    private int solveA(Matrix<Character> matrix) {
        Graph<MatrixElement<Character>> graph = matrixToGraph(matrix);
        Graph.GraphNode<MatrixElement<Character>> start = new Graph.GraphNode<>(matrix.getElement(0, 1));
        Map<Graph.GraphNode<MatrixElement<Character>>, Integer> paths = TopologicalSort.nodeToLongestPaths(
                graph, start, Graph.GraphEdge::getWeight);
        return paths.values().stream()
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private int solveB(Matrix<Character> matrix) {
        Graph<MatrixElement<Character>> graph = matrixToGraph(matrix);
        Graph<MatrixElement<Character>> twoWayGraph = graphToTwoWayGraph(graph);
        Graph.GraphNode<MatrixElement<Character>> start = new Graph.GraphNode<>(matrix.getElement(0, 1));
        Map<Graph.GraphNode<MatrixElement<Character>>, Integer> memo = new HashMap<>();
        Stack<State> stack = new Stack<>();
        stack.add(new State(start, new HashSet<>(), 0));
        int maxSteps = 0;
        while (!stack.isEmpty()) {
            State state = stack.pop();
            Graph.GraphNode<MatrixElement<Character>> current = state.getCurrent();
            Set<Graph.GraphNode<MatrixElement<Character>>> localVisited = new HashSet<>(state.getVisited());
            int steps = state.getSteps();
            if (localVisited.contains(current)) {
                continue;
            }
            localVisited.add(current);
            if (memo.containsKey(current)) {
                if (steps > memo.get(current)) {
                    memo.put(current, steps);
                    if (steps > maxSteps) {
                        maxSteps = steps;
                    }
                }
            } else {
                memo.put(current, steps);
            }
            for (Graph.GraphEdge<MatrixElement<Character>> edge : twoWayGraph.getConnections(current)) {
                stack.add(new State(edge.getConnection(), localVisited, steps + edge.getWeight()));
            }
        }
        return maxSteps;
    }

    private Graph<MatrixElement<Character>> graphToTwoWayGraph(Graph<MatrixElement<Character>> graph) {
        Graph<MatrixElement<Character>> twoWayGraph = new Graph<>();
        for (Graph.GraphNode<MatrixElement<Character>> node : graph.getNodes()) {
            for (Graph.GraphEdge<MatrixElement<Character>> edge : graph.getConnections(node)) {
                twoWayGraph.addConnection(edge.getConnection(), node, edge.getWeight());
                twoWayGraph.addConnection(node, edge.getConnection(), edge.getWeight());
            }
        }
        return twoWayGraph;
    }

    private Graph<MatrixElement<Character>> matrixToGraph(Matrix<Character> matrix) {
        MatrixElement<Character> start = matrix.getElement(0, 1);
        MatrixElement<Character> end = matrix.getElement(matrix.height() - 1, matrix.width(0) - 2);
        Graph<MatrixElement<Character>> graph = new Graph<>();
        Queue<MatrixToGraphState> queue = new LinkedList<>();
        queue.add(new MatrixToGraphState(start, null, new Graph.GraphNode<>(start), 0));
        Set<MatrixElement<Character>> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            MatrixToGraphState state = queue.poll();
            MatrixElement<Character> current = state.getCurrent();
            MatrixElement<Character> previous = state.getPrevious();
            Graph.GraphNode<MatrixElement<Character>> keyNode = state.getKeyNode();
            int steps = state.getSteps();
            List<MatrixElement<Character>> backtrackableSteps = backtrackableSteps(current, previous, matrix);
            if (backtrackableSteps.size() > 1) {
                Graph.GraphNode<MatrixElement<Character>> newNode = new Graph.GraphNode<>(current);
                graph.addConnection(keyNode, newNode, steps);
                keyNode = newNode;
                steps = 0;
            }
            if (current.equals(end)) {
                Graph.GraphNode<MatrixElement<Character>> endNode = new Graph.GraphNode<>(end);
                graph.addConnection(keyNode, endNode, steps);
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            for (MatrixElement<Character> backtrackableStep : backtrackableSteps) {
                boolean cannotGoDown = backtrackableStep.getRow() - current.getRow() == 1 && backtrackableStep.getValue() == '^';
                boolean cannotGoUp = backtrackableStep.getRow() - current.getRow() == -1 && backtrackableStep.getValue() == 'v';
                boolean cannotGoRight = backtrackableStep.getCol() - current.getCol() == 1 && backtrackableStep.getValue() == '<';
                boolean cannotGoLeft = backtrackableStep.getCol() - current.getCol() == -1 && backtrackableStep.getValue() == '>';
                if (cannotGoDown || cannotGoUp || cannotGoRight || cannotGoLeft) {
                    continue;
                }
                queue.add(new MatrixToGraphState(backtrackableStep, current, keyNode, steps + 1));
            }
        }
        return graph;
    }

    private List<MatrixElement<Character>> backtrackableSteps(MatrixElement<Character> current,
                                                              MatrixElement<Character> previous,
                                                              Matrix<Character> matrix) {
        int row = current.getRow();
        int col = current.getCol();
        char c = current.getValue();
        List<MatrixElement<Character>> eligibleSteps = new ArrayList<>();
        Map<Character, MatrixElement<Character>> nexts = ImmutableMap.of(
                '<', new MatrixElement<>(row, col - 1, null),
                '>', new MatrixElement<>(row, col + 1, null),
                'v', new MatrixElement<>(row + 1, col, null),
                '^', new MatrixElement<>(row - 1, col, null));
        if (nexts.containsKey(c)) {
            MatrixElement<Character> next = nexts.get(c);
            return matrix.withinBounds(next.getRow(), next.getCol())
                    ? ImmutableList.of(new MatrixElement<>(
                            next.getRow(),
                            next.getCol(),
                            matrix.getValue(next.getRow(), next.getCol())))
                    : ImmutableList.of();
        }
        for (MatrixElement<Character> next : nexts.values()) {
            if (!matrix.withinBounds(next.getRow(), next.getCol())) {
                continue;
            }
            char nextC = matrix.getValue(next.getRow(), next.getCol());
            if (nextC == '#') {
                continue;
            }
            if (next.equals(previous)) {
                continue;
            }
            eligibleSteps.add(new MatrixElement<>(next.getRow(), next.getCol(), nextC));
        }
        return eligibleSteps;
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, row -> row.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    @Value
    private static class MatrixToGraphState {
        private MatrixElement<Character> current;
        private MatrixElement<Character> previous;
        private Graph.GraphNode<MatrixElement<Character>> keyNode;
        private int steps;
    }

    @Value
    private static class State {
        private Graph.GraphNode<MatrixElement<Character>> current;
        private Set<Graph.GraphNode<MatrixElement<Character>>> visited;
        private int steps;
    }

}
