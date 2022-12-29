package com.aoc.y2015;

import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay2 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<List<Integer>> dims = parse();
        return dims.stream()
                .map(d -> {
                    int a = d.get(0);
                    int b = d.get(1);
                    int c = d.get(2);
                    return 2*a*b + 2*b*c + 2*c*a + a*b;
                })
                .reduce(Integer::sum).orElseThrow();
    }

    @Override
    protected Integer solveB() {
        List<List<Integer>> dims = parse();
        return dims.stream()
                .map(d -> {
                    int a = d.get(0);
                    int b = d.get(1);
                    int c = d.get(2);
                    return 2*a + 2*b + a*b*c;
                })
                .reduce(Integer::sum).orElseThrow();
    }

    private List<List<Integer>> parse() {
        List<List<Integer>> dims = new ArrayList<>();
        while (scanner.hasNextLine()) {
            List<Integer> line = Arrays.stream(scanner.nextLine().split("x"))
                    .map(Integer::parseInt)
                    .sorted()
                    .collect(Collectors.toList());
            dims.add(line);
        }
        return dims;
    }

}
