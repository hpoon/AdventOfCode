package com.aoc.y2015;

import com.aoc.Matrix;
import com.aoc.MatrixElement;
import com.aoc.Point2D;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import lombok.Value;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay6 extends ProblemDay<Integer, Integer> {

    private static final int MAX_RANGE = 1000;

    @Override
    protected Integer solveA() {
        List<Instruction> instructions = parse();
        Matrix<Boolean> lights = createA();
        instructions.forEach(ins -> operateA(lights, ins));
        return lights.score(MatrixElement::getValue, stream -> (int) stream.count());
    }

    @Override
    protected Integer solveB() {
        List<Instruction> instructions = parse();
        Matrix<Integer> lights = createB();
        instructions.forEach(ins -> operateB(lights, ins));
        return lights.score(
                Objects::nonNull,
                stream -> stream
                        .map(MatrixElement::getValue)
                        .filter(Objects::nonNull)
                        .reduce(Integer::sum).orElse(0));
    }

    private void operateA(Matrix<Boolean> grid, Instruction instruction) {
        InstructionType type = instruction.getType();
        Point2D start = instruction.getStart();
        Point2D end = instruction.getEnd();
        Map<InstructionType, Function<Boolean, Boolean>> operations = ImmutableMap.of(
                InstructionType.TURN_ON, bool -> true,
                InstructionType.TURN_OFF, bool -> false,
                InstructionType.TOGGLE, bool -> !bool);
        for (int row = start.getY(); row <= end.getY(); row++) {
            for (int col = start.getX(); col <= end.getX(); col++) {
                grid.set(row, col, operations.get(type).apply(grid.get(row, col)));
            }
        }
    }

    private void operateB(Matrix<Integer> grid, Instruction instruction) {
        InstructionType type = instruction.getType();
        Point2D start = instruction.getStart();
        Point2D end = instruction.getEnd();
        Map<InstructionType, Function<Integer, Integer>> operations = ImmutableMap.of(
                InstructionType.TURN_ON, val -> val + 1,
                InstructionType.TURN_OFF, val -> val == 0 ? 0 : val - 1,
                InstructionType.TOGGLE, val -> val + 2);
        for (int row = start.getY(); row <= end.getY(); row++) {
            for (int col = start.getX(); col <= end.getX(); col++) {
                grid.set(row, col, operations.get(type).apply(grid.get(row, col)));
            }
        }
    }

    private Matrix<Boolean> createA() {
        return new Matrix<>(IntStream.range(0, MAX_RANGE)
                .boxed()
                .map(i -> IntStream.range(0, MAX_RANGE)
                        .boxed()
                        .map(j -> false)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    private Matrix<Integer> createB() {
        return new Matrix<>(IntStream.range(0, MAX_RANGE)
                .boxed()
                .map(i -> IntStream.range(0, MAX_RANGE)
                        .boxed()
                        .map(j -> 0)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    private List<Instruction> parse() {
        List<Instruction> words = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            InstructionType type;
            if (line.startsWith("turn on")) {
                type = InstructionType.TURN_ON;
            } else if (line.startsWith("turn off")) {
                type = InstructionType.TURN_OFF;
            } else if (line.startsWith("toggle")) {
                type = InstructionType.TOGGLE;
            } else {
                throw new UnsupportedOperationException("Invalid type");
            }
            String[] components = line.split(" ");
            List<Integer> start = Arrays.stream(components[components.length - 3].split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<Integer> end = Arrays.stream(components[components.length - 1].split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            words.add(new Instruction(
                    type,
                    new Point2D(start.get(0), start.get(1)),
                    new Point2D(end.get(0), end.get(1))));
        }
        return words;
    }

    @Value
    private static class Instruction {
        private final InstructionType type;
        private final Point2D start;
        private final Point2D end;
    }

    private enum InstructionType {
        TURN_ON,
        TURN_OFF,
        TOGGLE
    }

}
