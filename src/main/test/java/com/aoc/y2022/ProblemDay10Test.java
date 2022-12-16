package com.aoc.y2022;

import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay10Test implements ProblemDayTest {

    private ProblemDay10 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay10();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(14360);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(
                "###...##..#..#..##..####.###..####.#####\n" +
                "#..#.#..#.#.#..#..#.#....#..#.#.......##\n" +
                "###..#....##...#..#.###..#..#.###....#.#\n" +
                "#..#.#.##.#.#..####.#....###..#.....#..#\n" +
                "#..#.#..#.#.#..#..#.#....#.#..#....#...#\n" +
                "###...###.#..#.#..#.####.#..#.####.####.\n");
    }

}
