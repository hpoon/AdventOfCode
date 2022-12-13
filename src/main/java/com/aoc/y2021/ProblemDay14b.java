package com.aoc.y2021;

import com.aoc.ProblemDay;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ProblemDay14b extends ProblemDay<Long> {

    public Long solve() {
        String sequence = scanner.nextLine();
        Map<String, Long> pairs = new HashMap<>();
        final String last = sequence.substring(sequence.length() - 1);
        for (int j = 0; j < sequence.length() - 1; j++) {
            final String pair = sequence.substring(j, j + 2);
            pairs.put(pair, pairs.getOrDefault(pair, 0L) + 1);
        }

        scanner.nextLine();
        final Map<String, String> rules = new HashMap<>();
        while (scanner.hasNextLine()) {
            final String[] rule = scanner.nextLine().split(" -> ");
            rules.put(rule[0], rule[1]);
        }

        for (int i = 0; i < 40; i++) {
            Map<String, Long> newPairs = new HashMap<>();
            for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
                final String pair = entry.getKey();
                final Long occurences = entry.getValue();
                final String insert = rules.get(pair);
                if (insert != null) {
                    final String left = pair.charAt(0) + insert;
                    final String right = insert + pair.charAt(1);
                    newPairs.put(left, newPairs.getOrDefault(left, 0L) + occurences);
                    newPairs.put(right, newPairs.getOrDefault(right, 0L) + occurences);
                }
            }
            pairs = newPairs;
        }

        final Map<Character, Long> counts = new HashMap<>();
        for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
            final String pair = entry.getKey();
            final Long occurrence = entry.getValue();
            final char left = pair.charAt(0);
            counts.put(left, counts.getOrDefault(left, 0L) + occurrence);
        }
        counts.put(last.charAt(0), counts.getOrDefault(last.charAt(0), 0L) + 1);
        return counts.values().stream().max(Comparator.naturalOrder()).get() -
                counts.values().stream().min(Comparator.naturalOrder()).get();
    }

}
