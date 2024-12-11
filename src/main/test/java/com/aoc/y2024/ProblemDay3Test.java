package com.aoc.y2024;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay3Test implements ProblemDayTest {

    private ProblemDay3 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay3();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(175615763);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(74361272);
    }

}
