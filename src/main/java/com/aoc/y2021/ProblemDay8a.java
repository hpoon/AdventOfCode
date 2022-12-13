package com.aoc.y2021;

import com.aoc.ProblemDay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay8a extends ProblemDay<Long> {

    private static final Map<Integer, Integer> SEGMENT_TO_NUMBER = new HashMap<Integer, Integer>() {{
        put(2, 1);
        put(4, 4);
        put(3, 7);
        put(7, 8);
    }};

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

}
