package com.aoc.y2022;

import com.aoc.*;

import java.util.*;

public class ProblemDay15a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        Map<Point2D, Point2D> sensorToBeacon = parse();
        List<Diamond2D> los = new ArrayList<>();
        for (Map.Entry<Point2D, Point2D> entry : sensorToBeacon.entrySet()) {
            Point2D sensor = entry.getKey();
            Point2D beacon = entry.getValue();
            int dist = sensor.manhattanDistance(beacon);
            los.add(new Diamond2D(sensor, dist));
        }
        int minX = los.stream().map(Diamond2D::getLeft).min(Comparator.naturalOrder()).orElseThrow();
        int maxX = los.stream().map(Diamond2D::getRight).max(Comparator.naturalOrder()).orElseThrow();
        int y = 2000000;
        int count = 0;
        for (int x = minX; x <= maxX; x++) {
            int finalX = x;
            boolean intersects = los.stream().anyMatch(d -> d.intersects(finalX, y));
            Point2D p = new Point2D(x, y);
            intersects &= sensorToBeacon.keySet().stream().noneMatch(s -> s.equals(p));
            intersects &= sensorToBeacon.values().stream().noneMatch(b -> b.equals(p));
            if (intersects) {
                count++;
            }
        }

        return count;
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
