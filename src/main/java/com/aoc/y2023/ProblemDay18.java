package com.aoc.y2023;

import com.aoc.Point2D;
import com.aoc.ProblemDay;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

public class ProblemDay18 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Instruction> instructions = parseA();
        return solve(instructions);
    }

    @Override
    public Long solveB() {
        List<Instruction> instructions = parseB();
        return solve(instructions);
    }

    private long solve(List<Instruction> instructions) {
        Point2D p1 = new Point2D(0, 0);
        long shoelace = 0;
        for (Instruction ins : instructions) {
            int dist = ins.getDist();
            Direction dir = ins.getDir();
            Point2D p2 = next(p1, dist, dir);
            shoelace += (long) p1.getX() * p2.getY() - (long) p1.getY() * p2.getX();
            p1 = p2;
        }
        shoelace = Math.abs(shoelace) / 2;

        long perimeter = 0;
        for (Instruction ins : instructions) {
            perimeter += ins.getDist();
        }
        return shoelace + perimeter / 2 + 1;
    }

    private Point2D next(Point2D current, int dist, Direction dir) {
        int x = current.getX();
        int y = current.getY();
        switch (dir) {
            case U: return new Point2D(x, y - dist);
            case D: return new Point2D(x, y + dist);
            case L: return new Point2D(x - dist, y);
            case R: return new Point2D(x + dist, y);
        }
        throw new RuntimeException("Shouldn't happen");
    }

    private List<Instruction> parseA() {
        List<Instruction> instructions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            instructions.add(new Instruction(
                    Direction.valueOf(tokens[0]),
                    Integer.parseInt(tokens[1])));
        }
        return instructions;
    }

    private List<Instruction> parseB() {
        List<Instruction> instructions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String token = scanner.nextLine().split(" ")[2]
                    .replace("(", "")
                    .replace(")", "")
                    .replace("#", "");
            instructions.add(new Instruction(
                    Direction.fromDigit(Integer.parseInt(token.substring(token.length() - 1))),
                    Integer.parseInt(token.substring(0, token.length() - 1), 16)));
        }
        return instructions;
    }

    @Value
    private static class Instruction {
        private Direction dir;
        private int dist;
    }

    private enum Direction {
        U, D, L, R;

        public static Direction fromDigit(int num) {
            switch (num) {
                case 0: return R;
                case 1: return D;
                case 2: return L;
                case 3: return U;
                default: throw new RuntimeException(String.format("No matches for: %d", num));
            }
        }
    }

}
