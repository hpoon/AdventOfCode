package com.aoc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CoordinateSystem3D<T> {

    private final Map<Point3D, T> space;

    public CoordinateSystem3D(Scanner scanner, Function<String, Map<Point3D, T>> rowParser) {
        space = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            space.putAll(rowParser.apply(line));
        }
    }

    public CoordinateSystem3D() {
        space = new HashMap<>();
    }

    public void put(Point3D point, T value) {
        space.put(point, value);
    }

    public T get(int x, int y, int z) {
        return space.getOrDefault(new Point3D(x, y, z), null);
    }

    public boolean contains(Point3D point) {
        return space.containsKey(point);
    }

    public Optional<Integer> zMax(Point2D point) {
        return space.keySet().stream()
                .filter(p -> p.getX() == point.getX() && p.getY() == point.getY())
                .map(Point3D::getZ)
                .max(Comparator.naturalOrder());
    }

//    public Range<Integer> xRange() {
//        int min = matrix.keySet().stream().map(Point3D::getX).min(Comparator.naturalOrder()).orElseThrow();
//        int max = matrix.keySet().stream().map(Point3D::getX).max(Comparator.naturalOrder()).orElseThrow();
//        return Range.closed(min, max);
//    }
//
//    public Range<Integer> yRange() {
//        int min = matrix.keySet().stream().map(Point3D::getY).min(Comparator.naturalOrder()).orElseThrow();
//        int max = matrix.keySet().stream().map(Point3D::getY).max(Comparator.naturalOrder()).orElseThrow();
//        return Range.closed(min, max);
//    }
//
//    public Range<Integer> zRange() {
//        int min = matrix.keySet().stream().map(Point3D::getZ).min(Comparator.naturalOrder()).orElseThrow();
//        int max = matrix.keySet().stream().map(Point3D::getZ).max(Comparator.naturalOrder()).orElseThrow();
//        return Range.closed(min, max);
//    }
//
//    public int xLength() {
//        Range<Integer> range = xRange();
//        return range.upperEndpoint() - range.lowerEndpoint() + 1;
//    }
//
//    public int yLength() {
//        Range<Integer> range = yRange();
//        return range.upperEndpoint() - range.lowerEndpoint() + 1;
//    }
//
//    public int zLength() {
//        Range<Integer> range = zRange();
//        return range.upperEndpoint() - range.lowerEndpoint() + 1;
//    }

//    public Set<Point3D> getElements() {
//        return matrix.keySet();
//    }

//    public void replace(MatrixElement<T> oldElement, MatrixElement<T> newElement) {
//        matrix.remove(oldElement);
//        matrix.put(newElement, newElement.getValue());
//    }

//    public void print(String blank) {
//        for (int row = 0; row < yLength(); row++) {
//            for (int col = 0; col < xLength(); col++) {
//                System.out.print(get(row, col) != null ? get(row, col) : blank);
//            }
//            System.out.print("\n");
//        }
//        System.out.print("\n");
//    }

}
