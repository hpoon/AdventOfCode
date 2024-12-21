package com.aoc.y2024;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ProblemDay17 extends ProblemDay<List<Long>, Long> {

    @Override
    public List<Long> solveA() {
        int a = parseRegister();
        int b = parseRegister();
        int c = parseRegister();
        List<Long> program = parseProgram();
        return solveA(a, b, c, program);
    }

    @Override
    public Long solveB() {
        parseRegister();
        int b = parseRegister();
        int c = parseRegister();
        List<Long> program = parseProgram();

        long best = Long.MAX_VALUE;
        Stack<String> stack = new Stack<>();
        LongStream.range(1, 8).forEach(i -> stack.add(String.valueOf(i)));
        while (!stack.isEmpty()) {
            String aStr = stack.pop();
            int solutionIndex = program.size() - aStr.length();
            if (solutionIndex < 0) {
                continue;
            }
            long a = Long.parseLong(aStr + "0".repeat(16 - aStr.length()), 8);
            long increment = 15 - aStr.length() >= 0
                    ? Long.parseLong("1" + "0".repeat(15 - aStr.length()), 8)
                    : 0L;
            for (long i = 0; i < 010L; i++) {
                List<Long> r = solveADecompiled(a, b, c);
                if (r.equals(program)) {
                    best = Math.min(best, a);
                    continue;
                }
                if (r.get(solutionIndex).equals(program.get(solutionIndex))) {
                    stack.add(Long.toOctalString(a).substring(0, aStr.length() + 1));
                }
                a = a + increment;
            }
        }
        return best;
    }

    private List<Long> solveADecompiled(long a, long b, long c) {
        List<Long> result = new ArrayList<>();

        // 2,4,1,7,7,5,1,7,0,3,4,1,5,5,3,0
        do {
            b = a % 8;                          // 2, 4
            b = b ^ 7;                          // 1, 7
            c = (long) (a / Math.pow(2, b));    // 7, 5
            b = b ^ 7;                          // 1, 7
            a = a / 8;                          // 0, 3
            b = b ^ c;                          // 4, x
            result.add(b % 8);                  // 5, 5
        } while (a != 0);
        return result;
    }

    private List<Long> solveA(long a, long b, long c, List<Long> program) {
        long[] instructions = new long[] { 0 };
        long[] registers = new long[] { a, b, c };
        Map<Long, Supplier<Long>> comboOperands = ImmutableMap.<Long, Supplier<Long>>builder()
                .put(0L, () -> 0L)
                .put(1L, () -> 1L)
                .put(2L, () -> 2L)
                .put(3L, () -> 3L)
                .put(4L, () -> registers[0])
                .put(5L, () -> registers[1])
                .put(6L, () -> registers[2])
                .build();
        Map<Long, Function<Long, Long>> operators = ImmutableMap.<Long, Function<Long, Long>>builder()
                .put(0L, operand -> registers[0] = (long)(registers[0] / Math.pow(2, comboOperands.get(operand).get())))
                .put(1L, operand -> registers[1] = registers[1] ^ operand)
                .put(2L, operand -> registers[1] = comboOperands.get(operand).get() % 8)
                .put(3L, operand -> {
                    if (registers[0] == 0) {
                        instructions[0] += 2;
                        return null;
                    }
                    instructions[0] = operand;
                    return null;
                })
                .put(4L, operand -> registers[1] = registers[1] ^ registers[2])
                .put(5L, operand -> comboOperands.get(operand).get() % 8)
                .put(6L, operand -> registers[1] = (int)(registers[0] / Math.pow(2, comboOperands.get(operand).get())))
                .put(7L, operand -> registers[2] = (int)(registers[0] / Math.pow(2, comboOperands.get(operand).get())))
                .build();

        List<Long> results = new ArrayList<>();
        do {
            long instruction = program.get((int) instructions[0]);
            long operand = program.get((int) instructions[0] + 1);
            Long result = operators.get(instruction).apply(operand);
            if (instruction != 3) {
                instructions[0] += 2;
            }
            if (instruction == 5) {
                results.add(result);
            }
        } while (instructions[0] < program.size());
        return results;
    }

    private int parseRegister() {
        return Integer.parseInt(scanner.nextLine()
                .replace("Register A: ", "")
                .replace("Register B: ", "")
                .replace("Register C: ", ""));
    }

    private List<Long> parseProgram() {
        scanner.nextLine();
        return Arrays.stream(scanner.nextLine().replace("Program: ", "").split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    @Value
    private static class State {
        long a;
        long b;
        long c;
        int ins;
        List<Long> results;
    }

}
