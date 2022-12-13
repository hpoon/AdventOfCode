package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.util.LinkedList;
import java.util.Queue;

public class ProblemDay1b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final Queue<Integer> queue = new LinkedList<>();
        int increases = 0;
        int curSum = 0;
        int prevSum = 0;

        for (int i = 0; i < 3; i++) {
            final int current = Integer.parseInt(scanner.nextLine());
            queue.add(current);
            curSum += current;
        }
        prevSum = curSum;

        while (scanner.hasNextLine()) {
            final int current = Integer.parseInt(scanner.nextLine());
            queue.add(current);
            curSum += current;
            curSum -= queue.poll();

            if (curSum > prevSum) {
                increases++;
            }
            prevSum = curSum;
        }
        return increases;
    }

}
