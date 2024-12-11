package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay6 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Character> map = parse();
        MatrixElement<Character> start = map.getValue('^')
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
        return traverse(start, map, false).size();
    }

    @Override
    public Integer solveB() {
        Matrix<Character> map = parse();
        MatrixElement<Character> start = map.getValue('^')
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
        Set<MatrixElement<Character>> solutionA = traverse(start, map, false);
        return map.score(
                e -> solutionA.contains(e) && e.getValue() != '^',
                stream -> stream
                        .map(e -> {
                            Matrix<Character> test = map.copy();
                            test.set(e.getRow(), e.getCol(), '#');
                            return traverse(start, test, true);
                        })
                        .filter(Set::isEmpty)
                        .map(s -> 1)
                        .reduce(Integer::sum)
                        .orElse(0));
    }

    private Set<MatrixElement<Character>> traverse(MatrixElement<Character> current,
                                                   Matrix<Character> map,
                                                   boolean checkForLoops) {
        Set<MatrixElement<Character>> visited = new HashSet<>();
        int steps = 0;
        boolean run = true;
        while (run) {
            steps++;
            int col = current.getCol();
            int row = current.getRow();
            if (map.getValue(row, col) != '#') {
                visited.add(current);
            }
            if (checkForLoops && steps >= map.height() * map.width(0)) {
                return new HashSet<>();
            }
            char dir = current.getValue();
            switch (dir) {
                case '^':
                    row--;
                    if (!map.withinBounds(row, col)) {
                        run = false;
                        break;
                    }
                    if (map.getValue(row, col) == '#') {
                        dir = '>';
                        row++;
                    }
                    break;
                case 'v':
                    row++;
                    if (!map.withinBounds(row, col)) {
                        run = false;
                        break;
                    }
                    if (map.getValue(row, col) == '#') {
                        dir = '<';
                        row--;
                    }
                    break;
                case '<':
                    col--;
                    if (!map.withinBounds(row, col)) {
                        run = false;
                        break;
                    }
                    if (map.getValue(row, col) == '#') {
                        dir = '^';
                        col++;
                    }
                    break;
                case '>':
                    col++;
                    if (!map.withinBounds(row, col)) {
                        run = false;
                        break;
                    }
                    if (map.getValue(row, col) == '#') {
                        dir = 'v';
                        col--;
                    }
                    break;
                default: throw new RuntimeException("Shouldn't happen");
            }
            current = new MatrixElement<>(row, col, dir);
        }
        return visited;
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, row -> row.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

}
