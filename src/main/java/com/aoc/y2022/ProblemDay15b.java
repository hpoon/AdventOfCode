package com.aoc.y2022;

import com.aoc.Diamond2D;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.google.common.collect.Range;

import java.util.*;

public class ProblemDay15b extends ProblemDay<Long> {

    @Override
    public Long solve() {
        Map<Point2D, Point2D> sensorToBeacon = parse();
        List<Diamond2D> los = new ArrayList<>();
        for (Map.Entry<Point2D, Point2D> entry : sensorToBeacon.entrySet()) {
            Point2D sensor = entry.getKey();
            Point2D beacon = entry.getValue();
            int dist = sensor.manhattanDistance(beacon);
            los.add(new Diamond2D(sensor, dist));
        }

        for (int y = 0; y <= 4000000; y++) {
            for (int x = 0; x <= 4000000; x++) {
                int finalX = x;
                int finalY = y;
                Optional<Diamond2D> intersected = los.stream().filter(d -> d.intersects(finalX, finalY)).findFirst();
                if (intersected.isPresent()) {
                    Range<Integer> xRange = intersected.get().xRangeAtRow(y);
                    x = xRange.upperEndpoint();
                }
                if (intersected.isEmpty()) {
                    return x * 4000000L + y;
                }
            }
        }
        throw new RuntimeException("No solution");
    }

    private Map<Point2D, Point2D> parse() {
        Map<Point2D, Point2D> sensorToBeacon = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine()
                    .replace(": closest beacon is at ", " ")
                    .replace("Sensor at ", "")
                    .replace(",", "")
                    .replace("x=", "")
                    .replace("y=", "");
            String[] parts = line.split(" ");
            sensorToBeacon.put(
                    new Point2D(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1])),
                    new Point2D(
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3])));
        }
        return sensorToBeacon;
    }

}
