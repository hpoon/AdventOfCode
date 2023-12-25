package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.collect.Range;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Model;
import com.microsoft.z3.RealExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import lombok.Value;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay24 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Hail> hails = parse(true);
        return solveA(hails, Range.closed(200000000000000L, 400000000000000L));
    }

    @Override
    public Long solveB() {
        List<Hail> hails = parse(false);
        return solveB(hails);
    }

    private long solveA(List<Hail> hails, Range<Long> roi) {
        long intersections = 0;
        for (int i = 0; i < hails.size(); i++) {
            for (int j = i + 1; j < hails.size(); j++) {
                Hail h1 = hails.get(i);
                Hail h2 = hails.get(j);
                if (collision(h1, h2, roi)) {
                    intersections++;
                }
            }
        }
        return intersections;
    }

    private long solveB(List<Hail> hails) {
        // Creating a context for Z3
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        // Creating variables representing the components of the vectors
        RealExpr x = ctx.mkRealConst("x");
        RealExpr y = ctx.mkRealConst("y");
        RealExpr z = ctx.mkRealConst("z");
        RealExpr vx = ctx.mkRealConst("vx");
        RealExpr vy = ctx.mkRealConst("vy");
        RealExpr vz = ctx.mkRealConst("vz");

        for (int i = 0; i < hails.size(); i++) {
            Vector3D p = hails.get(i).getPosition();
            Vector3D v = hails.get(i).getVelocity();
            RealExpr t = ctx.mkRealConst("t" + i);

            ArithExpr rX = ctx.mkAdd(x, ctx.mkMul(t, vx));
            ArithExpr rY = ctx.mkAdd(y, ctx.mkMul(t, vy));
            ArithExpr rZ = ctx.mkAdd(z, ctx.mkMul(t, vz));

            ArithExpr lX = ctx.mkAdd(ctx.mkReal((long) p.getX()), ctx.mkMul(t, ctx.mkReal((long) v.getX())));
            ArithExpr lY = ctx.mkAdd(ctx.mkReal((long) p.getY()), ctx.mkMul(t, ctx.mkReal((long) v.getY())));
            ArithExpr lZ = ctx.mkAdd(ctx.mkReal((long) p.getZ()), ctx.mkMul(t, ctx.mkReal((long) v.getZ())));

            BoolExpr eqX = ctx.mkEq(lX, rX);
            BoolExpr eqY = ctx.mkEq(lY, rY);
            BoolExpr eqZ = ctx.mkEq(lZ, rZ);

            solver.add(eqX);
            solver.add(eqY);
            solver.add(eqZ);
        }

        // Checking satisfiability and solving the equations
        Status status = solver.check();
        if (status == Status.SATISFIABLE) {
            Model model = solver.getModel();
            String sol1 = "" + model.eval(x, false);
            String sol2 = "" + model.eval(y, false);
            String sol3 = "" + model.eval(z, false);
            return Long.parseLong(sol1) + Long.parseLong(sol2) + Long.parseLong(sol3);
        } else {
            throw new RuntimeException("The equations have no solution or are unsatisfiable.");
        }
    }

    private boolean collision(Hail h1, Hail h2, Range<Long> roi) {
        Vector3D p1 = h1.getPosition();
        Vector3D v1 = h1.getVelocity();
        Vector3D p2 = h2.getPosition();
        Vector3D v2 = h2.getVelocity();
        RealMatrix coefficients = MatrixUtils.createRealMatrix(new double[][] {
                { v1.getX(), -v2.getX() },
                { v1.getY(), -v2.getY() }
        });
        RealVector constants = MatrixUtils.createRealVector(new double[] {
                p2.getX() - p1.getX(), p2.getY() - p1.getY()
        });
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        try {
            RealVector solution = solver.solve(constants);
            double left = p1.getZ() + solution.getEntry(0) * v1.getZ();
            double right = p2.getZ() + solution.getEntry(1) * v2.getZ();
            if (Math.abs(left - right) > 0.00001) {
                return false;
            }
            double x = p1.getX() + solution.getEntry(0) * v1.getX();
            double y = p1.getY() + solution.getEntry(0) * v1.getY();
            double z = p1.getZ() + solution.getEntry(0) * v1.getZ();
            Vector3D collision = new Vector3D(x, y, z);
            boolean insideRoi = roi.contains((long) collision.getX())
                    && roi.contains((long) collision.getY());
            if (!insideRoi) {
                return false;
            }
            return !(solution.getEntry(0) < 0) && !(solution.getEntry(1) < 0);
        } catch (SingularMatrixException e) {
            return false;
        }
    }

    private List<Hail> parse(boolean isPartA) {
        List<Hail> hails = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split("@");
            List<Long> p = Arrays.stream(tokens[0].replace(" ", "").split(","))
                    .map(Long::parseLong).collect(Collectors.toList());
            List<Long> v = Arrays.stream(tokens[1].replace(" ", "").split(","))
                    .map(Long::parseLong).collect(Collectors.toList());
            hails.add(
                    new Hail(
                            new Vector3D(p.get(0), p.get(1), isPartA ? 0 : p.get(2)),
                            new Vector3D(v.get(0), v.get(1), isPartA ? 0 : v.get(2))));
        }
        return hails;
    }

    @Value
    private static class Hail {
        private Vector3D position;
        private Vector3D velocity;
    }

}
