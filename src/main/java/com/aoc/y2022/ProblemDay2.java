package com.aoc.y2022;

import com.aoc.ProblemDay;

import java.util.*;

public class ProblemDay2 extends ProblemDay<Integer, Integer> {

    private static final Map<Set<String>, Integer> SCORE = new HashMap<>() {{
        put(new HashSet<>() {{ add("A"); add("X"); }}, 4);    // Rock / rock = 1 + 3
        put(new HashSet<>() {{ add("A"); add("Y"); }}, 8);    // Rock / paper = 2 + 6
        put(new HashSet<>() {{ add("A"); add("Z"); }}, 3);    // Rock / scissors = 3 + 0
        put(new HashSet<>() {{ add("B"); add("X"); }}, 1);    // Paper / rock = 1 + 0
        put(new HashSet<>() {{ add("B"); add("Y"); }}, 5);    // Paper / paper = 2 + 3
        put(new HashSet<>() {{ add("B"); add("Z"); }}, 9);    // Paper / scissors = 3 + 6
        put(new HashSet<>() {{ add("C"); add("X"); }}, 7);    // Scissors / rock = 1 + 6
        put(new HashSet<>() {{ add("C"); add("Y"); }}, 2);    // Scissors / paper = 2 + 0
        put(new HashSet<>() {{ add("C"); add("Z"); }}, 6);    // Scissors / scissors = 3 + 3
    }};

    private static final Map<Set<String>, String> PLAY = new HashMap<>() {{
        put(new HashSet<>() {{ add("A"); add("X"); }}, "Z");  // Rock / lose = scissors
        put(new HashSet<>() {{ add("A"); add("Y"); }}, "X");  // Rock / draw = rock
        put(new HashSet<>() {{ add("A"); add("Z"); }}, "Y");  // Rock / win = paper
        put(new HashSet<>() {{ add("B"); add("X"); }}, "X");  // Paper / lose = rock
        put(new HashSet<>() {{ add("B"); add("Y"); }}, "Y");  // Paper / draw = paper
        put(new HashSet<>() {{ add("B"); add("Z"); }}, "Z");  // Paper / win = scissors
        put(new HashSet<>() {{ add("C"); add("X"); }}, "Y");  // Scissors / lose = paper
        put(new HashSet<>() {{ add("C"); add("Y"); }}, "Z");  // Scissors / draw = scissors
        put(new HashSet<>() {{ add("C"); add("Z"); }}, "X");  // Scissors / win = rock
    }};

    @Override
    public Integer solveA() {
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
    public Integer solveB() {
        int score = 0;
        while (scanner.hasNextLine()) {
            final String[] hand = scanner.nextLine().split(" ");
            final String opponent = hand[0];
            final String result = hand[1];
            String play = PLAY.get(new HashSet<String>() {{ add(opponent); add(result); }});
            score += SCORE.get(new HashSet<String>() {{ add(opponent); add(play); }});
        }
        return score;
    }

}
