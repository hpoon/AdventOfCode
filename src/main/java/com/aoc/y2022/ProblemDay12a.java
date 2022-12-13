package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay12a extends ProblemDay<Integer> {

    private static final Map<Integer, Integer> SPECIAL_CHARACTERS = ImmutableMap.of(
            (int) 'S', (int) 'a', (int) 'E', (int) 'z');

    @Override
    public Integer solve() {
        Function<String, List<Integer>> function = line -> line.chars().boxed().collect(Collectors.toList());
        Matrix<Integer> map = new Matrix<>(scanner, function);
        Matrix.Element<Integer> start = map.get((int)'S').get(0);
        Matrix.Element<Integer> end = map.get((int) 'E').get(0);
        List<Matrix.Element<Integer>> path = map.bfs(
                start,
                end,
                this::isValidPath,
                false);
        return path.size();
    }

    private boolean isValidPath(int v1, int v2) {
        v1 = SPECIAL_CHARACTERS.getOrDefault(v1, v1);
        v2 = SPECIAL_CHARACTERS.getOrDefault(v2, v2);
        return v2 - v1 <= 1;
    }

}
