package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.Range;

public class ProblemDay4 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
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
    public Integer solveB() {
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

}
