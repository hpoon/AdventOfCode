package com.aoc.y2021;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ProblemDay14a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        String sequence = scanner.nextLine();
        scanner.nextLine();
        final Map<String, String> rules = new HashMap<>();
        while (scanner.hasNextLine()) {
            final String[] rule = scanner.nextLine().split(" -> ");
            rules.put(rule[0], rule[1]);
        }

        for (int i = 0; i < 10; i++) {
            StringBuilder newSequence = new StringBuilder();
            for (int j = 0; j < sequence.length() - 1; j++) {
                final String pair = sequence.substring(j, j + 2);
                final String insert = rules.get(pair);
                if (insert != null) {
                    newSequence.append(pair.charAt(0));
                    newSequence.append(insert);
                }
            }
            newSequence.append(sequence.charAt(sequence.length() - 1));
            sequence = newSequence.toString();
        }

        final Map<Character, Integer> counts = new HashMap<>();
        for (int i = 0; i < sequence.length(); i++) {
            counts.put(sequence.charAt(i), counts.getOrDefault(sequence.charAt(i), 0) + 1);
        }
        return counts.values().stream().max(Comparator.naturalOrder()).get() -
                counts.values().stream().min(Comparator.naturalOrder()).get();
    }

}
