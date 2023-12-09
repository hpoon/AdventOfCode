package com.aoc.y2015;

import com.aoc.Combinatorics;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay13 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        Map<Pair<String, String>, Integer> input = parse();
        List<String> people = people(input);
        return solve(input, people);
    }

    @Override
    protected Integer solveB() {
        Map<Pair<String, String>, Integer> input = parse();
        List<String> people = ImmutableList.<String>builder()
                .addAll(people(input))
                .add("Me")
                .build();
        return solve(input, people);
    }

    private int solve(Map<Pair<String, String>, Integer> pairings, List<String> people) {
        List<List<String>> permutations = new ArrayList<>();
        Combinatorics.permutations(people, new ArrayList<>(), permutations);
        return maxScore(permutations, pairings);
    }

    private int maxScore(List<List<String>> permutations, Map<Pair<String, String>, Integer> pairings) {
        int max = -999999;
        for (List<String> permutation: permutations) {
            max = Math.max(max, score(permutation, pairings));
        }
        return max;
    }

    private int score(List<String> permutation, Map<Pair<String, String>, Integer> pairings) {
        int score = 0;
        for (int i = 1; i < permutation.size(); i++) {
            score += pairings.getOrDefault(ImmutablePair.of(permutation.get(i-1), permutation.get(i)), 0);
            score += pairings.getOrDefault(ImmutablePair.of(permutation.get(i), permutation.get(i-1)), 0);
        }
        score += pairings.getOrDefault(ImmutablePair.of(permutation.get(permutation.size()-1), permutation.get(0)), 0);
        score += pairings.getOrDefault(ImmutablePair.of(permutation.get(0), permutation.get(permutation.size()-1)), 0);
        return score;
    }

    private List<String> people(Map<Pair<String, String>, Integer> pairings) {
        return ImmutableList.<String>builder()
                .addAll(pairings.keySet().stream()
                        .flatMap(pair -> ImmutableSet.of(pair.getLeft(), pair.getRight()).stream())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Map<Pair<String, String>, Integer> parse() {
        Map<Pair<String, String>, Integer> pairings = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] strings = scanner.nextLine().split(" ");
            Pair<String, String> pair = ImmutablePair.of(strings[0], strings[strings.length - 1].replace(".", ""));
            if (strings[2].equals("gain")) {
                pairings.put(pair, Integer.parseInt(strings[3]));
            } else {
                pairings.put(pair, -Integer.parseInt(strings[3]));
            }
        }
        return pairings;
    }

}
