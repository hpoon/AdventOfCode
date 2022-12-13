package com.aoc.y2021;

import com.aoc.ProblemDay;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ProblemDay7b extends ProblemDay<Long> {

    public Long solve() {
        final List<Integer> positions = Arrays.stream(scanner.nextLine().split(","))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        final long max = positions.stream().max(Long::compare).orElseThrow(() -> new RuntimeException("bad input"));
        final List<Long> test = LongStream.range(0, max).boxed().collect(Collectors.toList());

        final List<Long> costs = test.stream()
                .map(m -> positions.stream()
                        .map(pos -> Math.abs(pos - m) * (1 + Math.abs(pos - m)) / 2)
                        .reduce(0L, Long::sum))
                .collect(Collectors.toList());

        return costs.stream().min(Long::compare).orElse(0L);
    }

}
