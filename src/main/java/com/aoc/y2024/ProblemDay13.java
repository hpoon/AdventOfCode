package com.aoc.y2024;

import com.aoc.Point2DL;
import com.aoc.ProblemDay;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Model;
import com.microsoft.z3.RealExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class ProblemDay13 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<ClawGame> games = parse(true);
        return games.stream()
                .map(this::solveSlow)
                .reduce(Long::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @Override
    public Long solveB() {
        List<ClawGame> games = parse(false);
        return games.stream()
                .map(this::solveFast)
                .reduce(Long::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private long solveFast(ClawGame game) {
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        RealExpr a = ctx.mkRealConst("A");
        RealExpr b = ctx.mkRealConst("B");

        ArithExpr x = ctx.mkAdd(
                ctx.mkMul(ctx.mkReal(game.getA().getX()), a),
                ctx.mkMul(ctx.mkReal(game.getB().getX()), b));
        ArithExpr y = ctx.mkAdd(
                ctx.mkMul(ctx.mkReal(game.getA().getY()), a),
                ctx.mkMul(ctx.mkReal(game.getB().getY()), b));
        BoolExpr eqX = ctx.mkEq(ctx.mkReal(game.getPrize().getX()), x);
        BoolExpr eqY = ctx.mkEq(ctx.mkReal(game.getPrize().getY()), y);

        solver.add(eqX);
        solver.add(eqY);

        Status status = solver.check();
        if (status == Status.SATISFIABLE) {
            Model model = solver.getModel();
            String sol1 = "" + model.eval(a, false);
            String sol2 = "" + model.eval(b, false);
            // Whole number solutions only
            if (sol1.contains("/") || sol2.contains("/")) {
                return 0L;
            }
            return Long.parseLong(sol1) * ClawGame.A_COST + Long.parseLong(sol2) * ClawGame.B_COST;
        } else {
            throw new RuntimeException("The equations have no solution or are unsatisfiable.");
        }
    }

    private long solveSlow(ClawGame game) {
        Stack<ClawGame> stk = new Stack<>();
        stk.add(game);
        int cost = Integer.MAX_VALUE;
        Set<ClawGame> visited = new HashSet<>();
        while (!stk.isEmpty()) {
            ClawGame current = stk.pop();
            if (current.isWin()) {
                cost = Math.min(cost, current.getCost());
                continue;
            }
            if (current.isTooFar()) {
                continue;
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            stk.add(current.pressA());
            stk.add(current.pressB());
        }
        return cost == Integer.MAX_VALUE ? 0 : cost;
    }

    private List<ClawGame> parse(boolean isPartA) {
        List<ClawGame> games = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] buttonA = scanner.nextLine()
                    .replace("Button A: X+", "")
                    .replace(", Y+", " ")
                    .split(" ");
            String[] buttonB = scanner.nextLine()
                    .replace("Button B: X+", "")
                    .replace(", Y+", " ")
                    .split(" ");
            String[] prize = scanner.nextLine()
                    .replace("Prize: X=", "")
                    .replace(", Y=", " ")
                    .split(" ");
            Point2DL prizePos = new Point2DL(
                    Long.parseLong(prize[0]),
                    Long.parseLong(prize[1]));
            if (!isPartA) {
                prizePos = prizePos.translate(new Point2DL(10000000000000L, 10000000000000L));
            }
            games.add(new ClawGame(
                    new Point2DL(
                            Long.parseLong(buttonA[0]),
                            Long.parseLong(buttonA[1])),
                    new Point2DL(
                            Long.parseLong(buttonB[0]),
                            Long.parseLong(buttonB[1])),
                    prizePos,
                    new Point2DL(0, 0),
                    0));
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
        return games;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class ClawGame {

        public static final int A_COST = 3;
        public static final int B_COST = 1;

        private final Point2DL a;
        private final Point2DL b;
        private final Point2DL prize;
        private Point2DL pos;
        private int cost;

        public ClawGame pressA() {
            return new ClawGame(a, b, prize, pos.translate(a), cost + A_COST);
        }

        public ClawGame pressB() {
            return new ClawGame(a, b, prize, pos.translate(b), cost + B_COST);
        }

        public boolean isWin() {
            return prize.equals(pos);
        }

        public boolean isTooFar() {
            return pos.getX() > prize.getX() && pos.getY() > prize.getY();
        }

    }

}
