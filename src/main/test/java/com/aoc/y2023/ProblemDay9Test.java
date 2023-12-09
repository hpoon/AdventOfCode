package com.aoc.y2023;

import com.aoc.ProblemDayTest;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay9Test implements ProblemDayTest {

    private ProblemDay9 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay9();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(2105961943);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(1019);
    }

    @Test
    public void testSolveOneForward() {
        List<Integer> nums = ImmutableList.<Integer>builder()
                .add(10)
                .add(13)
                .add(16)
                .add(21)
                .add(30)
                .add(45)
                .build();
        assertThat(problem.solveOne(nums, true)).isEqualTo(68);
    }

    @Test
    public void testSolveOneBackward() {
        List<Integer> nums = ImmutableList.<Integer>builder()
                .add(10)
                .add(13)
                .add(16)
                .add(21)
                .add(30)
                .add(45)
                .build();
        assertThat(problem.solveOne(nums, false)).isEqualTo(5);
    }

}
