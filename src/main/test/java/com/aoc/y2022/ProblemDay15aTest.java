package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay15aTest implements ProblemDayTest {

    private ProblemDay15a problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay15a();
    }

    @Test
    @Override
    public void testSolve() {
        assertThat(problem.solve()).isEqualTo(5299855);
    }

}
