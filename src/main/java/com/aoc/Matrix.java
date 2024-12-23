package com.aoc;

import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@EqualsAndHashCode
public class Matrix<T> {

    private final List<List<T>> matrix;

    public Matrix() {
        matrix = new ArrayList<>();
    }

    public Matrix(Scanner scanner, Function<String, List<T>> rowParser) {
        matrix = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            matrix.add(rowParser.apply(line));
        }
    }

    public Matrix(int height, int width, T defaultValue) {
        matrix = new ArrayList<>();
        for (int r = 0; r < height; r++) {
            List<T> row = new ArrayList<>();
            for (int c = 0; c < width; c++) {
                row.add(defaultValue);
            }
            matrix.add(row);
        }
    }

    public Matrix(List<List<T>> matrix) {
        this.matrix = matrix;
    }

    public void addRow(List<T> row) {
        matrix.add(row);
    }

    public T getValue(int row, int col) {
        return matrix.get(row).get(col);
    }

    public MatrixElement<T> getElement(int row, int col) {
        return new MatrixElement<>(row, col, getValue(row, col));
    }

    public T getValue(MatrixElement<T> element) {
        return getValue(element.getRow(), element.getCol());
    }

    public List<MatrixElement<T>> getValue(T value) {
        List<MatrixElement<T>> results = new ArrayList<>();
        for (int row = 0; row < matrix.size(); row++) {
            for (int col = 0; col < matrix.get(row).size(); col++) {
                if (matrix.get(row).get(col).equals(value)) {
                    results.add(new MatrixElement<>(row, col, value));
                }
            }
        }
        return results;
    }

    public void set(int row, int col, T value) {
        matrix.get(row).set(col, value);
    }

    public List<Integer> getRowBounds(int row, Function<T, Boolean> isValidFunction) {
        List<Integer> indices = new ArrayList<>();
        for (int col = 0; col < matrix.get(row).size(); col++) {
            if (!withinBounds(row, col)) {
                continue;
            }
            if (isValidFunction.apply(getValue(row, col))) {
                indices.add(col);
            }
        }
        return indices;
    }

    public List<Integer> getColBounds(int col, Function<T, Boolean> isValidFunction) {
        List<Integer> indices = new ArrayList<>();
        for (int row = 0; row < matrix.size(); row++) {
            if (!withinBounds(row, col)) {
                continue;
            }
            if (isValidFunction.apply(getValue(row, col))) {
                indices.add(row);
            }
        }
        return indices;
    }

    public int height() {
        return matrix.size();
    }

    public int width(int row) {
        return matrix.get(row).size();
    }

    public <U> Matrix<U> apply(Function<MatrixElement<T>, U> kernel) {
        List<List<U>> result = new ArrayList<>();
        for (int row = 0; row < matrix.size(); row++) {
            List<U> resultRow = new ArrayList<>();
            for (int col = 0; col < matrix.get(row).size(); col++) {
                resultRow.add(kernel.apply(new MatrixElement<>(row, col, getValue(row, col))));
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    public <U> U score(Predicate<MatrixElement<T>> condition,
                       Function<Stream<MatrixElement<T>>, U> scoringFunction) {
        return scoringFunction.apply(IntStream.range(0, matrix.size())
                .boxed()
                .flatMap(row -> IntStream.range(0, matrix.get(row).size())
                        .boxed()
                        .map(col -> new MatrixElement<>(row, col, getValue(row, col)))
                        .filter(condition)));
    }

    public List<MatrixElement<T>> bfs(MatrixElement<T> start,
                                      MatrixElement<T> end,
                                      BiFunction<T, T, Boolean> isValidPathFunction,
                                      boolean includeDiagonals) {
        Queue<MatrixElement<T>> queue = new LinkedList<>();
        queue.add(start);
        Set<MatrixElement<T>> visited = new HashSet<>();
        visited.add(start);
        Map<MatrixElement<T>, MatrixElement<T>> childToParent = new HashMap<>();
        Set<Point2D> directions = Sets.newHashSet(
                new Point2D(-1, 0),
                new Point2D(1, 0),
                new Point2D(0, 1),
                new Point2D(0, -1));
        if (includeDiagonals) {
            directions.add(new Point2D(-1, -1));
            directions.add(new Point2D(-1, 1));
            directions.add(new Point2D(1, -1));
            directions.add(new Point2D(1, 1));
        }
        while (!queue.isEmpty()) {
            MatrixElement<T> element = queue.poll();
            int row = element.getRow();
            int col = element.getCol();
            T val = element.getValue();
            if (Objects.equals(val, end)) {
                break;
            }
            directions.stream()
                    .map(dir -> new Point2D(dir.getX() + col,dir.getY() + row))
                    .filter(dir -> withinBounds(dir.getY(), dir.getX()))
                    .map(dir -> new MatrixElement<>(dir.getY(), dir.getX(), getValue(dir.getY(), dir.getX())))
                    .filter(next -> !visited.contains(next))
                    .filter(next -> isValidPathFunction.apply(val, next.getValue()))
                    .forEach(next -> {
                        queue.add(next);
                        childToParent.put(next, element);
                        visited.add(next);
                    });
        }
        List<MatrixElement<T>> path = new ArrayList<>();
        MatrixElement<T> element = end;
        while (element != null) {
            path.add(element);
            element = childToParent.get(element);
        }
        Collections.reverse(path);
        return path;
    }

    public Matrix<T> copy() {
        return copy(0, 0, width(0), height());
    }

    public Matrix<T> copy(int row, int col, int width, int height) {
        if (!withinBounds(row, col) || !withinBounds(row + height - 1, col + width - 1)) {
            throw new RuntimeException("Out of bounds");
        }
        Matrix<T> copy = new Matrix<>();
        for (int r = row; r < row + height; r++) {
            List<T> l = new ArrayList<>();
            for (int c = col; c < col + width; c++) {
                l.add(getValue(r, c));
            }
            copy.addRow(l);
        }
        return copy;
    }

    public boolean withinBounds(int row, int col) {
        return row >= 0 && row < matrix.size() && col >= 0 && col < matrix.get(row).size();
    }

    public boolean withinBounds(MatrixElement<T> element) {
        return withinBounds(element.getRow(), element.getCol());
    }

    public void print() {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(row); col++) {
                System.out.print(getValue(row, col));
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public int manhattanDistance(MatrixElement<T> e1, MatrixElement<T> e2) {
        return Math.abs(e1.getRow() - e2.getRow()) + Math.abs(e1.getCol() - e2.getCol());
    }

    public Matrix<T> transpose() {
        Matrix<T> matrix = new Matrix<>();
        IntStream.range(0, width(0))
                .forEach(c -> matrix.addRow(IntStream.range(0, height())
                        .boxed()
                        .map(r -> getValue(r, c))
                        .collect(Collectors.toList())));
        return matrix;
    }

    public Matrix<T> rotateCW() {
        Matrix<T> matrix = new Matrix<>();
        for (int col = 0; col < width(0); col++) {
            List<T> rotated = new ArrayList<>();
            for (int row = height() - 1; row >= 0; row--) {
                rotated.add(getValue(row, col));
            }
            matrix.addRow(rotated);
        }
        return matrix;
    }

    public Matrix<T> rotateCCW() {
        Matrix<T> matrix = new Matrix<>();
        for (int col = width(0) - 1; col >= 0; col--) {
            List<T> rotated = new ArrayList<>();
            for (int row = 0; row < height(); row++) {
                rotated.add(getValue(row, col));
            }
            matrix.addRow(rotated);
        }
        return matrix;
    }

}
