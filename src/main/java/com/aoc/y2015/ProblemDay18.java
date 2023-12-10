package com.aoc.y2015;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay18 extends ProblemDay<Integer, Integer> {

    private static final int STEPS = 100;

    @Override
    protected Integer solveA() {
        Matrix<Boolean> input = parse();
        for (int i = 0; i < STEPS; i++) {
            Matrix<Boolean> finalInput = input;
            Function<MatrixElement<Boolean>, Boolean> kernel = element -> {
                int activatedNeighbours = activatedNeighbours(finalInput, element);
                // A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
                // A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
                if (Boolean.TRUE.equals(element.getValue())) {
                    return activatedNeighbours == 2 || activatedNeighbours == 3;
                } else {
                    return activatedNeighbours == 3;
                }
            };
            input = input.apply(kernel);
        }
        return input.score(
                Objects::nonNull,
                stream -> stream
                        .map(MatrixElement::getValue)
                        .filter(Objects::nonNull)
                        .map(b -> b ? 1 : 0)
                        .reduce(Integer::sum)
                        .orElse(0));
    }

    @Override
    protected Integer solveB() {
        Matrix<Boolean> input = parse();
        input.set(0, 0, true);
        input.set(0, input.width(0) - 1, true);
        input.set(input.height() - 1, 0, true);
        input.set(input.height() - 1, input.width(0) - 1, true);
        for (int i = 0; i < STEPS; i++) {
            Matrix<Boolean> finalInput = input;
            Function<MatrixElement<Boolean>, Boolean> kernel = element -> {
                if (element.getRow() == 0 && element.getCol() == 0) {
                    return true;
                }
                if (element.getRow() == 0 && element.getCol() == finalInput.width(0) - 1) {
                    return true;
                }
                if (element.getRow() == finalInput.height() - 1 && element.getCol() == 0) {
                    return true;
                }
                if (element.getRow() == finalInput.height() - 1 && element.getCol() == finalInput.width(0) - 1) {
                    return true;
                }
                int activatedNeighbours = activatedNeighbours(finalInput, element);
                // A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
                // A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
                if (Boolean.TRUE.equals(element.getValue())) {
                    return activatedNeighbours == 2 || activatedNeighbours == 3;
                } else {
                    return activatedNeighbours == 3;
                }
            };
            input = input.apply(kernel);
        }
        return input.score(
                Objects::nonNull,
                stream -> stream
                        .map(MatrixElement::getValue)
                        .filter(Objects::nonNull)
                        .map(b -> b ? 1 : 0)
                        .reduce(Integer::sum)
                        .orElse(0));
    }

    private int activatedNeighbours(Matrix<Boolean> grid, MatrixElement<Boolean> element) {
        int count = 0;
        int row = element.getRow();
        int col = element.getCol();
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (c < 0 || r < 0 || r >= grid.height() || c >= grid.width(r)) {
                    continue;
                }
                if (c == col && r == row) {
                    continue;
                }
                count += Boolean.TRUE.equals(grid.getValue(r, c)) ? 1 : 0;
            }
        }
        return count;
    }

    private Matrix<Boolean> parse() {
        Function<String, List<Boolean>> function = line -> line.chars().boxed()
                .map(c -> c == '#')
                .collect(Collectors.toList());
        return new Matrix<>(scanner, function);
    }

}
