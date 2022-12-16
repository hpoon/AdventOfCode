package com.aoc.y2021;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.HashSet;
import java.util.Set;

public class ProblemDay17b extends ProblemDay<Integer> {

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
        final int searchSpace = 256;

        final Set<Pair<Integer, Integer>> works = new HashSet<>();
        for (int vy0 = -searchSpace; vy0 < searchSpace; vy0++) {
            for (int vx0 = -searchSpace; vx0 < searchSpace; vx0++) {
                int y = 0;
                int vy = vy0;

                int x = 0;
                int vx = vx0;

                while (true) {
                    y += vy;
                    vy += a;

                    x += vx;
                    vx += Integer.compare(0, vx);

                    if (y >= goalY.getKey() && y <= goalY.getValue() && x >= goalX.getKey() && x <= goalX.getValue()) {
                        works.add(ImmutablePair.of(vx0, vy0));
                        break;
                    }

                    if (y < goalY.getKey()) {
                        break;
                    }
                }
            }
        }

        return works.size();
    }
}
