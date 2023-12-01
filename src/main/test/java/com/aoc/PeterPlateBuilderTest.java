package com.aoc;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

public class PeterPlateBuilderTest {

    @Test
    @SneakyThrows
    public void build() {
        // Make folder for current year
        String year = String.valueOf(ZonedDateTime.now().getYear());
        Path srcRoot = Paths.get(String.format("src/main/java/com/aoc/y%s", year));
        srcRoot.toFile().mkdir();
        Path srcTemplate = Paths.get("src/main/resources/PeterPlateSource.template");
        for (int day = 1; day <= 25; day++) {
            String src = Files.readString(srcTemplate).replace("{year}", year).replace("{day}", String.valueOf(day));
            Path srcPath = Paths.get(String.format("src/main/java/com/aoc/y%s/ProblemDay%d.java", year, day));
            Files.write(srcPath, src.getBytes(StandardCharsets.UTF_8));
        }

        Path testRoot = Paths.get(String.format("src/main/test/java/com/aoc/y%s", year));
        testRoot.toFile().mkdir();
        Path testTemplate = Paths.get("src/main/resources/PeterPlateTest.template");
        for (int day = 1; day <= 25; day++) {
            String test = Files.readString(testTemplate).replace("{year}", year).replace("{day}", String.valueOf(day));
            Path testPath = Paths.get(String.format("src/main/test/java/com/aoc/y%s/ProblemDay%dTest.java", year, day));
            Files.write(testPath, test.getBytes(StandardCharsets.UTF_8));
        }

        Path resRoot = Paths.get(String.format("src/main/resources/y%s", year));
        resRoot.toFile().mkdir();
        for (int day = 1; day <= 25; day++) {
            Path resPath = Paths.get(String.format("src/main/resources/y%s/day%d.txt", year, day));
            Files.write(resPath, "".getBytes(StandardCharsets.UTF_8));
        }
    }

}

