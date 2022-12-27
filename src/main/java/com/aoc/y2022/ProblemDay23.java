package com.aoc.y2022;

import com.aoc.MatrixElement;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.aoc.SparseMatrix;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay23 extends ProblemDay<Integer, Integer> {

    private static final int TOTAL_ROUNDS = 10;
    private static final Set<Point2D> N_NE_NW = ImmutableSet.of(
            new Point2D(0, -1), new Point2D(1, -1), new Point2D(-1, -1));
    private static final Set<Point2D> S_SE_SW = ImmutableSet.of(
            new Point2D(0, 1), new Point2D(1, 1), new Point2D(-1, 1));
    private static final Set<Point2D> W_NW_SW = ImmutableSet.of(
            new Point2D(-1, 0), new Point2D(-1, -1), new Point2D(-1, 1));
    private static final Set<Point2D> E_NE_SE = ImmutableSet.of(
            new Point2D(1, 0), new Point2D(1, -1), new Point2D(1, 1));

    @Override
    public Integer solveA() {
        SparseMatrix<Elf> map = parse();
        solve(map, TOTAL_ROUNDS);
        int width = map.width();
        int height = map.height();
        int occupied = map.getElements().size();
        return width * height - occupied;
    }

    @Override
    public Integer solveB() {
        SparseMatrix<Elf> map = parse();
        return solve(map, -1);
    }

    private int solve(SparseMatrix<Elf> map, int maxRounds) {
        int round = 1;
        while (true) {
            Map<MatrixElement<Elf>, Set<MatrixElement<Elf>>> destinationsToElves = new HashMap<>();
            for (MatrixElement<Elf> element : map.getElements()) {
                Elf elf = element.getValue();
                List<Optional<Point2D>> results = elf.evaluatePossibleMoves(element.getRow(), element.getCol());
                if (results.stream().allMatch(Optional::isPresent)
                        || results.stream().noneMatch(Optional::isPresent)) {
                    continue;
                }
                for (int i = 0; i < 4; i++) {
                    if (results.get(i).isPresent()) {
                        Point2D newLocation = results.get(i).get();
                        MatrixElement<Elf> newElement = new MatrixElement<>(
                                newLocation.getY(), newLocation.getX(), elf);
                        Set<MatrixElement<Elf>> elvesAtLocation = destinationsToElves.getOrDefault(
                                newElement, new HashSet<>());
                        elvesAtLocation.add(new MatrixElement<>(element.getRow(), element.getCol(), elf));
                        destinationsToElves.put(newElement, elvesAtLocation);
                        break;
                    }
                }
            }
            if (destinationsToElves.isEmpty()) {
                break;
            }
            for (Map.Entry<MatrixElement<Elf>, Set<MatrixElement<Elf>>> entry : destinationsToElves.entrySet()) {
                MatrixElement<Elf> newElement = entry.getKey();
                Set<MatrixElement<Elf>> elves = entry.getValue();
                if (elves.size() > 1) {
                    continue;
                } else {
                    map.replace(elves.iterator().next(), newElement);
                }
            }
            if (maxRounds > 0 && round == maxRounds) {
                break;
            }
            round++;
        }
        print(map);
        return round;
    }

    private SparseMatrix<Elf> parse() {
        SparseMatrix<Elf> map = new SparseMatrix<>();
        int row = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '#') {
                    Elf elf = new Elf();
                    Function<Point2D, Optional<Point2D>> top = p -> translate(
                            map, N_NE_NW, elf, p, new Point2D(0, -1));
                    Function<Point2D, Optional<Point2D>> bottom = p -> translate(
                            map, S_SE_SW, elf, p, new Point2D(0, 1));
                    Function<Point2D, Optional<Point2D>> left = p -> translate(
                            map, W_NW_SW, elf, p, new Point2D(-1, 0));
                    Function<Point2D, Optional<Point2D>> right = p -> translate(
                            map, E_NE_SE, elf, p, new Point2D(1, 0));
                    elf.pushBackCondition(top);
                    elf.pushBackCondition(bottom);
                    elf.pushBackCondition(left);
                    elf.pushBackCondition(right);
                    map.put(new MatrixElement<>(row, col, elf));
                }
            }
            row++;
        }
        return map;
    }

    private Optional<Point2D> translate(SparseMatrix<Elf> map,
                                        Set<Point2D> directions,
                                        Elf elf,
                                        Point2D position,
                                        Point2D movement) {
        boolean isOccupied = directions.stream().map(position::translate).anyMatch(p -> contains(map, elf, p));
        return isOccupied ? Optional.empty() : Optional.of(position.translate(movement));
    }

    private boolean contains(SparseMatrix<Elf> map, Elf elf, Point2D position) {
        return map.contains(new MatrixElement<>(position.getY(), position.getX(), elf));
    }

    @Value
    private static class Elf {

        @EqualsAndHashCode.Exclude
        private final Deque<Function<Point2D, Optional<Point2D>>> conditionals;

        public Elf() {
            conditionals = new ArrayDeque<>();
        }

        public List<Optional<Point2D>> evaluatePossibleMoves(int row, int col) {
            List<Optional<Point2D>> possibleMoves = conditionals.stream()
                    .map(p -> p.apply(new Point2D(col, row)))
                    .collect(Collectors.toList());
            conditionals.addLast(conditionals.pollFirst());
            return possibleMoves;
        }

        public void pushBackCondition(Function<Point2D, Optional<Point2D>> function) {
            conditionals.addLast(function);
        }

    }

    public void print(SparseMatrix<Elf> map) {
        Range<Integer> xRange = map.xRange();
        Range<Integer> yRange = map.yRange();
        for (int row = yRange.lowerEndpoint(); row <= yRange.upperEndpoint(); row++) {
            for (int col = xRange.lowerEndpoint(); col <= xRange.upperEndpoint(); col++) {
                if (map.contains(new MatrixElement<>(row, col, new Elf()))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
