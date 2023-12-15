package com.aoc.y2023;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay15Test implements ProblemDayTest {

    private ProblemDay15 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay15();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(513643);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(265345);
    }

    @Test
    public void testHash() {
        assertThat(problem.hash("rn")).isEqualTo(0);
        assertThat(problem.hash("cm")).isEqualTo(0);
    }
}
