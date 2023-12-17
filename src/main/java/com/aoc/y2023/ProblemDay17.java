package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class ProblemDay17 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Integer> matrix = parse();
        Pair<List<State>, Integer> paths = shortestPaths(matrix, 0, 3);
        return paths.getValue();
    }

    @Override
    public Integer solveB() {
        Matrix<Integer> matrix = parse();
        Pair<List<State>, Integer> paths = shortestPaths(matrix, 4, 10);
        return paths.getValue();
    }

    private Pair<List<State>, Integer> shortestPaths(Matrix<Integer> matrix,
                                                     int minSteps,
                                                     int maxSteps) {
        Map<State, Integer> distances = new HashMap<>();
        Queue<Pair<State, Integer>> priority = new PriorityQueue<>(Map.Entry.comparingByValue());
        Map<State, State> nodeToParent = new HashMap<>();
        State origin = new State(0, 0, null, 0);
        distances.put(origin, 0);
        State current = origin;
        while (current != null) {
            Map<State, Integer> edges = connections(current, matrix, minSteps, maxSteps);
            int weightToCurrent = distances.getOrDefault(current, 0);
            for (Map.Entry<State, Integer> edge : edges.entrySet()) {
                int currWeight = weightToCurrent + edges.get(edge.getKey());
                if (distances.containsKey(edge.getKey())) {
                    int currentSmallestWeight = distances.get(edge.getKey());
                    if (currWeight < currentSmallestWeight) {
                        priority.remove(ImmutablePair.of(edge.getKey(), distances.get(edge.getKey())));
                        distances.put(edge.getKey(), currWeight);
                        priority.add(ImmutablePair.of(edge.getKey(), currWeight));
                        nodeToParent.put(edge.getKey(), current);
                    }
                } else {
                    distances.put(edge.getKey(), currWeight);
                    priority.add(ImmutablePair.of(edge.getKey(), currWeight));
                    nodeToParent.put(edge.getKey(), current);
                }
            }
            current = !priority.isEmpty() ? priority.poll().getLeft() : null;
        }

        List<State> path = new ArrayList<>();
        State destination = distances.entrySet().stream()
                .filter(e -> e.getKey().getRow() == matrix.height() - 1)
                .filter(e -> e.getKey().getCol() == matrix.width(0) - 1)
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
        current = destination;
        while (current != null) {
            path.add(current);
            current = nodeToParent.get(current);
        }
        Collections.reverse(path);
        return ImmutablePair.of(path.get(0).equals(origin) ? path : ImmutableList.of(), distances.get(destination));
    }

    private Map<State, Integer> connections(State state, Matrix<Integer> matrix, int minSteps, int maxSteps) {
        int row = state.getRow();
        int col = state.getCol();
        Direction dir = state.getDir();
        int consecutive = state.getConsecutive();
        Map<Point2D, Direction> directions = ImmutableMap.of(
                new Point2D(-1, 0), Direction.LEFT,
                new Point2D(1, 0), Direction.RIGHT,
                new Point2D(0, 1), Direction.DOWN,
                new Point2D(0, -1), Direction.UP);
        Map<State, Integer> connections = new HashMap<>();
        for (Map.Entry<Point2D, Direction> entry : directions.entrySet()) {
            int newRow = row + entry.getKey().getY();
            int newCol = col + entry.getKey().getX();
            Direction newDir = entry.getValue();
            if (!matrix.withinBounds(newRow, newCol)) {
                continue;
            }
            computeConnection(dir, consecutive, newRow, newCol, newDir, minSteps, maxSteps)
                    .ifPresent(c -> connections.put(c, matrix.getValue(newRow, newCol)));
        }
        return connections;
    }

    private Optional<State> computeConnection(Direction dir,
                                              int consecutive,
                                              int newRow,
                                              int newCol,
                                              Direction newDir,
                                              int min,
                                              int max) {
        if (consecutive < min && dir != newDir && dir != null) {
            return Optional.empty();
        }
        if (dir != newDir.opposite()) {
            if (newDir == dir && consecutive < max) {
                return Optional.of(new State(
                        newRow,
                        newCol,
                        newDir,
                        consecutive + 1));
            } else if (newDir != dir) {
                return Optional.of(new State(
                        newRow,
                        newCol,
                        newDir,
                        1));
            }
        }
        return Optional.empty();
    }

    private Matrix<Integer> parse() {
        return new Matrix<>(
                scanner,
                str -> str.chars().mapToObj(Character::toString).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @Value
    @EqualsAndHashCode
    private static class State {
        int row;
        int col;
        Direction dir;
        int consecutive;
    }

    @AllArgsConstructor
    private enum Direction {
        UP, DOWN, LEFT, RIGHT;

        private Direction opposite() {
            switch (this) {
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
            }
            return null;
        }

    }

}
