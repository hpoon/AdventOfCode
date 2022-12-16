package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.util.*;

public class ProblemDay1 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        final List<Integer> elves = new ArrayList<>();
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
        return elves.stream().max(Comparator.naturalOrder()).orElse(0);
    }

    @Override
    public Integer solveB() {
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
