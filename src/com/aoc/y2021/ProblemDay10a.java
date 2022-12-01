package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class ProblemDay10a implements ProblemDay<Integer> {

    private Map<String, Integer> BRACKET_TO_POINTS = new HashMap<String, Integer>() {{
        put(")", 3);
        put("]", 57);
        put("}", 1197);
        put(">", 25137);
    }};

    private Map<String, String> BRACKET_MATCH = new HashMap<String, String>() {{
        put(")", "(");
        put("]", "[");
        put("}", "{");
        put(">", "<");
    }};

    private Scanner scanner;

    public Integer solve() {
        int points = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Stack<String> stk = new Stack<>();
            for (int i = 0; i < line.length(); i++) {
                final String bracket = line.substring(i, i+1);
                if (!BRACKET_MATCH.containsKey(bracket)) {
                    stk.add(bracket);
                } else if (stk.isEmpty()) {
                    points += BRACKET_TO_POINTS.get(bracket);
                } else {
                    final String top = stk.pop();
                    if (!BRACKET_MATCH.get(bracket).equals(top)) {
                        points += BRACKET_TO_POINTS.get(bracket);
                    }
                }
            }
        }

        return points;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day10.txt"));
        return scanner;
    }

}
