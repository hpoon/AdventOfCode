package com.aoc;

import com.aoc.y2022.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, ProblemDay<?, ?>> PROBLEMS = new HashMap<>() {{
        put("1", new ProblemDay14());
        put("2", new ProblemDay15());
        put("3", new ProblemDay14());
        put("4", new ProblemDay15());
        put("5", new ProblemDay14());
        put("6", new ProblemDay15());
        put("7", new ProblemDay14());
        put("8", new ProblemDay15());
        put("9", new ProblemDay14());
        put("10", new ProblemDay15());
        put("11", new ProblemDay14());
        put("12", new ProblemDay15());
        put("13", new ProblemDay15());
        put("14", new ProblemDay14());
        put("15", new ProblemDay15());
        put("16", new ProblemDay16());
        put("17", new ProblemDay17());
        put("18", new ProblemDay18());
        put("19", new ProblemDay19());
        put("20", new ProblemDay20());
        put("21", new ProblemDay21());
        put("22", new ProblemDay22());
        put("23", new ProblemDay23());
        put("24", new ProblemDay24());
        put("25", new ProblemDay25());
    }};

    public static void main(String[] args) {
        PROBLEMS.get("25").run('a');
    }

}
