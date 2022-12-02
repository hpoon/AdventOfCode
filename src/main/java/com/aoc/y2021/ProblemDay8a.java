package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay8a implements ProblemDay<Long> {

    private static final Map<Integer, Integer> SEGMENT_TO_NUMBER = new HashMap<Integer, Integer>() {{
        put(2, 1);
        put(4, 4);
        put(3, 7);
        put(7, 8);
    }};

    private Scanner scanner;

    public Long solve() {
        final List<List<Set<String>>> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String[] split1 = scanner.nextLine().split(" \\| ");
            final String[] outputs = split1[1].split(" ");
            final List<Set<String>> outputSets = Arrays.stream(outputs)
                    .map(signal -> IntStream.range(0, signal.length())
                            .mapToObj(i -> signal.substring(i, i+1))
                            .collect(Collectors.toSet()))
                    .collect(Collectors.toList());
            data.add(outputSets);
        }

        return data.stream()
                .flatMap(Collection::stream)
                .map(Set::size)
                .filter(SEGMENT_TO_NUMBER::containsKey)
                .count();
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day8.txt"));
        return scanner;
    }

}
