package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProblemDay25 extends ProblemDay<String, Integer> {

    private static final int BASE = 5;
    private static final Map<Character, Integer> SNAFU_TO_INT_MAPPING = ImmutableMap.of(
            '0', 0,
            '1', 1,
            '2', 2,
            '-', -1,
            '=', -2);
    private static final Map<Integer, Character> INT_TO_SNAFU_MAPPING = ImmutableMap.of(
            -2, '=',
            -1, '-',
            0, '0',
            1, '1',
            2, '2');

    @Override
    public String solveA() {
        return solve();
    }

    @Override
    public Integer solveB() {
        throw new NotImplementedException("Day 25 has no part B");
    }

    private String solve() {
        List<String> snafus = new ArrayList<>();
        while (scanner.hasNextLine()) {
            snafus.add(scanner.nextLine());
        }
        return snafus.stream().reduce(this::add).orElseThrow();
    }

    private String add(String snafu1, String snafu2) {
        int idx = 0;
        StringBuilder total = new StringBuilder();
        int carry = 0;
        while (true) {
            int i1 = snafu1.length() - idx - 1;
            int i2 = snafu2.length() - idx - 1;
            if (i1 < 0 && i2 < 0) {
                break;
            }
            int digit1 = i1 >= 0 ? SNAFU_TO_INT_MAPPING.get(snafu1.charAt(i1)) : 0;
            int digit2 = i2 >= 0 ? SNAFU_TO_INT_MAPPING.get(snafu2.charAt(i2)) : 0;
            int add = digit1 + digit2 + carry;
            carry = 0;
            if (add > 2) {
                add -= BASE;
                carry = 1;
            } else if (add < -2) {
                add += BASE;
                carry = -1;
            }
            total.insert(0, INT_TO_SNAFU_MAPPING.get(add));
            idx++;
        }
        return carry > 0 ? total.insert(0, carry).toString() : total.toString();
    }

}
