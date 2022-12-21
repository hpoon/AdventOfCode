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
//        put("19a", new ProblemDay19a());
//        put("19b", new ProblemDay19b());
//        put("20a", new ProblemDay20a());
//        put("20b", new ProblemDay20b());
//        put("21a", new ProblemDay21a());
//        put("21b", new ProblemDay21b());
//        put("22a", new ProblemDay22a());
//        put("22b", new ProblemDay22b());
//        put("23a", new ProblemDay23a());
//        put("23b", new ProblemDay23b());
//        put("24a", new ProblemDay24a());
//        put("25a", new ProblemDay25a());
    }};

    public static void main(String[] args) {
        PROBLEMS.get("18").run('b');
    }

}
