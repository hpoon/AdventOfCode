package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.Sets;

import java.util.*;

public class ProblemDay3 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        int priority = 0;
        while (scanner.hasNextLine()) {
            final String rucksack = scanner.nextLine();
            final Set<Character> firstHalf = convertToSet(rucksack, 0, rucksack.length() / 2);
            final Set<Character> secondHalf = convertToSet(rucksack, rucksack.length() / 2, rucksack.length());
            final Set<Character> intersection = Sets.intersection(firstHalf, secondHalf);
            for (final Character c : intersection) {
                int score = 0;
                if (c >= 'A' && c <= 'Z') {
                    score = c - 'A' + 27;
                } else {
                    score = c - 'a' + 1;
                }
                priority += score;
            }
        }
        return priority;
    }

    @Override
    public Integer solveB() {
        int priority = 0;
        int line = 0;
        List<Set<Character>> group = new ArrayList<>();
        while (scanner.hasNextLine()) {
            line++;
            final String rucksack = scanner.nextLine();
            final Set<Character> set = convertToSet(rucksack, 0, rucksack.length());
            group.add(set);
            if (line % 3 == 0) {
                Set<Character> intersection = Sets.intersection(
                        Sets.intersection(group.get(0), group.get(1)),
                        group.get(2));
                for (final Character c : intersection) {
                    int score = 0;
                    if (c >= 'A' && c <= 'Z') {
                        score = c - 'A' + 27;
                    } else {
                        score = c - 'a' + 1;
                    }
                    priority += score;
                }
                group = new ArrayList<>();
            }
        }
        return priority;
    }

    private Set<Character> convertToSet(final String str, int start, int end) {
        final Set<Character> set = new HashSet<>();
        for (int i = start; i < end; i++) {
            set.add(str.charAt(i));
        }
        return set;
    }
}
