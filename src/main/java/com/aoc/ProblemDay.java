package com.aoc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public abstract class ProblemDay<T, U> {

    protected final Scanner scanner;

    protected abstract T solveA();

    protected abstract U solveB();

    public ProblemDay() {
        String clazz = this.getClass().getName();
        int year = Integer.parseInt(clazz.substring(9, 13));
        int day = Integer.parseInt(clazz
                .replace(".", "")
                .replaceAll("[0-9]{4}", "")
                .replaceAll("[A-Za-z]", ""));
        this.scanner = getProblemInputStream(year, day);
    }

    public void run(char part) {
        try (scanner) {
            Instant t1 = Instant.now();
            switch (part) {
                case 'a': System.out.println(solveA()); break;
                case 'b': System.out.println(solveB()); break;
                default: throw new RuntimeException(String.format("Unsupported part: %c", part));
            }
            Instant t2 = Instant.now();
            System.out.printf("Problem solved in: %d ms%n", Duration.between(t1, t2).toMillis());
        }
    }

    private Scanner getProblemInputStream(int year, int day) {
        Path path = Paths.get(".", String.format("src/main/resources/y%d/day%d.txt", year, day));
        try {
            return new Scanner(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not open file at path: %s", path));
        }
    }

}
