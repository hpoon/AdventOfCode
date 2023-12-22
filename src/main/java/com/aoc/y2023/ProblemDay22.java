package com.aoc.y2023;

import com.aoc.Point2D;
import com.aoc.Point3D;
import com.aoc.ProblemDay;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay22 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Brick> bricks = parse();
        return solveA(bricks);
    }

    @Override
    public Long solveB() {
        List<Brick> bricks = parse();
        return solveB(bricks);
    }

    private long solveA(List<Brick> bricks) {
        List<Brick> placed = placeBricks(bricks, null);
        long disintegratable = 0;
        for (Brick toRemove : placed) {
            Set<Brick> newPlaced = new HashSet<>(placeBricks(placed, toRemove));
            disintegratable += placed.stream()
                    .filter(b -> !b.equals(toRemove))
                    .collect(Collectors.toSet()).equals(newPlaced)
                    ? 1 : 0;
        }
        return disintegratable;
    }

    private long solveB(List<Brick> bricks) {
        List<Brick> placed = placeBricks(bricks, null);
        long bricksRemoved = 0;
        for (Brick toRemove : placed) {
            Set<Brick> newPlaced = new HashSet<>(placeBricks(placed, toRemove));
            Set<Brick> intersection = Sets.intersection(
                    placed.stream()
                            .filter(b -> !b.equals(toRemove))
                            .collect(Collectors.toSet()),
                    newPlaced);
            bricksRemoved += newPlaced.size() - intersection.size();
        }
        return bricksRemoved;
    }

    private List<Brick> placeBricks(List<Brick> bricks, Brick ignore) {
        Map<Point2D, Integer> heights = new HashMap<>();
        List<Brick> placed = new ArrayList<>();
        for (Brick brick : bricks) {
            if (brick.equals(ignore)) {
                continue;
            }
            placed.add(drop(brick, heights));
        }
        return placed;
    }

    private Brick drop(Brick brick, Map<Point2D, Integer> heights) {
        Brick prevPlacement = null;
        Brick placement = brick.copy();
        while (!intersects(placement, heights)) {
            prevPlacement = placement.copy();
            placement = placement.drop();
        }
        prevPlacement.getCubes().forEach(c -> heights.put(new Point2D(c.getX(), c.getY()), c.getZ()));
        return prevPlacement;
    }

    private boolean intersects(Brick brick, Map<Point2D, Integer> heights) {
        for (Point3D cube : brick.getCubes()) {
            Point2D xy = new Point2D(cube.getX(), cube.getY());
            int height = heights.getOrDefault(xy, 0);
            if (cube.getZ() <= height) {
                return true;
            }
        }
        return false;
    }

    private List<Brick> parse() {
        List<Brick> bricks = new ArrayList<>();
        int i = 0;
        while (scanner.hasNextLine()) {
            List<Integer> tokens = Arrays.stream(scanner.nextLine().replace("~", ",").split(","))
                    .map(Integer::parseInt).collect(Collectors.toList());
            Point3D p1 = new Point3D(tokens.get(0), tokens.get(1), tokens.get(2));
            Point3D p2 = new Point3D(tokens.get(3), tokens.get(4), tokens.get(5));
            List<Point3D> cubes = new ArrayList<>();
            for (int x = p1.getX(); x <= p2.getX(); x++) {
                for (int y = p1.getY(); y <= p2.getY(); y++) {
                    for (int z = p1.getZ(); z <= p2.getZ(); z++) {
                        cubes.add(new Point3D(x, y, z));
                    }
                }
            }
            bricks.add(new Brick(i, cubes));
            i++;
        }
        return bricks.stream()
                .sorted(Comparator.comparing(b -> b.getCubes().get(0).getZ()))
                .collect(Collectors.toList());
    }

    @Value
    @EqualsAndHashCode
    private static class Brick {

        private int label;

        private List<Point3D> cubes;

        public Brick copy() {
            return new Brick(
                    label,
                    cubes.stream()
                            .map(p -> new Point3D(p.getX(), p.getY(), p.getZ()))
                            .collect(Collectors.toList()));
        }

        public Brick drop() {
            return new Brick(
                    label,
                    cubes.stream()
                            .map(p -> new Point3D(p.getX(), p.getY(), p.getZ() - 1))
                            .collect(Collectors.toList()));
        }

    }

}
