package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class ProblemDay2 extends ProblemDay<Integer, Integer> {

    private static final Map<String, Integer> BAG = ImmutableMap.of(
            "red", 12, "green", 13, "blue", 14);

    @Override
    public Integer solveA() {
        return solutions(true)
                .keySet()
                .stream()
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @Override
    public Integer solveB() {
        Map<Integer, Map<String, Integer>> solutions = solutions(false);
        return solutions
                .values()
                .stream()
                .map(s -> s.values()
                        .stream()
                        .reduce((v1, v2) -> v1 * v2)
                        .orElseThrow(() -> new RuntimeException("Shouldn't happen")))
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    public Map<Integer, Map<String, Integer>> solutions(boolean testFit) {
        Map<Integer, Map<String, Integer>> solutions = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(": ");
            String[] game = tokens[1].split("; ");
            int id = Integer.parseInt(tokens[0].split(" ")[1]);
            Map<String, Integer> colours = new HashMap<>();
            for (String g : game) {
                String[] round = g.split(", ");
                for (String s : round) {
                    String[] r = s.split(" ");
                    int num = Integer.parseInt(r[0]);
                    String colour = r[1].replace(",", "").replace(";", "");
                    colours.put(colour, Math.max(colours.getOrDefault(colour, 0), num));
                }
            }
            boolean fits = true;
            if (testFit) {
                for (Map.Entry<String, Integer> entry : colours.entrySet()) {
                    String c = entry.getKey();
                    int n = entry.getValue();
                    if (BAG.getOrDefault(c, 0) < n) {
                        fits = false;
                        break;
                    }
                }
            }

            if (fits) {
                solutions.put(id, colours);
            }
        }
        return solutions;
    }

}
