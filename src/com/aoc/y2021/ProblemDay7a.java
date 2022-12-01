package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProblemDay7a implements ProblemDay<Long> {

    private Scanner scanner;

    public Long solve() {
        final List<Integer> positions = Arrays.stream(scanner.nextLine().split(","))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
        final long median = positions.get(positions.size() / 2);
        return positions.stream().map(pos -> Math.abs(pos - median)).reduce(0L, Long::sum);
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day7.txt"));
        return scanner;
    }

}
