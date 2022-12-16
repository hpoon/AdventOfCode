package com.aoc.y2021;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Arrays;

public class ProblemDay17a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        // target area: x=169..206, y=-108..-68
        final String line = scanner.nextLine();
        final String[] split = line.split(", ");
        final String[] splitX = split[0].split("\\.\\.");
        final Pair<Integer, Integer> goalX = ImmutablePair.of(
                Integer.parseInt(splitX[0].substring(15)),
                Integer.parseInt(splitX[1]));

        final String[] splitY = split[1].split("\\.\\.");
        final Pair<Integer, Integer> goalY = ImmutablePair.of(
                Integer.parseInt(splitY[0].substring(2)),
                Integer.parseInt(splitY[1]));

        final int a = -1;
        int[][] max = new int[512][512];

        for (int vy0 = 0; vy0 < max.length; vy0++) {
            for (int vx0 = 0; vx0 < max[vy0].length; vx0++) {
                int y = 0;
                int vy = vy0;
                int maxY = 0;

                int x = 0;
                int vx = vx0;

                while (true) {
                    y += vy;
                    vy += a;

                    x += vx;
                    vx += Integer.compare(0, vx);

                    if (y > maxY) {
                        maxY = y;
                    }

                    if (y >= goalY.getKey() && y <= goalY.getValue() && x >= goalX.getKey() && x <= goalX.getValue()) {
                        max[vy0][vx0] = maxY;
                        break;
                    }

                    if (y < goalY.getKey()) {
                        break;
                    }
                }
            }
        }

        return Arrays.stream(max).flatMapToInt(Arrays::stream).max().getAsInt();
    }
}
