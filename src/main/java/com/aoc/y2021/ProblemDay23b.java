package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class ProblemDay23b implements ProblemDay<Integer> {

    private static final int STACK_LIMIT = 4;

    private static final Set<Integer> HALLWAY = new HashSet<Integer>() {{
        add(1); add(2); add(4); add(6); add(8); add(10); add(11);
    }};

    private static final Map<String, Character> ACTIVE = new HashMap<String, Character>() {{
        put("A1", 'A'); put("A2", 'A'); put("A3", 'A'); put("A4", 'A');
        put("B1", 'B'); put("B2", 'B'); put("B3", 'B'); put("B4", 'B');
        put("C1", 'C'); put("C2", 'C'); put("C3", 'C'); put("C4", 'C');
        put("D1", 'D'); put("D2", 'D'); put("D3", 'D'); put("D4", 'D');
    }};

    private static final Map<Character, Integer> TARGET_COLUMN = new HashMap<Character, Integer>() {{
        put('A', 3); put('B', 5); put('C', 7); put('D', 9);
    }};

    private static final Map<Character, Integer> COST = new HashMap<Character, Integer>() {{
        put('A', 1); put('B', 10); put('C', 100); put('D', 1000);
    }};

    private Scanner scanner;

    @Override
    public Integer solve() {
        final Map<Character, Stack<Pair<String, Pair<Integer, Integer>>>> stacks = new HashMap<>();
        stacks.put('A', new Stack<>());
        stacks.put('B', new Stack<>());
        stacks.put('C', new Stack<>());
        stacks.put('D', new Stack<>());

        stacks.get('A').add(ImmutablePair.of("B1", ImmutablePair.of(5, 3)));
        stacks.get('A').add(ImmutablePair.of("D1", ImmutablePair.of(4, 3)));
        stacks.get('A').add(ImmutablePair.of("D2", ImmutablePair.of(3, 3)));
        stacks.get('A').add(ImmutablePair.of("B2", ImmutablePair.of(2, 3)));
        stacks.get('B').add(ImmutablePair.of("C1", ImmutablePair.of(5, 5)));
        stacks.get('B').add(ImmutablePair.of("B3", ImmutablePair.of(4, 5)));
        stacks.get('B').add(ImmutablePair.of("C2", ImmutablePair.of(3, 5)));
        stacks.get('B').add(ImmutablePair.of("C3", ImmutablePair.of(2, 5)));
        stacks.get('C').add(ImmutablePair.of("D3", ImmutablePair.of(5, 7)));
        stacks.get('C').add(ImmutablePair.of("A1", ImmutablePair.of(4, 7)));
        stacks.get('C').add(ImmutablePair.of("B4", ImmutablePair.of(3, 7)));
        stacks.get('C').add(ImmutablePair.of("A2", ImmutablePair.of(2, 7)));
        stacks.get('D').add(ImmutablePair.of("A3", ImmutablePair.of(5, 9)));
        stacks.get('D').add(ImmutablePair.of("C4", ImmutablePair.of(4, 9)));
        stacks.get('D').add(ImmutablePair.of("A4", ImmutablePair.of(3, 9)));
        stacks.get('D').add(ImmutablePair.of("D4", ImmutablePair.of(2, 9)));

//        stacks.get('A').add(ImmutablePair.of("A1", ImmutablePair.of(5, 3)));
//        stacks.get('A').add(ImmutablePair.of("D1", ImmutablePair.of(4, 3)));
//        stacks.get('A').add(ImmutablePair.of("D2", ImmutablePair.of(3, 3)));
//        stacks.get('A').add(ImmutablePair.of("B1", ImmutablePair.of(2, 3)));
//        stacks.get('B').add(ImmutablePair.of("D3", ImmutablePair.of(5, 5)));
//        stacks.get('B').add(ImmutablePair.of("B2", ImmutablePair.of(4, 5)));
//        stacks.get('B').add(ImmutablePair.of("C1", ImmutablePair.of(3, 5)));
//        stacks.get('B').add(ImmutablePair.of("C2", ImmutablePair.of(2, 5)));
//        stacks.get('C').add(ImmutablePair.of("C3", ImmutablePair.of(5, 7)));
//        stacks.get('C').add(ImmutablePair.of("A2", ImmutablePair.of(4, 7)));
//        stacks.get('C').add(ImmutablePair.of("B3", ImmutablePair.of(3, 7)));
//        stacks.get('C').add(ImmutablePair.of("B4", ImmutablePair.of(2, 7)));
//        stacks.get('D').add(ImmutablePair.of("A3", ImmutablePair.of(5, 9)));
//        stacks.get('D').add(ImmutablePair.of("C4", ImmutablePair.of(4, 9)));
//        stacks.get('D').add(ImmutablePair.of("A4", ImmutablePair.of(3, 9)));
//        stacks.get('D').add(ImmutablePair.of("D4", ImmutablePair.of(2, 9)));

//        stacks.get('A').add(ImmutablePair.of("B1", ImmutablePair.of(3, 3)));
//        stacks.get('A').add(ImmutablePair.of("B2", ImmutablePair.of(2, 3)));
//        stacks.get('B').add(ImmutablePair.of("C1", ImmutablePair.of(3, 5)));
//        stacks.get('B').add(ImmutablePair.of("C2", ImmutablePair.of(2, 5)));
//        stacks.get('C').add(ImmutablePair.of("D1", ImmutablePair.of(3, 7)));
//        stacks.get('C').add(ImmutablePair.of("A1", ImmutablePair.of(2, 7)));
//        stacks.get('D').add(ImmutablePair.of("A2", ImmutablePair.of(3, 9)));
//        stacks.get('D').add(ImmutablePair.of("D2", ImmutablePair.of(2, 9)));

//        stacks.get('A').add(ImmutablePair.of("A1", ImmutablePair.of(3, 3)));
//        stacks.get('A').add(ImmutablePair.of("B1", ImmutablePair.of(2, 3)));
//        stacks.get('B').add(ImmutablePair.of("D1", ImmutablePair.of(3, 5)));
//        stacks.get('B').add(ImmutablePair.of("C1", ImmutablePair.of(2, 5)));
//        stacks.get('C').add(ImmutablePair.of("C2", ImmutablePair.of(3, 7)));
//        stacks.get('C').add(ImmutablePair.of("B2", ImmutablePair.of(2, 7)));
//        stacks.get('D').add(ImmutablePair.of("A2", ImmutablePair.of(3, 9)));
//        stacks.get('D').add(ImmutablePair.of("D2", ImmutablePair.of(2, 9)));

        final Map<String, Pair<Integer, Integer>> hallwayPositions = new HashMap<>();
//        final Set<Pair<Integer, Integer>> occupied = stacks.values().stream()
//                .flatMap(Collection::stream)
//                .map(Pair::getValue)
//                .collect(Collectors.toSet());
        final State aStar = new State(hallwayPositions, stacks);
        final Queue<State> bfs = new LinkedList<>();
        final Map<State, Integer> memo = new HashMap<>();
        bfs.add(aStar);
        memo.put(aStar, 0);
        while (!bfs.isEmpty()) {
            final State current = bfs.poll();
            final int energy = memo.get(current);
            final Set<State> next = score(current.hallwayPositions, energy, memo, current.stacks);
            bfs.addAll(next);
        }

        return memo.entrySet().stream().filter(e -> e.getKey().isSolution()).map(Map.Entry::getValue).min(Comparator.naturalOrder()).orElse(0);
    }

    private Set<State> score(final Map<String, Pair<Integer, Integer>> hallwayPositions,
                             final int energy,
                             final Map<State, Integer> memo,
                             final Map<Character, Stack<Pair<String, Pair<Integer, Integer>>>> stacks) {
        final Set<State> next = new HashSet<>();
        final Map<String, Pair<Integer, Integer>> moveablePositions = new HashMap<>();
        moveablePositions.putAll(hallwayPositions);
        moveablePositions.putAll(stacks.values().stream()
                .filter(s -> !s.isEmpty())
                .map(Stack::peek)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
        for (final Map.Entry<String, Pair<Integer, Integer>> position : moveablePositions.entrySet()) {
            final char active = ACTIVE.get(position.getKey());
            // hallwayPositions.getOrDefault("B2", ImmutablePair.of(0, 0)).equals(ImmutablePair.of(1, 4))
            // hallwayPositions.getOrDefault("B2", ImmutablePair.of(0, 0)).equals(ImmutablePair.of(1, 4)) && hallwayPositions.getOrDefault("C1", ImmutablePair.of(0, 0)).equals(ImmutablePair.of(1, 6))
            // hallwayPositions.getOrDefault("B2", ImmutablePair.of(0, 0)).equals(ImmutablePair.of(1, 4)) && stacks.get('C').peek().getKey().equals("C1")
            // hallwayPositions.getOrDefault("B2", ImmutablePair.of(0, 0)).equals(ImmutablePair.of(1, 4)) && stacks.get('C').peek().getKey().equals("C1") && hallwayPositions.getOrDefault("D1", ImmutablePair.of(0, 0)).equals(ImmutablePair.of(1, 6))
            final Set<Pair<Integer, Integer>> validMoves = validMoves(position.getValue(), hallwayPositions, active, stacks);
            for (final Pair<Integer, Integer> move : validMoves) {
                final Map<String, Pair<Integer, Integer>> newHallwayPositions = new HashMap<>(hallwayPositions);
                final Map<Character, Stack<Pair<String, Pair<Integer, Integer>>>> newStacks = stacks.entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        e -> e.getValue().stream()
                                                .map(p -> Pair.of(
                                                        p.getKey(),
                                                        Pair.of(p.getValue().getKey(), p.getValue().getValue())))
                                                .collect(Collectors.toCollection(Stack::new))));

                final Optional<Stack<Pair<String, Pair<Integer, Integer>>>> newStackOptional = newStacks.values().stream()
                        .filter(s -> !s.isEmpty())
                        .filter(s -> {
                            final Pair<String, Pair<Integer, Integer>> p = s.peek();
                            final String letter = p.getKey();
                            return position.getKey().equals(letter);
                        })
                        .findAny();
                newStackOptional.ifPresent(Stack::pop);

                newHallwayPositions.remove(position.getKey());
                if (move.getKey() == 1) {
                    newHallwayPositions.put(position.getKey(), move);
                } else {
                    newStacks.get(active).add(ImmutablePair.of(position.getKey(), move));
                }

                final int newEnergy = energy + COST.get(active) *
                        (Math.abs(position.getValue().getKey() - move.getKey()) +
                                Math.abs(position.getValue().getValue() - move.getValue()));

                final State newState = new State(newHallwayPositions, newStacks);
                if (!memo.containsKey(newState)) {
                    next.add(newState);
                    memo.put(newState, newEnergy);
                } else {
                    final int oldEnergy = memo.get(newState);
                    if (newEnergy < oldEnergy) {
                        memo.put(newState, newEnergy);
                    }
                }
            }
        }
        return next;
    }

    private Set<Pair<Integer, Integer>> validMoves(final Pair<Integer, Integer> position,
                                                   final Map<String, Pair<Integer, Integer>> hallwayOccupied,
                                                   final char active,
                                                   final Map<Character, Stack<Pair<String, Pair<Integer, Integer>>>> stacks) {
        final Stack<Pair<String, Pair<Integer, Integer>>> stack = stacks.get(active);
        if (position.getKey() == 1) {
            final int targetCol = TARGET_COLUMN.get(active);
            final int currentCol = position.getValue();
            final boolean isPathOccupied = (currentCol > targetCol)
                    ? hallwayOccupied.values().stream()
                            .filter(p -> p.getKey() == 1)
                            .map(Pair::getValue)
                            .anyMatch(c -> c < currentCol && targetCol < c)
                    : hallwayOccupied.values().stream()
                            .filter(p -> p.getKey() == 1)
                            .map(Pair::getValue)
                            .anyMatch(c -> c > currentCol && targetCol > c);
            if (isPathOccupied) {
                return new HashSet<>();
            } else if (stack.stream().map(Pair::getKey).anyMatch(k -> !k.contains(String.valueOf(active)))) {
                return new HashSet<>();
            } else {
                return new HashSet<>() {{
                    add(ImmutablePair.of(STACK_LIMIT - stack.size() + 1, TARGET_COLUMN.get(active)));
                }};
            }
        } else {
            if (stack.size() == STACK_LIMIT && stack.stream().map(Pair::getKey).allMatch(k -> k.contains(String.valueOf(active)))) {
                return new HashSet<>();
            } else if (stack.stream().map(Pair::getValue).collect(Collectors.toSet()).contains(position) &&
                    stack.stream().map(Pair::getKey).allMatch(k -> k.contains(String.valueOf(active)))) {
                return new HashSet<>();
            } else {
                final Set<Pair<Integer, Integer>> valid = new HashSet<>();
                for (int i = position.getValue(); i <= 11; i++) {
                    if (!HALLWAY.contains(i)) {
                        continue;
                    }
                    final Pair<Integer, Integer> hallway = ImmutablePair.of(1, i);
                    if (!hallwayOccupied.containsValue(hallway)) {
                        valid.add(hallway);
                    } else {
                        break;
                    }
                }
                for (int i = position.getValue(); i >= 1; i--) {
                    if (!HALLWAY.contains(i)) {
                        continue;
                    }
                    final Pair<Integer, Integer> hallway = ImmutablePair.of(1, i);
                    if (!hallwayOccupied.containsValue(hallway)) {
                        valid.add(hallway);
                    } else {
                        break;
                    }
                }
                return valid;
            }
        }
    }

    private static final class State {

        private final Map<String, Pair<Integer, Integer>> hallwayPositions;

        private final Map<Character, Stack<Pair<String, Pair<Integer, Integer>>>> stacks;

        public State(final Map<String, Pair<Integer, Integer>> hallwayPositions,
                     final Map<Character, Stack<Pair<String, Pair<Integer, Integer>>>> stacks) {
            this.hallwayPositions = hallwayPositions;
            this.stacks = stacks;
        }

        public boolean isSolution() {
            return stacks.entrySet().stream()
                    .filter(e -> e.getValue().size() == STACK_LIMIT)
                    .filter(e -> e.getValue().stream().allMatch(p -> p.getKey().contains(String.valueOf(e.getKey()))))
                    .count() == 4;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(hallwayPositions, state.hallwayPositions) &&
                    Objects.equals(stacks, state.stacks);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hallwayPositions, stacks);
        }
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day23.txt"));
        return scanner;
    }
}
