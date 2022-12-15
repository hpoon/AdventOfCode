package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay14aTest implements ProblemDayTest {

    private ProblemDay14a problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay14a();
    }

    @Test
    @Override
    public void testSolve() {
        assertThat(problem.solve()).isEqualTo(1133);
    }

}
