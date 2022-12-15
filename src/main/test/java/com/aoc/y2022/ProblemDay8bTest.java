package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay8bTest implements ProblemDayTest {

    private ProblemDay8b problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay8b();
    }

    @Test
    @Override
    public void testSolve() {
        assertThat(problem.solve()).isEqualTo(422059);
    }

}
