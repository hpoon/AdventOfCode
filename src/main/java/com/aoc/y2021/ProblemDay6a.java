package com.aoc.y2021;

import com.aoc.ProblemDay;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay6a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final List<Integer> states = Arrays.stream(scanner.nextLine().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for (int i = 0; i < 80; i++) {
            final int length = states.size();
            for (int j = 0; j < length; j++) {
                final int state = states.get(j);
                if (state == 0) {
                    states.add(8);
                    states.set(j, 6);
                } else {
                    states.set(j, state - 1);
                }
            }
        }

        return states.size();
    }

}
