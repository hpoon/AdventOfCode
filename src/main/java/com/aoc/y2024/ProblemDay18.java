package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProblemDay18 extends ProblemDay<Integer, String> {

    private final int HEIGHT = 71;
    private final int WIDTH = 71;
    private final int PART_A_LIMIT = 1024;

    @Override
    public Integer solveA() {
        List<Point2D> blocks = parse();
        return solveA(blocks.subList(0, PART_A_LIMIT));
    }

    @Override
    public String solveB() {
        List<Point2D> blocks = parse();
        int a = 0;
        int b = blocks.size();
        int best = Integer.MAX_VALUE;
        while (a <= b) {
            int mid = (a + b) / 2;
            int steps = solveA(blocks.subList(0, mid));
            if (steps > 0) {
                a = mid + 1;
            } else {
                best = Math.min(mid - 1, best);
                b = mid - 1;
            }
        }
        return String.format("%d,%d", blocks.get(best).getX(), blocks.get(best).getY());
    }

    private int solveA(List<Point2D> blocks) {
        Matrix<Character> map = new Matrix<>(HEIGHT, WIDTH, '.');
        blocks.forEach(block -> map.set(block.getY(), block.getX(), '#'));
        Matrix<Boolean> visited = new Matrix<>(HEIGHT, WIDTH, false);
        Queue<State> bfs = new LinkedList<>();
        bfs.add(new State(0, 0, 0));
        while (!bfs.isEmpty()) {
            State state = bfs.poll();
            int step = state.step;
            int row = state.row;
            int col = state.col;
            if (row < 0 || row >= HEIGHT || col < 0 || col >= WIDTH) {
                continue;
            }
            if (visited.getValue(row, col)) {
                continue;
            }
            if (map.getValue(row, col) == '#') {
                continue;
            }
            map.set(row, col, 'O');
            visited.set(row, col, true);
            if (row == HEIGHT - 1 && col == WIDTH - 1) {
                return step;
            }
            bfs.add(new State(step + 1, row + 1, col));
            bfs.add(new State(step + 1, row - 1, col));
            bfs.add(new State(step + 1, row, col + 1));
            bfs.add(new State(step + 1, row, col - 1));
        }
        return -1;
    }

    private List<Point2D> parse() {
        List<Point2D> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(",");
            list.add(new Point2D(
                    Integer.parseInt(tokens[0]),
                    Integer.parseInt(tokens[1])));
        }
        return list;
    }

    @Value
    private static class State {
        int step;
        int row;
        int col;
    }

}
