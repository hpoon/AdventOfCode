package com.aoc.y2024;

import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProblemDay19 extends ProblemDay<Integer, Long> {

    @Override
    public Integer solveA() {
        List<String> towels = parseTowels();
        List<String> combos = parseCombos();
        return solveA(combos, towels);
    }

    @Override
    public Long solveB() {
        List<String> towels = parseTowels();
        List<String> combos = parseCombos();
        return solveB(combos, towels);
    }

    private long solveB(List<String> combos, List<String> towels) {
        long count = 0;
        for (String combo : combos) {
            if (buildTowel("", combo, combo, towels, new HashMap<>())) {
                count += buildPermutations("", combo, combo, towels, new HashMap<>());
            }
        }
        return count;
    }

    private int solveA(List<String> combos, List<String> towels) {
        int count = 0;
        for (String combo : combos) {
            Map<String, Boolean> memo = new HashMap<>();
            if (buildTowel("", combo, combo, towels, memo)) {
                count++;
            }
        }
        return count;
    }

    private long buildPermutations(String current,
                                   String remaining,
                                   String combo,
                                   List<String> towels,
                                   Map<String, Long> memo) {
        if (memo.containsKey(remaining)) {
            return memo.get(remaining);
        }
        long result = 0;
        for (String towel : towels) {
            if ((current + towel).length() > combo.length()) {
                continue;
            }
            String nextCurrent = current + towel;
            if (!combo.startsWith(nextCurrent)) {
                continue;
            }
            if (nextCurrent.equals(combo)) {
                result++;
                continue;
            }
            String nextRemaining = combo.substring((current + towel).length());
            result += buildPermutations(
                    nextCurrent,
                    nextRemaining,
                    combo,
                    towels,
                    memo);
        }
        memo.put(remaining, result);
        return result;
    }

    private boolean buildTowel(String current,
                               String remaining,
                               String combo,
                               List<String> towels,
                               Map<String, Boolean> memo) {
        if (memo.containsKey(remaining)) {
            return memo.get(remaining);
        }
        if (!combo.startsWith(current)) {
            return false;
        }
        if (current.equals(combo)) {
            return true;
        }
        boolean result = false;
        for (String towel : towels) {
            if ((current + towel).length() > combo.length()) {
                continue;
            }
            result |= buildTowel(
                    current + towel,
                    combo.substring((current + towel).length()),
                    combo,
                    towels,
                    memo);
        }
        memo.put(remaining, result);
        return result;
    }

    private List<String> parseCombos() {
        List<String> combos = new ArrayList<>();
        while (scanner.hasNextLine()) {
            combos.add(scanner.nextLine());
        }
        return combos;
    }

    private List<String> parseTowels() {
        List<String> towels = Arrays.stream(scanner.nextLine().split(", "))
                .collect(Collectors.toList());
        scanner.nextLine();
        return towels;
    }

}
