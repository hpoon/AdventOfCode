
package com.aoc.y2021;

import com.aoc.ProblemDay;
import javafx.util.Pair;

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

public class ProblemDay23a implements ProblemDay<Integer> {

    private static final Set<Integer> HALLWAY = new HashSet<Integer>() {{
        add(1); add(2); add(4); add(6); add(8); add(10); add(11);
    }};

    private static final Map<Pair<Integer, Integer>, Pair<Integer, Integer>> BLOCKED_SPAWN =
            new HashMap<Pair<Integer, Integer>, Pair<Integer, Integer>>() {{
        put(new Pair<>(3, 3), new Pair<>(2, 3));
        put(new Pair<>(3, 5), new Pair<>(2, 5));
        put(new Pair<>(3, 7), new Pair<>(2, 7));
        put(new Pair<>(3, 9), new Pair<>(2, 9));
    }};

    private static final Map<Character, Pair<Integer, Integer>> FRONT_DESTINATION = new HashMap<Character, Pair<Integer, Integer>>() {{
        put('A', new Pair<>(2, 3));
        put('B', new Pair<>(2, 5));
        put('C', new Pair<>(2, 7));
        put('D', new Pair<>(2, 9));
    }};

    private static final Map<Character, Pair<Integer, Integer>> REAR_DESTINATION = new HashMap<Character, Pair<Integer, Integer>>() {{
        put('A', new Pair<>(3, 3));
        put('B', new Pair<>(3, 5));
        put('C', new Pair<>(3, 7));
        put('D', new Pair<>(3, 9));
    }};

    private static final Map<String, Character> ACTIVE = new HashMap<String, Character>() {{
        put("A1", 'A'); put("A2", 'A'); put("B1", 'B'); put("B2", 'B');
        put("C1", 'C'); put("C2", 'C'); put("D1", 'D'); put("D2", 'D');
    }};

    private static final Map<Character, Integer> COST = new HashMap<Character, Integer>() {{
        put('A', 1); put('B', 10); put('C', 100); put('D', 1000);
    }};

    private Scanner scanner;

    @Override
    public Integer solve() {
        final Map<String, Pair<Integer, Integer>> positions = new HashMap<String, Pair<Integer, Integer>>() {{
            put("A1", new Pair<>(2, 7));
            put("A2", new Pair<>(3, 9));
            put("B1", new Pair<>(2, 3));
            put("B2", new Pair<>(3, 3));
            put("C1", new Pair<>(2, 5));
            put("C2", new Pair<>(3, 5));
            put("D1", new Pair<>(2, 9));
            put("D2", new Pair<>(3, 7));
        }};
//        final Map<String, Pair<Integer, Integer>> positions = new HashMap<String, Pair<Integer, Integer>>() {{
//            put("A1", new Pair<>(3, 3));
//            put("A2", new Pair<>(3, 9));
//            put("B1", new Pair<>(2, 3));
//            put("B2", new Pair<>(2, 7));
//            put("C1", new Pair<>(2, 5));
//            put("C2", new Pair<>(3, 7));
//            put("D1", new Pair<>(3, 5));
//            put("D2", new Pair<>(2, 9));
//        }};
        final Set<Pair<Integer, Integer>> occupied = new HashSet<>(positions.values());
        final State aStar = new State(positions, occupied);
        final Queue<State> bfs = new LinkedList<>();
        final Map<State, Integer> memo = new HashMap<>();
        bfs.add(aStar);
        memo.put(aStar, 0);
        while (!bfs.isEmpty()) {
            final State current = bfs.poll();
            final int energy = memo.get(current);
            final Set<State> next = score(current.positions, current.occupied, energy, memo);
            bfs.addAll(next);
        }

        return memo.entrySet().stream().filter(e -> e.getKey().isSolution()).map(Map.Entry::getValue).min(Comparator.naturalOrder()).orElse(0);
    }

    private Set<State> score(final Map<String, Pair<Integer, Integer>> positions,
                             final Set<Pair<Integer, Integer>> occupied,
                             final int energy,
                             final Map<State, Integer> memo) {
        final Set<State> next = new HashSet<>();
        for (final Map.Entry<String, Pair<Integer, Integer>> entry : positions.entrySet()) {
            final char active = ACTIVE.get(entry.getKey());
            final Pair<Integer, Integer> position = entry.getValue();
            final Set<Pair<Integer, Integer>> validMoves = validMoves(position, occupied, active, positions);
            for (final Pair<Integer, Integer> move : validMoves) {
                final Map<String, Pair<Integer, Integer>> newPositions = new HashMap<>(positions);
                newPositions.put(entry.getKey(), move);

                final Set<Pair<Integer, Integer>> newOccupied = new HashSet<>(occupied);
                newOccupied.remove(position);
                newOccupied.add(move);

                final int newEnergy = energy + COST.get(active) *
                        (Math.abs(position.getKey() - move.getKey()) + Math.abs(position.getValue() - move.getValue()));

                final State newState = new State(newPositions, newOccupied);
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
                                                   final Set<Pair<Integer, Integer>> occupied,
                                                   final char active,
                                                   final Map<String, Pair<Integer, Integer>> positions) {
        if (position.getKey() == 1) {
            final Pair<Integer, Integer> front = FRONT_DESTINATION.get(active);
            final Pair<Integer, Integer> rear = REAR_DESTINATION.get(active);
            final int targetCol = front.getValue();
            final int currentCol = position.getValue();
            final boolean isPathOccupied = (currentCol > targetCol)
                    ? occupied.stream().filter(p -> p.getKey() == 1).map(Pair::getValue).anyMatch(c -> c < currentCol && targetCol < c)
                    : occupied.stream().filter(p -> p.getKey() == 1).map(Pair::getValue).anyMatch(c -> c > currentCol && targetCol > c);
            if (isPathOccupied) {
                return new HashSet<>();
            }
            if (occupied.contains(front)) {
                return new HashSet<>();
            } else {
                if (!occupied.contains(rear)) {
                    return new HashSet<Pair<Integer, Integer>>() {{
                        add(rear);
                    }};
                } else {
                    final boolean isRearCorrect = positions.entrySet().stream()
                            .filter(e -> e.getKey().contains(String.valueOf(active)))
                            .map(Map.Entry::getValue)
                            .anyMatch(p -> p.equals(rear));
                    if (!isRearCorrect) {
                        return new HashSet<>();
                    } else {
                        return new HashSet<Pair<Integer, Integer>>() {{
                            add(front);
                        }};
                    }
                }
            }
        } else if (position.getKey() == 2) {
            final Pair<Integer, Integer> front = FRONT_DESTINATION.get(active);
            final Pair<Integer, Integer> rear = REAR_DESTINATION.get(active);
            final Optional<String> letterAtRear = positions.entrySet().stream()
                    .filter(e -> e.getValue().equals(rear))
                    .map(Map.Entry::getKey)
                    .findAny();
            if (position.equals(front) && letterAtRear.isPresent() && letterAtRear.get().contains(String.valueOf(active))) {
                return new HashSet<>();
            } else {
                final Set<Pair<Integer, Integer>> valid = new HashSet<>();
                for (int i = position.getValue(); i <= 11; i++) {
                    if (!HALLWAY.contains(i)) {
                        continue;
                    }
                    final Pair<Integer, Integer> hallway = new Pair<>(1, i);
                    if (!occupied.contains(hallway)) {
                        valid.add(hallway);
                    } else {
                        break;
                    }
                }
                for (int i = position.getValue(); i >= 1; i--) {
                    if (!HALLWAY.contains(i)) {
                        continue;
                    }
                    final Pair<Integer, Integer> hallway = new Pair<>(1, i);
                    if (!occupied.contains(hallway)) {
                        valid.add(hallway);
                    } else {
                        break;
                    }
                }
                return valid;
            }
        } else {
            if (position.equals(REAR_DESTINATION.get(active))) {
                return new HashSet<>();
            } else if (BLOCKED_SPAWN.containsKey(position) && occupied.contains(BLOCKED_SPAWN.get(position))) {
                return new HashSet<>();
            } else {
                final Set<Pair<Integer, Integer>> valid = new HashSet<>();
                for (int i = position.getValue(); i <= 11; i++) {
                    if (!HALLWAY.contains(i)) {
                        continue;
                    }
                    final Pair<Integer, Integer> hallway = new Pair<>(1, i);
                    if (!occupied.contains(hallway)) {
                        valid.add(hallway);
                    } else {
                        break;
                    }
                }
                for (int i = position.getValue(); i >= 1; i--) {
                    if (!HALLWAY.contains(i)) {
                        continue;
                    }
                    final Pair<Integer, Integer> hallway = new Pair<>(1, i);
                    if (!occupied.contains(hallway)) {
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

        private final Map<String, Pair<Integer, Integer>> positions;

        private final Set<Pair<Integer, Integer>> occupied;

        public State(final Map<String, Pair<Integer, Integer>> positions,
                     final Set<Pair<Integer, Integer>> occupied) {
            this.positions = positions;
            this.occupied = occupied;
        }

        public boolean isSolution() {
            for (final Map.Entry<String, Pair<Integer, Integer>> entry : positions.entrySet()) {
                final String active = entry.getKey();
                final Pair<Integer, Integer> position = entry.getValue();
                final Pair<Integer, Integer> front = FRONT_DESTINATION.get(ACTIVE.get(active));
                final Pair<Integer, Integer> rear = REAR_DESTINATION.get(ACTIVE.get(active));
                if (!position.equals(front) && !position.equals(rear)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(positions, state.positions) && Objects.equals(occupied, state.occupied);
        }

        @Override
        public int hashCode() {
            return Objects.hash(positions, occupied);
        }
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day23.txt"));
        return scanner;
    }
}
