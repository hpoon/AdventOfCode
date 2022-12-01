package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemDay1b implements ProblemDay<Integer> {

    private Scanner scanner;

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

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day1.txt"));
        return scanner;
    }
}
