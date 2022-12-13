package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProblemDay10a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        int cycles = 0;
        int x = 1;
        Map<Integer, Integer> cycleToX = new HashMap<>();
        while (scanner.hasNextLine()) {
            String op = scanner.nextLine();
            if (op.startsWith("noop")) {
                cycles++;
                cycleToX.put(cycles, x);
            } else if (op.startsWith("addx")) {
                int value = Integer.parseInt(op.split(" ")[1]);
                cycles++;
                cycleToX.put(cycles, x);
                cycles++;
                cycleToX.put(cycles, x);
                x += value;
            }
        }
        for (int i = 1; i <= 220; i++) {
            if (!cycleToX.containsKey(i)) {
                cycleToX.put(i, cycleToX.get(i-1));
            }
        }
        Set<Integer> scoring = ImmutableSet.of(20, 60, 100, 140, 180, 220);
        return scoring.stream().mapToInt(cycle -> cycle * cycleToX.get(cycle)).sum();
    }

}
