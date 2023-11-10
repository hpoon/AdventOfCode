package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay12Test implements ProblemDayTest {

    private ProblemDay12 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay12();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(119433);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(68466);
    }

    @Test
    public void testSolve_array() {
        assertThat(problem.solve("[1,2,3]", true)).isEqualTo(6);
    }

    @Test
    public void testSolve_unnested() {
        assertThat(problem.solve("{\"a\":2,\"b\":4}", true)).isEqualTo(6);
    }

    @Test
    public void testSolve_nested() {
        assertThat(problem.solve("{\"a\":{\"b\":4},\"c\":-1}", true)).isEqualTo(3);
    }

    @Test
    public void testSolve_redObjectInList() {
        assertThat(problem.solve("[1,{\"c\":\"red\",\"b\":2},3]", false)).isEqualTo(4);
    }

    @Test
    public void testSolve_redObjectOnly() {
        assertThat(problem.solve("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}", false)).isEqualTo(0);
    }

    @Test
    public void testSolve_redTextInList() {
        assertThat(problem.solve("[1,\"red\",5]", false)).isEqualTo(6);
    }

}
