package com.aoc.y2024;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay7 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Equation> equations = parse();
        List<Character> operators = ImmutableList.of('+', '*');
        return equations.stream().map(e -> solveA(e, operators)).reduce(Math::addExact).orElse(0L);
    }

    @Override
    public Long solveB() {
        List<Equation> equations = parse();
        List<Character> operators = ImmutableList.of('+', '*', '|');
        return equations.stream().map(e -> solveA(e, operators)).reduce(Math::addExact).orElse(0L);
    }

    private long solveA(Equation equation, List<Character> operators) {
        if (!solveA(equation.getOperands().get(0), 1, operators, equation)) {
            return 0;
        }
        return equation.getAnswer();
    }

    private boolean solveA(long current, int index, List<Character> operators, Equation equation) {
        if (current > equation.getAnswer()) {
            return false;
        }
        if (current == equation.getAnswer() && index == equation.getOperands().size()) {
            return true;
        }
        if (index >= equation.getOperands().size()) {
            return false;
        }
        boolean found = false;
        for (char operator : operators) {
            long operand = equation.getOperands().get(index);
            switch (operator) {
                case '+':
                    found |= solveA(Math.addExact(current, operand), index + 1, operators, equation);
                    break;
                case '*':
                    found |= solveA(Math.multiplyExact(current, operand), index + 1, operators, equation);
                    break;
                case '|':
                    found |= solveA(Long.parseLong(String.valueOf(current) + operand), index + 1, operators, equation);
                    break;
                default:
                    throw new RuntimeException("Shouldn't happen");
            }
        }
        return found;
    }

    private List<Equation> parse() {
        List<Equation> equations = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(":");
            long answer = Long.parseLong(tokens[0]);
            List<Long> operands = Arrays.stream(tokens[1].split(" "))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            equations.add(new Equation(answer, operands));
        }
        return equations;
    }

    @Value
    private static class Equation {
        private long answer;
        private List<Long> operands;
    }

}
