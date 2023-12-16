package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay16 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<String> matrix = parse();
        return solve(matrix, new Laser(Direction.RIGHT, 0, 0));
    }

    @Override
    public Integer solveB() {
        Matrix<String> matrix = parse();
        int max = 0;
        for (int r = 0; r < matrix.height(); r++) {
            int left = solve(matrix, new Laser(Direction.RIGHT, r, 0));
            int right = solve(matrix, new Laser(Direction.LEFT, r, matrix.width(r) - 1));
            max = Math.max(Math.max(left, right), max);
        }
        for (int c = 0; c < matrix.width(0); c++) {
            int top = solve(matrix, new Laser(Direction.DOWN, 0, c));
            int bottom = solve(matrix, new Laser(Direction.UP, matrix.height() - 1, c));
            max = Math.max(Math.max(top, bottom), max);
        }
        return max;
    }

    private int solve(Matrix<String> matrix, Laser start) {
        Matrix<String> activations = matrix.copy().apply(element -> ".");
        Queue<Laser> queue = new LinkedList<>();
        queue.add(start);
        Map<MatrixElement<String>, Set<Laser>> visitedBy = new HashMap<>();
        while (!queue.isEmpty()) {
            Laser l = queue.poll();
            Direction d = l.getDirection();
            int r = l.getRow();
            int c = l.getCol();
            if (!matrix.withinBounds(r, c)) {
                continue;
            }
            MatrixElement<String> e = matrix.getElement(r, c);
            Set<Laser> nodeVisitedByLasers = visitedBy.getOrDefault(e, new HashSet<>());
            if (nodeVisitedByLasers.contains(l)) {
                continue;
            }
            nodeVisitedByLasers.add(l);
            visitedBy.put(e, nodeVisitedByLasers);
            activations.set(r, c, "#");
            String s = e.getValue();
            if (StringUtils.isEmpty(s)) {
                throw new RuntimeException("Shouldn't happen");
            }
            switch (s) {
                case ".":
                    queue.add(new Laser(d, getNextRow(d, r), getNextCol(d, c)));
                    break;
                case "|":
                    switch (d) {
                        case UP:
                        case DOWN:
                            queue.add(new Laser(d, getNextRow(d, r), getNextCol(d, c)));
                            break;
                        case LEFT:
                        case RIGHT:
                            queue.add(new Laser(Direction.UP, r-1, c));
                            queue.add(new Laser(Direction.DOWN, r+1, c));
                            break;
                    }
                    break;
                case "-":
                    switch (d) {
                        case UP:
                        case DOWN:
                            queue.add(new Laser(Direction.LEFT, r, c-1));
                            queue.add(new Laser(Direction.RIGHT, r, c+1));
                            break;
                        case LEFT:
                        case RIGHT:
                            queue.add(new Laser(d, getNextRow(d, r), getNextCol(d, c)));
                            break;
                    }
                    break;
                case "/":
                    switch (d) {
                        case UP:
                            queue.add(new Laser(Direction.RIGHT, r, c+1));
                            break;
                        case DOWN:
                            queue.add(new Laser(Direction.LEFT, r, c-1));
                            break;
                        case LEFT:
                            queue.add(new Laser(Direction.DOWN, r+1, c));
                            break;
                        case RIGHT:
                            queue.add(new Laser(Direction.UP, r-1, c));
                            break;
                    }
                    break;
                case "\\":
                    switch (d) {
                        case UP:
                            queue.add(new Laser(Direction.LEFT, r, c-1));
                            break;
                        case DOWN:
                            queue.add(new Laser(Direction.RIGHT, r, c+1));
                            break;
                        case LEFT:
                            queue.add(new Laser(Direction.UP, r-1, c));
                            break;
                        case RIGHT:
                            queue.add(new Laser(Direction.DOWN, r+1, c));
                            break;
                    }
            }
        }
        return activations.score(e -> Objects.equals(e.getValue(), "#"), s -> s.mapToInt(i -> 1).sum());
    }

    private int getNextRow(Direction d, int r) {
        switch (d) {
            case UP:
                return r-1;
            case DOWN:
                return r+1;
            case LEFT:
            case RIGHT:
                return r;
            default: throw new RuntimeException("Shouldn't happen");
        }
    }

    private int getNextCol(Direction d, int c) {
        switch (d) {
            case UP:
            case DOWN:
                return c;
            case LEFT:
                return c-1;
            case RIGHT:
                return c+1;
            default: throw new RuntimeException("Shouldn't happen");
        }
    }

    private Matrix<String> parse() {
        Matrix<String> matrix = new Matrix<>();
        while (scanner.hasNextLine()) {
            matrix.addRow(scanner.nextLine().chars().mapToObj(Character::toString).collect(Collectors.toList()));
        }
        return matrix;
    }

    @Value
    private static class Laser {
        private Direction direction;
        private int row;
        private int col;
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

}
