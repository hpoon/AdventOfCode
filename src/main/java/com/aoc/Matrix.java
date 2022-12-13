package com.aoc;

import com.google.common.collect.Sets;
import lombok.Value;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Matrix<T> {

    private final List<List<T>> matrix;

    public Matrix(Scanner scanner, Function<String, List<T>> rowParser) {
        matrix = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            matrix.add(rowParser.apply(line));
        }
    }

    private Matrix(List<List<T>> matrix) {
        this.matrix = matrix;
    }

    public T get(int row, int col) {
        return matrix.get(row).get(col);
    }

    public T get(Element<T> element) {
        return get(element.getRow(), element.getCol());
    }

    public List<Element<T>> get(T value) {
        List<Element<T>> results = new ArrayList<>();
        for (int row = 0; row < matrix.size(); row++) {
            for (int col = 0; col < matrix.get(row).size(); col++) {
                if (matrix.get(row).get(col).equals(value)) {
                    results.add(new Element<>(row, col, value));
                }
            }
        }
        return results;
    }

    public int height() {
        return matrix.size();
    }

    public int width(int row) {
        return matrix.get(row).size();
    }

    public Matrix<T> apply(Function<Element<T>, T> kernel) {
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < matrix.size(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int col = 0; col < matrix.get(row).size(); col++) {
                resultRow.add(kernel.apply(new Element<>(row, col, get(row, col))));
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    public <U> U score(Predicate<Element<T>> condition,
                      Function<Stream<Element<T>>, U> scoringFunction) {
        return scoringFunction.apply(IntStream.range(0, matrix.size())
                .boxed()
                .flatMap(row -> IntStream.range(0, matrix.get(row).size())
                        .boxed()
                        .map(col -> new Element<>(row, col, get(row, col)))
                        .filter(condition)));
    }

    public List<Element<T>> bfs(Element<T> start,
                                Element<T> end,
                                BiFunction<T, T, Boolean> isValidPathFunction,
                                boolean includeDiagonals) {
        Queue<Element<T>> queue = new LinkedList<>();
        queue.add(start);
        Set<Element<T>> visited = new HashSet<>();
        visited.add(start);
        Map<Element<T>, Element<T>> childToParent = new HashMap<>();
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
            Element<T> element = queue.poll();
            int row = element.getRow();
            int col = element.getCol();
            T val = element.getValue();
            if (val.equals(end)) {
                break;
            }
            directions.stream()
                    .map(dir -> new Point2D(dir.getX() + col,dir.getY() + row))
                    .filter(dir -> withinBounds(dir.getY(), dir.getX()))
                    .map(dir -> new Element<>(dir.getY(), dir.getX(), get(dir.getY(), dir.getX())))
                    .filter(next -> !visited.contains(next))
                    .filter(next -> isValidPathFunction.apply(val, next.getValue()))
                    .forEach(next -> {
                        queue.add(next);
                        childToParent.put(next, element);
                        visited.add(next);
                    });
        }
        List<Element<T>> path = new ArrayList<>();
        Element<T> element = childToParent.get(end);
        while (element != null) {
            path.add(element);
            element = childToParent.get(element);
        }
        Collections.reverse(path);
        return path;
    }

    private boolean withinBounds(int row, int col) {
        return row >= 0 && row < matrix.size() && col >= 0 && col < matrix.get(row).size();
    }

    @Value
    public static class Element<T> {
        private int row;
        private int col;
        private T value;
    }

}
