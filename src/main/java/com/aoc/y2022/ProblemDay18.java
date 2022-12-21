package com.aoc.y2022;

import com.aoc.Point3D;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay18 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Set<Point3D> points = parse();
        return calculateSurfaceArea(points);
    }

    @Override
    public Integer solveB() {
        Set<Point3D> points = parse();
        int xMin = points.stream()
                .map(Point3D::getX)
                .min(Comparator.naturalOrder())
                .orElseThrow() - 1;
        int xMax = points.stream()
                .map(Point3D::getX)
                .max(Comparator.naturalOrder())
                .orElseThrow() + 1;
        int yMin = points.stream()
                .map(Point3D::getY)
                .min(Comparator.naturalOrder())
                .orElseThrow() - 1;
        int yMax = points.stream()
                .map(Point3D::getY)
                .max(Comparator.naturalOrder())
                .orElseThrow() + 1;
        int zMin = points.stream()
                .map(Point3D::getZ)
                .min(Comparator.naturalOrder())
                .orElseThrow() - 1;
        int zMax = points.stream()
                .map(Point3D::getZ)
                .max(Comparator.naturalOrder())
                .orElseThrow() + 1;

        Set<Point3D> set = new LinkedHashSet<>();
        Point3D start = new Point3D(xMin, yMin, zMin);
        set.add(start);
        Set<Point3D> traversableNegativeSpace = new HashSet<>();
        while (!set.isEmpty()) {
            Point3D point = set.iterator().next();
            set.remove(point);
            Set<Point3D> neighbours = getNeighbouringPoints(point);
            if (traversableNegativeSpace.contains(point)) {
                continue;
            }
            if (point.getX() < xMin || point.getX() > xMax) {
                continue;
            }
            if (point.getY() < yMin || point.getY() > yMax) {
                continue;
            }
            if (point.getZ() < zMin || point.getZ() > zMax) {
                continue;
            }
            traversableNegativeSpace.add(point);
            for (Point3D neighbour : neighbours) {
                if (points.contains(neighbour)) {
                    continue;
                }
                set.add(neighbour);
            }
        }

        return calculateSurfaceArea(traversableNegativeSpace)
                - 2 * (xMax - xMin + 1) * (yMax - yMin + 1)
                - 2 * (yMax - yMin + 1) * (zMax - zMin + 1)
                - 2 * (zMax - zMin + 1) * (xMax - xMin + 1);
    }

    private int calculateSurfaceArea(Set<Point3D> points) {
        int area = 0;
        for (Point3D point : points) {
            area += 6 - Sets.intersection(getNeighbouringPoints(point), points).size();
        }
        return area;
    }

    private Set<Point3D> getNeighbouringPoints(Point3D point) {
        return ImmutableSet.<Point3D>builder()
                .add(new Point3D(point.getX() + 1, point.getY(), point.getZ()))
                .add(new Point3D(point.getX() - 1, point.getY(), point.getZ()))
                .add(new Point3D(point.getX(), point.getY() + 1, point.getZ()))
                .add(new Point3D(point.getX(), point.getY() - 1, point.getZ()))
                .add(new Point3D(point.getX(), point.getY(), point.getZ() + 1))
                .add(new Point3D(point.getX(), point.getY(), point.getZ() - 1))
                .build();
    }

    private Set<Point3D> parse() {
        Set<Point3D> points = new HashSet<>();
        while (scanner.hasNextLine()) {
            List<Integer> pts = Arrays.stream(scanner.nextLine().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            points.add(new Point3D(pts.get(0), pts.get(1), pts.get(2)));
        }
        return points;
    }

}
