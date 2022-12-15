package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay12aTest implements ProblemDayTest {

    private ProblemDay12a problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay12a();
    }

    @Test
    @Override
    public void testSolve() {
        assertThat(problem.solve()).isEqualTo(447);
    }

}
