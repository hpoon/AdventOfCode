package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProblemDay4 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        Matrix<Character> matrix = parse();
        return matrix.score(
                element -> true,
                e -> e.mapToLong(element -> findXmasA(element, matrix)).sum());
    }

    @Override
    public Long solveB() {
        Matrix<Character> matrix = parse();
        return matrix.score(
                element -> findXmasB(element.getRow(), element.getCol(), 0, 'A', matrix),
                Stream::count);
    }

    private long findXmasA(MatrixElement<Character> element, Matrix<Character> matrix) {
        int[][] dirs = new int[][] {
                { 1, 0 },
                { -1, 0 },
                { 0, 1 },
                { 0, -1 },
                { 1, -1 },
                { 1, 1 },
                { -1, -1 },
                { -1, 1 },
        };
        return Arrays.stream(dirs)
                .map(dir -> findXmasA(element.getRow(), element.getCol(), dir[0], dir[1], 0, "XMAS", new boolean[matrix.height()][matrix.width(0)], matrix))
                .filter(res -> res)
                .count();
    }

    private boolean findXmasA(int row, int col, int rowDir, int colDir, int index, String target, boolean[][] visited, Matrix<Character> matrix) {
        if (row < 0 || row >= matrix.height() || col < 0 || col >= matrix.width(row)) {
            return false;
        }
        if (visited[row][col]) {
            return false;
        }
        char current = matrix.getValue(row, col);
        if (index == target.length() - 1 && current == target.charAt(index)) {
            return true;
        }
        if (index == target.length() - 1) {
            return false;
        }
        if (current != target.charAt(index)) {
            return false;
        }
        visited[row][col] = true;
        return findXmasA(row + rowDir, col + colDir, rowDir, colDir, index + 1, target, visited, matrix);
    }

    private boolean findXmasB(int row, int col, int index, char target, Matrix<Character> matrix) {
        if (row < 0 || row >= matrix.height() || col < 0 || col >= matrix.width(row)) {
            return false;
        }
        char current = matrix.getValue(row, col);
        if (index == 1 && current == target) {
            return true;
        }
        if (index == 1) {
            return false;
        }
        if (current != target) {
            return false;
        }
        boolean result =
                (findXmasB(row - 1, col - 1, index + 1, 'M', matrix) &&
                 findXmasB(row - 1, col + 1, index + 1, 'S', matrix) &&
                 findXmasB(row + 1, col - 1, index + 1, 'M', matrix) &&
                 findXmasB(row + 1, col + 1, index + 1, 'S', matrix)) ||
                (findXmasB(row - 1, col - 1, index + 1, 'S', matrix) &&
                 findXmasB(row - 1, col + 1, index + 1, 'M', matrix) &&
                 findXmasB(row + 1, col - 1, index + 1, 'S', matrix) &&
                 findXmasB(row + 1, col + 1, index + 1, 'M', matrix)) ||
                (findXmasB(row - 1, col - 1, index + 1, 'M', matrix) &&
                 findXmasB(row - 1, col + 1, index + 1, 'M', matrix) &&
                 findXmasB(row + 1, col - 1, index + 1, 'S', matrix) &&
                 findXmasB(row + 1, col + 1, index + 1, 'S', matrix)) ||
                (findXmasB(row - 1, col - 1, index + 1, 'S', matrix) &&
                 findXmasB(row - 1, col + 1, index + 1, 'S', matrix) &&
                 findXmasB(row + 1, col - 1, index + 1, 'M', matrix) &&
                 findXmasB(row + 1, col + 1, index + 1, 'M', matrix));
        return result;
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, row -> row.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

}
