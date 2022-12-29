package com.aoc.y2015;

import com.aoc.Point2D;
import com.aoc.ProblemDay;

import java.util.*;

public class ProblemDay3 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Character> dirs = parse();
        Point2D current = new Point2D(0, 0);
        Map<Point2D, Integer> frequencies = new HashMap<>();
        frequencies.put(current, 1);
        for (char dir : dirs) {
            switch (dir) {
                case '>': current = current.translate(new Point2D(1, 0)); break;
                case '<': current = current.translate(new Point2D(-1, 0)); break;
                case '^': current = current.translate(new Point2D(0, -1)); break;
                case 'v': current = current.translate(new Point2D(0, 1)); break;
            }
            frequencies.put(current, frequencies.getOrDefault(current, 0) + 1);
        }
        return frequencies.size();
    }

    @Override
    protected Integer solveB() {
        List<Character> dirs = parse();

        Map<Integer, Point2D> turnToCurrent = new HashMap<>();
        turnToCurrent.put(0, new Point2D(0, 0));
        turnToCurrent.put(1, new Point2D(0, 0));

        Map<Point2D, Integer> frequencies = new HashMap<>();
        frequencies.put(turnToCurrent.get(0), 2);
        for (int i = 0; i < dirs.size(); i++) {
            int turn = i % 2;
            char dir = dirs.get(i);
            Point2D current = turnToCurrent.get(turn);
            switch (dir) {
                case '>': current = current.translate(new Point2D(1, 0)); break;
                case '<': current = current.translate(new Point2D(-1, 0)); break;
                case '^': current = current.translate(new Point2D(0, -1)); break;
                case 'v': current = current.translate(new Point2D(0, 1)); break;
            }
            turnToCurrent.put(turn, current);
            frequencies.put(current, frequencies.getOrDefault(current, 0) + 1);
        }
        return frequencies.size();
    }

    private List<Character> parse() {
        List<Character> dirs = new ArrayList<>();
        String line = scanner.nextLine();
        for (int i = 0; i < line.length(); i++) {
            dirs.add(line.charAt(i));
        }
        return dirs;
    }

}
