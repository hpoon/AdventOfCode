package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay20Test implements ProblemDayTest {

    private ProblemDay20 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay20();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(665280);
    }

    @Test
    @Override
    public void testSolveB() {
        // 42949673 (too high)
        assertThat(problem.solveB()).isEqualTo(705600);
    }

}
