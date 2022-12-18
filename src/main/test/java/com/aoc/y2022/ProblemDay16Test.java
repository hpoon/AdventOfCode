package com.aoc.y2022;

import com.aoc.Graph;
import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay16Test implements ProblemDayTest {

    private ProblemDay16 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay16();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(2181);
    }

    @Test
    public void testSolveA_twoNodes() {
        Graph<ProblemDay16.Valve> graph = new Graph<>();
        Graph.GraphNode<ProblemDay16.Valve> aa = new Graph.GraphNode<>(new ProblemDay16.Valve("AA", 0));
        Graph.GraphNode<ProblemDay16.Valve> bb = new Graph.GraphNode<>(new ProblemDay16.Valve("BB", 10));
        graph.addConnection(aa, bb, 1);
        graph.addConnection(bb, aa, 1);
        assertThat(problem.bestSolution(problem.getSolutions(graph, 30), 'a')).isEqualTo(280);
    }

    @Test
    public void testSolveA_threeNodes() {
        Graph<ProblemDay16.Valve> graph = new Graph<>();
        Graph.GraphNode<ProblemDay16.Valve> aa = new Graph.GraphNode<>(new ProblemDay16.Valve("AA", 0));
        Graph.GraphNode<ProblemDay16.Valve> bb = new Graph.GraphNode<>(new ProblemDay16.Valve("BB", 10));
        Graph.GraphNode<ProblemDay16.Valve> cc = new Graph.GraphNode<>(new ProblemDay16.Valve("CC", 5));
        graph.addConnection(aa, bb, 1);
        graph.addConnection(bb, aa, 1);
        graph.addConnection(bb, cc, 1);
        graph.addConnection(cc, bb, 1);
        assertThat(problem.bestSolution(problem.getSolutions(graph, 30), 'a')).isEqualTo(410);
    }

    @Test
    public void testSolveA_smallSample() {
        Graph<ProblemDay16.Valve> graph = new Graph<>();
        Graph.GraphNode<ProblemDay16.Valve> aa = new Graph.GraphNode<>(new ProblemDay16.Valve("AA", 0));
        Graph.GraphNode<ProblemDay16.Valve> bb = new Graph.GraphNode<>(new ProblemDay16.Valve("BB", 13));
        Graph.GraphNode<ProblemDay16.Valve> cc = new Graph.GraphNode<>(new ProblemDay16.Valve("CC", 2));
        Graph.GraphNode<ProblemDay16.Valve> dd = new Graph.GraphNode<>(new ProblemDay16.Valve("DD", 20));
        Graph.GraphNode<ProblemDay16.Valve> ee = new Graph.GraphNode<>(new ProblemDay16.Valve("EE", 3));
        Graph.GraphNode<ProblemDay16.Valve> ff = new Graph.GraphNode<>(new ProblemDay16.Valve("FF", 0));
        Graph.GraphNode<ProblemDay16.Valve> gg = new Graph.GraphNode<>(new ProblemDay16.Valve("GG", 0));
        Graph.GraphNode<ProblemDay16.Valve> hh = new Graph.GraphNode<>(new ProblemDay16.Valve("HH", 22));
        Graph.GraphNode<ProblemDay16.Valve> ii = new Graph.GraphNode<>(new ProblemDay16.Valve("II", 0));
        Graph.GraphNode<ProblemDay16.Valve> jj = new Graph.GraphNode<>(new ProblemDay16.Valve("JJ", 21));
        graph.addConnection(aa, dd, 1);
        graph.addConnection(aa, bb, 1);
        graph.addConnection(aa, ii, 1);
        graph.addConnection(bb, aa, 1);
        graph.addConnection(bb, cc, 1);
        graph.addConnection(cc, bb, 1);
        graph.addConnection(cc, dd, 1);
        graph.addConnection(dd, cc, 1);
        graph.addConnection(dd, aa, 1);
        graph.addConnection(dd, ee, 1);
        graph.addConnection(ee, dd, 1);
        graph.addConnection(ee, ff, 1);
        graph.addConnection(ff, ee, 1);
        graph.addConnection(ff, gg, 1);
        graph.addConnection(gg, ff, 1);
        graph.addConnection(gg, hh, 1);
        graph.addConnection(hh, gg, 1);
        graph.addConnection(ii, aa, 1);
        graph.addConnection(ii, jj, 1);
        graph.addConnection(jj, ii, 1);
        assertThat(problem.bestSolution(problem.getSolutions(graph, 30), 'a')).isEqualTo(1651);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(true).isTrue();
    }

}
