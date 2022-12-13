package com.aoc;

import com.aoc.y2022.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, ProblemDay<?>> PROBLEMS = new HashMap<>() {{
        put("1a", new ProblemDay1a());
        put("1b", new ProblemDay1b());
        put("2a", new ProblemDay2a());
        put("2b", new ProblemDay2b());
        put("3a", new ProblemDay3a());
        put("3b", new ProblemDay3b());
        put("4a", new ProblemDay4a());
        put("4b", new ProblemDay4b());
        put("5a", new ProblemDay5a());
        put("5b", new ProblemDay5b());
        put("6a", new ProblemDay6a());
        put("6b", new ProblemDay6b());
        put("7a", new ProblemDay7a());
        put("7b", new ProblemDay7b());
        put("8a", new ProblemDay8a());
        put("8b", new ProblemDay8b());
        put("9a", new ProblemDay9a());
        put("9b", new ProblemDay9b());
        put("10a", new ProblemDay10a());
        put("10b", new ProblemDay10b());
        put("11a", new ProblemDay11a());
        put("11b", new ProblemDay11b());
        put("12a", new ProblemDay12a());
        put("12b", new ProblemDay12b());
//        put("13a", new ProblemDay13a());
//        put("13b", new ProblemDay13b());
//        put("14a", new ProblemDay14a());
//        put("14b", new ProblemDay14b());
//        put("15a", new ProblemDay15a());
//        put("15b", new ProblemDay15b());
//        put("16a", new ProblemDay16a());
//        put("16b", new ProblemDay16b());
//        put("17a", new ProblemDay17a());
//        put("17b", new ProblemDay17b());
//        put("18a", new ProblemDay18a());
//        put("18b", new ProblemDay18b());
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
        PROBLEMS.get("12b").run();
    }
}
