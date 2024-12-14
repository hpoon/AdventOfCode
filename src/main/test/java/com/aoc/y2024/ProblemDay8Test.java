package com.aoc.y2024;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay8Test implements ProblemDayTest {

    private ProblemDay8 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay8();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(409);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(1308);
    }

}
