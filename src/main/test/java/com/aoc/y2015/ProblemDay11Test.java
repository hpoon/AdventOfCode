package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay11Test implements ProblemDayTest {

    private ProblemDay11 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay11();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo("cqjxxyzz");
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo("cqkaabcc");
    }

}
