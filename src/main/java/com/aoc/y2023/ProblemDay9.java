package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay9 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<List<Integer>> numsOfNums = parse();
        return solve(numsOfNums, true);
    }

    @Override
    public Integer solveB() {
        List<List<Integer>> numsOfNums = parse();
        return solve(numsOfNums, false);
    }

    private int solve(List<List<Integer>> numsOfNums, boolean forward) {
        int sum = 0;
        for (List<Integer> nums : numsOfNums) {
            sum += solveOne(nums, forward);
        }
        return sum;
    }

    @VisibleForTesting
    protected int solveOne(List<Integer> nums, boolean forward) {
        List<List<Integer>> results = calculateDiffs(nums);
        results.get(results.size() - 1).add(forward ? results.get(results.size() - 1).size() : 0, 0);
        for (int i = results.size() - 2; i >= 0; i--) {
            List<Integer> current = results.get(i);
            List<Integer> lower = results.get(i+1);
            int next = forward
                    ? current.get(current.size() - 1) + lower.get(lower.size() - 1)
                    : current.get(0) - lower.get(0);
            if (i == 0) {
                return next;
            } else {
                current.add(forward ? current.size() : 0, next);
            }
        }
        throw new RuntimeException("Shouldn't happen");
    }

    private static List<List<Integer>> calculateDiffs(List<Integer> nums) {
        List<List<Integer>> results = new ArrayList<>();
        results.add(nums);
        while (results.get(results.size() - 1).stream().anyMatch(i -> i != 0)) {
            List<Integer> last = results.get(results.size() - 1);
            List<Integer> next = new ArrayList<>();
            for (int i = 1; i < last.size(); i++) {
                int diff = last.get(i) - last.get(i-1);
                next.add(diff);
            }
            results.add(next);
        }
        return results;
    }

    private List<List<Integer>> parse() {
        List<List<Integer>> numsOfNums = new ArrayList<>();
        while (scanner.hasNextLine()) {
            numsOfNums.add(Arrays.stream(scanner.nextLine().split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
        }
        return numsOfNums;
    }

}
