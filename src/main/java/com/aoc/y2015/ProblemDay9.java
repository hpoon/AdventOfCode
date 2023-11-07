package com.aoc.y2015;

import com.aoc.Graph;
import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;

public class ProblemDay9 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        Graph<String> graph = parse();
        int best = 9999999;
        for (Graph.GraphNode<String> origin : graph.getNodes()) {
            int dist = solveA(origin, graph, new HashSet<>(), 0, best);
            if (dist < best) {
                best = dist;
            }
        }
        return best;
    }

    @Override
    protected Integer solveB() {
        Graph<String> graph = parse();
        int best = 0;
        for (Graph.GraphNode<String> origin : graph.getNodes()) {
            int dist = solveB(origin, graph, new HashSet<>(), 0, best);
            if (dist > best) {
                best = dist;
            }
        }
        return best;
    }

    private int solveA(Graph.GraphNode<String> node, Graph<String> graph, Set<Graph.GraphNode<String>> visited, int runningTotal, int best) {
        Set<Graph.GraphNode<String>> eligible = Sets.difference(Sets.difference(graph.getNodes(), visited), ImmutableSet.of(node));
        Set<Graph.GraphNode<String>> newVisited = new HashSet<>(visited);
        newVisited.add(node);
        if (eligible.size() == 1) {
            int dist = graph.getAdjacentConnectionWeight(node, eligible.iterator().next());
            return Math.min(best, runningTotal + dist);
        } else {
            for (Graph.GraphNode<String> next : eligible) {
                int dist = graph.getAdjacentConnectionWeight(node, next);
                best = solveA(next, graph, newVisited, runningTotal + dist, best);
            }
        }
        return best;
    }

    private int solveB(Graph.GraphNode<String> node, Graph<String> graph, Set<Graph.GraphNode<String>> visited, int runningTotal, int best) {
        Set<Graph.GraphNode<String>> eligible = Sets.difference(Sets.difference(graph.getNodes(), visited), ImmutableSet.of(node));
        Set<Graph.GraphNode<String>> newVisited = new HashSet<>(visited);
        newVisited.add(node);
        if (eligible.size() == 1) {
            int dist = graph.getAdjacentConnectionWeight(node, eligible.iterator().next());
            return Math.max(best, runningTotal + dist);
        } else {
            for (Graph.GraphNode<String> next : eligible) {
                int dist = graph.getAdjacentConnectionWeight(node, next);
                best = solveB(next, graph, newVisited, runningTotal + dist, best);
            }
        }
        return best;
    }

    private Graph<String> parse() {
        final Graph<String> graph = new Graph<>();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            Graph.GraphNode<String> a = new Graph.GraphNode<>(line[0]);
            Graph.GraphNode<String> b = new Graph.GraphNode<>(line[2]);
            int distance = Integer.parseInt(line[4]);
            graph.addConnection(a, b, distance);
            graph.addConnection(b, a, distance);
        }
        return graph;
    }
    
}
