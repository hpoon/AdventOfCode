package com.aoc.y2015;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProblemDay5 extends ProblemDay<Long, Long> {

    private static final List<Predicate<String>> PREDICATES_A = ImmutableList.of(
            word -> {
                int vowels = 0;
                for (int i = 0; i < word.length(); i++) {
                    switch (word.charAt(i)) {
                        case 'a':
                        case 'e':
                        case 'i':
                        case 'o':
                        case 'u':
                            vowels++;
                            break;
                    }
                }
                return vowels >= 3;
            },
            word -> {
                for (int i = 0; i < word.length() - 1; i++) {
                    char c1 = word.charAt(i);
                    char c2 = word.charAt(i+1);
                    if (c1 == c2) {
                        return true;
                    }
                }
                return false;
            },
            word -> ImmutableSet.of("ab", "cd", "pq", "xy").stream().noneMatch(word::contains));

    private static final List<Predicate<String>> PREDICATES_B = ImmutableList.of(
            word -> {
                for (int i = 0; i < word.length() - 1; i++) {
                    String substr1 = word.substring(i, i+2);
                    for (int j = i+2; j < word.length() - 1; j++) {
                        String substr2 = word.substring(j, j+2);
                        if (substr1.equals(substr2)) {
                            return true;
                        }
                    }
                }
                return false;
            },
            word -> {
                for (int i = 0; i < word.length() - 2; i++) {
                    char c1 = word.charAt(i);
                    char c3 = word.charAt(i+2);
                    if (c1 == c3) {
                        return true;
                    }
                }
                return false;
            });

    @Override
    protected Long solveA() {
        List<String> words = parse();
        return solve(words, PREDICATES_A);
    }

    @Override
    protected Long solveB() {
        List<String> words = parse();
        return solve(words, PREDICATES_B);
    }

    private long solve(List<String> words, List<Predicate<String>> predicates) {
        return words.stream()
                .map(word -> predicates.stream().filter(p -> p.test(word)).count())
                .filter(matches -> matches == predicates.size())
                .count();
    }

    private List<String> parse() {
        List<String> words = new ArrayList<>();
        while (scanner.hasNextLine()) {
            words.add(scanner.nextLine());
        }
        return words;
    }

}
