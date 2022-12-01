package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ProblemDay7b implements ProblemDay<Long> {

    private Scanner scanner;

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

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day7.txt"));
        return scanner;
    }

}
