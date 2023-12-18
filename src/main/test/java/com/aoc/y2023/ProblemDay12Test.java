package com.aoc.y2023;

import com.aoc.ProblemDayTest;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay12Test implements ProblemDayTest {

    private ProblemDay12 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay12();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(7622);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(4964259839627L);
    }

    @Test
    public void testSolve() {
        assertThat(problem.solve("???.###", ImmutableList.of(1, 1, 3), new HashMap<>())).isEqualTo(1);
    }

}
