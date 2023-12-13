package com.aoc.y2023;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay13Test implements ProblemDayTest {

    private ProblemDay13 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay13();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(33122);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(32312);
    }

}
