package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay10 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Integer> map = parse();
        return map.score(
                e -> e.getValue() == 0,
                s -> s.map(e -> {
                    Set<MatrixElement<Integer>> results = new HashSet<>();
                    findTrails(e.getRow(), e.getCol(), 0, results, map);
                    return results.size();
                }))
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @Override
    public Integer solveB() {
        Matrix<Integer> map = parse();
        return map.score(
                        e -> e.getValue() == 0,
                        s -> s.map(e -> {
                            Set<Set<MatrixElement<Integer>>> results = new HashSet<>();
                            findRatings(e.getRow(), e.getCol(), 0, new HashSet<>(), results, map);
                            return results.size();
                        }))
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private void findTrails(int row,
                            int col,
                            int nextHeight,
                            Set<MatrixElement<Integer>> results,
                            Matrix<Integer> map) {
        if (row < 0 || row >= map.height() || col < 0 || col >= map.width(row)) {
            return;
        }
        MatrixElement<Integer> current = map.getElement(row, col);
        if (current.getValue() != nextHeight) {
            return;
        }
        if (current.getValue() == 9) {
            results.add(current);
            return;
        }
        findTrails(row + 1, col, nextHeight + 1, results, map);
        findTrails(row - 1, col, nextHeight + 1, results, map);
        findTrails(row, col + 1, nextHeight + 1, results, map);
        findTrails(row, col - 1, nextHeight + 1, results, map);
    }

    private void findRatings(int row,
                             int col,
                             int nextHeight,
                             Set<MatrixElement<Integer>> currentPath,
                             Set<Set<MatrixElement<Integer>>> results,
                             Matrix<Integer> map) {
        if (row < 0 || row >= map.height() || col < 0 || col >= map.width(row)) {
            return;
        }
        MatrixElement<Integer> current = map.getElement(row, col);
        if (current.getValue() != nextHeight) {
            return;
        }
        currentPath.add(current);
        if (current.getValue() == 9) {
            results.add(currentPath);
            return;
        }
        findRatings(row + 1, col, nextHeight + 1, new HashSet<>(currentPath), results, map);
        findRatings(row - 1, col, nextHeight + 1, new HashSet<>(currentPath), results, map);
        findRatings(row, col + 1, nextHeight + 1, new HashSet<>(currentPath), results, map);
        findRatings(row, col - 1, nextHeight + 1, new HashSet<>(currentPath), results, map);
    }

    private Matrix<Integer> parse() {
        return new Matrix<>(
                scanner,
                str -> str.chars().mapToObj(Character::toString).map(Integer::parseInt).collect(Collectors.toList()));
    }

}
