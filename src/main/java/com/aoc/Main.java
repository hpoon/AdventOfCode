package com.aoc;

import com.aoc.y2015.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, ProblemDay<?, ?>> PROBLEMS = new HashMap<>() {{
        put("1", new ProblemDay1());
        put("2", new ProblemDay2());
        put("3", new ProblemDay3());
    }};

    public static void main(String[] args) {
        PROBLEMS.get("3").run('b');
    }

}
