package com.aoc.y2023;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.NotImplementedException;
import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class ProblemDay25 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Graph<String, DefaultWeightedEdge> network = parse();
        return solve(network);
    }

    @Override
    public Integer solveB() {
        throw new NotImplementedException();
    }

    private int solve(Graph<String, DefaultWeightedEdge> graph) {
        StoerWagnerMinimumCut<String, DefaultWeightedEdge> minCut = new StoerWagnerMinimumCut<>(graph);
        if (minCut.minCutWeight() != 3) {
            throw new RuntimeException(String.format(
                    "Expected minimum cut to be 3, but instead got %f",
                    minCut.minCutWeight()));
        }
        int groupA = minCut.minCut().size();
        int groupB = graph.vertexSet().size() - groupA;
        return groupA * groupB;
    }

    Graph<String, DefaultWeightedEdge> parse() {
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(": ");
            String key = tokens[0];
            String[] values = tokens[1].split(" ");
            graph.addVertex(key);
            for (String value : values) {
                graph.addVertex(value);
                graph.addEdge(key, value);
            }
        }
        return graph;
    }

}
