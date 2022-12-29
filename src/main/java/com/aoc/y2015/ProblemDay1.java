package com.aoc.y2015;

import com.aoc.ProblemDay;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay1 extends ProblemDay<Long, Integer> {

    @Override
    protected Long solveA() {
        String str = scanner.nextLine();
        Map<Character, Long> freq = IntStream.range(0, str.length())
                .boxed()
                .map(str::charAt)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        return freq.get('(') - freq.get(')');
    }

    @Override
    protected Integer solveB() {
        String str = scanner.nextLine();
        int floor = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '(': floor++; break;
                case ')': floor--; break;
            }
            if (floor == -1) {
                return i+1;
            }
        }
        throw new RuntimeException("Unexpected");
    }

}
