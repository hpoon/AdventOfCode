package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay20 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        Map<String, Module> modules = parse();
        return solveA(modules, 1000);
    }

    @Override
    public Long solveB() {
        Map<String, Module> modules = parse();
        return solveB(modules);
    }

    private long solveA(Map<String, Module> modules, int pushes) {
        long lows = 0;
        long highs = 0;
        for (int i = 0; i < pushes; i++) {
            Pair<Long, Long> one = solveOne(modules, Optional.empty());
            lows += one.getLeft();
            highs += one.getRight();
        }
        return highs * lows;
    }

    private long solveB(Map<String, Module> modules) {
        Module rx = modules.get("rx");
        List<Module> toRx = rx.getSources().get(0).getSources();
        CycleTracker tracker = new CycleTracker(
                toRx.stream().map(Module::getName).collect(Collectors.toSet()),
                new HashMap<>(),
                0L);
        while (true) {
            solveOne(modules, Optional.of(tracker));
            if (tracker.complete()) {
                break;
            }
        }
        return tracker.score();
    }

    private Pair<Long, Long> solveOne(Map<String, Module> modules,
                                      Optional<CycleTracker> tracker) {
        long lows = 0;
        long highs = 0;
        Queue<Signal> queue = new LinkedList<>();
        queue.add(new Signal(null, modules.get("broadcaster"), false));
        tracker.ifPresent(CycleTracker::increment);
        while (!queue.isEmpty()) {
            Signal signal = queue.poll();
            Module source = signal.getFrom();
            Module destination = signal.getTo();
            boolean pulse = signal.isPulse();
            if (pulse) {
                highs++;
            } else {
                lows++;
            }
            if (destination instanceof ReceiveModule) {
                continue;
            }
            tracker.ifPresent(t -> t.track(source, pulse));
            List<Signal> nextSignals = destination.process(source, pulse);
            queue.addAll(nextSignals);
        }
        return ImmutablePair.of(lows, highs);
    }

    private Map<String, Module> parse() {
        Map<String, List<String>> connections = new HashMap<>();
        Map<String, Module> modules = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine()
                    .replace(",", "")
                    .replace(" ->", "")
                    .split(" ");
            String name = null;
            Module module = null;
            List<String> destinations = IntStream.range(1, tokens.length)
                    .boxed()
                    .map(i -> tokens[i])
                    .collect(Collectors.toList());
            if (tokens[0].equals("broadcaster")) {
                name = "broadcaster";
                module = new BroadcastModule();
            }
            if (tokens[0].startsWith("%")) {
                name = tokens[0].substring(1);
                module = new FlipFlopModule(name);
            }
            if (tokens[0].startsWith("&")) {
                name = tokens[0].substring(1);
                module = new ConjunctionModule(name);
            }
            connections.put(name, destinations);
            modules.put(name, module);
        }
        modules.put("rx", new ReceiveModule());
        for (Map.Entry<String, List<String>> entry : connections.entrySet()) {
            String src = entry.getKey();
            List<String> dsts = entry.getValue();
            for (String dst : dsts) {
                modules.get(src).addDestination(modules.get(dst));
                modules.get(dst).addSource(modules.get(src));
            }
        }
        return modules;
    }

    @AllArgsConstructor
    private static class CycleTracker {
        private Set<String> keystones;
        @Getter private Map<String, Long> cycles;
        private long presses;

        public void track(Module module, boolean pulse) {
            if (module == null) {
                return;
            }
            if (keystones.contains(module.getName()) && !cycles.containsKey(module.getName()) && pulse) {
                cycles.put(module.getName(), presses);
            }
        }

        public void increment() {
            presses++;
        }

        public boolean complete() {
            return cycles.size() == keystones.size();
        }

        public long score() {
            if (!complete()) {
                throw new RuntimeException("Needs to be complete before scoring");
            }
            return cycles.values().stream()
                    .reduce((v1, v2) -> v1 * v2)
                    .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
        }

    }

    @Value
    private static class Signal {
        Module from;
        Module to;
        boolean pulse;
    }

    @ToString
    private static abstract class Module {
        @Getter protected String name;
        @Getter protected List<Module> sources;
        @Getter protected List<Module> destinations;

        public Module(String name) {
            this.name = name;
            this.sources = new ArrayList<>();
            this.destinations = new ArrayList<>();
        }

        public void addSource(Module module) {
            sources.add(module);
        }

        public void addDestination(Module module) {
            destinations.add(module);
        }

        public abstract List<Signal> process(Module source, boolean signal);

        public abstract boolean signal();
    }

    @ToString
    private static class FlipFlopModule extends Module {

        private boolean state;

        public FlipFlopModule(String name) {
            super(name);
            this.state = false;
        }

        @Override
        public List<Signal> process(Module source, boolean signal) {
            if (signal) {
                return ImmutableList.of();
            }
            state = !state;
            return destinations.stream().map(d -> new Signal(this, d, signal())).collect(Collectors.toList());
        }

        @Override
        public boolean signal() {
            return state;
        }

    }

    @ToString
    private static class ConjunctionModule extends Module {

        private final Map<String, Boolean> state;

        public ConjunctionModule(String name) {
            super(name);
            state = new HashMap<>();
        }

        @Override
        public void addSource(Module module) {
            super.addSource(module);
            state.put(module.getName(), false);
        }

        @Override
        public List<Signal> process(Module source, boolean signal) {
            state.put(source.getName(), signal);
            return destinations.stream()
                    .map(d -> new Signal(this, d, signal()))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean signal() {
            return !state.values().stream().allMatch(v -> v);
        }

    }

    @ToString
    private static class BroadcastModule extends Module {

        public BroadcastModule() {
            super("broadcaster");
        }

        @Override
        public List<Signal> process(Module source, boolean signal) {
            return destinations.stream().map(d -> new Signal(this, d, false)).collect(Collectors.toList());
        }

        @Override
        public boolean signal() {
            return false;
        }

    }

    private static class ReceiveModule extends Module {

        public ReceiveModule() {
            super("rx");
        }

        @Override
        public List<Signal> process(Module source, boolean signal) {
            throw new NotImplementedException();
        }

        @Override
        public boolean signal() {
            throw new NotImplementedException();
        }
    }

}
