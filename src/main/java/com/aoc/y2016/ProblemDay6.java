package com.aoc.y2016;

import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemDay6 extends ProblemDay<String, String> {

    @Override
    public String solveA() {
        List<String> strings = parse();
        return solve(strings, true);
    }

    @Override
    public String solveB() {
        List<String> strings = parse();
        return solve(strings, false);
    }

    private String solve(List<String> strings, boolean isMax) {
        List<String> transposed = transpose(strings);
        StringBuilder builder = new StringBuilder();
        for (String string : transposed) {
            Map<Character, Integer> freqs = new HashMap<>();
            for (int i = 0; i < string.length(); i++) {
                freqs.put(string.charAt(i), freqs.getOrDefault(string.charAt(i), 0) + 1);
            }
            int limit = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            char limitChar = '_';
            for (Map.Entry<Character, Integer> entry : freqs.entrySet()) {
                if ((isMax && entry.getValue() > limit) || (!isMax && entry.getValue() < limit)) {
                    limit = entry.getValue();
                    limitChar = entry.getKey();
                }
            }
            builder.append(limitChar);
        }
        return builder.toString();
    }

    private List<String> transpose(List<String> strings) {
        List<String> transposed = new ArrayList<>();
        for (int col = 0; col < strings.get(0).length(); col++) {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < strings.size(); row++) {
                sb.append(strings.get(row).charAt(col));
            }
            transposed.add(sb.toString());
        }
        return transposed;
    }

    private List<String> parse() {
        List<String> strings = new ArrayList<>();
        while (scanner.hasNextLine()) {
            strings.add(scanner.nextLine());
        }
        return strings;
    }

}
