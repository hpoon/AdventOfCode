package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProblemDay10 extends ProblemDay<Integer, String> {

    @Override
    public Integer solveA() {
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

    @Override
    public String solveB() {
        int cycles = 0;
        int x = 1;
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String op = scanner.nextLine();
            if (op.startsWith("noop")) {
                cycles++;
                draw(sb, cycles, x);
            } else if (op.startsWith("addx")) {
                int value = Integer.parseInt(op.split(" ")[1]);
                cycles++;
                draw(sb, cycles, x);
                cycles++;
                draw(sb, cycles, x);
                x += value;
            }
        }
        return sb.toString();
    }

    private void draw(StringBuilder sb, int cycle, int x) {
        final int width = 40;
        sb.append(Range.closed(x-1, x+1).contains(cycle % width - 1) ? "#" : ".");
        if (cycle % width == 0) {
            sb.append("\n");
        }
    }

}
