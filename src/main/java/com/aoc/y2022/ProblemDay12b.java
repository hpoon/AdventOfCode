package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay12b extends ProblemDay<Integer> {

    private static final Map<Integer, Integer> SPECIAL_CHARACTERS = ImmutableMap.of(
            (int) 'S', (int) 'a', (int) 'E', (int) 'z');

    @Override
    public Integer solve() {
        Function<String, List<Integer>> function = line -> line.chars().boxed().collect(Collectors.toList());
        Matrix<Integer> map = new Matrix<>(scanner, function);
        List<Matrix.Element<Integer>> starts = map.get((int)'a');
        Matrix.Element<Integer> end = map.get((int) 'E').get(0);
        int min = Integer.MAX_VALUE;
        for (Matrix.Element<Integer> start : starts) {
            List<Matrix.Element<Integer>> path = map.bfs(
                    start,
                    end,
                    this::isValidPath,
                    false);
            if (path.isEmpty()) {
                continue;
            }
            if (path.size() < min) {
                min = path.size();
            }
        }
        return min;
    }

    private boolean isValidPath(int v1, int v2) {
        v1 = SPECIAL_CHARACTERS.getOrDefault(v1, v1);
        v2 = SPECIAL_CHARACTERS.getOrDefault(v2, v2);
        return v2 - v1 <= 1;
    }

}
