package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay15bTest implements ProblemDayTest {

    private ProblemDay15b problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay15b();
    }

    @Test
    @Override
    public void testSolve() {
        assertThat(problem.solve()).isEqualTo(13615843289729L);
    }

}
