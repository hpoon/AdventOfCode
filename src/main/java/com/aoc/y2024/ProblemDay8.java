package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.Point2D;
import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay8 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<Character> map = parse();
        Map<Character, List<MatrixElement<Character>>> groups = groupAntennas(map);
        return calculateAntiNodes(groups, map, true);
    }

    @Override
    public Integer solveB() {
        Matrix<Character> map = parse();
        Map<Character, List<MatrixElement<Character>>> groups = groupAntennas(map);
        return calculateAntiNodes(groups, map, false);
    }

    private int calculateAntiNodes(Map<Character, List<MatrixElement<Character>>> groups,
                                   Matrix<Character> map,
                                   boolean isFinite) {
        Set<Point2D> antinodes = new HashSet<>();
        for (List<MatrixElement<Character>> group : groups.values()) {
            for (int i = 0; i < group.size(); i++) {
                if (group.size() > 1 && !isFinite) {
                    antinodes.add(new Point2D(group.get(i).getCol(),group.get(i).getRow()));
                }
                for (int j = i + 1; j < group.size(); j++) {
                    MatrixElement<Character> nodei = group.get(i);
                    MatrixElement<Character> nodej = group.get(j);
                    int width = nodei.getCol() - nodej.getCol();
                    int height = nodei.getRow() - nodej.getRow();

                    int currentWidth = width;
                    int currentHeight = height;
                    do {
                        Point2D antinodei = new Point2D(nodei.getCol() + currentWidth, nodei.getRow() + currentHeight);
                        Point2D antinodej = new Point2D(nodej.getCol() - currentWidth, nodej.getRow() - currentHeight);
                        boolean added = false;
                        if (map.withinBounds(antinodei.getY(), antinodei.getX())) {
                            added = true;
                            antinodes.add(antinodei);
                        }
                        if (map.withinBounds(antinodej.getY(), antinodej.getX())) {
                            added = true;
                            antinodes.add(antinodej);
                        }
                        if (!added) {
                            break;
                        }
                        currentWidth += width;
                        currentHeight += height;
                    } while (!isFinite);
                }
            }
        }
        return antinodes.size();
    }

    private Map<Character, List<MatrixElement<Character>>> groupAntennas(Matrix<Character> matrix) {
        Map<Character, List<MatrixElement<Character>>> groups = new HashMap<>();
        for (int row = 0; row < matrix.height(); row++) {
            for (int col = 0; col < matrix.width(row); col++) {
                MatrixElement<Character> e = matrix.getElement(row, col);
                if (e.getValue() == '.') {
                    continue;
                }
                List<MatrixElement<Character>> group = groups.getOrDefault(e.getValue(), new ArrayList<>());
                group.add(e);
                groups.put(e.getValue(), group);
            }
        }
        return groups;
    }

    private Matrix<Character> parse() {
        return new Matrix<>(scanner, row -> row.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

}
