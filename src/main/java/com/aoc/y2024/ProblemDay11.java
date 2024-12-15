package com.aoc.y2024;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProblemDay11 extends ProblemDay<Integer, Long> {

    @Override
    public Integer solveA() {
        List<Long> stones = parse();
        return blinkSlow(stones, 25);
    }

    @Override
    public Long solveB() {
        List<Long> stones = parse();
        return blinkFast(stones, 75);
    }

    private long blinkFast(List<Long> stones, int iterations) {
        Map<Long, Long> stonesToFrequencies = count(stones);
        for (int i = 0; i < iterations; i++) {
            Map<Long, Long> nextStonesToFrequencies = new HashMap<>();
            for (Map.Entry<Long, Long> entry : stonesToFrequencies.entrySet()) {
                long stone = entry.getKey();
                long frequency = entry.getValue();
                List<Long> nextStones = blink(stone);
                for (long nextStone : nextStones) {
                    nextStonesToFrequencies.put(
                            nextStone,
                            nextStonesToFrequencies.getOrDefault(nextStone, 0L) + frequency);
                }
            }
            stonesToFrequencies = nextStonesToFrequencies;
        }
        return stonesToFrequencies.values()
                .stream()
                .reduce(Math::addExact)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private int blinkSlow(List<Long> stones, int iterations) {
        for (int i = 0; i < iterations; i++) {
            stones = stones.stream()
                    .flatMap(stone -> blink(stone).stream())
                    .collect(Collectors.toList());
        }
        return stones.size();
    }

    private List<Long> blink(long stone) {
        if (stone == 0) {
            return ImmutableList.of(1L);
        }
        String stoneStr = String.valueOf(stone);
        if (stoneStr.length() % 2 == 0) {
            long v1 = Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2));
            long v2 = Long.parseLong(stoneStr.substring(stoneStr.length() / 2));
            return ImmutableList.of(v1, v2);
        }
        return ImmutableList.of(Math.multiplyExact(stone, 2024L));
    }

    private Map<Long, Long> count(List<Long> stones) {
        Map<Long, Long> frequencies = new HashMap<>();
        for (long stone : stones) {
            frequencies.put(stone, frequencies.getOrDefault(stone, 0L) + 1);
        }
        return frequencies;
    }

    private List<Long> parse() {
        return Arrays.stream(scanner.nextLine().split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

}
