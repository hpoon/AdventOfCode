package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay13 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<Matrix<String>> matrices = parse();
        int score = 0;
        for (Matrix<String> matrix : matrices) {
            List<Integer> horizontal = reflections(matrix);
            List<Integer> vertical = reflections(matrix.transpose());
            score += horizontal.stream().map(r -> r * 100).reduce(Integer::sum).orElse(0)
                    + vertical.stream().reduce(Integer::sum).orElse(0);
        }
        return score;
    }

    @Override
    public Integer solveB() {
        List<Matrix<String>> matrices = parse();
        int score = 0;
        for (Matrix<String> matrix : matrices) {
            int copyScore = 0;
            List<Integer> horizontalOriginal = reflections(matrix);
            List<Integer> verticalOriginal = reflections(matrix.transpose());
            for (int row = 0; row < matrix.height(); row++) {
                for (int col = 0; col < matrix.width(row); col++) {
                    Matrix<String> copy = matrix.copy();
                    copy.set(row, col, Objects.equals(copy.getValue(row, col), "#") ? "." : "#");
                    List<Integer> horizontalNew = reflections(copy)
                            .stream()
                            .filter(r -> !horizontalOriginal.contains(r))
                            .collect(Collectors.toList());
                    List<Integer> verticalNew = reflections(copy.transpose())
                            .stream()
                            .filter(r -> !verticalOriginal.contains(r))
                            .collect(Collectors.toList());
                    copyScore = horizontalNew.stream().map(r -> r * 100).reduce(Integer::sum).orElse(0)
                            + verticalNew.stream().reduce(Integer::sum).orElse(0);
                    if (copyScore > 0) {
                        break;
                    }
                }
                if (copyScore > 0) {
                    score += copyScore;
                    break;
                }
            }
        }
        return score;
    }

    private List<Integer> reflections(Matrix<String> matrix) {
        Map<Set<Integer>, Boolean> equals = new HashMap<>();
        for (int r1 = 0; r1 < matrix.height(); r1++) {
            for (int r2 = 0; r2 < matrix.height(); r2++) {
                if (r1 == r2) {
                    continue;
                }
                if (equals.containsKey(ImmutableSet.of(r1, r2))) {
                    continue;
                }
                int finalR1 = r1;
                List<String> row1 = IntStream.range(0, matrix.width(r1))
                        .boxed()
                        .map(c -> matrix.getValue(finalR1, c))
                        .collect(Collectors.toList());
                int finalR2 = r2;
                List<String> row2 = IntStream.range(0, matrix.width(r1))
                        .boxed()
                        .map(c -> matrix.getValue(finalR2, c))
                        .collect(Collectors.toList());
                equals.put(ImmutableSet.of(r1, r2), row1.equals(row2));
            }
        }

        List<Integer> rowMatches = new ArrayList<>();
        for (int row = 0; row < matrix.height() - 1; row++) {
            int top = row;
            int bottom = row + 1;
            boolean matches = true;
            while (top >= 0 && bottom < matrix.height()) {
                if (!equals.getOrDefault(ImmutableSet.of(top, bottom), false)) {
                    matches = false;
                    break;
                }
                top--;
                bottom++;
            }
            if (matches) {
                rowMatches.add(row + 1);
            }
        }
        return rowMatches;
    }

    private List<Matrix<String>> parse() {
        List<Matrix<String>> matrices = new ArrayList<>();
        while (scanner.hasNextLine()) {
            Matrix<String> matrix = new Matrix<>();
            String str;
            while (scanner.hasNextLine()) {
                str = scanner.nextLine();
                if (StringUtils.isBlank(str)) {
                    break;
                }
                matrix.addRow(str.chars().mapToObj(Character::toString).collect(Collectors.toList()));
            }
            matrices.add(matrix);
        }
        return matrices;
    }

}
