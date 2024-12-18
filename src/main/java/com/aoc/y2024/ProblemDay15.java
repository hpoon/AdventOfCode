package com.aoc.y2024;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemDay15 extends ProblemDay<Integer, Integer> {

    private static final Set<Character> BOX_CHARS = ImmutableSet.of('[', ']', 'O');

    @Override
    public Integer solveA() {
        Matrix<Character> map = parseMap(true);
        Queue<Character> commands = parseCommands();
        return solve(map, commands);
    }

    @Override
    public Integer solveB() {
        Matrix<Character> map = parseMap(false);
        Queue<Character> commands = parseCommands();
        return solve(map, commands);
    }

    private int solve(Matrix<Character> map, Queue<Character> commands) {
        MatrixElement<Character> position = map.getValue('@').get(0);
        map.print();
        while (!commands.isEmpty()) {
            char command = commands.poll();
            switch (command) {
                case '^': position = updateMap(map, position, -1, 0); break;
                case 'v': position = updateMap(map, position, 1, 0); break;
                case '<': position = updateMap(map, position, 0, -1); break;
                case '>': position = updateMap(map, position, 0, 1); break;
                default: throw new RuntimeException("Shouldn't happen");
            }
        }
        map.print();
        return map.score(
                e -> BOX_CHARS.contains(e.getValue()) && e.getValue() != ']',
                stream -> stream
                        .map(e -> e.getRow() * 100 + e.getCol())
                        .reduce(Integer::sum)
                        .orElseThrow(() -> new RuntimeException("Shouldn't happen")));
    }

    private MatrixElement<Character> updateMap(Matrix<Character> map,
                                               MatrixElement<Character> pos,
                                               int rowStep,
                                               int colStep) {
        MatrixElement<Character> nextStep = map.getElement(pos.getRow() + rowStep, pos.getCol() + colStep);
        if (nextStep.getValue() == '#') {
            return pos;
        }
        if (nextStep.getValue() == '.') {
            map.set(pos.getRow(), pos.getCol(), '.');
            map.set(nextStep.getRow(), nextStep.getCol(), '@');
            return nextStep;
        }
        Queue<MatrixElement<Character>> queue = new LinkedList<>();
        queue.add(nextStep);
        Set<MatrixElement<Character>> boxes = new HashSet<>();

        while (!queue.isEmpty()) {
            MatrixElement<Character> current = queue.poll();
            if (boxes.contains(current)) {
                continue;
            }
            char currentValue = current.getValue();
            if (!BOX_CHARS.contains(currentValue)) {
                continue;
            }
            boxes.add(current);
            switch (currentValue) {
                case '[': queue.add(map.getElement(current.getRow(), current.getCol() + 1)); break;
                case ']': queue.add(map.getElement(current.getRow(), current.getCol() - 1)); break;
                case 'O': queue.add(map.getElement(current.getRow() + rowStep, current.getCol() + colStep)); break;
                default: throw new RuntimeException("Shouldn't happen");
            }
            queue.add(map.getElement(current.getRow() + rowStep, current.getCol() + colStep));
        }
        boolean isBlocked = boxes.stream()
                .map(box -> map.getValue(box.getRow() + rowStep, box.getCol() + colStep))
                .anyMatch(box -> box == '#');
        if (!isBlocked) {
            boxes.forEach(box -> map.set(box.getRow(), box.getCol(), '.'));
            boxes.forEach(box -> map.set(box.getRow() + rowStep, box.getCol() + colStep, box.getValue()));
            map.set(pos.getRow(), pos.getCol(), '.');
            map.set(nextStep.getRow(), nextStep.getCol(), '@');
        }
        return isBlocked ? pos : nextStep;
    }

    private Matrix<Character> parseMap(boolean isPartA) {
        List<List<Character>> matrix = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            if (isPartA) {
                matrix.add(line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
            } else {
                matrix.add(line.chars()
                        .mapToObj(c -> (char) c)
                        .flatMap(c -> {
                            switch (c) {
                                case '#': return ImmutableList.of('#', '#').stream();
                                case 'O': return ImmutableList.of('[', ']').stream();
                                case '.': return ImmutableList.of('.', '.').stream();
                                case '@': return ImmutableList.of('@', '.').stream();
                                default: throw new RuntimeException("Shouldn't happen");
                            }
                        }).collect(Collectors.toList()));
            }
        }
        return new Matrix<>(matrix);
    }

    private Queue<Character> parseCommands() {
        Queue<Character> commands = new LinkedList<>();
        while (scanner.hasNextLine()) {
            scanner.nextLine().chars()
                    .mapToObj(c -> (char) c)
                    .forEach(commands::add);
        }
        return commands;
    }

}
