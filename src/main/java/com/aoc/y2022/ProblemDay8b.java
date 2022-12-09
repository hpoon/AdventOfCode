package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay8b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
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
                stream -> stream.mapToInt(Matrix.Element::getValue).max().orElse(-1));
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day8.txt"));
        return scanner;
    }

    private int countLineOfSight(Matrix<Integer> grid,
                                 Matrix.Element<Integer> element,
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
