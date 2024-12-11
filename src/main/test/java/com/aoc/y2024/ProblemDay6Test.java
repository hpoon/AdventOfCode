package com.aoc.y2024;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay6Test implements ProblemDayTest {

    private ProblemDay6 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay6();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(5531);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(2165);
    }

}
