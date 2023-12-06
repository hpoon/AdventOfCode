package com.aoc.y2023;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay6 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Long> timeLimit = parseLineA();
        List<Long> records = parseLineA();
        long result = 1;
        for (long i = 0; i < timeLimit.size(); i++) {
            long time = timeLimit.get((int) i);
            long dist = records.get((int) i);
            long wins = 0;
            for (long t = 0; t < time; t++) {
                if (distance(time, t) > dist) {
                    wins++;
                }
            }
            result *= wins;
        }
        return result;
    }

    @Override
    public Long solveB() {
        long timeLimit = parseLineB();
        long record = parseLineB();
        long optimalTime = timeAtLongestDistance(timeLimit);
        long min = binarySearch(timeLimit, record, optimalTime, true);
        long max = binarySearch(timeLimit, record, optimalTime, false);
        return max - min + 1;
    }

    private long binarySearch(long timeLimit, long record, long optimalTime, boolean findMin) {
        long a = findMin ? 0 : optimalTime;
        long b = findMin ? optimalTime : timeLimit;
        long mid = (a + b) / 2;
        while (a < b) {
            long distanceAtMidMinus1 = distance(timeLimit, mid - 1);
            long distanceAtMid = distance(timeLimit, mid);
            long distanceAtMidPlus1 = distance(timeLimit, mid + 1);
            if (findMin) {
                if (distanceAtMidMinus1 > record && distanceAtMid > record && distanceAtMidPlus1 > record) {
                    b = mid;
                } else if (distanceAtMidMinus1 < record && distanceAtMid < record && distanceAtMidPlus1 < record) {
                    a = mid;
                } else if (distanceAtMidMinus1 < record && distanceAtMid > record) {
                    return mid;
                } else if (distanceAtMid < record && distanceAtMidMinus1 > record) {
                    return mid - 1;
                } else if (distanceAtMid < record && distanceAtMidPlus1 > record) {
                    return mid + 1;
                }
            } else {
                if (distanceAtMidMinus1 > record && distanceAtMid > record && distanceAtMidPlus1 > record) {
                    a = mid;
                } else if (distanceAtMidMinus1 < record && distanceAtMid < record && distanceAtMidPlus1 < record) {
                    b = mid;
                } else if (distanceAtMidMinus1 > record && distanceAtMid < record) {
                    return mid - 1;
                } else if (distanceAtMid > record && distanceAtMidPlus1 < record) {
                    return mid;
                } else if (distanceAtMid < record && distanceAtMidPlus1 > record) {
                    return mid + 1;
                }
            }
            mid = (a + b) / 2;
        }
        return mid;
    }

    private long distance(long maxTime, long timeHeld) {
        return (maxTime - timeHeld) * timeHeld;
    }

    private long timeAtLongestDistance(long maxTime) {
        return maxTime / 2;
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
