package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay14Test implements ProblemDayTest {

    private ProblemDay14 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay14();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(2640);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(1102);
    }

}
