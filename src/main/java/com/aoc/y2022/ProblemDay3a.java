package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ProblemDay3a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
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
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day3.txt"));
        return scanner;
    }

    private Set<Character> convertToSet(final String str, int start, int end) {
        final Set<Character> set = new HashSet<>();
        for (int i = start; i < end; i++) {
            set.add(str.charAt(i));
        }
        return set;
    }
}
