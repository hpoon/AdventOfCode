package com.aoc.y2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay7a extends ProblemDay<Long> {

    public Long solve() {
        final List<Integer> positions = Arrays.stream(scanner.nextLine().split(","))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
        final long median = positions.get(positions.size() / 2);
        return positions.stream().map(pos -> Math.abs(pos - median)).reduce(0L, Long::sum);
    }

}
