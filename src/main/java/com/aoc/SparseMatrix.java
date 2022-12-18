package com.aoc;

import com.google.common.collect.Range;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SparseMatrix<T> {

    private final Map<MatrixElement<T>, T> matrix;

    public SparseMatrix(Scanner scanner, Function<String, List<MatrixElement<T>>> rowParser) {
        matrix = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            rowParser.apply(line).forEach(element -> matrix.put(element, element.getValue()));
        }
    }

    public SparseMatrix() {
        matrix = new HashMap<>();
    }

    public void put(MatrixElement<T> element) {
        matrix.put(element, element.getValue());
    }

    public T get(int row, int col) {
        return matrix.get(new MatrixElement<T>(row, col, null));
    }

    public T get(MatrixElement<T> element) {
        return get(element.getRow(), element.getCol());
    }

    public List<MatrixElement<T>> get(T value) {
        return matrix.keySet().stream().filter(v -> v.equals(value)).collect(Collectors.toList());
    }

    public boolean contains(MatrixElement<T> value) {
        return matrix.containsKey(value);
    }

    public Range<Integer> yRange() {
        int min = matrix.keySet().stream().map(MatrixElement::getRow).min(Comparator.naturalOrder()).orElseThrow();
        int max = matrix.keySet().stream().map(MatrixElement::getRow).max(Comparator.naturalOrder()).orElseThrow();
        return Range.closed(min, max);
    }

}
