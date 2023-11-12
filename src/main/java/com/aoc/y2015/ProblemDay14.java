package com.aoc.y2015;

import com.aoc.ProblemDay;
import lombok.Value;

import java.util.*;

public class ProblemDay14 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Reindeer> input = parse();
        return solveA(input, 2503);
    }

    @Override
    protected Integer solveB() {
        List<Reindeer> input = parse();
        return solveB(input, 2503);
    }

    private int solveA(List<Reindeer> reindeer, int seconds) {
        int max = 0;
        for (Reindeer r : reindeer) {
            max = Math.max(max, scoreA(r, seconds));
        }
        return max;
    }

    private int scoreA(Reindeer reindeer, int seconds) {
        int cycleTime = reindeer.getRestTime() + reindeer.getSpeedTime();
        int multiples = seconds / cycleTime;
        int remainder = seconds % cycleTime;
        return reindeer.getSpeed() * reindeer.getSpeedTime() * multiples +
                Math.min(remainder, reindeer.getSpeedTime()) * reindeer.getSpeed();
    }

    private int solveB(List<Reindeer> reindeer, int seconds) {
        Map<Reindeer, Integer> scores = new HashMap<>();
        for (int t = 1; t <= seconds; t++) {
            int max = 0;
            Set<Reindeer> leaders = new HashSet<>();
            for (Reindeer r : reindeer) {
                int distance = scoreA(r, t);
                if (distance == max) {
                    leaders.add(r);
                } else if (distance > max) {
                    max = distance;
                    leaders = new HashSet<>();
                    leaders.add(r);
                }
            }
            for (Reindeer r : leaders) {
                scores.put(r, scores.getOrDefault(r, 0) + 1);
            }
        }
        return scores.values().stream().max(Comparator.naturalOrder()).orElseThrow(RuntimeException::new);
    }

    private List<Reindeer> parse() {
        List<Reindeer> reindeer = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] strings = scanner.nextLine().split(" ");
            reindeer.add(
                    new Reindeer(
                            strings[0],
                            Integer.parseInt(strings[3]),
                            Integer.parseInt(strings[6]),
                            Integer.parseInt(strings[13])));
        }
        return reindeer;
    }

    @Value
    private static class Reindeer {
        private String name;
        private int speed;
        private int speedTime;
        private int restTime;
    }

}
