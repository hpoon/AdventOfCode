package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProblemDay8 extends ProblemDay<Long, Integer> {

    public Long solveA() {
        Matrix<Integer> grid = new Matrix<>(
                scanner,
                line -> IntStream
                        .range(0, line.length())
                        .boxed()
                        .map(line::charAt)
                        .map(Character::getNumericValue)
                        .collect(Collectors.toList()));

        Matrix<Integer> leftMaxes = grid.apply(p -> IntStream.range(0, p.getCol())
                .map(col -> grid.get(p.getRow(), col))
                .max()
                .orElse(-1));
        Matrix<Integer> rightMaxes = grid.apply(p -> IntStream.range(p.getCol() + 1, grid.width(p.getRow()))
                .map(col -> grid.get(p.getRow(), col))
                .max()
                .orElse(-1));
        Matrix<Integer> topMaxes = grid.apply(p -> IntStream.range(0, p.getRow())
                .map(row -> grid.get(row, p.getCol()))
                .max()
                .orElse(-1));
        Matrix<Integer> bottomMaxes = grid.apply(p -> IntStream.range(p.getRow() + 1, grid.height())
                .map(row -> grid.get(row, p.getCol()))
                .max()
                .orElse(-1));
        Matrix<Integer> smallestMax = leftMaxes
                .apply(p -> Math.min(p.getValue(), rightMaxes.get(p)))
                .apply(p -> Math.min(p.getValue(), topMaxes.get(p)))
                .apply(p -> Math.min(p.getValue(), bottomMaxes.get(p)));
        return grid.score(
                p -> grid.get(p) > smallestMax.get(p),
                Stream::count);
    }

    @Override
    public Integer solveB() {
        Matrix<Integer> grid = new Matrix<>(
                scanner,
                line -> IntStream
                        .range(0, line.length())
                        .boxed()
                        .map(line::charAt)
                        .map(Character::getNumericValue)
                        .collect(Collectors.toList()));

        Matrix<Integer> leftCount = grid.apply(p -> countLineOfSight(
                grid, p, p.getCol() - 1, 0, true, true));
        Matrix<Integer> rightCount = grid.apply(p -> countLineOfSight(
                grid, p, p.getCol() + 1, grid.width(p.getRow()), false, true));
        Matrix<Integer> topCount = grid.apply(p -> countLineOfSight(
                grid, p, p.getRow() - 1, 0, true, false));
        Matrix<Integer> bottomCount = grid.apply(p -> countLineOfSight(
                grid, p, p.getRow() + 1, grid.height(), false, false));
        Matrix<Integer> score = leftCount
                .apply(p -> p.getValue() * rightCount.get(p))
                .apply(p -> p.getValue() * topCount.get(p))
                .apply(p -> p.getValue() * bottomCount.get(p));
        return score.score(
                p -> true,
                stream -> stream.mapToInt(MatrixElement::getValue).max().orElse(-1));
    }


    private int countLineOfSight(Matrix<Integer> grid,
                                 MatrixElement<Integer> element,
                                 int start,
                                 int end,
                                 boolean reverse,
                                 boolean isHorizontal) {
        int count = 0;
        int step = reverse ? -1 : 1;
        if (start < 0 || end < 0) {
            return 0;
        }
        if (isHorizontal && (start > grid.width(element.getRow()) || end > grid.width(element.getRow()))) {
            return 0;
        }
        if (!isHorizontal && (start > grid.height()) || end > grid.height()) {
            return 0;
        }
        for (int i = start; reverse ? i >= end : i < end; i += step) {
            count++;
            if (isHorizontal && grid.get(element) <= grid.get(element.getRow(), i)) {
                break;
            } else if (!isHorizontal && grid.get(element) <= grid.get(i, element.getCol())) {
                break;
            }
        }
        return count;
    }

}
