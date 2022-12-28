package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProblemDay25 extends ProblemDay<String, Integer> {

    private static final int BASE = 5;
    private static final Map<Character, Long> SNAFU_TO_LONG_MAPPING = ImmutableMap.of(
            '0', 0L,
            '1', 1L,
            '2', 2L,
            '-', -1L,
            '=', -2L);
    private static final Map<Long, Character> LONG_TO_SNAFU_MAPPING = ImmutableMap.of(
            -2L, '=',
            -1L, '-',
            0L, '0',
            1L, '1',
            2L, '2');

    @Override
    public String solveA() {
        List<Long> longs = parse();
        long sum = longs.stream().reduce(Long::sum).orElseThrow();
        return longToSnafu(sum);
    }

    @Override
    public Integer solveB() {
        throw new NotImplementedException("Day 25 has no part B");
    }

    private List<Long> parse() {
        List<Long> longs = new ArrayList<>();
        while (scanner.hasNextLine()) {
            longs.add(snafuToLong(scanner.nextLine()));
        }
        return longs;
    }

    private long snafuToLong(String snafu) {
        long sum = 0;
        for (int i = 0; i < snafu.length(); i++) {
            long digit = SNAFU_TO_LONG_MAPPING.get(snafu.charAt(i));
            sum += digit * Math.pow(BASE, snafu.length() - i - 1);
        }
        return sum;
    }

    private String longToSnafu(long longNum) {
        long num = longNum;
        String result = "";
        while (true) {
            long div = num / BASE;
            long mod = num % BASE;
            if (mod > 2) {
                div++;
                mod -= BASE;
            }
            result = LONG_TO_SNAFU_MAPPING.get(mod) + result;
            num = div;
            if (div == 0) {
                break;
            }
        }
        return result;
    }

}
