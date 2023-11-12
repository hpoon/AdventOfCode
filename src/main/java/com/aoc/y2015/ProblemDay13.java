package com.aoc.y2015;

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
        List<List<String>> combos = new ArrayList<>();
        permutations(people, new ArrayList<>(), combos);
        return maxScore(combos, pairings);
    }

    private int maxScore(List<List<String>> combos, Map<Pair<String, String>, Integer> pairings) {
        int max = -999999;
        for (List<String> combo: combos) {
            max = Math.max(max, score(combo, pairings));
        }
        return max;
    }

    private int score(List<String> combo, Map<Pair<String, String>, Integer> pairings) {
        int score = 0;
        for (int i = 1; i < combo.size(); i++) {
            score += pairings.getOrDefault(ImmutablePair.of(combo.get(i-1), combo.get(i)), 0);
            score += pairings.getOrDefault(ImmutablePair.of(combo.get(i), combo.get(i-1)), 0);
        }
        score += pairings.getOrDefault(ImmutablePair.of(combo.get(combo.size()-1), combo.get(0)), 0);
        score += pairings.getOrDefault(ImmutablePair.of(combo.get(0), combo.get(combo.size()-1)), 0);
        return score;
    }

    private void permutations(List<String> people, List<String> combo, List<List<String>> combos) {
        if (people.isEmpty()) {
            combos.add(combo);
            return;
        }
        for (int i = 0; i < people.size(); i++) {
            List<String> newCombo = new ArrayList<>(combo);
            newCombo.add(people.get(i));
            List<String> newPeople = new ArrayList<>(people);
            newPeople.remove(i);
            permutations(newPeople, newCombo, combos);
        }
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
