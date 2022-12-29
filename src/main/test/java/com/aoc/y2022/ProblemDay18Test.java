package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay18Test implements ProblemDayTest {

    private ProblemDay18 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay18();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(4348);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(2546);
    }

}
