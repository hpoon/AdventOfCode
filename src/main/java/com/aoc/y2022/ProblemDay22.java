package com.aoc.y2022;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay22 extends ProblemDay<Integer, Integer> {

    private static final boolean IS_ACTUAL_SUBMISSION = true;
    private static final int DIMENSIONS = IS_ACTUAL_SUBMISSION ? 50 : 4;
    private static final Map<Character, Integer> FACING = ImmutableMap.of(
            '>', 0,
            'v', 1,
            '<', 2,
            '^', 3);

    @Override
    public Integer solveA() {
        Matrix<Character> map = parseMap();
        List<String> path = parsePath();
        MatrixElement<Character> current = findStartingLocation(map);
        int facing = FACING.get('>');
        for (String str : path) {
            boolean isNumeric = StringUtils.isNumeric(str);
            if (isNumeric) {
                int moves = Integer.parseInt(str);
                for (int i = 1; i <= moves; i++) {
                    MatrixElement<Character> next = getNextTile(map, current.getRow(), current.getCol(), facing);
                    if (next.equals(current)) {
                        break;
                    }
                    current = next;
                }
            } else {
                switch (str) {
                    case "R": facing = (facing + 1) % FACING.size(); break;
                    case "L": facing = (facing - 1 == -1) ? FACING.size() - 1 : facing - 1; break;
                    default: throw new RuntimeException("Unexpected");
                }
            }
        }
        return 1000 * (current.getRow() + 1) + 4 * (current.getCol() + 1) + facing;
    }

    @Override
    public Integer solveB() {
        Matrix<Character> map = parseMap();
        List<String> path = parsePath();
        Map<Integer, Matrix<Character>> faces = extractFaces(map);

        // Hardcoding the transitions cuz a generalized solution is too hard
        Matrix<Character> f0 = faces.get(0);
        Matrix<Character> f1 = faces.get(1);
        Matrix<Character> f2 = faces.get(2);
        Matrix<Character> f3 = faces.get(3);
        Matrix<Character> f4 = faces.get(4);
        Matrix<Character> f5 = faces.get(5);
        Map<CubeTransition, CubeTransition> transitions = IS_ACTUAL_SUBMISSION
                ? getActualSubmissionTransitions(f0, f1, f2, f3, f4, f5)
                : getSmallExampleTransitions(f0, f1, f2, f3, f4, f5);

        Pair<MatrixElement<Character>, CubeTransition> current = ImmutablePair.of(
                findStartingLocation(f0),
                new CubeTransition(0, f0,0));
        for (String str : path) {
            int facing = current.getRight().getFacing();
            boolean isNumeric = StringUtils.isNumeric(str);
            if (isNumeric) {
                int moves = Integer.parseInt(str);
                for (int i = 1; i <= moves; i++) {
                    Pair<MatrixElement<Character>, CubeTransition> next = getNextTile(
                            current.getRight(),
                            transitions,
                            current.getLeft().getRow(),
                            current.getLeft().getCol());
                    if (next.equals(current)) {
                        break;
                    }
                    current = next;
                }
            } else {
                switch (str) {
                    case "R": facing = (facing + 1) % FACING.size(); break;
                    case "L": facing = (facing - 1 == -1) ? FACING.size() - 1 : facing - 1; break;
                    default: throw new RuntimeException("Unexpected");
                }
                current = ImmutablePair.of(
                        current.getLeft(),
                        new CubeTransition(current.getRight().getId(), current.getRight().getMap(), facing));
            }
        }

        // Which map am I on now
        int endingMapIdx = current.getRight().getId();
        int finalRow = current.getLeft().getRow();
        int finalCol = current.getLeft().getCol();
        switch(endingMapIdx) {
            case 0:
                finalCol += DIMENSIONS;
                break;
            case 1:
                finalCol += 2 * DIMENSIONS;
                break;
            case 2:
                finalRow += DIMENSIONS;
                finalCol += DIMENSIONS;
                break;
            case 3:
                finalRow += 2 * DIMENSIONS;
                break;
            case 4:
                finalRow += 2 * DIMENSIONS;
                finalCol += DIMENSIONS;
                break;
            case 5:
                finalRow += 3 * DIMENSIONS;
                break;
        }
        finalRow += 1;
        finalCol += 1;

        return 1000 * finalRow + 4 * finalCol + current.getRight().getFacing();
    }

    private Map<CubeTransition, CubeTransition> getSmallExampleTransitions(Matrix<Character> f0,
                                                                           Matrix<Character> f1,
                                                                           Matrix<Character> f2,
                                                                           Matrix<Character> f3,
                                                                           Matrix<Character> f4,
                                                                           Matrix<Character> f5) {
        Map<CubeTransition, CubeTransition> transitions = new HashMap<>();

        transitions.put(new CubeTransition(0, f0, FACING.get('>')), new CubeTransition(5, f5,FACING.get('<')));
        transitions.put(new CubeTransition(0, f0, FACING.get('v')), new CubeTransition(3, f3,FACING.get('v')));
        transitions.put(new CubeTransition(0, f0, FACING.get('<')), new CubeTransition(2, f2,FACING.get('v')));
        transitions.put(new CubeTransition(0, f0, FACING.get('^')), new CubeTransition(1, f1,FACING.get('v')));

        transitions.put(new CubeTransition(1, f1,FACING.get('>')), new CubeTransition(2, f2,FACING.get('>')));
        transitions.put(new CubeTransition(1, f1,FACING.get('v')), new CubeTransition(4, f4,FACING.get('^')));
        transitions.put(new CubeTransition(1, f1,FACING.get('<')), new CubeTransition(5, f5,FACING.get('^')));
        transitions.put(new CubeTransition(1, f1,FACING.get('^')), new CubeTransition(0, f0,FACING.get('v')));

        transitions.put(new CubeTransition(2, f2,FACING.get('>')), new CubeTransition(3, f3,FACING.get('>')));
        transitions.put(new CubeTransition(2, f2,FACING.get('v')), new CubeTransition(4, f4,FACING.get('>')));
        transitions.put(new CubeTransition(2, f2,FACING.get('<')), new CubeTransition(1, f1,FACING.get('<')));
        transitions.put(new CubeTransition(2, f2,FACING.get('^')), new CubeTransition(0, f0,FACING.get('>')));

        transitions.put(new CubeTransition(3, f3,FACING.get('>')), new CubeTransition(5, f5,FACING.get('v')));
        transitions.put(new CubeTransition(3, f3,FACING.get('v')), new CubeTransition(4, f4,FACING.get('v')));
        transitions.put(new CubeTransition(3, f3,FACING.get('<')), new CubeTransition(2, f2,FACING.get('<')));
        transitions.put(new CubeTransition(3, f3,FACING.get('^')), new CubeTransition(0, f0,FACING.get('^')));

        transitions.put(new CubeTransition(4, f4,FACING.get('>')), new CubeTransition(5, f5,FACING.get('>')));
        transitions.put(new CubeTransition(4, f4,FACING.get('v')), new CubeTransition(1, f1,FACING.get('^')));
        transitions.put(new CubeTransition(4, f4,FACING.get('<')), new CubeTransition(2, f2,FACING.get('^')));
        transitions.put(new CubeTransition(4, f4,FACING.get('^')), new CubeTransition(3, f3,FACING.get('^')));

        transitions.put(new CubeTransition(5, f5,FACING.get('>')), new CubeTransition(0, f0,FACING.get('<')));
        transitions.put(new CubeTransition(5, f5,FACING.get('v')), new CubeTransition(1, f1,FACING.get('>')));
        transitions.put(new CubeTransition(5, f5,FACING.get('<')), new CubeTransition(4, f4,FACING.get('<')));
        transitions.put(new CubeTransition(5, f5,FACING.get('^')), new CubeTransition(3, f3,FACING.get('<')));
        return transitions;
    }

    private Map<CubeTransition, CubeTransition> getActualSubmissionTransitions(Matrix<Character> f0,
                                                                               Matrix<Character> f1,
                                                                               Matrix<Character> f2,
                                                                               Matrix<Character> f3,
                                                                               Matrix<Character> f4,
                                                                               Matrix<Character> f5) {
        Map<CubeTransition, CubeTransition> transitions = new HashMap<>();

        transitions.put(new CubeTransition(0, f0, FACING.get('>')), new CubeTransition(1, f1,FACING.get('>')));
        transitions.put(new CubeTransition(0, f0, FACING.get('v')), new CubeTransition(2, f2,FACING.get('v')));
        transitions.put(new CubeTransition(0, f0, FACING.get('<')), new CubeTransition(3, f3,FACING.get('>')));
        transitions.put(new CubeTransition(0, f0, FACING.get('^')), new CubeTransition(5, f5,FACING.get('>')));

        transitions.put(new CubeTransition(1, f1,FACING.get('>')), new CubeTransition(4, f4,FACING.get('<')));
        transitions.put(new CubeTransition(1, f1,FACING.get('v')), new CubeTransition(2, f2,FACING.get('<')));
        transitions.put(new CubeTransition(1, f1,FACING.get('<')), new CubeTransition(0, f0,FACING.get('<')));
        transitions.put(new CubeTransition(1, f1,FACING.get('^')), new CubeTransition(5, f5,FACING.get('^')));

        transitions.put(new CubeTransition(2, f2,FACING.get('>')), new CubeTransition(1, f1,FACING.get('^')));
        transitions.put(new CubeTransition(2, f2,FACING.get('v')), new CubeTransition(4, f4,FACING.get('v')));
        transitions.put(new CubeTransition(2, f2,FACING.get('<')), new CubeTransition(3, f3,FACING.get('v')));
        transitions.put(new CubeTransition(2, f2,FACING.get('^')), new CubeTransition(0, f0,FACING.get('^')));

        transitions.put(new CubeTransition(3, f3,FACING.get('>')), new CubeTransition(4, f4,FACING.get('>')));
        transitions.put(new CubeTransition(3, f3,FACING.get('v')), new CubeTransition(5, f5,FACING.get('v')));
        transitions.put(new CubeTransition(3, f3,FACING.get('<')), new CubeTransition(0, f0,FACING.get('>')));
        transitions.put(new CubeTransition(3, f3,FACING.get('^')), new CubeTransition(2, f2,FACING.get('>')));

        transitions.put(new CubeTransition(4, f4,FACING.get('>')), new CubeTransition(1, f1,FACING.get('<')));
        transitions.put(new CubeTransition(4, f4,FACING.get('v')), new CubeTransition(5, f5,FACING.get('<')));
        transitions.put(new CubeTransition(4, f4,FACING.get('<')), new CubeTransition(3, f3,FACING.get('<')));
        transitions.put(new CubeTransition(4, f4,FACING.get('^')), new CubeTransition(2, f2,FACING.get('^')));

        transitions.put(new CubeTransition(5, f5,FACING.get('>')), new CubeTransition(4, f4,FACING.get('^')));
        transitions.put(new CubeTransition(5, f5,FACING.get('v')), new CubeTransition(1, f1,FACING.get('v')));
        transitions.put(new CubeTransition(5, f5,FACING.get('<')), new CubeTransition(0, f0,FACING.get('v')));
        transitions.put(new CubeTransition(5, f5,FACING.get('^')), new CubeTransition(3, f3,FACING.get('^')));
        return transitions;
    }

    private Map<Integer, Matrix<Character>> extractFaces(Matrix<Character> map) {
        Map<Integer, Matrix<Character>> faces = new HashMap<>();
        Function<Character, Boolean> isValidFunction = c -> c != 32;
        int count = 0;
        for (int row = 0; row < map.height(); row += DIMENSIONS) {
            for (int col = 0; col < map.width(row); col += DIMENSIONS) {
                List<Integer> rowBounds = map.getRowBounds(row, isValidFunction);
                List<Integer> colBounds = map.getColBounds(col, isValidFunction);
                Range<Integer> rowRange = Range.closed(rowBounds.get(0), rowBounds.get(rowBounds.size() - 1));
                Range<Integer> colRange = Range.closed(colBounds.get(0), colBounds.get(colBounds.size() - 1));
                if (!rowRange.contains(col) || !colRange.contains(row)) {
                    continue;
                }
                faces.put(count, map.copy(row, col, DIMENSIONS, DIMENSIONS));
                count++;
            }
        }
        return faces;
    }

    private Pair<MatrixElement<Character>, CubeTransition> getNextTile(CubeTransition transition,
                                                                       Map<CubeTransition, CubeTransition> transitions,
                                                                       int row,
                                                                       int col) {
        CubeTransition nextTransition = transition;
        int nextRow = row;
        int nextCol = col;
        Function<Character, Boolean> isValidFunction = c -> c != 32;
        Matrix<Character> map = nextTransition.getMap();
        int facing = transition.getFacing();
        List<Integer> rowBounds = map.getRowBounds(nextRow, isValidFunction);
        List<Integer> colBounds = map.getColBounds(nextCol, isValidFunction);
        Range<Integer> rangeIndices;
        switch (facing) {
            case 0:
                nextCol += 1;
                rangeIndices = Range.closed(rowBounds.get(0), rowBounds.get(rowBounds.size() - 1));
                if (!rangeIndices.contains(nextCol)) {
                    nextTransition = convertFace(transitions, transition);
                    int c = convertCol(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    int r = convertRow(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    nextCol = c;
                    nextRow = r;
                }
                break;
            case 1:
                nextRow += 1;
                rangeIndices = Range.closed(colBounds.get(0), colBounds.get(colBounds.size() - 1));
                if (!rangeIndices.contains(nextRow)) {
                    nextTransition = convertFace(transitions, transition);
                    int c = convertCol(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    int r = convertRow(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    nextCol = c;
                    nextRow = r;
                }
                break;
            case 2:
                nextCol -= 1;
                rangeIndices = Range.closed(rowBounds.get(0), rowBounds.get(rowBounds.size() - 1));
                if (!rangeIndices.contains(nextCol)) {
                    nextTransition = convertFace(transitions, transition);
                    int c = convertCol(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    int r = convertRow(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    nextCol = c;
                    nextRow = r;
                }
                break;
            case 3:
                nextRow -= 1;
                rangeIndices = Range.closed(colBounds.get(0), colBounds.get(colBounds.size() - 1));
                if (!rangeIndices.contains(nextRow)) {
                    nextTransition = convertFace(transitions, transition);
                    int c = convertCol(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    int r = convertRow(nextRow, nextCol, transition.getFacing(), nextTransition.getFacing());
                    nextCol = c;
                    nextRow = r;
                }
                break;
            default:
                throw new RuntimeException("Unexpected");
        }
        char tile = nextTransition.getMap().getValue(nextRow, nextCol);
        if (tile == '#') {
            // Don't move to new spot
            return ImmutablePair.of(new MatrixElement<>(row, col, '.'), transition);
        } else {
            // Move to new spot
            return ImmutablePair.of(new MatrixElement<>(nextRow, nextCol, '.'), nextTransition);
        }
    }

    private CubeTransition convertFace(Map<CubeTransition, CubeTransition> transitions, CubeTransition transition) {
        return transitions.get(transition);
    }

    private int convertRow(int row, int col, int fromFacing, int toFacing) {
        Map<Pair<Integer, Integer>, Integer> mapping = ImmutableMap.<Pair<Integer, Integer>, Integer>builder()
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('>')), row)
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('v')), 0)
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('<')), DIMENSIONS - 1 - row) // updated
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('^')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('>')), col)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('v')), 0)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('<')), col)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('^')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('>')), DIMENSIONS - 1 - row) // updated
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('v')), 0)
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('<')), row)
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('^')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('>')), col)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('v')), 0)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('<')), col)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('^')), DIMENSIONS - 1)
                .build();
        return mapping.get(ImmutablePair.of(fromFacing, toFacing));
    }

    private int convertCol(int row, int col, int fromFacing, int toFacing) {
        Map<Pair<Integer, Integer>, Integer> mapping = ImmutableMap.<Pair<Integer, Integer>, Integer>builder()
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('>')), 0)
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('v')), DIMENSIONS - row - 1)  // updated
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('<')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('>'), FACING.get('^')), row)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('>')), 0)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('v')), col)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('<')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('v'), FACING.get('^')), DIMENSIONS - col - 1)  // updated
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('>')), 0)
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('v')), row)
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('<')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('<'), FACING.get('^')), row)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('>')), 0)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('v')), col)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('<')), DIMENSIONS - 1)
                .put(ImmutablePair.of(FACING.get('^'), FACING.get('^')), col)
                .build();
        return mapping.get(ImmutablePair.of(fromFacing, toFacing));
    }

    private MatrixElement<Character> getNextTile(Matrix<Character> map, int row, int col, int facing) {
        int nextRow = row;
        int nextCol = col;
        Function<Character, Boolean> isValidFunction = c -> c != 32;
        List<Integer> rowBounds = map.getRowBounds(nextRow, isValidFunction);
        List<Integer> colBounds = map.getColBounds(nextCol, isValidFunction);
        Range<Integer> rangeIndices;
        switch (facing) {
            case 0:
                nextCol += 1;
                rangeIndices = Range.closed(rowBounds.get(0), rowBounds.get(rowBounds.size() - 1));
                nextCol = rangeIndices.contains(nextCol) ? nextCol : nextCol - rowBounds.size();
                break;
            case 1:
                nextRow += 1;
                rangeIndices = Range.closed(colBounds.get(0), colBounds.get(colBounds.size() - 1));
                nextRow = rangeIndices.contains(nextRow) ? nextRow : nextRow - colBounds.size();
                break;
            case 2:
                nextCol -= 1;
                rangeIndices = Range.closed(rowBounds.get(0), rowBounds.get(rowBounds.size() - 1));
                nextCol = rangeIndices.contains(nextCol) ? nextCol : nextCol + rowBounds.size();
                break;
            case 3:
                nextRow -= 1;
                rangeIndices = Range.closed(colBounds.get(0), colBounds.get(colBounds.size() - 1));
                nextRow = rangeIndices.contains(nextRow) ? nextRow : nextRow + colBounds.size();
                break;
            default:
                throw new RuntimeException("Unexpected");
        }
        char tile = map.getValue(nextRow, nextCol);
        if (tile == '#') {
            // Don't move to new spot
            return new MatrixElement<>(row, col, '.');
        } else {
            // Move to new spot
            return new MatrixElement<>(nextRow, nextCol, '.');
        }
    }

    private MatrixElement<Character> findStartingLocation(Matrix<Character> map) {
        return map.getValue('.').get(0);
    }

    private Matrix<Character> parseMap() {
        Matrix<Character> map = new Matrix<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("")) {
                break;
            }
            List<Character> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                row.add(line.charAt(i));
            }
            map.addRow(row);
        }
        return map;
    }

    private List<String> parsePath() {
        String line = scanner.nextLine();
        List<String> path = new ArrayList<>();

        List<String> numbers = Arrays.stream(line.split("[A-Z]"))
                .collect(Collectors.toList());
        List<String> letters = Arrays.stream(line.split("[0-9]"))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        for (int i = 0; i < Math.min(numbers.size(), letters.size()); i++) {
            path.add(numbers.get(i));
            path.add(letters.get(i));
        }
        if (numbers.size() > letters.size()) {
            path.add(numbers.get(numbers.size() - 1));
        }
        return path;
    }

    @Value
    private static class CubeTransition {
        private int id;
        private Matrix<Character> map;
        private int facing;
    }

}
