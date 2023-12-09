package com.aoc.y2016;

import com.aoc.Point2D;
import com.aoc.ProblemDay;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay1 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<String> instructions = parse();
        return solve(instructions, true);
    }

    @Override
    public Integer solveB() {
        List<String> instructions = parse();
        return solve(instructions, false);
    }

    private int solve(List<String> instructions, boolean solveA) {
        Point2D pos = new Point2D(0, 0);
        int dir = 0;
        Set<Point2D> visited = new HashSet<>();
        for (String instruction : instructions) {
            int rotation = instruction.charAt(0) == 'L' ? 0 : 1;
            int steps = Integer.parseInt(instruction.substring(1));
            dir = nextDir(dir, rotation);
            int x = 0;
            int y = 0;
            switch (dir) {
                case 0: y = steps; break;
                case 1: x = steps; break;
                case 2: y = -steps; break;
                case 3: x = -steps; break;
            }
            if (!solveA) {
                if (x == 0) {
                    for (int i = 1; i <= steps; i++) {
                        Point2D p = new Point2D(pos.getX(), pos.getY() + y / Math.abs(y) * i);
                        if (visited.contains(p)) {
                            return p.manhattanDistance(new Point2D(0, 0));
                        }
                        visited.add(p);
                    }
                }
                if (y == 0) {
                    for (int i = 1; i <= steps; i++) {
                        Point2D p = new Point2D(pos.getX() + x / Math.abs(x) * i, pos.getY());
                        if (visited.contains(p)) {
                            return p.manhattanDistance(new Point2D(0, 0));
                        }
                        visited.add(p);
                    }
                }
            }
            pos = new Point2D(pos.getX() + x, pos.getY() + y);
        }
        return pos.manhattanDistance(new Point2D(0, 0));
    }

    // 0 left rotate
    // 1 right rotate
    //
    // 0 north
    // 1 east
    // 2 south
    // 3 west
    private int nextDir(int current, int rotation) {
        int nextDir = rotation == 0
                ? current - 1
                : current + 1;
        nextDir = nextDir < 0 ? nextDir + 4 : nextDir;
        return nextDir >= 4 ? nextDir - 4 : nextDir;
    }

    private List<String> parse() {
        return Arrays.stream(scanner
                        .nextLine()
                        .replace(", ", " ")
                        .split(" "))
                .collect(Collectors.toList());
    }

}
