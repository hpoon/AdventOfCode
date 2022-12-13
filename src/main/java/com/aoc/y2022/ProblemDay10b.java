package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.Range;

public class ProblemDay10b extends ProblemDay<String> {

    public String solve() {
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
