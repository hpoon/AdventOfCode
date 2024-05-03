package com.aoc.y2016;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay5Test implements ProblemDayTest {

    private ProblemDay5 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay5();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo("c6697b55");
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo("8c35d1ab");
    }

}
