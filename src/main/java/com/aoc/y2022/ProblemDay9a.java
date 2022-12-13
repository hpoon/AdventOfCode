package com.aoc.y2022;

import com.aoc.Point2D;
import com.aoc.ProblemDay;

import java.util.HashSet;
import java.util.Set;

public class ProblemDay9a extends ProblemDay<Integer> {

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
        Point2D head;
        Point2D tail;

        public Rope() {
            head = new Point2D(0, 0);
            tail = new Point2D(0, 0);
        }

        public Point2D move(char dir) {
            switch (dir) {
                case 'U': head.setY(head.getY() + 1); break;
                case 'D': head.setY(head.getY() - 1); break;
                case 'L': head.setX(head.getX() - 1); break;
                case 'R': head.setX(head.getX() + 1); break;
            }
            if (isTailTouching()) {
                return new Point2D(tail.getX(), tail.getY());
            } else if (isTailAlignedOnX()) {
                tail.setY(head.getY() - tail.getY() > 0 ? tail.getY() + 1 : tail.getY() - 1);
            } else if (isTailAlignedOnY()) {
                tail.setX(head.getX() - tail.getX() > 0 ? tail.getX() + 1 : tail.getX() - 1);
            } else {
                tail.setY(head.getY() - tail.getY() > 0 ? tail.getY() + 1 : tail.getY() - 1);
                tail.setX(head.getX() - tail.getX() > 0 ? tail.getX() + 1 : tail.getX() - 1);
            }
            return new Point2D(tail.getX(), tail.getY());
        }

        private boolean isTailTouching() {
            return distanceX() <= 1 && distanceY() <= 1;
        }

        private boolean isTailAlignedOnX() {
            return distanceX() == 0;
        }

        private boolean isTailAlignedOnY() {
            return distanceY() == 0;
        }

        private int distanceX() {
            return Math.abs(head.getX() - tail.getX());
        }

        private int distanceY() {
            return Math.abs(head.getY() - tail.getY());
        }
    }

}
