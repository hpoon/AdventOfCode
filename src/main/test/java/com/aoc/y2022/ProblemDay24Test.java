
package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay24Test implements ProblemDayTest {

    private ProblemDay24 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay24();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(279);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(762);
    }

}
