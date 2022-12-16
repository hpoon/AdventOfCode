package com.aoc.y2021;

public class ProblemDay1a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        int prev = -1;
        int increases = 0;
        while (scanner.hasNextLine()) {
            final int current = Integer.parseInt(scanner.nextLine());
            if (current > prev && prev != -1) {
                increases++;
            }
            prev = current;
        }
        return increases;
    }

}
