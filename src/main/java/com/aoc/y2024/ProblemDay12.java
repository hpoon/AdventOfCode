package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay12 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Character> garden = parse();
        List<Matrix<Boolean>> sections = mapSections(garden);
        return score(sections, true);
    }

    @Override
    public Integer solveB() {
        Matrix<Character> garden = parse();
        List<Matrix<Boolean>> sections = mapSections(garden);
        return score(sections, false);
    }

    private int score(List<Matrix<Boolean>> sections, boolean usePerimeter) {
        int score = 0;
        for (Matrix<Boolean> section : sections) {
            int area = calculateArea(section);
            int perimeter = usePerimeter
                    ? calculatePerimeter(section)
                    : calculateSides(section);
            score = Math.addExact(score, Math.multiplyExact(area, perimeter));
        }
        return score;
    }

    private int calculateArea(Matrix<Boolean> section) {
        Queue<MatrixElement<Boolean>> bfs = new LinkedList<>();
        bfs.add(section.score(e -> e.getValue(), stream -> stream).findFirst().get());
        Matrix<Boolean> visited = section.copy().apply(e -> false);
        int area = 0;
        while (!bfs.isEmpty()) {
            MatrixElement<Boolean> e = bfs.poll();
            int row = e.getRow();
            int col = e.getCol();
            if (!section.withinBounds(e.getRow(), e.getCol())) {
                continue;
            }
            if (visited.getValue(e.getRow(), e.getCol())) {
                continue;
            }
            MatrixElement<Boolean> element = section.getElement(row, col);
            if (!element.getValue()) {
                continue;
            }
            visited.set(row, col, true);
            area++;
            bfs.add(new MatrixElement<>(row + 1, col, null));
            bfs.add(new MatrixElement<>(row - 1, col, null));
            bfs.add(new MatrixElement<>(row, col + 1, null));
            bfs.add(new MatrixElement<>(row, col - 1, null));
        }
        return area;
    }

    private int calculatePerimeter(Matrix<Boolean> section) {
        Queue<MatrixElement<Boolean>> bfs = new LinkedList<>();
        bfs.add(section.score(e -> e.getValue(), stream -> stream).findFirst().get());
        Matrix<Boolean> visited = section.copy().apply(e -> false);
        int perimeter = 0;
        while (!bfs.isEmpty()) {
            MatrixElement<Boolean> e = bfs.poll();
            int row = e.getRow();
            int col = e.getCol();
            if (!section.withinBounds(e.getRow(), e.getCol())) {
                continue;
            }
            if (visited.getValue(e.getRow(), e.getCol())) {
                continue;
            }
            MatrixElement<Boolean> element = section.getElement(row, col);
            if (!element.getValue()) {
                continue;
            }
            visited.set(row, col, true);

            perimeter += section.withinBounds(row + 1, col) && !section.getValue(row + 1, col) ? 1 : 0;
            perimeter += section.withinBounds(row - 1, col) && !section.getValue(row - 1, col) ? 1 : 0;
            perimeter += section.withinBounds(row, col + 1) && !section.getValue(row, col + 1) ? 1 : 0;
            perimeter += section.withinBounds(row, col - 1) && !section.getValue(row, col - 1) ? 1 : 0;

            bfs.add(new MatrixElement<>(row + 1, col, null));
            bfs.add(new MatrixElement<>(row - 1, col, null));
            bfs.add(new MatrixElement<>(row, col + 1, null));
            bfs.add(new MatrixElement<>(row, col - 1, null));
        }
        return perimeter;
    }

    private int calculateSides(Matrix<Boolean> section) {
        Matrix<Boolean> sectionRotated = section.rotateCW();
        int sides = 0;
        List<Matrix<Boolean>> toCheck = ImmutableList.of(section, sectionRotated);
        for (Matrix<Boolean> s : toCheck) {
            for (int row = 0; row < section.height() - 1; row++) {
                boolean sideA = false;
                boolean sideB = false;
                for (int col = 0; col < s.width(row); col++) {
                    boolean top = s.getValue(row, col);
                    boolean bot = s.getValue(row + 1, col);
                    if (!top && bot) {
                        sideA = true;
                    } else if (top && !bot) {
                        sideB = true;
                    } else {
                        if (sideA) {
                            sides++;
                        }
                        if (sideB) {
                            sides++;
                        }
                        sideA = false;
                        sideB = false;
                    }
                }
            }
        }
        return sides;
    }

    private void mapSection(int row,
                            int col,
                            char plant,
                            Matrix<Boolean> section,
                            Matrix<Boolean> visited,
                            Matrix<Character> garden) {
        if (row < 0 || row >= garden.height() || col < 0 || col >= garden.width(row)) {
            return;
        }
        if (section.getValue(row, col)) {
            return;
        }
        if (visited.getValue(row, col)) {
            return;
        }
        MatrixElement<Character> element = garden.getElement(row, col);
        if (element.getValue() != plant) {
            return;
        }
        section.set(row, col, true);
        visited.set(row, col, true);
        mapSection(row + 1, col, plant, section, visited, garden);
        mapSection(row - 1, col, plant, section, visited, garden);
        mapSection(row, col + 1, plant, section, visited, garden);
        mapSection(row, col - 1, plant, section, visited, garden);
    }

    private List<Matrix<Boolean>> mapSections(Matrix<Character> garden) {
        Matrix<Boolean> visited = garden.copy().apply(e -> false);
        List<Matrix<Boolean>> sections = new ArrayList<>();
        garden.score(
                e -> !visited.getValue(e.getRow(), e.getCol()) && e.getValue() != ' ' ,
                stream -> stream.map(e1 -> {
                    Matrix<Boolean> section = garden.copy().apply(e2 -> false);
                    mapSection(e1.getRow(), e1.getCol(), e1.getValue(), section, visited, garden);
                    return section;
                })
        ).forEach(sections::add);
        return sections;
    }

    private Matrix<Character> parse() {
        List<List<Character>> gardenAsLists = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Character> row = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            if (gardenAsLists.isEmpty()) {
                gardenAsLists.add(IntStream.range(0, row.size() + 2)
                        .boxed().map(i -> ' ')
                        .collect(Collectors.toList()));
            }
            gardenAsLists.add(ImmutableList.<Character>builder()
                    .add(' ')
                    .addAll(row)
                    .add(' ')
                    .build());
            if (!scanner.hasNextLine()) {
                gardenAsLists.add(IntStream.range(0, row.size() + 2)
                        .boxed().map(i -> ' ')
                        .collect(Collectors.toList()));
            }
        }
        return new Matrix<>(gardenAsLists);
    }

}
