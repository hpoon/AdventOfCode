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

public class ProblemDay11 extends ProblemDay<Integer, Long> {

    @Override
    public Integer solveA() {
        List<Monkey> monkeys = new ArrayList<>();
        while (scanner.hasNextLine()) {
            monkeys.add(new Monkey(scanner));
        }

        for (int round = 1; round <= 20; round++) {
            for (Monkey monkey : monkeys) {
                Multimap<Integer, Long> monkeyToWorryLevel = monkey.inspectA();
                for (Map.Entry<Integer, Collection<Long>> entry : monkeyToWorryLevel.asMap().entrySet()) {
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

    public Long solveB() {
        List<Monkey> monkeys = new ArrayList<>();
        while (scanner.hasNextLine()) {
            monkeys.add(new Monkey(scanner));
        }

        int superModulo = monkeys.stream().map(Monkey::getDiv).reduce((v1, v2) -> v1 * v2).orElseThrow();
        for (int round = 1; round <= 10000; round++) {
            for (Monkey monkey : monkeys) {
                Multimap<Integer, Long> monkeyToWorryLevel = monkey.inspectB(superModulo);
                for (Map.Entry<Integer, Collection<Long>> entry : monkeyToWorryLevel.asMap().entrySet()) {
                    monkeys.get(entry.getKey()).getWorryLevels().addAll(entry.getValue());
                }
            }
        }

        PriorityQueue<Long> maxes = new PriorityQueue<>(Comparator.reverseOrder());
        monkeys.stream()
                .mapToLong(Monkey::getInspected)
                .forEach(maxes::add);
        return maxes.poll() * maxes.poll();
    }

    @RequiredArgsConstructor
    private static class Monkey {

        @Getter
        @NonNull
        private List<Long> worryLevels;

        @NonNull
        private Function<Long, Long> operation;

        @NonNull
        private Function<Long, Integer> test;

        @Getter
        private int div;

        @Getter
        private int inspected;

        public Monkey(Scanner scanner) {
            scanner.nextLine();
            String[] items = scanner.nextLine()
                    .replace("  Starting items: ", "")
                    .replace(",", "")
                    .split(" ");
            this.worryLevels = Arrays.stream(items)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            String[] operations = scanner.nextLine()
                    .replace("  Operation: ", "")
                    .replace("new = old ", "")
                    .split(" ");
            Optional<Integer> operand = NumberUtils.isParsable(operations[1])
                    ? Optional.of(Integer.parseInt(operations[1]))
                    : Optional.empty();
            int div = Integer.parseInt(scanner.nextLine()
                    .replace("  Test: divisible by ", ""));
            int ifTrue = Integer.parseInt(scanner.nextLine()
                    .replace("    If true: throw to monkey ", ""));
            int ifFalse = Integer.parseInt(scanner.nextLine()
                    .replace("    If false: throw to monkey ", ""));
            this.test = test -> test % div == 0 ? ifTrue : ifFalse;
            this.div = div;
            switch (operations[0]) {
                case "+":
                    this.operation = old -> old + operand.get();
                    break;
                case "*":
                    this.operation = old -> operand
                            .map(o -> old * o)
                            .orElseGet(() -> old * old);
                    break;
                default: throw new RuntimeException("Invalid string");
            }
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        public Multimap<Integer, Long> inspectA() {
            Multimap<Integer, Long> monkeyToWorryLevel = ArrayListMultimap.create();
            for (long item : worryLevels) {
                inspected++;
                long newWorryLevel = operation.apply(item) / 3;
                int toMonkey = test.apply(newWorryLevel);
                monkeyToWorryLevel.put(toMonkey, newWorryLevel);
            }
            worryLevels.clear();
            return monkeyToWorryLevel;
        }

        public Multimap<Integer, Long> inspectB(int superModulo) {
            Multimap<Integer, Long> monkeyToWorryLevel = ArrayListMultimap.create();
            for (long item : worryLevels) {
                inspected++;
                long newWorryLevel = operation.apply(item) % superModulo;
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

        private Function<Long, Integer> test(String testLine, String trueLine, String falseLine) {
            int div = Integer.parseInt(testLine.replace("  Test: divisible by ", ""));
            int ifTrue = Integer.parseInt(trueLine.replace("    If true: throw to monkey ", ""));
            int ifFalse = Integer.parseInt(falseLine.replace("    If false: throw to monkey ", ""));
            return test -> test % div == 0 ? ifTrue : ifFalse;
        }
    }

}
