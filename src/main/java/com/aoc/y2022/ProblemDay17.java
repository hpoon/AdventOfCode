package com.aoc.y2022;

import com.aoc.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay17 extends ProblemDay<Integer, Long> {

    private static List<RockShape> ROCK_SHAPES = Arrays.stream(RockShape.values()).collect(Collectors.toList());
    private static Point2D SPAWN = new Point2D(2, 3);
    private static int WIDTH = 7;

    @Override
    public Integer solveA() {
        List<Character> winds = parse();
        return solve(winds, 2022, 0, 0, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public Long solveB() {
        List<Character> winds = parse();
        List<Integer> rockIdxWithFlatStack = new ArrayList<>();
        List<Integer> windIdxWithFlatStack = new ArrayList<>();
        long check = solve(
                winds,
                100000,
                0,
                0,
                rockIdxWithFlatStack,
                windIdxWithFlatStack);

        // Based on my inputs, there is a flat stack with same wind at i=460, i=2195, i=3930
        // So the macrocycle is actually every 1735 iterations, we get back to where we started
        // So we need to get how many repeats there are here
        int rocksAtCycleStart = rockIdxWithFlatStack.stream().min(Comparator.naturalOrder()).orElseThrow() + 1;
        int cycleIter = 1735;
        int rocksAtCycleEnd = rocksAtCycleStart + cycleIter;

        int heightAtCycleStart = solve(
                winds,
                rocksAtCycleStart,
                0,
                0,
                new ArrayList<>(),
                new ArrayList<>());
        int heightAtCycleEnd = solve(
                winds,
                rocksAtCycleEnd,
                0,
                0,
                new ArrayList<>(),
                new ArrayList<>());
        int heightDiff = heightAtCycleEnd - heightAtCycleStart;

        long maxRocks = 1000000000000L;
        long repeats = (maxRocks - rocksAtCycleStart) / cycleIter;
        long remainder = (maxRocks - rocksAtCycleStart) % cycleIter;

        int remainderHeight = solve(
                winds,
                (int) remainder,
                1,      // After a flat stack, next piece is the + piece
                2672,   // Cycle ends at windIdx 2671, so start at the next one
                new ArrayList<>(),
                new ArrayList<>());
        return heightAtCycleStart + repeats * heightDiff + remainderHeight;
    }

    private int solve(List<Character> winds,
                      int rocks,
                      int startingRockIdx,
                      int startingWindIdx,
                      List<Integer> rockIdxWithFlatStack,
                      List<Integer> windIdxWithFlatStack) {
        SparseMatrix<Character> grid = new SparseMatrix<>();
        int windIdx = startingWindIdx;
        Map<Integer, Integer> xToTop = new HashMap<>();
        IntStream.range(0, WIDTH).forEach(x -> xToTop.put(x, -1));
        for (int rockIdx = startingRockIdx; rockIdx < rocks + startingRockIdx; rockIdx++) {
            RockShape rockShape = ROCK_SHAPES.get(rockIdx % ROCK_SHAPES.size());
            int topOfStack = xToTop.values().stream().max(Comparator.naturalOrder()).orElseThrow();
            Rock rock = new Rock(rockShape, new Point2D(SPAWN.getX(), topOfStack + SPAWN.getY() + 1));
            while (true) {
                char wind = winds.get(windIdx % winds.size());
                windIdx++;
                rock.push(grid, wind);
                List<MatrixElement<Character>> newFallLocation = rock.fall(grid);
                if (CollectionUtils.isEmpty(newFallLocation)) {
                    rock.getLocation().forEach(grid::put);
                    rock.getLocation().forEach(l -> {
                        int existing = xToTop.get(l.getCol());
                        if (l.getRow() > existing) {
                            xToTop.put(l.getCol(), l.getRow());
                        }
                    });
                    recordIfFlatStack(
                            winds,
                            xToTop,
                            rockIdx,
                            windIdx-1,
                            rockIdxWithFlatStack,
                            windIdxWithFlatStack);
                    break;
                }
            }
        }
        int yMax = grid.yRange().upperEndpoint();
        printState(grid, yMax-25, yMax);
        return xToTop.values().stream().max(Comparator.naturalOrder()).orElse(0) + 1;
    }

    private void recordIfFlatStack(List<Character> winds,
                                   Map<Integer, Integer> xToTop,
                                   int rockIdx,
                                   int windIdx,
                                   List<Integer> rockIdxWithFlatStack,
                                   List<Integer> windIdxWithFlatStack) {
        long count = new HashSet<>(xToTop.values()).stream().filter(v -> v > 0).count();
        int windIdxModulo = windIdx % winds.size();
        if (count == 1 && windIdxModulo == 2671) {
            rockIdxWithFlatStack.add(rockIdx);
            windIdxWithFlatStack.add(windIdx % winds.size());
        }
    }

    private void printState(SparseMatrix<Character> grid, int yMin, int yMax) {
        StringBuilder sb = new StringBuilder();
        for (int y = yMax; y >= yMin; y--) {
            for (int x = -1; x < 8; x++) {
                if (y == -1 && (x == -1 || x == WIDTH)) {
                    sb.append('+');
                } else if (y == -1) {
                    sb.append('-');
                } else {
                    MatrixElement<Character> e = new MatrixElement<>(y, x, '#');
                    if (x == -1 || x == WIDTH) {
                        sb.append('|');
                    } else if (grid.contains(e)) {
                        sb.append(e.getValue());
                    } else {
                        sb.append('.');
                    }
                }
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

    private List<Character> parse() {
        return scanner.nextLine().chars().mapToObj(i -> (char) i).collect(Collectors.toList());
    }

    private enum RockShape {
        H_LINE,
        PLUS,
        ANGLE,
        V_LINE,
        BOX
    }

    private static class Rock {

        private static final List<Point2D> H_LINE = ImmutableList.of(
                new Point2D(0, 0), new Point2D(1, 0), new Point2D(2, 0), new Point2D(3, 0));
        private static final List<Point2D> PLUS = ImmutableList.of(
                new Point2D(0, 1), new Point2D(1, 1), new Point2D(2, 1), new Point2D(1, 2), new Point2D(1, 0));
        private static final List<Point2D> ANGLE = ImmutableList.of(
                new Point2D(0, 0), new Point2D(1, 0), new Point2D(2, 0), new Point2D(2, 1), new Point2D(2, 2));
        private static final List<Point2D> V_LINE = ImmutableList.of(
                new Point2D(0, 0), new Point2D(0, 1), new Point2D(0, 2), new Point2D(0, 3));
        private static final List<Point2D> BOX = ImmutableList.of(
                new Point2D(0, 0), new Point2D(1, 0), new Point2D(0, 1), new Point2D(1, 1));

        private static final Map<RockShape, List<Point2D>> SHAPE_TO_POINTS = ImmutableMap.of(
                RockShape.H_LINE, H_LINE,
                RockShape.PLUS, PLUS,
                RockShape.ANGLE, ANGLE,
                RockShape.V_LINE, V_LINE,
                RockShape.BOX, BOX);

        private List<MatrixElement<Character>> points;

        public Rock(RockShape rockShape, Point2D spawnOffset) {
            this.points = convert(SHAPE_TO_POINTS.get(rockShape));
            this.points = translateUncheckedMatrix(spawnOffset);
        }

        public List<MatrixElement<Character>> getLocation() {
            return points;
        }

        public List<MatrixElement<Character>> fall(SparseMatrix<Character> grid) {
            List<MatrixElement<Character>> newPoints = translateUncheckedMatrix(new Point2D(0, -1));
            return getAndUpdateIfValid(newPoints, grid);
        }

        public List<MatrixElement<Character>> push(SparseMatrix<Character> grid,
                                                   char wind) {
            Point2D offset;
            switch (wind) {
                case '<': offset = new Point2D(-1, 0); break;
                case '>': offset = new Point2D(1, 0); break;
                default: throw new RuntimeException(String.format("Unsupported direction: %c", wind));
            }
            List<MatrixElement<Character>> newPoints = translateUncheckedMatrix(offset);
            return getAndUpdateIfValid(newPoints, grid);
        }

        private List<MatrixElement<Character>> translateUncheckedMatrix(Point2D offset) {
            return points.stream()
                    .map(e -> new MatrixElement<>(e.getRow() + offset.getY(), e.getCol() + offset.getX(), '#'))
                    .collect(Collectors.toList());
        }

        private List<MatrixElement<Character>> convert(List<Point2D> points) {
            return points.stream()
                    .map(p -> new MatrixElement<>(p.getY(), p.getX(), '#'))
                    .collect(Collectors.toList());
        }

        private List<MatrixElement<Character>> getAndUpdateIfValid(List<MatrixElement<Character>> newPoints,
                                                                    SparseMatrix<Character> grid) {
            if (!validMove(newPoints, grid)) {
                return ImmutableList.of();
            } else {
                this.points = newPoints;
                return getLocation();
            }
        }

        private boolean validMove(List<MatrixElement<Character>> newPoints,
                                  SparseMatrix<Character> grid) {
            boolean isCollision = newPoints.stream().anyMatch(grid::contains);
            if (isCollision) {
                return false;
            }
            boolean isPastEdge = newPoints.stream()
                    .anyMatch(e -> e.getCol() >= WIDTH || e.getCol() < 0 || e.getRow() < 0);
            return !isPastEdge;
        }

    }

}
