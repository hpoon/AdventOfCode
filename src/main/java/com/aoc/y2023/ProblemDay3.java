package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay3 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        return solve(
                false,
                adjacents -> adjacents
                        .values()
                        .stream()
                        .map(v -> v
                                .stream()
                                .reduce(Integer::sum)
                                .orElseThrow(() -> new RuntimeException("Shouldn't happen")))
                        .reduce(Integer::sum)
                        .orElseThrow(() -> new RuntimeException("Shouldn't happen")));
    }

    @Override
    public Integer solveB() {
        return solve(
                true,
                gears -> gears.values().stream()
                .filter(integers -> CollectionUtils.size(integers) == 2)
                .map(integers -> integers
                        .stream()
                        .reduce((v1, v2) -> v1 * v2)
                        .orElseThrow(() -> new RuntimeException("Shouldn't happen")))
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen")));
    }

    private int solve(boolean gearsOnly,
                      Function<Map<MatrixElement<String>, List<Integer>>, Integer> scoringFunction) {
        Matrix<String> matrix = parse();
        Map<MatrixElement<String>, List<Integer>> gears = new HashMap<>();
        for (int row = 0; row < matrix.height(); row++) {
            List<MatrixElement<String>> elements = new ArrayList<>();
            for (int col = 0; col < matrix.width(row); col++) {
                String value = matrix.get(row, col);
                if (isCellANumber(value)) {
                    elements.add(new MatrixElement<>(row, col, value));
                } else {
                    if (elements.isEmpty()) {
                        continue;
                    }
                    int num = Integer.parseInt(elements
                            .stream()
                            .map(MatrixElement::getValue)
                            .reduce((s1, s2) -> s1 + s2).orElse("0"));
                    List<Point2D> cells = ImmutableList.<Point2D>builder()
                            .add(new Point2D(-1, -1))
                            .add(new Point2D(0, -1))
                            .add(new Point2D(1, -1))
                            .add(new Point2D(-1, 0))
                            .add(new Point2D(1, 0))
                            .add(new Point2D(-1, 1))
                            .add(new Point2D(0, 1))
                            .add(new Point2D(1, 1))
                            .build();
                    List<MatrixElement<String>> finalElements = elements;
                    Set<MatrixElement<String>> adjacents = elements.stream()
                            .flatMap(e -> {
                                int r = e.getRow();
                                int c = e.getCol();
                                return cells.stream()
                                        .filter(p -> {
                                            int adjRow = r + p.getY();
                                            int adjCol = c + p.getX();
                                            return matrix.withinBounds(adjRow, adjCol);
                                        })
                                        .map(p -> {
                                            int adjRow = r + p.getY();
                                            int adjCol = c + p.getX();
                                            String adjVal = matrix.get(adjRow, adjCol);
                                            return new MatrixElement<>(adjRow, adjCol, adjVal);
                                        });
                            })
                            .filter(e -> !finalElements.contains(e))
                            .collect(Collectors.toSet());
                    List<MatrixElement<String>> adjacentGears = adjacents.stream()
                            .filter(e -> !isCellANumber(e.getValue()))
                            .filter(e -> gearsOnly ? "*".equals(e.getValue()) : !".".equals(e.getValue()))
                            .collect(Collectors.toList());
                    adjacentGears.forEach(gear -> {
                        List<Integer> part = gears.getOrDefault(gear, new ArrayList<>());
                        part.add(num);
                        gears.put(gear, part);
                    });
                    elements = new ArrayList<>();
                }
            }
        }
        return scoringFunction.apply(gears);
    }

    private Matrix<String> parse() {
        Matrix<String> matrix = new Matrix<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            matrix.addRow(line.chars().mapToObj(c -> String.format("%c", c)).collect(Collectors.toList()));
        }
        return matrix;
    }

    private boolean isCellANumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
