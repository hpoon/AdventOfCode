package com.aoc;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

    @Value
    public static class Element<T> {
        private int row;
        private int col;
        private T value;
    }

}
