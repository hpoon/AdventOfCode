package com.aoc.y2023;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay6 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Long> timeLimits = parseLineA();
        List<Long> records = parseLineA();
        long result = 1;
        for (long i = 0; i < timeLimits.size(); i++) {
            Pair<Long, Long> roots = roots(-1, timeLimits.get((int) i), -records.get((int) i));
            result *= roots.getValue() - roots.getKey() + 1;
        }
        return result;
    }

    @Override
    public Long solveB() {
        long timeLimit = parseLineB();
        long record = parseLineB();
        Pair<Long, Long> roots = roots(-1, timeLimit, -record);
        return roots.getValue() - roots.getKey() + 1;
    }

    private Pair<Long, Long> roots(long a, long b, long c) {
        double discriminant = Math.sqrt(Math.pow(b, 2.0) - 4 * a * c);
        double zero1 = (-b - discriminant) / (2 * a);
        double zero2 = (-b + discriminant) / (2 * a);
        return ImmutablePair.of((long) Math.ceil(Math.min(zero1, zero2)), (long) Math.floor(Math.max(zero1, zero2)));
    }

    private List<Long> parseLineA() {
        return Arrays.stream(
                scanner.nextLine()
                        .replace("Time:      ", "")
                        .replace("Distance:  ", "")
                        .split(" "))
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private long parseLineB() {
        return Long.parseLong(scanner.nextLine()
                .replace("Time:      ", "")
                .replace("Distance:  ", "")
                .replace(" ", ""));
    }

}
