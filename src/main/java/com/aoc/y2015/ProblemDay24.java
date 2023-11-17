package com.aoc.y2015;

import com.aoc.ProblemDay;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay24 extends ProblemDay<Long, Long> {

    @Override
    protected Long solveA() {
        List<Integer> weights = parse();
        return solve(weights, 3);
    }

    @Override
    protected Long solveB() {
        List<Integer> weights = parse();
        return solve(weights, 4);
    }

    private long solve(List<Integer> weights, int compartments) {
        int target = weights.stream().reduce(0, Integer::sum) / compartments;
        for (int r = 1; r < weights.size(); r++) {
            List<List<Integer>> combos = new ArrayList<>();
            combinations(weights, r, 0, new ArrayList<>(), combos);
            List<List<Integer>> validCombos = combos.stream()
                    .filter(c -> c.stream().reduce(Integer::sum).orElse(0) == target)
                    .collect(Collectors.toList());
            if (validCombos.isEmpty()) {
                continue;
            }
            return validCombos.stream()
                    .map(c -> c.stream()
                            .mapToLong(i -> i)
                            .reduce((v1, v2) -> (v1 * v2))
                            .orElse(Long.MAX_VALUE))
                    .min(Comparator.naturalOrder())
                    .orElse(Long.MAX_VALUE);
        }
        throw new RuntimeException("Shouldn't happen");
    }

    private List<Integer> parse() {
        List<Integer> weights = new ArrayList<>();
        while (scanner.hasNextLine()) {
            weights.add(Integer.parseInt(scanner.nextLine()));
        }
        return weights;
    }

    public void combinations(List<Integer> weights,
                             int r,
                             int start,
                             List<Integer> combination,
                             List<List<Integer>> result) {
        if (r == 0) {
            result.add(new ArrayList<>(combination));
            return;
        }
        for (int i = start; i <= weights.size() - r; i++) {
            combination.add(weights.get(i));
            combinations(weights, r - 1, i + 1, combination, result);
            combination.remove(combination.size() - 1);
        }
    }

}
