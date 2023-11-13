package com.aoc.y2015;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay17 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Integer> input = parse();
        Set<Map<Integer, Integer>> solutions = new HashSet<>();
        solve(input, new HashMap<>(), 150, solutions, new HashSet<>());
        return undedupeSolution(input, solutions);
    }

    @Override
    protected Integer solveB() {
        List<Integer> input = parse();
        Set<Map<Integer, Integer>> solutions = new HashSet<>();
        solve(input, new HashMap<>(), 150, solutions, new HashSet<>());
        int minContainers = 99999999;
        for (Map<Integer, Integer> solution : solutions) {
            int containers = solution.values().stream().mapToInt(i -> i).sum();
            if (containers < minContainers) {
                minContainers = containers;
            }
        }
        int finalMinContainers = minContainers;
        solutions = solutions.stream()
                .filter(solution -> solution.values().stream().mapToInt(i -> i).sum() == finalMinContainers)
                .collect(Collectors.toSet());
        return undedupeSolution(input, solutions);
    }

    private Integer undedupeSolution(List<Integer> input, Set<Map<Integer, Integer>> solutions) {
        Map<Integer, Long> frequencies = input.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        int count = solutions.size();
        for (Map<Integer, Integer> solution : solutions) {
            int dupes = 0;
            for (Map.Entry<Integer, Integer> entry : solution.entrySet()) {
                int num = entry.getKey();
                int solFreq = entry.getValue();
                long inputFreq = frequencies.get(num);
                if (inputFreq > 1 && solFreq == 1) {
                    dupes += 1;
                }
            }
            count += Math.pow(2, dupes) - 1;
        }
        return count;
    }

    private void solve(List<Integer> input,
                       Map<Integer, Integer> current,
                       int target,
                       Set<Map<Integer, Integer>> solutions,
                       Set<Map<Integer, Integer>> visited) {
        int sum = current.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue()).sum();
        if (sum > target) {
            return;
        }
        if (sum == target) {
            solutions.add(current);
            return;
        }
        for (int i = 0; i < input.size(); i++) {
            final List<Integer> newInput = ImmutableList.<Integer>builder()
                    .addAll(input.subList(0, i))
                    .addAll(input.subList(i+1, input.size()))
                    .build();
            int toAdd = input.get(i);
            final Map<Integer, Integer> newCurrent = new HashMap<>(current);
            newCurrent.put(toAdd, newCurrent.getOrDefault(toAdd, 0) + 1);
            if (visited.contains(newCurrent)) {
                continue;
            }
            visited.add(newCurrent);
            solve(newInput, newCurrent, target, solutions, visited);
        }
    }

    private List<Integer> parse() {
        List<Integer> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(Integer.parseInt(scanner.nextLine()));
        }
        return input;
    }

}
