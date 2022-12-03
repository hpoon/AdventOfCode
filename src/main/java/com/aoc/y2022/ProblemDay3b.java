package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ProblemDay3b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
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
