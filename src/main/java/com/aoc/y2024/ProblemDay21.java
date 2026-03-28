package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ProblemDay21 extends ProblemDay<Integer, Integer> {

    private static final List<List<Integer>> DIRS = ImmutableList.of(
            ImmutableList.of(1, 0),
            ImmutableList.of(0, -1),
            ImmutableList.of(0, 1),
            ImmutableList.of(-1, 0));
    private static final Map<List<Integer>, Character> DIRS_TO_CHAR = ImmutableMap.of(
            ImmutableList.of(1, 0), 'v',
            ImmutableList.of(0, -1), '<',
            ImmutableList.of(0, 1), '>',
            ImmutableList.of(-1, 0), '^');


    @Override
    public Integer solveA() {
        List<List<Character>> codes = parse();
        return solveA(codes);
    }

    @Override
    public Integer solveB() {
        return 0;
    }

    private int solveA(List<List<Character>> codes) {
        Matrix<Character> keypad = keypad();
        MatrixElement<Character> keypadStart = keypad.getValue('A').get(0);
        Matrix<Character> arrowpad = arrowpad();
        MatrixElement<Character> arrowpadStart = arrowpad.getValue('A').get(0);

        for (List<Character> code : codes) {
            MatrixElement<Character> start = keypadStart;
            List<List<Character>> pathSegments = new ArrayList<>();
            for (char key : code) {
                TraversalResult res = path(start, key, keypad);
                pathSegments.add(res.getPath());
                start = res.getLast();
            }
            pathSegments.forEach(segment -> System.out.printf("%s", Joiner.on("").join(segment)));
            System.out.println();

            List<List<Character>> path = pathSegments;
            for (int i = 0; i < 2; i++) {
                List<List<Character>> newPath = new ArrayList<>();
                start = arrowpadStart;
                for (List<Character> segment : path) {
                    for (char c : segment) {
                        TraversalResult res = path(start, c, arrowpad);
                        newPath.add(res.getPath());
                        start = res.getLast();
                    }
                }
                path = newPath;
                path.forEach(segment -> System.out.printf("%s", Joiner.on("").join(segment)));
            System.out.println();
            }

            System.out.println();
        }

        return 0;
    }

    private TraversalResult path(MatrixElement<Character> start,
                                 char end,
                                 Matrix<Character> pad) {
        Queue<State> queue = new LinkedList<>();
        queue.add(new State(start.getRow(), start.getCol(), 'A'));
        Set<MatrixElement<Character>> visited = new HashSet<>();
        Map<State, State> steps = new HashMap<>();
        while (!queue.isEmpty()) {
            State state = queue.poll();
            int row = state.getRow();
            int col = state.getCol();
            if (!pad.withinBounds(row, col)) {
                continue;
            }
            MatrixElement<Character> element = pad.getElement(row, col);
            if (element.getValue() == 'x') {
                continue;
            }
            if (visited.contains(element)) {
                continue;
            }
            visited.add(element);
            if (element.getValue().equals(end)) {
                List<Character> segment = new ArrayList<>();
                State s = state;
                while (s.getRow() != start.getRow() || s.getCol() != start.getCol()) {
                    segment.add(0, s.getDir());
                    s = steps.get(s);
                }
//                Collections.sort(segment);
                segment.add('A');
                return new TraversalResult(
                        segment,
                        element);
            }
            for (List<Integer> dir : DIRS) {
                State nextState = new State(
                        element.getRow() + dir.get(0),
                        element.getCol() + dir.get(1),
                        DIRS_TO_CHAR.get(dir));
                steps.put(nextState, state);
                queue.add(nextState);
            }
        }
        throw new RuntimeException("Didn't find end");
    }

    private Matrix<Character> arrowpad() {
        return new Matrix<>(ImmutableList.of(
                ImmutableList.of('x', '^', 'A'),
                ImmutableList.of('<', 'v', '>')));
    }

    private Matrix<Character> keypad() {
        return new Matrix<>(ImmutableList.of(
                ImmutableList.of('7', '8', '9'),
                ImmutableList.of('4', '5', '6'),
                ImmutableList.of('1', '2', '3'),
                ImmutableList.of('x', '0', 'A')));
    }

    private List<List<Character>> parse() {
        List<List<Character>> codes = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Character> code = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                code.add(line.charAt(i));
            }
            codes.add(code);
        }
        return codes;
    }

    @Value
    @EqualsAndHashCode
    private static class State {
        private int row;
        private int col;
        private char dir;
    }

    @Value
    private static class TraversalResult {
        private List<Character> path;
        private MatrixElement<Character> last;
    }


    private int solveOne(List<Character> code,
                         Matrix<Character> keypad,
                         MatrixElement<Character> keypadStart,
                         Matrix<Character> arrowpad,
                         MatrixElement<Character> arrowpadStart) {
        Queue<State2> queue = new LinkedList<>();
        queue.add(new State2(
                ImmutableList.of(arrowpadStart, arrowpadStart, keypadStart),
                "",
                ""));
        List<Character> moves = ImmutableList.of('^')
        while (!queue.isEmpty()) {
            State2 state = queue.poll();
            List<MatrixElement<Character>> cursors = state.getCursors();
            String totalPath = state.getTotalPath();
            String segment = state.getSegment();
            if (segment.length() > 3) {
                continue;
            }
        }
        return 0;
    }

    @Value
    @EqualsAndHashCode
    private static class State2 {
        private List<MatrixElement<Character>> cursors;
        private String totalPath;
        private String segment;
    }
}
