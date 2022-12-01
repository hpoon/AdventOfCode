package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class ProblemDay10b implements ProblemDay<Long> {

    private Map<String, Integer> BRACKET_TO_POINTS = new HashMap<String, Integer>() {{
        put(")", 1);
        put("]", 2);
        put("}", 3);
        put(">", 4);
    }};

    private Map<String, String> REVERSE_BRACKET_MATCH = new HashMap<String, String>() {{
        put(")", "(");
        put("]", "[");
        put("}", "{");
        put(">", "<");
    }};

    private Map<String, String> BRACKET_MATCH = new HashMap<String, String>() {{
        put("(", ")");
        put("[", "]");
        put("{", "}");
        put("<", ">");
    }};

    private Scanner scanner;

    public Long solve() {
        final List<String> incomplete = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Stack<String> stk = new Stack<>();
            boolean isLegit = true;
            for (int i = 0; i < line.length(); i++) {
                final String bracket = line.substring(i, i+1);
                if (!REVERSE_BRACKET_MATCH.containsKey(bracket)) {
                    stk.add(bracket);
                } else if (stk.isEmpty()) {
                    isLegit = false;
                    break;
                } else {
                    final String top = stk.peek();
                    if (!REVERSE_BRACKET_MATCH.get(bracket).equals(top)) {
                        isLegit = false;
                        break;
                    } else {
                        stk.pop();
                    }
                }
            }
            if (isLegit && !stk.isEmpty()) {
                incomplete.add(line);
            }
        }

        final List<Long> scores = new ArrayList<>();
        for (final String line : incomplete) {
            final Stack<String> stk = new Stack<>();
            for (int i = 0; i < line.length(); i++) {
                final String bracket = line.substring(i, i+1);
                if (!REVERSE_BRACKET_MATCH.containsKey(bracket)) {
                    stk.add(bracket);
                } else {
                    final String top = stk.peek();
                    if (!REVERSE_BRACKET_MATCH.get(bracket).equals(top)) {
                        break;
                    } else {
                        stk.pop();
                    }
                }
            }

            long score = 0;
            while (!stk.isEmpty()) {
                final String top = stk.pop();
                final String matching = BRACKET_MATCH.get(top);
                score *= 5;
                score += BRACKET_TO_POINTS.get(matching);
            }
            scores.add(score);
        }

        scores.sort(Comparator.naturalOrder());
        return scores.get(scores.size() / 2);
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day10.txt"));
        return scanner;
    }

}
