package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay19Test implements ProblemDayTest {

    private ProblemDay19 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay19();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(576);
    }

    @Test
    @Override
    public void testSolveB() {
        // 147 (too low)
        assertThat(problem.solveB()).isEqualTo(924);
    }

}
