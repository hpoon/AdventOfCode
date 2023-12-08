package com.aoc.y2023;

import com.aoc.ProblemDay;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.math3.util.ArithmeticUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay8 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        Queue<String> instructions = parseInstructions();
        MultiKeyMap<String, String> map = parseMap();
        return solveA(instructions, map, "AAA", str -> str.equals("ZZZ"));
    }

    @Override
    public Long solveB() {
        Queue<String> instructions = parseInstructions();
        MultiKeyMap<String, String> map = parseMap();
        Set<String> locs = getLocationsBySuffix(map, "A");

        // Luckily one start location cannot reach more than one different end locations
        List<Long> solutions = new ArrayList<>();
        for (String loc : locs) {
            solutions.add(solveA(instructions, map, loc, str -> str.endsWith("Z")));
        }

        long lcm = solutions.get(0);
        for (int i = 1; i < solutions.size(); i++) {
            lcm = ArithmeticUtils.lcm(lcm, solutions.get(i));
        }
        return lcm;
    }

    private long solveA(Queue<String> instructions,
                        MultiKeyMap<String, String> map,
                        String start,
                        Predicate<String> endCondition) {
        Queue<String> instructionsCopy = new LinkedList<>(instructions);
        String loc = start;
        long steps = 0;
        while (true) {
            String dir = instructionsCopy.poll();
            loc = map.get(loc, dir);
            steps++;
            if (endCondition.evaluate(loc)) {
                break;
            }
            instructionsCopy.add(dir);
        }
        return steps;
    }

    private static Set<String> getLocationsBySuffix(MultiKeyMap<String, String> map, String suffix) {
        return map.keySet().stream()
                .map(key -> key.getKey(0))
                .filter(key -> key.endsWith(suffix))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Queue<String> parseInstructions() {
        Queue<String> ins = new LinkedList<>();
        String str = scanner.nextLine();
        scanner.nextLine(); // Skip next line for later
        for (int i = 0; i < str.length(); i++) {
            ins.add(str.substring(i, i+1));
        }
        return ins;
    }

    private MultiKeyMap<String, String> parseMap() {
        MultiKeyMap<String, String> map = new MultiKeyMap<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine()
                    .replace(" = (", " ")
                    .replace(",", "")
                    .replace(")", "")
                    .split(" ");
            map.put(tokens[0], "L", tokens[1]);
            map.put(tokens[0], "R", tokens[2]);
        }
        return map;
    }

}
