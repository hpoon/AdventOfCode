package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay1Test implements ProblemDayTest {

    private ProblemDay1 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay1();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(138);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(1771);
    }

}
