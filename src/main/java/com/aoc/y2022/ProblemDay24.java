package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import lombok.Value;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay24 extends ProblemDay<Integer, Integer> {

    private static final Point2D STARTING_LOCATION = new Point2D(1, 0);
    private static final Point2D UP = new Point2D(0, -1);
    private static final Point2D DOWN = new Point2D(0, 1);
    private static final Point2D LEFT = new Point2D(-1, 0);
    private static final Point2D RIGHT = new Point2D(1, 0);
    private static final Map<Character, Point2D> WIND_DIRECTIONS = ImmutableMap.of(
            '>', RIGHT,
            'v', DOWN,
            '<', LEFT,
            '^', UP);

    @Override
    public Integer solveA() {
        Matrix<Character> map = parse();
        Range<Integer> xRange = Range.closed(1, map.width(0) - 2);
        Range<Integer> yRange = Range.closed(1, map.height() - 2);
        Map<Integer, Map<Point2D, Set<Wind>>> winds = precomputeWinds(parseInitialWinds(map), xRange, yRange);
        Point2D endingLocation = new Point2D(map.width(0) - 2, map.height() - 1);
        return dfs(map, winds, STARTING_LOCATION, endingLocation, 0);
    }

    @Override
    public Integer solveB() {
        Matrix<Character> map = parse();
        Range<Integer> xRange = Range.closed(1, map.width(0) - 2);
        Range<Integer> yRange = Range.closed(1, map.height() - 2);
        Map<Integer, Map<Point2D, Set<Wind>>> winds = precomputeWinds(parseInitialWinds(map), xRange, yRange);
        Point2D endingLocation = new Point2D(map.width(0) - 2, map.height() - 1);
        int startToEnd1 = dfs(map, winds, STARTING_LOCATION, endingLocation, 0);
        int endToStart1 = dfs(map, winds, endingLocation, STARTING_LOCATION, startToEnd1);
        return dfs(map, winds, STARTING_LOCATION, endingLocation, endToStart1);
    }

    private int dfs(Matrix<Character> map,
                    Map<Integer, Map<Point2D, Set<Wind>>> winds,
                    Point2D start,
                    Point2D end,
                    int startingTime) {
        Queue<Pair<Point2D, Integer>> queue = new LinkedList<>();
        queue.add(ImmutablePair.of(start, startingTime));
        Set<Pair<Point2D, Integer>> visited = new HashSet<>();
        int bestTime = Integer.MAX_VALUE;
        print(map, winds.get(startingTime % winds.size()), start, end, startingTime);
        while (!queue.isEmpty()) {
            Pair<Point2D, Integer> pair = queue.poll();
            if (visited.contains(pair)) {
                continue;
            }
            Point2D location = pair.getLeft();
            int time = pair.getRight();
            if (location.equals(end) && time < bestTime) {
                bestTime = time;
                continue;
            }
            // Don't bother if the time is already worse than the best
            if (time >= bestTime) {
                continue;
            }
            visited.add(pair);
            Map<Point2D, Set<Wind>> windsAtTime = winds.get((time + 1) % winds.size());
            List<Point2D> validMoves = validMoves(map, windsAtTime, location, start, end);
            queue.addAll(validMoves.stream().map(m -> ImmutablePair.of(m, time + 1)).collect(Collectors.toList()));
        }
        print(map, winds.get(bestTime % winds.size()), end, end, bestTime);
        return bestTime;
    }

    private List<Point2D> validMoves(Matrix<Character> map,
                                     Map<Point2D, Set<Wind>> winds,
                                     Point2D location,
                                     Point2D start,
                                     Point2D end) {
        List<Point2D> validMoves = new ArrayList<>();
        validMoves.add(location.translate(UP));
        validMoves.add(location.translate(DOWN));
        validMoves.add(location.translate(LEFT));
        validMoves.add(location.translate(RIGHT));
        validMoves.add(new Point2D(location));
        // Try the ones that are closest to the target first
        validMoves.sort(Comparator.comparing(end::manhattanDistance));
        return validMoves.stream()
                .filter(loc -> !winds.containsKey(loc))
                .filter(loc -> loc.getX() > 0)
                .filter(loc -> loc.getX() < map.width(0) - 1)
                .filter(loc -> loc.getY() > 0 || loc.equals(start) || loc.equals(end))
                .filter(loc -> loc.getY() < map.height() - 1 || loc.equals(end) || loc.equals(start))
                .collect(Collectors.toList());
    }

    private Map<Integer, Map<Point2D, Set<Wind>>> precomputeWinds(Map<Point2D, Set<Wind>> initialWinds,
                                                                  Range<Integer> xRange,
                                                                  Range<Integer> yRange) {
        Map<Integer, Map<Point2D, Set<Wind>>> windsAtTime = new HashMap<>();
        windsAtTime.put(0, initialWinds);
        int time = 1;
        while (true) {
            Map<Point2D, Set<Wind>> prevWind = windsAtTime.get(time - 1);
            Map<Point2D, Set<Wind>> newWinds = new HashMap<>();
            prevWind.values().stream().flatMap(Set::stream).forEach(w -> {
                Wind newWind = w.increment(xRange, yRange);
                Set<Wind> existingWinds = newWinds.getOrDefault(newWind.getLocation(), new HashSet<>());
                existingWinds.add(newWind);
                newWinds.put(newWind.getLocation(), existingWinds);
            });
            if (newWinds.equals(initialWinds)) {
                break;
            }
            windsAtTime.put(time, newWinds);
            time++;
        }
        return windsAtTime;
    }

    private void print(Matrix<Character> map,
                       Map<Point2D, Set<Wind>> winds,
                       Point2D location,
                       Point2D end,
                       int time) {
        System.out.println(String.format("Time: %d", time));
        for (int row = 0; row < map.height(); row++) {
            for (int col = 0; col < map.width(row); col++) {
                char c;
                Point2D p = new Point2D(col, row);
                if (p.equals(location)) {
                    c = 'x';
                } else if (p.equals(STARTING_LOCATION)) {
                    c = '.';
                } else if (p.equals(end)) {
                    c = '.';
                } else if (row == 0 || row == map.height() - 1) {
                    c = '#';
                } else if (col == 0 || col == map.width(row) - 1) {
                    c = '#';
                } else if (winds.containsKey(p)) {
                    Set<Wind> set = winds.get(p);
                    if (set.size() > 1) {
                        c = (char)(set.size() + '0');
                    } else {
                        Point2D dir = set.iterator().next().getDirection();
                        if (dir.equals(UP)) {
                            c = '^';
                        } else if (dir.equals(DOWN)) {
                            c = 'v';
                        } else if (dir.equals(LEFT)) {
                            c = '<';
                        } else {
                            c = '>';
                        }
                    }
                } else {
                    c = '.';
                }
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, line -> {
            List<Character> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                row.add(line.charAt(i));
            }
            return row;
        });
    }

    private Map<Point2D, Set<Wind>> parseInitialWinds(Matrix<Character> map) {
        Map<Point2D, Set<Wind>> winds = new HashMap<>();
        for (int row = 0; row < map.height(); row++) {
            for (int col = 0; col < map.width(row); col++) {
                char c = map.get(row, col);
                if (WIND_DIRECTIONS.containsKey(c)) {
                    Set<Wind> existingWinds = winds.getOrDefault(new Point2D(col, row), new HashSet<>());
                    Point2D location = new Point2D(col, row);
                    existingWinds.add(new Wind(location, WIND_DIRECTIONS.get(c)));
                    winds.put(location, existingWinds);
                }
            }
        }
        return winds;
    }

    @Value
    private static final class Wind {

        private Point2D location;
        private Point2D direction;

        public Wind increment(Range<Integer> xRange, Range<Integer> yRange) {
            Point2D newLocation = location.translate(direction);
            if (newLocation.getX() > xRange.upperEndpoint()) {
                newLocation = new Point2D(xRange.lowerEndpoint(), newLocation.getY());
            } else if (newLocation.getX() < xRange.lowerEndpoint()) {
                newLocation = new Point2D(xRange.upperEndpoint(), newLocation.getY());
            } else if (newLocation.getY() > yRange.upperEndpoint()) {
                newLocation = new Point2D(newLocation.getX(), yRange.lowerEndpoint());
            } else if (newLocation.getY() < yRange.lowerEndpoint()) {
                newLocation = new Point2D(newLocation.getX(), yRange.upperEndpoint());
            }
            return new Wind(newLocation, direction);
        }

    }

}
