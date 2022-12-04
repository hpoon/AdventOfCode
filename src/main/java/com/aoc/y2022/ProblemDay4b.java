package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.Range;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ProblemDay4b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        int intersected = 0;
        while (scanner.hasNextLine()) {
            final String[] assignments = scanner.nextLine().split(",");
            final String[] pair1 = assignments[0].split("-");
            final String[] pair2 = assignments[1].split("-");
            final Range<Integer> range1 = Range.closed(Integer.parseInt(pair1[0]), Integer.parseInt(pair1[1]));
            final Range<Integer> range2 = Range.closed(Integer.parseInt(pair2[0]), Integer.parseInt(pair2[1]));
            if (range1.isConnected(range2)) {
                intersected++;
            }
        }
        return intersected;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day4.txt"));
        return scanner;
    }

    private Set<Character> convertToSet(final String str, int start, int end) {
        final Set<Character> set = new HashSet<>();
        for (int i = start; i < end; i++) {
            set.add(str.charAt(i));
        }
        return set;
    }
}
