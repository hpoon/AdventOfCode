package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.util.*;

public class ProblemDay1b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final Queue<Integer> elves = new PriorityQueue<>(Comparator.reverseOrder());
        int sum = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (Util.isBlank(line)) {
                elves.add(sum);
                sum = 0;
            } else {
                final int cals = Integer.parseInt(line);
                sum += cals;
            }
        }
        return elves.size() >= 3 ? elves.poll() + elves.poll() + elves.poll() : 0;
    }

}
