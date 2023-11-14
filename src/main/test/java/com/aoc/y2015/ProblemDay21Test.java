package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay21Test implements ProblemDayTest {

    private ProblemDay21 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay21();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(78);
    }

    @Test
    @Override
    public void testSolveB() {
        // 233 (too high)
        assertThat(problem.solveB()).isEqualTo(148);
    }

}
