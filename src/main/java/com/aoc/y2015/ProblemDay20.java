package com.aoc.y2015;

import com.aoc.ProblemDay;

public class ProblemDay20 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        int target = parse();
        int house = 0;
        int num = 0;
        while (num < target) {
            num = 0;
            house++;
            for (int i = 1; i <= house; i++) {
                if (house % i == 0) {
                    num += 10 * i;
                }
            }
        }
        return house;
    }

    @Override
    protected Integer solveB() {
        int target = parse();
        int house = 0;
        int num = 0;
        int exhausted = 0;
        while (num < target) {
            num = 0;
            house++;
            for (int i = exhausted + 1; i <= house; i++) {
                if (house % i == 0) {
                    num += 11 * i;
                }
            }
            if (house % (50 * (exhausted + 1)) == 0) {
                exhausted++;
            }
            if (house % 100000 == 0) {
                System.out.printf("House: %d, Presents: %d%n", house, num);
            }
        }
        return house;
    }

    private int parse() {
        return Integer.parseInt(scanner.nextLine());
    }

}
