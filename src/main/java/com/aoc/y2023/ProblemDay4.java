package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay4 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        int score = 0;
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" \\| ");
            Set<Integer> winning = Arrays.stream(line[0].split(": ")[1].split(" "))
                    .filter(str -> !str.equals(""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            Set<Integer> card = Arrays.stream(line[1].split(" "))
                    .filter(str -> !str.equals(""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            Set<Integer> matches = Sets.intersection(winning, card);
            score += Math.pow(2, matches.size() - 1);
        }
        return score;
    }

    @Override
    public Integer solveB() {
        Map<Integer, Pair<Set<Integer>, Set<Integer>>> cardNumToSets = new HashMap<>();
        Map<Integer, Integer> occurrences = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" \\| ");
            Set<Integer> winning = Arrays.stream(line[0].split(": ")[1].split(" "))
                    .filter(str -> !str.equals(""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            Set<Integer> card = Arrays.stream(line[1].split(" "))
                    .filter(str -> !str.equals(""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            cardNumToSets.put(
                    Integer.parseInt(line[0]
                            .split(": ")[0]
                            .replace(" ", "")
                            .replace("Card", "")),
                    ImmutablePair.of(winning, card));
        }
        Queue<Integer> queue = IntStream.rangeClosed(1, cardNumToSets.size())
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));
        while (!queue.isEmpty()) {
            int num = queue.poll();
            Set<Integer> winning = cardNumToSets.get(num).getKey();
            Set<Integer> card = cardNumToSets.get(num).getValue();
            Set<Integer> matches = Sets.intersection(winning, card);
            queue.addAll(IntStream.rangeClosed(num + 1, num + matches.size())
                    .boxed()
                    .collect(Collectors.toList()));
            occurrences.put(num, occurrences.getOrDefault(num, 0) + 1);
        }
        return occurrences.values()
                .stream()
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

}
