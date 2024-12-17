package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay14 extends ProblemDay<Long, Integer> {

    private static final int GRID_WIDTH = 101;
    private static final int GRID_HEIGHT = 103;

    @Override
    public Long solveA() {
        List<Robot> robots = parse();
        IntStream.range(0, 100).forEach(i -> robots.forEach(Robot::move));
        return scoreA(robots);
    }

    @Override
    public Integer solveB() {
        List<Robot> robots = parse();
        IntStream.range(0, 6512).forEach(i -> {
            robots.forEach(Robot::move);
            Set<Point2D> points = robots
                    .stream()
                    .map(r -> r.pos)
                    .collect(Collectors.toSet());
            if (points.size() == robots.size()) {
                System.out.println(i+1);
                matrix(robots).print();
            }
        });
        return 6512;
    }

    private Matrix<Character> matrix(List<Robot> robots) {
        Set<Point2D> points = robots
                .stream()
                .map(r -> r.pos)
                .collect(Collectors.toSet());
        List<List<Character>> matrix = new ArrayList<>();
        for (int row = 0; row < GRID_HEIGHT; row++) {
            List<Character> r = new ArrayList<>();
            for (int col = 0; col < GRID_WIDTH; col++) {
                Point2D p = new Point2D(col, row);
                if (points.contains(p)) {
                    r.add('#');
                } else {
                    r.add('.');
                }
            }
            matrix.add(r);
        }
        return new Matrix<>(matrix);
    }

    private long scoreA(List<Robot> robots) {
        return robots
                .stream()
                .collect(Collectors.groupingBy(
                        Robot::quadrant,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getKey() > 0)
                .map(Map.Entry::getValue)
                .reduce(Math::multiplyExact)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private List<Robot> parse() {
        List<Robot> robots = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine()
                    .replace("p=", "")
                    .replace("v=", "")
                    .replace(",", " ")
                    .split(" ");
            robots.add(new Robot(
                    new Point2D(
                            Integer.parseInt(tokens[0]),
                            Integer.parseInt(tokens[1])),
                    new Point2D(
                            Integer.parseInt(tokens[2]),
                            Integer.parseInt(tokens[3]))));
        }
        return robots;
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    private static class Robot {

        private Point2D pos;
        private Point2D velocity;

        public void move() {
            pos = pos.translate(velocity);
            if (pos.getX() >= GRID_WIDTH) {
                pos = pos.translate(new Point2D(-GRID_WIDTH, 0));
            } else if (pos.getX() < 0) {
                pos = pos.translate(new Point2D(GRID_WIDTH, 0));
            }
            if (pos.getY() >= GRID_HEIGHT) {
                pos = pos.translate(new Point2D(0, -GRID_HEIGHT));
            } else if (pos.getY() < 0) {
                pos = pos.translate(new Point2D(0, GRID_HEIGHT));
            }
        }

        public int quadrant() {
            int x = GRID_WIDTH / 2;
            int y = GRID_HEIGHT / 2;
            if (pos.getX() < x && pos.getY() < y) {
                return 1;
            } else if (pos.getX() > x && pos.getY() < y) {
                return 2;
            } else if (pos.getX() < x && pos.getY() > y) {
                return 3;
            } else if (pos.getX() > x && pos.getY() > y){
                return 4;
            } else {
                return 0;
            }
        }

    }

}
