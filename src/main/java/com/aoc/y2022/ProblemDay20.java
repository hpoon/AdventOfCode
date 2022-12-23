package com.aoc.y2022;

import com.aoc.ProblemDay;
import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay20 extends ProblemDay<Long, Long> {

    private static final int DECRYPTION_KEY = 811589153;
    private static final int MIX_ITERATIONS_A = 1;
    private static final int MIX_ITERATIONS_B = 10;

    @Override
    public Long solveA() {
        List<Number> list = parse();
        return mix(list, MIX_ITERATIONS_A);
    }

    @Override
    public Long solveB() {
        List<Number> list = parse();
        list = list.stream()
                .map(number -> new Number(number.getOriginalIndex(), number.getValue() * DECRYPTION_KEY))
                .collect(Collectors.toList());
        return mix(list, MIX_ITERATIONS_B);
    }

    private long mix(List<Number> list, int iterations) {
        List<Number> mixed = new ArrayList<>(list);
        for (int iter = 0; iter < iterations; iter++) {
            for (Number number : list) {
                long index = mixed.indexOf(number);
                mixed.remove(number);
                index = (index + number.getValue()) % mixed.size();
                while (index < 0) {
                    index += mixed.size();
                }
                mixed.add((int) index, number);
            }
        }

        Number zero = mixed.stream().filter(n -> n.getValue() == 0).findFirst().orElseThrow();
        int indexOfZero = mixed.indexOf(zero);
        int a = (1000 + indexOfZero) % mixed.size();
        int b = (2000 + indexOfZero) % mixed.size();
        int c = (3000 + indexOfZero) % mixed.size();
        return mixed.get(a).getValue() + mixed.get(b).getValue() + mixed.get(c).getValue();
    }

    private List<Number> parse() {
        List<Number> list = new ArrayList<>();
        int count = 0;
        while (scanner.hasNextLine()) {
            list.add(new Number(count, Integer.parseInt(scanner.nextLine())));
            count++;
        }
        return list;
    }

    @Value
    private static class Number {
        private long originalIndex;
        private long value;
    }

}
