package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay11a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        List<Monkey> monkeys = new ArrayList<>();
        while (scanner.hasNextLine()) {
            monkeys.add(new Monkey(scanner));
        }

        for (int round = 1; round <= 20; round++) {
            for (Monkey monkey : monkeys) {
                Multimap<Integer, Integer> monkeyToWorryLevel = monkey.inspect();
                for (Map.Entry<Integer, Collection<Integer>> entry : monkeyToWorryLevel.asMap().entrySet()) {
                    monkeys.get(entry.getKey()).getWorryLevels().addAll(entry.getValue());
                }
            }
        }

        PriorityQueue<Integer> maxes = new PriorityQueue<>(Comparator.reverseOrder());
        monkeys.stream()
                .map(Monkey::getInspected)
                .forEach(maxes::add);
        return maxes.poll() * maxes.poll();
    }

    @RequiredArgsConstructor
    private static class Monkey {

        @Getter
        @NonNull
        private List<Integer> worryLevels;

        @NonNull
        private Function<Integer, Integer> operation;

        @NonNull
        private Function<Integer, Integer> test;

        @Getter
        private int inspected;

        public Monkey(Scanner scanner) {
            scanner.nextLine();
            String[] items = scanner.nextLine()
                    .replace("  Starting items: ", "")
                    .replace(",", "")
                    .split(" ");
            this.worryLevels = Arrays.stream(items)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            this.operation = operation(scanner.nextLine());
            this.test = test(scanner.nextLine(), scanner.nextLine(), scanner.nextLine());
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        public Multimap<Integer, Integer> inspect() {
            Multimap<Integer, Integer> monkeyToWorryLevel = ArrayListMultimap.create();
            for (int item : worryLevels) {
                inspected++;
                int newWorryLevel = operation.apply(item);
                int toMonkey = test.apply(newWorryLevel);
                monkeyToWorryLevel.put(toMonkey, newWorryLevel);
            }
            worryLevels.clear();
            return monkeyToWorryLevel;
        }

        private Function<Integer, Integer> operation(String line) {
            String[] operations = line
                    .replace("  Operation: ", "")
                    .replace("new = old ", "")
                    .split(" ");
            final Optional<Integer> constant = NumberUtils.isParsable(operations[1])
                    ? Optional.of(Integer.parseInt(operations[1]))
                    : Optional.empty();
            switch (operations[0]) {
                case "+": return old -> (old + constant.orElse(old)) / 3;
                case "-": return old -> (old - constant.orElse(old)) / 3;
                case "*": return old -> (old * constant.orElse(old)) / 3;
                case "/": return old -> (old / constant.orElse(old)) / 3;
                default: throw new RuntimeException("Invalid string " + line);
            }
        }

        private Function<Integer, Integer> test(String testLine, String trueLine, String falseLine) {
            int div = Integer.parseInt(testLine.replace("  Test: divisible by ", ""));
            int ifTrue = Integer.parseInt(trueLine.replace("    If true: throw to monkey ", ""));
            int ifFalse = Integer.parseInt(falseLine.replace("    If false: throw to monkey ", ""));
            return test -> test % div == 0 ? ifTrue : ifFalse;
        }
    }

}
