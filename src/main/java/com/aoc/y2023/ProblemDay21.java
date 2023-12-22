package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay21 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        Matrix<Character> matrix = parse();
        return solveA(matrix.getValue('S').get(0), matrix, 64L);
    }

    @Override
    public Long solveB() {
        // Input has a property where if the steps reached == width - 1 (input is a square),
        // we have filled everything up to the corner.  If steps reached == width - 2, we've reached "odd" number case
        // In our case, input has width of 131.
        Matrix<Character> matrix = parse();
        int steps = 26501365;
        int height = matrix.height();
        int repeats = steps / height;
        int cornerRemainder = steps - height / 2 - (repeats - 1) * height - 1;
        int outerDiagonalRemainder = steps - repeats * height - 1;
        int innerDiagonalRemainder = height + outerDiagonalRemainder;
        MatrixElement<Character> center = matrix.getValue('S').get(0);
        long oddSteps = solveA(center, matrix, height - 2);
        long evenSteps = solveA(center, matrix, height - 1);
        long total = 0;
        for (int row = 0; row < repeats; row++) {
            int rowRepeats = 2 * row + 1;
            int odds = (repeats % 2 == 0) ? rowRepeats / 2 : rowRepeats / 2 + 1;
            int evens = (repeats % 2 == 0) ? rowRepeats / 2 + 1 : rowRepeats / 2;
            long rowSum = oddSteps * odds + evenSteps * evens;
            total += (row == repeats - 1) ? rowSum : 2 * rowSum;
        }
        total += solveA(new MatrixElement<>(center.getRow(), height - 1, '.'), matrix, cornerRemainder);
        total += solveA(new MatrixElement<>(center.getRow(), 0, '.'), matrix, cornerRemainder);
        total += solveA(new MatrixElement<>(0, center.getCol(), '.'), matrix, cornerRemainder);
        total += solveA(new MatrixElement<>(height - 1, center.getCol(), '.'), matrix, cornerRemainder);

        total += repeats * solveA(new MatrixElement<>(0, 0, '.'), matrix, outerDiagonalRemainder);
        total += repeats * solveA(new MatrixElement<>(0, height -1, '.'), matrix, outerDiagonalRemainder);
        total += repeats * solveA(new MatrixElement<>(height - 1, 0, '.'), matrix, outerDiagonalRemainder);
        total += repeats * solveA(new MatrixElement<>(height - 1, height - 1, '.'), matrix, outerDiagonalRemainder);

        total += (repeats - 1) * solveA(new MatrixElement<>(0, 0, '.'), matrix, innerDiagonalRemainder);
        total += (repeats - 1) * solveA(new MatrixElement<>(0, height -1, '.'), matrix, innerDiagonalRemainder);
        total += (repeats - 1) * solveA(new MatrixElement<>(height - 1, 0, '.'), matrix, innerDiagonalRemainder);
        total += (repeats - 1) * solveA(new MatrixElement<>(height - 1, height - 1, '.'), matrix, innerDiagonalRemainder);

        return total;
    }

    private long solveA(MatrixElement<Character> start, Matrix<Character> matrix, long maxSteps) {
        Queue<Pair<MatrixElement<Character>, Long>> queue = new LinkedList<>();
        queue.add(ImmutablePair.of(start, 0L));
        Set<MatrixElement<Character>> visited = new HashSet<>();
        Matrix<Character> result = matrix.copy();
        if (maxSteps > 0) {
            while (!queue.isEmpty()) {
                Pair<MatrixElement<Character>, Long> pair = queue.poll();
                MatrixElement<Character> position = pair.getLeft();
                long steps = pair.getRight();
                if (visited.contains(position)) {
                    continue;
                }
                visited.add(position);
                if (steps > maxSteps) {
                    continue;
                }
                if (steps >= 0) {
                    queue.addAll(neighbours(position, matrix).stream()
                            .map(pos -> ImmutablePair.of(pos, steps + 1))
                            .collect(Collectors.toSet()));
                    if (steps % 2 == maxSteps % 2) {
                        result.set(position.getRow(), position.getCol(), 'O');
                    }
                }
            }
        }
        return result.score(e -> Objects.equals(e.getValue(), 'O'), s -> s.mapToLong(i -> 1).sum());
    }

    Set<MatrixElement<Character>> neighbours(MatrixElement<Character> element, Matrix<Character> matrix) {
        Set<Point2D> directions = Sets.newHashSet(
                new Point2D(-1, 0),
                new Point2D(1, 0),
                new Point2D(0, 1),
                new Point2D(0, -1));
        return directions.stream()
                .map(dir -> new Point2D(dir.getX() + element.getCol(),dir.getY() + element.getRow()))
                .filter(dir -> matrix.withinBounds(dir.getY(), dir.getX()))
                .map(dir -> new MatrixElement<>(
                                        dir.getY(),
                                        dir.getX(),
                                        matrix.getValue(dir.getY(), dir.getX())))
                .filter(elem -> !Objects.equals(elem.getValue(), '#'))
                .collect(Collectors.toSet());
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, row -> row.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

}
