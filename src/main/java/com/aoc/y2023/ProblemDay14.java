package com.aoc.y2023;

import com.aoc.Matrix;
import com.aoc.ProblemDay;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProblemDay14 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Matrix<String> matrix = parse();
        Matrix<String> tilted = tiltN(matrix);
        return tilted.score(
                elem -> Objects.equals(elem.getValue(), "O"),
                s -> s.mapToInt(e -> matrix.height() - e.getRow()).sum());
    }

    @Override
    public Integer solveB() {
        Matrix<String> matrix = parse();
        Matrix<String> cur = matrix.copy();
        int cycles = 1;
        Map<Matrix<String>, Integer> visited = new HashMap<>();
        int cycleSettled = 0;
        int cycleLength = 0;
        do {
            cur = cycle(cur);
            if (visited.containsKey(cur)) {
                cycleSettled = visited.get(cur);
                cycleLength = cycles - visited.get(cur);
                break;
            }
            visited.put(cur, cycles);
            cycles++;
        } while (cycles <= 1000000000);

        int remainder = (1000000000 - cycleSettled) % cycleLength;
        cur = matrix.copy();
        for (int i = 0; i < cycleSettled + remainder; i++) {
            cur = cycle(cur);
        }
        return cur.score(
                elem -> Objects.equals(elem.getValue(), "O"),
                s -> s.mapToInt(e -> matrix.height() - e.getRow()).sum());
    }

    private Matrix<String> cycle(Matrix<String> matrix) {
        Matrix<String> copy= tiltN(matrix);
        copy = tiltW(copy);
        copy = tiltS(copy);
        copy = tiltE(copy);
        return copy;
    }

    private Matrix<String> tiltE(Matrix<String> matrix) {
        return tiltN(matrix.rotateCCW()).rotateCW();
    }

    private Matrix<String> tiltS(Matrix<String> matrix) {
        return tiltN(matrix.rotateCW().rotateCW()).rotateCCW().rotateCCW();
    }

    private Matrix<String> tiltW(Matrix<String> matrix) {
        return tiltN(matrix.rotateCW()).rotateCCW();
    }

    private Matrix<String> tiltN(Matrix<String> matrix) {
        Matrix<String> copy = matrix.copy();
        Matrix<Boolean> occupiedMatrix = copy.apply(element ->
                Objects.equals(element.getValue(), "#") || Objects.equals(element.getValue(), "O"));
        for (int col = 0; col < copy.width(0); col++) {
            int floor = -1;
            for (int row = 0; row < copy.height(); row++) {
                boolean isOccupied = occupiedMatrix.getValue(row, col);
                if (isOccupied) {
                    if (row - floor > 1 && copy.getValue(row, col).equals("O")) {
                        copy.set(floor + 1, col, "O");
                        copy.set(row, col, ".");
                        floor++;
                    } else {
                        floor = row;
                    }
                }
            }
        }
        return copy;
    }

    Matrix<String> parse() {
        Matrix<String> matrix = new Matrix<>();
        while (scanner.hasNextLine()) {
            matrix.addRow(scanner.nextLine().chars().mapToObj(Character::toString).collect(Collectors.toList()));
        }
        return matrix;
    }

}
