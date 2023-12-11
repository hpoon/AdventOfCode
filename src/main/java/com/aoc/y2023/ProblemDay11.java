package com.aoc.y2023;

import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.aoc.SparseMatrix;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.*;
import java.util.stream.IntStream;

public class ProblemDay11 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        SparseMatrix<String> universe = parse();
        SparseMatrix<String> expandedUniverse = expand(universe, 2);
        return solve(expandedUniverse);
    }

    @Override
    public Long solveB() {
        SparseMatrix<String> universe = parse();
        SparseMatrix<String> expandedUniverse = expand(universe, 1000000);
        return solve(expandedUniverse);
    }

    private long solve(SparseMatrix<String> universe) {
        List<MatrixElement<String>> galaxies = universe.get("#");
        MultiKeyMap<MatrixElement<String>, Long> distances = new MultiKeyMap<>();
        for (MatrixElement<String> start : galaxies) {
            for (MatrixElement<String> end : galaxies) {
                if (start == end) {
                    continue;
                }
                if (distances.containsKey(end, start)) {
                    continue;
                }
                long distance = universe.manhattanDistance(start, end);
                if (!distances.containsKey(start, end)) {
                    distances.put(start, end, distance);
                } else if (distance < distances.get(start, end)) {
                    distances.put(start, end, distance);
                }
            }
        }
        return distances
                .values()
                .stream()
                .reduce(Long::sum)
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private SparseMatrix<String> expand(SparseMatrix<String> universe, int expansion) {
        Set<Long> emptyRows = new HashSet<>();
        Set<Long> emptyCols = new HashSet<>();
        for (int row = 0; row < universe.height(); row++) {
            int finalRow = row;
            boolean isRowEmpty = IntStream.range(0, universe.width())
                    .boxed()
                    .filter(col -> universe.get(finalRow, col).equals("."))
                    .count()
                    == universe.width();
            if (isRowEmpty) {
                emptyRows.add((long) row);
            }
        }
        for (int col = 0; col < universe.width(); col++) {
            int finalCol = col;
            boolean isColEmpty = IntStream.range(0, universe.height())
                    .boxed()
                    .filter(row -> universe.get(row, finalCol).equals("."))
                    .count()
                    == universe.height();
            if (isColEmpty) {
                emptyCols.add((long) col);
            }
        }
        SparseMatrix<String> expandedUniverse = new SparseMatrix<>();
        int expandedRow = 0;
        for (int row = 0; row < universe.height(); row++) {
            int expandedCol = 0;
            for (int col = 0; col < universe.width(); col++) {
                expandedUniverse.put(new MatrixElement<>(expandedRow, expandedCol, universe.get(row, col)));
                expandedCol += emptyCols.contains((long) col) ? expansion : 1;
            }
            expandedRow += emptyRows.contains((long) row) ? expansion : 1;
        }
        return expandedUniverse;
    }

    private SparseMatrix<String> parse() {
        SparseMatrix<String> universe = new SparseMatrix<>();
        int row = 0;
        while (scanner.hasNextLine()) {
            String rowStr = scanner.nextLine();
            for (int col = 0; col < rowStr.length(); col++) {
                universe.put(new MatrixElement<>(row, col, rowStr.substring(col, col+1)));
            }
            row++;
        }
        return universe;
    }

}
