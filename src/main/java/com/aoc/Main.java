package com.aoc;

import com.aoc.y2015.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, ProblemDay<?, ?>> PROBLEMS = new HashMap<>() {{
        put("1", new ProblemDay1());
    }};

    public static void main(String[] args) {
        PROBLEMS.get("1").run('a');
    }

}
