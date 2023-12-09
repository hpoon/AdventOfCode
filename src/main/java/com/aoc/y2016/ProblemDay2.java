package com.aoc.y2016;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay2 extends ProblemDay<String, String> {

    @Override
    public String solveA() {
        List<List<String>> instructions = parse();
        return solve(instructions, true);
    }

    @Override
    public String solveB() {
        List<List<String>> instructions = parse();
        return solve(instructions, false);
    }

    private String solve(List<List<String>> instructions, boolean solveA) {
        List<List<String>> keypad = solveA ? keypadA() : keypadB();
        String code = "";
        for (List<String> instruction : instructions) {
            Pair<Integer, Integer> pos = ImmutablePair.of(2, solveA ? 2 : 0);
            for (String ins : instruction) {
                pos = move(pos, ins, keypad);
            }
            code += keypad.get(pos.getLeft()).get(pos.getRight());
        }
        return code;
    }

    private Pair<Integer, Integer> move(Pair<Integer, Integer> pos, String dir, List<List<String>> keypad) {
        int row = pos.getLeft();
        int col = pos.getRight();
        switch (dir) {
            case "U": row = row - 1 < 0 ? row : row - 1; break;
            case "D": row = row + 1 >= keypad.size() ? row : row + 1; break;
            case "L": col = col - 1 < 0 ? col : col - 1; break;
            case "R": col = col + 1 >= keypad.get(row).size() ? col : col + 1; break;
        }
        if (StringUtils.isBlank(keypad.get(row).get(col))) {
            return pos;
        }
        return ImmutablePair.of(row, col);
    }

    private List<List<String>> keypadA() {
        return ImmutableList.of(
                ImmutableList.of("1", "2", "3"),
                ImmutableList.of("4", "5", "6"),
                ImmutableList.of("7", "8", "9"));
    }

    private List<List<String>> keypadB() {
        return ImmutableList.of(
                ImmutableList.of(" ", " ", "1", " ", " "),
                ImmutableList.of(" ", "2", "3", "4", " "),
                ImmutableList.of("5", "6", "7", "8", "9"),
                ImmutableList.of(" ", "A", "B", "C", " "),
                ImmutableList.of(" ", " ", "D", " ", " "));
    }

    private List<List<String>> parse() {
        List<List<String>> instructions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            instructions.add(IntStream.range(0, line.length())
                    .mapToObj(i -> line.substring(i, i+1))
                    .collect(Collectors.toList()));
        }
        return instructions;
    }

}
