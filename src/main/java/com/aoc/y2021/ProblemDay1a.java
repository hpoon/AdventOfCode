package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProblemDay1a implements ProblemDay<Integer> {

    private Scanner scanner;

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

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day1.txt"));
        return scanner;
    }
}
