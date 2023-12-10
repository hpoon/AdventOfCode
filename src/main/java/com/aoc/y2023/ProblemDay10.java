package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay10 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<String> map = parse();
        Map<MatrixElement<String>, Integer> visitedPipesToSteps = traverse(map);
        return visitedPipesToSteps.values().stream()
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @Override
    public Integer solveB() {
        Matrix<String> map = parse();
        Map<MatrixElement<String>, Integer> visitedPipesToSteps = traverse(map);
        return enclosed(map, visitedPipesToSteps);
    }

    private int enclosed(Matrix<String> map, Map<MatrixElement<String>, Integer> visitedPipesToSteps) {
        // The pipes that don't matter count as "." for area calculation, but make them spaces to visualize
        map = map.apply(element -> !visitedPipesToSteps.containsKey(element) ? " " : element.getValue());
        map.print();

        // Expand the matrix (so that the pipes that are next to each other will have a space in between)
        // I'll account for this area later
        Matrix<String> expandedMap = new Matrix<>();
        for (int row = 0; row < map.height(); row++) {
            List<String> expandedRow = new ArrayList<>();
            for (int col = 0; col < map.width(row); col++) {
                expandedRow.add(map.getValue(row, col));
                expandedRow.add(" ");
            }
            expandedMap.addRow(expandedRow);
            expandedMap.addRow(IntStream.range(0, expandedMap.width(row))
                    .boxed()
                    .map(i -> " ")
                    .collect(Collectors.toList()));
        }
        expandedMap.print();

        // Connects the pipes that were there to support the flood fill operation
        Matrix<String> final1 = expandedMap;
        expandedMap.apply(element -> {
            int row = element.getRow();
            int col = element.getCol();
            if (Objects.equals(element.getValue(), "-")) {
                if (final1.withinBounds(row, col - 1)
                        && StringUtils.isBlank(final1.getValue(row, col - 1))) {
                    final1.set(row, col - 1, "-");
                }
                if (final1.withinBounds(row, col + 1)
                        && StringUtils.isBlank(final1.getValue(row, col + 1))) {
                    final1.set(row, col + 1, "-");
                }
            }
            if (Objects.equals(element.getValue(), "|")) {
                if (final1.withinBounds(row - 1, col)
                        && StringUtils.isBlank(final1.getValue(row - 1, col))) {
                    final1.set(row - 1, col, "|");
                }
                if (final1.withinBounds(row + 1, col)
                        && StringUtils.isBlank(final1.getValue(row + 1, col))) {
                    final1.set(row + 1, col, "|");
                }
            }
            if (Objects.equals(element.getValue(), "F")) {
                if (final1.withinBounds(row, col + 1)
                        && StringUtils.isBlank(final1.getValue(row, col + 1))) {
                    final1.set(row, col + 1, "-");
                }
                if (final1.withinBounds(row + 1, col)
                        && StringUtils.isBlank(final1.getValue(row + 1, col))) {
                    final1.set(row + 1, col, "|");
                }
            }
            if (Objects.equals(element.getValue(), "7")) {
                if (final1.withinBounds(row, col - 1)
                        && StringUtils.isBlank(final1.getValue(row, col - 1))) {
                    final1.set(row, col - 1, "-");
                }
                if (final1.withinBounds(row + 1, col)
                        && StringUtils.isBlank(final1.getValue(row + 1, col))) {
                    final1.set(row + 1, col, "|");
                }
            }
            if (Objects.equals(element.getValue(), "J")) {
                if (final1.withinBounds(row, col - 1)
                        && StringUtils.isBlank(final1.getValue(row, col - 1))) {
                    final1.set(row, col - 1, "-");
                }
                if (final1.withinBounds(row - 1, col)
                        && StringUtils.isBlank(final1.getValue(row - 1, col))) {
                    final1.set(row - 1, col, "|");
                }
            }
            if (Objects.equals(element.getValue(), "L")) {
                if (final1.withinBounds(row, col + 1)
                        && StringUtils.isBlank(final1.getValue(row, col + 1))) {
                    final1.set(row, col + 1, "-");
                }
                if (final1.withinBounds(row - 1, col)
                        && StringUtils.isBlank(final1.getValue(row - 1, col))) {
                    final1.set(row - 1, col, "|");
                }
            }
            return element.getValue();
        });

        // Get the area of the outside by tracking the visited nodes on the outside
        MatrixElement<String> start = expandedMap.getElement(0, 0);
        Queue<MatrixElement<String>> queue = new LinkedList<>();
        queue.add(start);
        Set<MatrixElement<String>> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            MatrixElement<String> pos = queue.poll();
            if (visited.contains(pos)) {
                continue;
            }
            visited.add(pos);
            Set<MatrixElement<String>> moves = eligibleFloodFillMoves(pos, expandedMap);
            queue.addAll(moves);
        }

        // Mark the visited ones with dots for easily visualization
        expandedMap = expandedMap.apply(element -> visited.contains(element) ? "." : element.getValue());
        expandedMap.print();

        // Keep only the not visited ones cuz these are the areas we care about
        Matrix<String> final2 = expandedMap;
        Set<MatrixElement<String>> notVisited = IntStream.range(0, expandedMap.height())
                .boxed()
                .flatMap(row -> IntStream.range(0, final2.width(row))
                        .boxed()
                        .filter(col -> !visited.contains(final2.getElement(row, col)))
                        .map(col -> final2.getElement(row, col)))
                .collect(Collectors.toSet());

        // Take the cells we don't care about and remove the extra rows and columns added from the expansion
        Set<MatrixElement<String>> reduced = notVisited.stream()
                .filter(e -> e.getRow() % 2 == 0)
                .filter(e -> e.getCol() % 2 == 0)
                .collect(Collectors.toSet());

        // We have to subtract the size of the visited pipes since they don't count in area either
        return reduced.size() - visitedPipesToSteps.size();
    }

    private Map<MatrixElement<String>, Integer> traverse(Matrix<String> map) {
        MatrixElement<String> start = map.getValue("S").iterator().next();
        Queue<MatrixElement<String>> queue = new LinkedList<>();
        queue.add(start);
        Map<MatrixElement<String>, Integer> visited = new HashMap<>();
        Matrix<String> debug = new Matrix<>();
        IntStream.range(0, map.height())
                .forEach(row -> debug.addRow(
                        IntStream.range(0, map.width(row))
                                .mapToObj(col -> ".")
                                .collect(Collectors.toList())));
        debug.set(start.getRow(), start.getCol(), start.getValue());
        while (!queue.isEmpty()) {
            MatrixElement<String> pos = queue.poll();
            Set<MatrixElement<String>> moves = eligiblePipeMoves(pos, map);
            moves.stream()
                    .filter(m -> !visited.containsKey(m))
                    .forEach(m -> {
                        visited.put(m, visited.getOrDefault(pos, 0) + 1);
                        queue.add(m);
                        debug.set(m.getRow(), m.getCol(), m.getValue());
                    });
//            debug.print();
        }
        visited.put(start, 0);
//        debug.print();
        return visited;
    }

    private Set<MatrixElement<String>> eligibleFloodFillMoves(MatrixElement<String> pos, Matrix<String> map) {
        Set<MatrixElement<String>> moves = new HashSet<>();
        // Up
        if (map.withinBounds(pos.getRow() - 1, pos.getCol())
                && StringUtils.isBlank(map.getValue(pos.getRow() - 1, pos.getCol()))) {
            moves.add(map.getElement(pos.getRow() - 1, pos.getCol()));
        }
        // Down
        if (map.withinBounds(pos.getRow() + 1, pos.getCol())
                && StringUtils.isBlank(map.getValue(pos.getRow() + 1, pos.getCol()))) {
            moves.add(map.getElement(pos.getRow() + 1, pos.getCol()));
        }
        // Left
        if (map.withinBounds(pos.getRow(), pos.getCol() - 1)
                && StringUtils.isBlank(map.getValue(pos.getRow(), pos.getCol() - 1))) {
            moves.add(map.getElement(pos.getRow(), pos.getCol() - 1));
        }
        // Right
        if (map.withinBounds(pos.getRow(), pos.getCol() + 1)
                && StringUtils.isBlank(map.getValue(pos.getRow(), pos.getCol() + 1))) {
            moves.add(map.getElement(pos.getRow(), pos.getCol() + 1));
        }
        return moves;
    }

    private Set<MatrixElement<String>> eligiblePipeMoves(MatrixElement<String> pos, Matrix<String> map) {
        Set<MatrixElement<String>> moves = new HashSet<>();
        MultiKeyMap<String, Set<String>> allowed = new MultiKeyMap<>();
        allowed.put("S", "up", ImmutableSet.of("|", "7", "F"));
        allowed.put("S", "down", ImmutableSet.of("|", "J", "L"));
        allowed.put("S", "left", ImmutableSet.of("-", "L", "F"));
        allowed.put("S", "right", ImmutableSet.of("-", "J", "7"));
        allowed.put("|", "up", ImmutableSet.of("|", "7", "F"));
        allowed.put("|", "down", ImmutableSet.of("|", "J", "L"));
        allowed.put("|", "left", ImmutableSet.of());
        allowed.put("|", "right", ImmutableSet.of());
        allowed.put("-", "up", ImmutableSet.of());
        allowed.put("-", "down", ImmutableSet.of());
        allowed.put("-", "left", ImmutableSet.of("-", "F", "L"));
        allowed.put("-", "right", ImmutableSet.of("-", "J", "7"));
        allowed.put("7", "up", ImmutableSet.of());
        allowed.put("7", "down", ImmutableSet.of("|", "J", "L"));
        allowed.put("7", "left", ImmutableSet.of("-", "F", "L"));
        allowed.put("7", "right", ImmutableSet.of());
        allowed.put("F", "up", ImmutableSet.of());
        allowed.put("F", "down", ImmutableSet.of("|", "J", "L"));
        allowed.put("F", "left", ImmutableSet.of());
        allowed.put("F", "right", ImmutableSet.of("-", "J", "7"));
        allowed.put("J", "up", ImmutableSet.of("|", "F", "7"));
        allowed.put("J", "down", ImmutableSet.of());
        allowed.put("J", "left", ImmutableSet.of("-", "F", "L"));
        allowed.put("J", "right", ImmutableSet.of());
        allowed.put("L", "up", ImmutableSet.of("|", "F", "7"));
        allowed.put("L", "down", ImmutableSet.of());
        allowed.put("L", "left", ImmutableSet.of());
        allowed.put("L", "right", ImmutableSet.of("-", "7", "J"));

        Set<String> dirs = new HashSet<>();
        // Up
        if (map.withinBounds(pos.getRow() - 1, pos.getCol())
                && allowed.get(map.getValue(pos.getRow(), pos.getCol()), "up")
                .contains(map.getValue(pos.getRow() - 1, pos.getCol()))) {
            moves.add(map.getElement(pos.getRow() - 1, pos.getCol()));
            dirs.add("up");
        }
        // Down
        if (map.withinBounds(pos.getRow() + 1, pos.getCol())
                && allowed.get(map.getValue(pos.getRow(), pos.getCol()), "down")
                .contains(map.getValue(pos.getRow() + 1, pos.getCol()))) {
            moves.add(map.getElement(pos.getRow() + 1, pos.getCol()));
            dirs.add("down");
        }
        // Left
        if (map.withinBounds(pos.getRow(), pos.getCol() - 1)
                && allowed.get(map.getValue(pos.getRow(), pos.getCol()), "left")
                .contains(map.getValue(pos.getRow(), pos.getCol() - 1))) {
            moves.add(map.getElement(pos.getRow(), pos.getCol() - 1));
            dirs.add("left");
        }
        // Right
        if (map.withinBounds(pos.getRow(), pos.getCol() + 1)
                && allowed.get(map.getValue(pos.getRow(), pos.getCol()), "right")
                .contains(map.getValue(pos.getRow(), pos.getCol() + 1))) {
            moves.add(map.getElement(pos.getRow(), pos.getCol() + 1));
            dirs.add("right");
        }

        if (Objects.equals(pos.getValue(), "S")) {
            if (dirs.equals(ImmutableSet.of("up", "right"))) {
                map.set(pos.getRow(), pos.getCol(), "L");
            }
            if (dirs.equals(ImmutableSet.of("right", "down"))) {
                map.set(pos.getRow(), pos.getCol(), "F");
            }
            if (dirs.equals(ImmutableSet.of("down", "left"))) {
                map.set(pos.getRow(), pos.getCol(), "7");
            }
            if (dirs.equals(ImmutableSet.of("left", "up"))) {
                map.set(pos.getRow(), pos.getCol(), "J");
            }
            if (dirs.equals(ImmutableSet.of("up"))) {
                map.set(pos.getRow(), pos.getCol(), "|");
            }
            if (dirs.equals(ImmutableSet.of("down"))) {
                map.set(pos.getRow(), pos.getCol(), "|");
            }
            if (dirs.equals(ImmutableSet.of("left"))) {
                map.set(pos.getRow(), pos.getCol(), "-");
            }
            if (dirs.equals(ImmutableSet.of("right"))) {
                map.set(pos.getRow(), pos.getCol(), "-");
            }
        }

        return moves;
    }

    private Matrix<String> parse() {
        Matrix<String> map = new Matrix<>();
        while (scanner.hasNextLine()) {
            map.addRow(scanner.nextLine().chars().mapToObj(Character::toString).collect(Collectors.toList()));
        }
        return map;
    }

}
