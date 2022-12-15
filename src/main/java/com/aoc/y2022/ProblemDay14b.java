package com.aoc.y2022;

import com.aoc.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay14b extends ProblemDay<Integer> {

    private static final MatrixElement<Character> SOURCE = new MatrixElement(0, 500, 'o');

    @Override
    public Integer solve() {
        SparseMatrix<Character> grid = new SparseMatrix<>(scanner, this::parseLine);
        int boundary = grid.yRange().upperEndpoint() + 2;
        int grains = 0;
        while (addGrainOfSand(grid, boundary)) {
            grains++;
        }
        return grains;
    }

    private boolean addGrainOfSand(SparseMatrix<Character> grid, int boundary) {
        MatrixElement<Character> pos = new MatrixElement<>(SOURCE);
        if (grid.contains(pos)) {
            return false;
        }
        while (true) {
            grid.put(new MatrixElement<>(boundary, pos.getCol(), '#'));
            grid.put(new MatrixElement<>(boundary, pos.getCol()-1, '#'));
            grid.put(new MatrixElement<>(boundary, pos.getCol()+1, '#'));
            MatrixElement<Character> down = new MatrixElement<>(pos.getRow() + 1, pos.getCol(), pos.getValue());
            MatrixElement<Character> left = new MatrixElement<>(pos.getRow() + 1, pos.getCol() - 1, pos.getValue());
            MatrixElement<Character> right = new MatrixElement<>(pos.getRow() + 1, pos.getCol() + 1, pos.getValue());
            if (grid.contains(down)) {
                if (grid.contains(left)) {
                    if (grid.contains(right)) {
                        grid.put(pos);
                        return true;
                    }
                    pos = right;
                    continue;
                }
                pos = left;
                continue;
            }
            pos = down;
        }
    }

    private List<MatrixElement<Character>> parseLine(String str) {
        List<Line2D> lines = new ArrayList<>();
        List<Point2D> points = Arrays.stream(str.split(" -> "))
                .map(s -> {
                    List<Integer> xy = Arrays.stream(s.split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    return new Point2D(xy.get(0), xy.get(1));
                })
                .collect(Collectors.toList());
        for (int i = 0; i < points.size() - 1; i++) {
            Point2D p1 = points.get(i);
            Point2D p2 = points.get(i+1);
            lines.add(new Line2D(p1, p2));
        }
        List<MatrixElement<Character>> elements = new ArrayList<>();
        lines.forEach(l -> {
            int minX = Math.min(l.getP1().getX(), l.getP2().getX());
            int maxX = Math.max(l.getP1().getX(), l.getP2().getX());
            int minY = Math.min(l.getP1().getY(), l.getP2().getY());
            int maxY = Math.max(l.getP1().getY(), l.getP2().getY());
            switch (l.direction()) {
                case HORIZONTAL:
                    IntStream.rangeClosed(minX, maxX)
                            .boxed()
                            .map(x -> new MatrixElement<>(l.getP1().getY(), x, '#'))
                            .forEach(elements::add);
                    break;
                case VERTICAL:
                    IntStream.rangeClosed(minY, maxY)
                            .boxed()
                            .map(y -> new MatrixElement<>(y, l.getP1().getX(), '#'))
                            .forEach(elements::add);
                    break;
                default: throw new RuntimeException("Invalid line direction");
            }
        });
        return elements;
    }

}
