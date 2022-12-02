package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ProblemDay2a implements ProblemDay<Integer> {

    private static final Map<Set<String>, Integer> SCORE = new HashMap<Set<String>, Integer>() {{
        put(new HashSet<String>() {{ add("A"); add("X"); }}, 4);    // Rock / rock = 1 + 3
        put(new HashSet<String>() {{ add("A"); add("Y"); }}, 8);    // Rock / paper = 2 + 6
        put(new HashSet<String>() {{ add("A"); add("Z"); }}, 3);    // Rock / scissors = 3 + 0
        put(new HashSet<String>() {{ add("B"); add("X"); }}, 1);    // Paper / rock = 1 + 0
        put(new HashSet<String>() {{ add("B"); add("Y"); }}, 5);    // Paper / paper = 2 + 3
        put(new HashSet<String>() {{ add("B"); add("Z"); }}, 9);    // Paper / scissors = 3 + 6
        put(new HashSet<String>() {{ add("C"); add("X"); }}, 7);    // Scissors / rock = 1 + 6
        put(new HashSet<String>() {{ add("C"); add("Y"); }}, 2);    // Scissors / paper = 2 + 0
        put(new HashSet<String>() {{ add("C"); add("Z"); }}, 6);    // Scissors / scissors = 3 + 3
    }};

    private Scanner scanner;

    public Integer solve() {
        int score = 0;
        while (scanner.hasNextLine()) {
            final String[] hand = scanner.nextLine().split(" ");
            final String opponent = hand[0];
            final String me = hand[1];
            score += SCORE.get(new HashSet<String>() {{ add(opponent); add(me); }});
        }
        return score;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2022/day2.txt"));
        return scanner;
    }
}
