package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay12 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Pair<String, List<Integer>>> lines = parse();
        long ways = 0;
        for (Pair<String, List<Integer>> line : lines) {
            String pattern = line.getKey();
            List<Integer> damage = line.getValue();
            ways += solve(pattern, damage, new HashMap<>());
        }
        return ways;
    }

    @Override
    public Long solveB() {
        List<Pair<String, List<Integer>>> lines = parse();
        long ways = 0;
        for (Pair<String, List<Integer>> line : lines) {
            String pattern = Joiner.on("?").join(
                    IntStream.range(0, 5)
                            .boxed()
                            .map(i -> line.getKey())
                            .collect(Collectors.toList()));
            List<Integer> damage = IntStream.range(0, 5)
                    .boxed()
                    .flatMap(i -> line.getValue().stream())
                    .collect(Collectors.toList());
            ways += solve(pattern, damage, new HashMap<>());
        }
        return ways;
    }

    @VisibleForTesting
    protected long solve(String remainder, List<Integer> damage, Map<Memoization, Long> memo) {
        Memoization m = new Memoization(remainder, damage);
        if (memo.containsKey(m)) {
            return memo.get(m);
        }
        if (StringUtils.isBlank(remainder)) {
            return damage.isEmpty() ? 1 : 0;
        }
        if (damage.isEmpty()) {
            return remainder.matches("^[.?]+$") ? 1 : 0;
        }

        long ways = 0;
        char next = remainder.charAt(0);
        int dmg = damage.get(0);
        if (next == '#') {
            if (remainder.length() >= dmg) {
                if (remainder.length() == dmg
                        && remainder.substring(0, dmg).matches("^[?#]+$")) {
                    ways = solve(remainder.substring(dmg), damage.subList(1, damage.size()), memo);
                } else if (remainder.length() > dmg
                        && remainder.substring(0, dmg + 1).matches(String.format("^[?#]{%d}(?:[.?][?#]*)?$", dmg))) {
                    ways = solve(remainder.substring(dmg + 1), damage.subList(1, damage.size()), memo);
                }
            }
        } else if (next == '.') {
            ways = solve(remainder.substring(1), damage, memo);
        } else {
            ways = solve('.' + remainder.substring(1), damage, memo)
                    + solve('#' + remainder.substring(1), damage, memo);
        }
        memo.put(m, ways);
        return ways;
    }

    private List<Pair<String, List<Integer>>> parse() {
        List<Pair<String, List<Integer>>> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            lines.add(ImmutablePair.of(
                    tokens[0],
                    Arrays.stream(tokens[1].split(",")).map(Integer::parseInt).collect(Collectors.toList())
            ));
        }
        return lines;
    }

    @Value
    @EqualsAndHashCode
    private static class Memoization {
        String remainder;
        List<Integer> damage;
    }

}
