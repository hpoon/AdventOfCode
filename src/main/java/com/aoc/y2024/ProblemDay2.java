package com.aoc.y2024;

import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay2 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<List<Integer>> reports = parse();
        int safes = 0;
        for (List<Integer> report : reports) {
            safes += isSafe(report) ? 1 : 0;
        }
        return safes;
    }

    @Override
    public Integer solveB() {
        List<List<Integer>> reports = parse();
        int safes = 0;
        for (List<Integer> report : reports) {
            boolean safe = isSafe(report);
            if (safe) {
                safes++;
                continue;
            }
            for (int i = 0; i < report.size(); i++) {
                int finalI = i;
                List<Integer> reduced = IntStream.range(0, report.size()).filter(j -> finalI != j)
                        .boxed()
                        .map(report::get)
                        .collect(Collectors.toList());
                safe = isSafe(reduced);
                if (safe) {
                    safes++;
                    break;
                }
            }
        }
        return safes;
    }

    private static boolean isSafe(List<Integer> report) {
        boolean increasing = report.get(0) < report.get(report.size() - 1);
        boolean safe = true;
        for (int i = 1; i < report.size(); i++) {
            int diff = Math.abs(report.get(i) - report.get(i-1));
            safe = increasing
                    ? report.get(i) > report.get(i - 1)
                    : report.get(i) < report.get(i - 1);
            safe &= diff >= 1 && diff <= 3;
            if (!safe) {
                return false;
            }
        }
        return true;
    }

    private List<List<Integer>> parse() {
        List<List<Integer>> reports = new ArrayList<>();
        while (scanner.hasNextLine()) {
            List<Integer> report = Arrays.stream(scanner.nextLine().split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            reports.add(report);
        }
        return reports;
    }

}
