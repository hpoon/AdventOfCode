package com.aoc.y2024;

import com.aoc.ProblemDayTest;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay17Test implements ProblemDayTest {

    private ProblemDay17 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay17();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(ImmutableList.of(2L, 0L, 4L, 2L, 7L, 0L, 1L, 0L, 3L));
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(265601188299675L);
    }

}
