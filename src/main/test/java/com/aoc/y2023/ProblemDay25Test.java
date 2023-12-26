package com.aoc.y2023;

import com.aoc.ProblemDayTest;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProblemDay25Test implements ProblemDayTest {

    private ProblemDay25 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay25();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(543564);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThrows(NotImplementedException.class, () -> problem.solveB());
    }

}
