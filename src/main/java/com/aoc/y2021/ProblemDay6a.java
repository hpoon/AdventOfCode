package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProblemDay6a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<Integer> states = Arrays.stream(scanner.nextLine().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for (int i = 0; i < 80; i++) {
            final int length = states.size();
            for (int j = 0; j < length; j++) {
                final int state = states.get(j);
                if (state == 0) {
                    states.add(8);
                    states.set(j, 6);
                } else {
                    states.set(j, state - 1);
                }
            }
        }

        return states.size();
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day6.txt"));
        return scanner;
    }

}
