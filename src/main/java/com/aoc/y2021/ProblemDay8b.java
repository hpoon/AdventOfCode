package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay8b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        int sum = 0;
        while (scanner.hasNextLine()) {
            final String[] split1 = scanner.nextLine().split(" \\| ");
            final String[] data = split1[0].split(" ");
            final String[] outputs = split1[1].split(" ");

            final List<Set<String>> signal = Arrays.stream(data)
                    .map(d -> IntStream.range(0, d.length())
                            .mapToObj(i -> d.substring(i, i+1))
                            .collect(Collectors.toSet()))
                    .collect(Collectors.toList());
            final Map<Set<String>, String> segmentToNumber = new HashMap<>();
            final Set<String> set1 = signal.stream()
                    .filter(s -> s.size() == 2).findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));
            final Set<String> set4 = signal.stream()
                    .filter(s -> s.size() == 4).findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));
            final Set<String> set7 = signal.stream()
                    .filter(s -> s.size() == 3).findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));
            final Set<String> set8 = signal.stream()
                    .filter(s -> s.size() == 7).findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));
            final Set<String> set9 = signal.stream()
                    .filter(s -> s.size() == 6)
                    .filter(s -> s.containsAll(set4))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));

            final Set<String> full = new HashSet<String>() {{
                add("a");
                add("b");
                add("c");
                add("d");
                add("e");
                add("f");
                add("g");
            }};
            final Set<String> set9Inverted = new HashSet<>(full);
            set9Inverted.removeAll(set9);

            final Set<String> set2 = signal.stream()
                    .filter(s -> s.size() == 5)
                    .filter(s -> s.containsAll(set9Inverted))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));

            final Set<String> set2Inverted = new HashSet<>(full);
            set2Inverted.removeAll(set2);

            final Set<String> set5 = signal.stream()
                    .filter(s -> s.size() == 5)
                    .filter(s -> s.containsAll(set2Inverted))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));

            final Set<String> set3 = signal.stream()
                    .filter(s -> s.size() == 5)
                    .filter(s -> !s.containsAll(set2))
                    .filter(s -> !s.containsAll(set5))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));

            final Set<String> set6 = signal.stream()
                    .filter(s -> s.size() == 6)
                    .filter(s -> !s.containsAll(set1))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));

            final Set<String> set0 = signal.stream()
                    .filter(s -> s.size() == 6)
                    .filter(s -> !s.containsAll(set9))
                    .filter(s -> s.containsAll(set1))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Missing"));

            segmentToNumber.put(set0, "0");
            segmentToNumber.put(set1, "1");
            segmentToNumber.put(set2, "2");
            segmentToNumber.put(set3, "3");
            segmentToNumber.put(set4, "4");
            segmentToNumber.put(set5, "5");
            segmentToNumber.put(set6, "6");
            segmentToNumber.put(set7, "7");
            segmentToNumber.put(set8, "8");
            segmentToNumber.put(set9, "9");

            final List<Set<String>> set = Arrays.stream(outputs)
                    .map(s -> IntStream.range(0, s.length())
                            .mapToObj(i -> s.substring(i, i+1))
                            .collect(Collectors.toSet()))
                    .collect(Collectors.toList());

            sum += Integer.parseInt(set.stream()
                    .map(segmentToNumber::get)
                    .reduce("", (a, b) -> a + b));
        }
        return sum;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day8.txt"));
        return scanner;
    }

}
