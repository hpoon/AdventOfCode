package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ProblemDay4a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        int fullyEnclosed = 0;
        while (scanner.hasNextLine()) {
            final String[] assignments = scanner.nextLine().split(",");
            final String[] pair1 = assignments[0].split("-");
            final String[] pair2 = assignments[1].split("-");
            final Range<Integer> range1 = Range.closed(Integer.parseInt(pair1[0]), Integer.parseInt(pair1[1]));
            final Range<Integer> range2 = Range.closed(Integer.parseInt(pair2[0]), Integer.parseInt(pair2[1]));
            if (range1.encloses(range2) || range2.encloses(range1)) {
                fullyEnclosed++;
            }
        }
        return fullyEnclosed;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day4.txt"));
        return scanner;
    }

}
