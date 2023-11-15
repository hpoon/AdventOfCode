package com.aoc.y2015;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay22Test implements ProblemDayTest {

    private ProblemDay22 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay22();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(900);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(1216);
    }

}
