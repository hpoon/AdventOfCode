package com.aoc.y2015;

import com.aoc.ProblemDay;

public class ProblemDay10 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        String input = parse();
        return solve(input, 40);
    }

    @Override
    protected Integer solveB() {
        String input = parse();
        return solve(input, 50);
    }

    private int solve(String input, int iterations) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            output = new StringBuilder();
            int consecutive = 1;
            int number = input.charAt(0) - '0';
            for (int j = 1; j < input.length(); j++) {
                if (input.charAt(j-1) == input.charAt(j)) {
                    consecutive++;
                } else {
                    output.append(String.format("%d%d", consecutive, number));
                    consecutive = 1;
                    number = input.charAt(j) - '0';
                }
            }
            output.append(String.format("%d%d", consecutive, number));
            input = output.toString();
        }
        return output.length();
    }

    private String parse() {
        return scanner.nextLine();
    }
    
}
