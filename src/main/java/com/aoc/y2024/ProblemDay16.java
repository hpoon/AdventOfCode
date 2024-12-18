package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
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
import java.util.stream.Collectors;

public class ProblemDay16 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Character> maze = parse();
        Map<State, Integer> bestScore = solveA(maze);
        return score(bestScore, maze);
    }

    @Override
    public Integer solveB() {
        Matrix<Character> maze = parse();
        Map<State, Integer> bestScore = solveA(maze);
        return solveB(bestScore, maze);
    }

    private Map<State, Integer> solveA(Matrix<Character> maze) {
        MatrixElement<Character> start = maze.getValue('S').get(0);
        Map<State, Integer> bestScore = new HashMap<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(new State(start.getRow(), start.getCol(), '>', 0));
        while (!queue.isEmpty()) {
            State state = queue.poll();
            int row = state.getRow();
            int col = state.getCol();
            if (!maze.withinBounds(row, col)) {
                continue;
            }

            MatrixElement<Character> current = maze.getElement(row, col);
            char dir = state.getDirection();
            int score = state.getScore();
            if (current.getValue() == '#') {
                continue;
            }
            if (current.getValue() == 'E') {
                int updatedScore = Math.min(score, bestScore.getOrDefault(state, Integer.MAX_VALUE));
                bestScore.remove(state);
                bestScore.put(new State(row, col, dir, updatedScore), updatedScore);
                continue;
            }
            if (bestScore.containsKey(state) && bestScore.get(state) < score) {
                continue;
            }
            bestScore.remove(state);
            bestScore.put(new State(row, col, dir, score), score);
            queue.addAll(ImmutableList.of(
                    new State(row + 1, col, 'v', 'v' == dir ? score + 1 : score + 1001),
                    new State(row - 1, col, '^', '^' == dir ? score + 1 : score + 1001),
                    new State(row, col + 1, '>', '>' == dir ? score + 1 : score + 1001),
                    new State(row, col - 1, '<', '<' == dir ? score + 1 : score + 1001)));
        }
        return bestScore;
    }

    private int solveB(Map<State, Integer> bestScore, Matrix<Character> maze) {
        final int best = score(bestScore, maze);
        List<State> starts = bestScore.entrySet()
                .stream()
                .filter(entry -> maze.getValue(entry.getKey().getRow(), entry.getKey().getCol()) == 'E')
                .filter(entry -> entry.getValue() == best)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Set<MatrixElement<Character>> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>(starts);
        while (!queue.isEmpty()) {
            State current = queue.poll();
            int row = current.getRow();
            int col = current.getCol();
            int dir = current.getDirection();
            int score = bestScore.get(current);
            visited.add(new MatrixElement<>(row, col, null));
            List<State> toAdd = new ArrayList<>();
            switch (dir) {
                case '^':
                    toAdd.add(new State(row + 1, col, '^', 0));
                    toAdd.add(new State(row + 1, col, '<', 0));
                    toAdd.add(new State(row + 1, col, '>', 0));
                    break;
                case 'v':
                    toAdd.add(new State(row - 1, col, 'v', 0));
                    toAdd.add(new State(row - 1, col, '<', 0));
                    toAdd.add(new State(row - 1, col, '>', 0));
                    break;
                case '<':
                    toAdd.add(new State(row, col + 1, '<', 0));
                    toAdd.add(new State(row, col + 1, '^', 0));
                    toAdd.add(new State(row, col + 1, 'v', 0));
                    break;
                case '>':
                    toAdd.add(new State(row, col - 1, '>', 0));
                    toAdd.add(new State(row, col - 1, '^', 0));
                    toAdd.add(new State(row, col - 1, 'v', 0));
                    break;
            }
            queue.addAll(toAdd.stream()
                    .filter(bestScore::containsKey)
                    .filter(add -> bestScore.get(add) == score -1 || bestScore.get(add) == score - 1001)
                    .collect(Collectors.toList()));
        }
        return visited.size();
    }

    private int score(Map<State, Integer> bestScore, Matrix<Character> maze) {
        return bestScore.entrySet()
                .stream()
                .filter(entry -> maze.getValue(entry.getKey().getRow(), entry.getKey().getCol()) == 'E')
                .map(Map.Entry::getValue)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, row -> row.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    @Value
    @EqualsAndHashCode(exclude = { "score" })
    private static class State {
        private int row;
        private int col;
        private char direction;
        private int score;
    }

}
