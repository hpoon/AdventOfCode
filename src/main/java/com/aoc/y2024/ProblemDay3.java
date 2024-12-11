package com.aoc.y2024;

import com.aoc.ProblemDay;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProblemDay3 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        int sum = 0;
        Pattern pattern = Pattern.compile("mul\\([0-9]+,[0-9]+\\)");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            sum += matcher.results()
                    .map(match -> match.group().replace("mul(", "").replace(")", ""))
                    .map(match -> match.split(","))
                    .map(tokens -> Integer.parseInt(tokens[0]) * Integer.parseInt(tokens[1]))
                    .mapToInt(Integer::intValue).sum();
        }
        return sum;
    }

    @Override
    public Integer solveB() {
        int sum = 0;
        Pattern doMulPattern = Pattern.compile("do\\(\\)");
        Pattern dontMulPattern = Pattern.compile("don't\\(\\)");
        Pattern mulPattern = Pattern.compile("mul\\([0-9]+,[0-9]+\\)");
        boolean doMul = true;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher doMatches = doMulPattern.matcher(line);
            Matcher dontMatches = dontMulPattern.matcher(line);
            Matcher mulMatches = mulPattern.matcher(line);
            Map<Integer, Operand> matches = new TreeMap<>();
            doMatches.results().forEach(result -> matches.put(result.start(), new Operand(Operator.DO_MUL)));
            dontMatches.results().forEach(result -> matches.put(result.start(), new Operand(Operator.DONT_MUL)));
            mulMatches.results().forEach(result -> matches.put(result.start(), new MulOperand(
                    Arrays.stream(result.group().replace("mul(", "").replace(")", "").split(","))
                            .map(Integer::parseInt)
                            .reduce((v1, v2) -> v1 * v2)
                            .orElse(1))));
            for (Operand operand : matches.values()) {
                switch (operand.getOperator()) {
                    case DO_MUL: doMul = true; break;
                    case DONT_MUL: doMul = false; break;
                    case MUL:
                        sum += doMul
                                ? operand.operate()
                                : 0;
                        break;
                }
            }
        }
        return sum;
    }

    private enum Operator {
        DO_MUL, DONT_MUL, MUL
    }

    @AllArgsConstructor
    private static class Operand {
        @Getter protected Operator operator;

        protected int operate() {
            return 0;
        }
    }

    private static class MulOperand extends Operand {
        private int result;

        public MulOperand(int result) {
            super(Operator.MUL);
            this.result = result;
        }

        @Override
        public int operate() {
            return this.result;
        }

    }

}
