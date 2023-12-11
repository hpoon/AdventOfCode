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
        return matrix.keySet().stream().filter(v -> Objects.equals(v.getValue(), value)).collect(Collectors.toList());
    }

    public boolean contains(MatrixElement<T> value) {
        return matrix.containsKey(value);
    }

    public Range<Integer> xRange() {
        int min = matrix.keySet().stream().map(MatrixElement::getCol).min(Comparator.naturalOrder()).orElseThrow();
        int max = matrix.keySet().stream().map(MatrixElement::getCol).max(Comparator.naturalOrder()).orElseThrow();
        return Range.closed(min, max);
    }

    public Range<Integer> yRange() {
        int min = matrix.keySet().stream().map(MatrixElement::getRow).min(Comparator.naturalOrder()).orElseThrow();
        int max = matrix.keySet().stream().map(MatrixElement::getRow).max(Comparator.naturalOrder()).orElseThrow();
        return Range.closed(min, max);
    }

    public int width() {
        Range<Integer> range = xRange();
        return range.upperEndpoint() - range.lowerEndpoint() + 1;
    }

    public int height() {
        Range<Integer> range = yRange();
        return range.upperEndpoint() - range.lowerEndpoint() + 1;
    }

    public Set<MatrixElement<T>> getElements() {
        return matrix.keySet();
    }

    public void replace(MatrixElement<T> oldElement, MatrixElement<T> newElement) {
        matrix.remove(oldElement);
        matrix.put(newElement, newElement.getValue());
    }

    public void print(String blank) {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                System.out.print(get(row, col) != null ? get(row, col) : blank);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public int manhattanDistance(MatrixElement<T> e1, MatrixElement<T> e2) {
        return Math.abs(e1.getRow() - e2.getRow()) + Math.abs(e1.getCol() - e2.getCol());
    }

}
