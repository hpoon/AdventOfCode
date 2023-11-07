package com.aoc;

import com.aoc.y2015.*;

import java.util.HashMap;
import java.util.Map;

public class PerfectPeterPlate {

    private static final Map<String, ProblemDay<?, ?>> PROBLEMS = new HashMap<>() {{
        put("1", new ProblemDay1());
        put("2", new ProblemDay2());
        put("3", new ProblemDay3());
        put("4", new ProblemDay4());
        put("5", new ProblemDay5());
        put("6", new ProblemDay6());
        put("7", new ProblemDay7());
        put("8", new ProblemDay8());
        put("9", new ProblemDay9());
    }};

    public static void main(String[] args) {
        PROBLEMS.get("9").run('b');
    }

}
