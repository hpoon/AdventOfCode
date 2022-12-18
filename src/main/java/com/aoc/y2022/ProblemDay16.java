package com.aoc.y2022;

import com.aoc.FloydWarshall;
import com.aoc.Graph;
import com.aoc.ProblemDay;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import lombok.Value;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay16 extends ProblemDay<Integer, Integer> {

    private static final int MAX_MINUTES_PART_A = 30;
    private static final int MAX_MINUTES_PART_B = 26;

    @Override
    public Integer solveA() {
        Graph<Valve> graph = parse();
        List<Map<Valve, Integer>> solutions = getSolutions(graph, MAX_MINUTES_PART_A);
        return bestSolution(solutions, 'a');
    }

    @Override
    public Integer solveB() {
        Graph<Valve> graph = parse();
        List<Map<Valve, Integer>> solutions = getSolutions(graph, MAX_MINUTES_PART_B);
        return bestSolution(solutions, 'b');
    }

    protected List<Map<Valve, Integer>> getSolutions(Graph<Valve> graph, int maxMinutes) {
        Map<Pair<Graph.GraphNode<Valve>, Graph.GraphNode<Valve>>, Integer> nodePairsToDistance =
                FloydWarshall.shortestPaths(graph);
        Graph.GraphNode<Valve> origin = new Graph.GraphNode<>(new Valve("AA", 0));
        return dfs(
                nodePairsToDistance,
                origin,
                graph.getNodes(),
                maxMinutes,
                new HashMap<>());
    }

    protected int bestSolution(List<Map<Valve, Integer>> solutions, char part) {
        if (part == 'a') {
            return solutions.stream().map(this::score).max(Comparator.naturalOrder()).orElseThrow();
        } else {
            Map<Set<Valve>, Integer> maxScoreForValves = new HashMap<>();
            for (Map<Valve, Integer> solution : solutions) {
                int score = score(solution);
                if (score == 0) {
                    continue;
                }
                // Hack cuz I'm using max will have higher scores
                if (score < 1000) {
                    continue;
                }
                int existingScore = maxScoreForValves.getOrDefault(solution.keySet(), 0);
                if (score > existingScore) {
                    maxScoreForValves.put(solution.keySet(), score);
                }
            }
            int max = 0;
            for (Map.Entry<Set<Valve>, Integer> s1 : maxScoreForValves.entrySet()) {
                for (Map.Entry<Set<Valve>, Integer> s2 : maxScoreForValves.entrySet()) {
                    Set<Valve> shared = Sets.intersection(s1.getKey(), s2.getKey());
                    if (!shared.isEmpty()) {
                        continue;
                    }
                    int score = s1.getValue() + s2.getValue();
                    if (score > max) {
                        max = score;
                    }
                }
            }
            return max;
        }
    }

    private List<Map<Valve, Integer>> dfs(
            Map<Pair<Graph.GraphNode<Valve>, Graph.GraphNode<Valve>>, Integer> nodePairsToDistance,
            Graph.GraphNode<Valve> origin,
            Set<Graph.GraphNode<Valve>> valves,
            int timeRemaining,
            Map<Valve, Integer> openValveToTimeRemaining) {
        List<Map<Valve, Integer>> result = new ArrayList<>() {{
            add(openValveToTimeRemaining);
        }};
        if (timeRemaining < 2) {
            return result;
        }
        for (Graph.GraphNode<Valve> valve : valves) {
            int distanceToValve = nodePairsToDistance.get(ImmutablePair.of(origin, valve));
            int newTimeRemaining = timeRemaining - (distanceToValve + 1);
            Map<Valve, Integer> copy = new HashMap<>(openValveToTimeRemaining);
            copy.put(valve.getValue(), newTimeRemaining);
            Set<Graph.GraphNode<Valve>> newValves = valves.stream()
                    .filter(v -> !v.equals(valve))
                    .filter(v -> v.getValue().getFlow() > 0)
                    .collect(Collectors.toSet());
            result.addAll(dfs(nodePairsToDistance, valve, newValves, newTimeRemaining,copy));
        }
        return result;
    }

    private int score(Map<Valve, Integer> openToTimeRemaining) {
        return openToTimeRemaining.entrySet().stream()
                .filter(e -> e.getValue() >= 0)
                .map(e -> e.getKey().getFlow() * e.getValue())
                .reduce(Integer::sum)
                .orElse(0);
    }

    private Graph<Valve> parse() {
        Map<String, Integer> valveToFlow = new HashMap<>();
        Multimap<String, String> valveToDestinations = ArrayListMultimap.create();
        while (scanner.hasNextLine()) {
            String[] elements = scanner.nextLine()
                    .replace("Valve ", "")
                    .replace("has flow rate=", "")
                    .replace("tunnels", "tunnel")
                    .replace("valves", "valve")
                    .replace("leads", "lead")
                    .replace("; tunnel lead to valve", "")
                    .replace(",", "")
                    .split(" ");
            valveToFlow.put(elements[0], Integer.parseInt(elements[1]));
            for (int i = 2; i < elements.length; i++) {
                valveToDestinations.put(elements[0], elements[i]);
            }
        }
        Graph<Valve> graph = new Graph<>();
        for (String origin : valveToFlow.keySet()) {
            int flow = valveToFlow.get(origin);
            Collection<String> destinations = valveToDestinations.get(origin);
            Graph.GraphNode<Valve> originNode = new Graph.GraphNode<>(new Valve(origin, flow));
            destinations.forEach(destination -> {
                Graph.GraphNode<Valve> destinationNode = new Graph.GraphNode<>(
                        new Valve(destination, valveToFlow.get(destination)));
                graph.addConnection(originNode, destinationNode, 1);
            });
        }
        return graph;
    }

    @Value
    protected static class Valve {
        private final String label;
        private final int flow;
    }

}
