package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.annotations.VisibleForTesting;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay15 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<String> commands = parse();
        return commands
                .stream()
                .map(this::hash)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @Override
    public Integer solveB() {
        List<String> commands = parse();
        Map<Integer, Map<String, Integer>> boxes = new TreeMap<>();
        for (String command : commands) {
            if (command.contains("=")) {
                String[] tokens = command.split("=");
                int hash = hash(tokens[0]);
                int power = Integer.parseInt(tokens[1]);
                Map<String, Integer> slots = boxes.getOrDefault(hash, new LinkedHashMap<>());
                slots.put(tokens[0], power);
                boxes.put(hash, slots);
            } else {
                String[] tokens = command.split("-");
                int hash = hash(tokens[0]);
                Map<String, Integer> slots = boxes.getOrDefault(hash, new LinkedHashMap<>());
                slots.remove(tokens[0]);
                boxes.put(hash, slots);
            }
        }
        return boxes
                .entrySet()
                .stream()
                .map(e -> {
                    int boxScore = e.getKey() + 1;
                    int slotScore = 0;
                    int i = 1;
                    for (Map.Entry<String, Integer> s : e.getValue().entrySet()) {
                        slotScore += i * s.getValue();
                        i++;
                    }
                    return boxScore * slotScore;
                })
                .reduce(Integer::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @VisibleForTesting
    protected int hash(String string) {
        int value = 0;
        for (int i = 0; i < string.length(); i++) {
            value += string.charAt(i);
            value *= 17;
            value %= 256;
        }
        return value;
    }

    private List<String> parse() {
        List<String> commands = new ArrayList<>();
        while (scanner.hasNextLine()) {
            commands.addAll(Arrays.stream(scanner.nextLine().split(",")).collect(Collectors.toList()));
        }
        return commands;
    }

}
