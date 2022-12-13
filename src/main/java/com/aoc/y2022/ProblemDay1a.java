package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProblemDay1a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
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

}
