package com.aoc.y2022;

import com.aoc.Point2D;
import com.aoc.ProblemDay;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay9b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        Rope rope = new Rope();
        Set<Point2D> tailVisited = new HashSet<>();
        while (scanner.hasNextLine()) {
            String[] cmds = scanner.nextLine().split(" ");
            char dir = cmds[0].charAt(0);
            int units = Integer.parseInt(cmds[1]);
            for (int i = 0; i < units; i++) {
                tailVisited.add(rope.move(dir));
            }
        }
        return tailVisited.size();
    }

    private static class Rope {
        List<Point2D> knots;

        public Rope() {
            knots = IntStream.range(0, 10).mapToObj(i -> new Point2D(0, 0)).collect(Collectors.toList());
        }

        public Point2D move(char dir) {
            switch (dir) {
                case 'U': get(0).setY(getY(0) + 1); break;
                case 'D': get(0).setY(getY(0) - 1); break;
                case 'L': get(0).setX(getX(0) - 1); break;
                case 'R': get(0).setX(getX(0) + 1); break;
            }
            for (int i = 0; i < knots.size() - 1; i++) {
                if (isMoveOnX(i)) {
                    get(i+1).setY(getY(i) - getY(i+1) > 0 ? getY(i+1) + 1 : getY(i+1) - 1);
                } else if (isMoveOnY(i)) {
                    get(i+1).setX(getX(i) - getX(i+1) > 0 ? getX(i+1) + 1 : getX(i+1) - 1);
                } else if (isTooFar(i)) {
                    get(i+1).setY(getY(i) - getY(i+1) > 0 ? getY(i+1) + 1 : getY(i+1) - 1);
                    get(i+1).setX(getX(i) - getX(i+1) > 0 ? getX(i+1) + 1 : getX(i+1) - 1);
                }
            }
            return new Point2D(getX(knots.size() - 1), getY(knots.size() - 1));
        }

        private boolean isTooFar(int index) {
            return distanceX(index) > 1 || distanceY(index) > 1;
        }

        private boolean isMoveOnX(int index) {
            return distanceX(index) == 0 && distanceY(index) > 1;
        }

        private boolean isMoveOnY(int index) {
            return distanceX(index) > 1 && distanceY(index) == 0;
        }

        private int distanceX(int index) {
            return Math.abs(knots.get(index).getX() - knots.get(index + 1).getX());
        }

        private int distanceY(int index) {
            return Math.abs(knots.get(index).getY() - knots.get(index + 1).getY());
        }

        private Point2D get(int index) {
            return knots.get(index);
        }

        private int getX(int index) {
            return get(index).getX();
        }

        private int getY(int index) {
            return get(index).getY();
        }
    }

}
