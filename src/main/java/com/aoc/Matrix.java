package com.aoc;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public Matrix(List<List<T>> matrix) {
        this.matrix = matrix;
    }

    public void addRow(List<T> row) {
        matrix.add(row);
    }

    public T get(int row, int col) {
        return matrix.get(row).get(col);
    }

    public T get(MatrixElement<T> element) {
        return get(element.getRow(), element.getCol());
    }

    public List<MatrixElement<T>> get(T value) {
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
            if (isValidFunction.apply(get(row, col))) {
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
            if (isValidFunction.apply(get(row, col))) {
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

    public Matrix<T> apply(Function<MatrixElement<T>, T> kernel) {
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < matrix.size(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int col = 0; col < matrix.get(row).size(); col++) {
                resultRow.add(kernel.apply(new MatrixElement<>(row, col, get(row, col))));
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
                        .map(col -> new MatrixElement<>(row, col, get(row, col)))
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
            if (val.equals(end)) {
                break;
            }
            directions.stream()
                    .map(dir -> new Point2D(dir.getX() + col,dir.getY() + row))
                    .filter(dir -> withinBounds(dir.getY(), dir.getX()))
                    .map(dir -> new MatrixElement<>(dir.getY(), dir.getX(), get(dir.getY(), dir.getX())))
                    .filter(next -> !visited.contains(next))
                    .filter(next -> isValidPathFunction.apply(val, next.getValue()))
                    .forEach(next -> {
                        queue.add(next);
                        childToParent.put(next, element);
                        visited.add(next);
                    });
        }
        List<MatrixElement<T>> path = new ArrayList<>();
        MatrixElement<T> element = childToParent.get(end);
        while (element != null) {
            path.add(element);
            element = childToParent.get(element);
        }
        Collections.reverse(path);
        return path;
    }

    public Matrix<T> copy(int row, int col, int width, int height) {
        if (!withinBounds(row, col) || !withinBounds(row + height - 1, col + width - 1)) {
            throw new RuntimeException("Out of bounds");
        }
        Matrix<T> copy = new Matrix<>();
        for (int r = row; r < row + height; r++) {
            List<T> l = new ArrayList<>();
            for (int c = col; c < col + width; c++) {
                l.add(get(r, c));
            }
            copy.addRow(l);
        }
        return copy;
    }

    public boolean withinBounds(int row, int col) {
        return row >= 0 && row < matrix.size() && col >= 0 && col < matrix.get(row).size();
    }

}
