package com.aoc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public abstract class ProblemDay<T> {

    protected final Scanner scanner;

    protected abstract T solve();

    public ProblemDay() {
        this.scanner = getProblemInputStream(
                Integer.parseInt(
                        this.getClass().getName()
                                .replace(".", "")
                                .replaceAll("[0-9]{4}", "")
                                .replaceAll("[A-Za-z]", "")));
    }

    public void run() {
        try (scanner) {
            Instant t1 = Instant.now();
            System.out.println(solve());
            Instant t2 = Instant.now();
            System.out.printf("Problem solved in: %d ms%n", Duration.between(t1, t2).toMillis());
        }
    }

    private Scanner getProblemInputStream(int day) {
        Path path = Paths.get(".", String.format("src/main/resources/y2022/day%d.txt", day));
        try {
            return new Scanner(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not open file at path: %s", path));
        }
    }

}
