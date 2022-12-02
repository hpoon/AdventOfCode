package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProblemDay2a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        int surface = 0;
        int depth = 0;
        while (scanner.hasNextLine()) {
            final String[] line = scanner.nextLine().split(" ");
            final String command = line[0];
            final int value = Integer.parseInt(line[1]);
            if (command.equals("forward")) {
                surface += value;
            } else if (command.equals("up")) {
                depth -= value;
            } else if (command.equals("down")) {
                depth += value;
            }
        }
        return surface * depth;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day2.txt"));
        return scanner;
    }
}
