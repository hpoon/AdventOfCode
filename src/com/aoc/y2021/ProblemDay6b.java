package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay6b implements ProblemDay<Long> {

    private Scanner scanner;

    public Long solve() {
        final List<Integer> states = Arrays.stream(scanner.nextLine().split(",")).map(Integer::parseInt).collect(Collectors.toList());

        Map<Integer, Long> stateMap = states.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long fish = states.size();
        for (int i = 0; i < 256; i++) {
            final Map<Integer, Long> changes = new HashMap<>();
            for (final Map.Entry<Integer, Long> entry : stateMap.entrySet()) {
                final int state = entry.getKey();
                final long count = entry.getValue();
                if (state == 0) {
                    changes.put(8, changes.getOrDefault(8, 0L) + count);
                    changes.put(6, changes.getOrDefault(6, 0L) + count);
                    changes.put(0, changes.getOrDefault(0, 0L) - count);
                    fish += count;
                } else {
                    changes.put(state - 1, changes.getOrDefault(state - 1, 0L) + count);
                    changes.put(state, changes.getOrDefault(state, 0L) - count);
                }
            }

            for (final Map.Entry<Integer, Long> entry : changes.entrySet()) {
                stateMap.put(entry.getKey(), stateMap.getOrDefault(entry.getKey(), 0L) + entry.getValue());
            }
        }

        return fish;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day6.txt"));
        return scanner;
    }

}
