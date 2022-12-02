package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProblemDay1a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<Integer> elves = new ArrayList<>();
        int sum = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (Util.isBlank(line)) {
                elves.add(sum);
                sum = 0;
            } else {
                final int cals = Integer.parseInt(line);
                sum += cals;
            }
        }
        return elves.stream().max(Comparator.naturalOrder()).orElse(0);
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day1.txt"));
        return scanner;
    }
}
