package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProblemDay8a implements ProblemDay<Long> {

    private Scanner scanner;

    public Long solve() {
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
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day8.txt"));
        return scanner;
    }

}
