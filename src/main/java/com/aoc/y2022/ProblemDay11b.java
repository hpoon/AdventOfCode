package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay11b implements ProblemDay<Long> {

    private Scanner scanner;

    public Long solve() {
        List<Monkey> monkeys = new ArrayList<>();
        while (scanner.hasNextLine()) {
            monkeys.add(new Monkey(scanner));
        }

        int superModulo = monkeys.stream().map(Monkey::getModulo).reduce((v1, v2) -> v1 * v2).orElseThrow();
        for (int round = 1; round <= 10000; round++) {
            for (Monkey monkey : monkeys) {
                Multimap<Integer, Long> monkeyToWorryLevel = monkey.inspect(superModulo);
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

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day11.txt"));
        return scanner;
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
        private int modulo;

        @Getter
        private long inspected;

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
            this.modulo = div;
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

        public Multimap<Integer, Long> inspect(int superModulo) {
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
    }

}
