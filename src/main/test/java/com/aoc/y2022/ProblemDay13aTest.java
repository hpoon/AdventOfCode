package com.aoc.y2022;

import com.aoc.NestedList;
import com.aoc.ProblemDayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay13aTest implements ProblemDayTest {

    private ProblemDay13a problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay13a();
    }

    @Test
    @Override
    public void testSolve() {
        assertThat(problem.solve()).isEqualTo(4821);
    }

    @Test
    public void testCompare1() {
        NestedList l1 = new NestedList("[1,1,3,1,1]");
        NestedList l2 = new NestedList("[1,1,5,1,1]");
        assertThat(l1.compareTo(l2)).isEqualTo(-1);
    }

    @Test
    public void testCompare2() {
        NestedList l1 = new NestedList("[3,[[6,8,9,9],[1,9,1,8],3],[]]");
        NestedList l2 = new NestedList("[[],5,1,[]]");
        assertThat(l1.compareTo(l2)).isEqualTo(1);
    }

    @Test
    public void testCompare3() {
        NestedList l1 = new NestedList("[3]");
        NestedList l2 = new NestedList("[[]]");
        assertThat(l1.compareTo(l2)).isEqualTo(1);
    }

    @Test
    public void testCompare4() {
        NestedList l1 = new NestedList("[[]]");
        NestedList l2 = new NestedList("[3]");
        assertThat(l1.compareTo(l2)).isEqualTo(-1);
    }

    @Test
    public void testCompare5() {
        NestedList l1 = new NestedList("[7,7,7,7]");
        NestedList l2 = new NestedList("[7,7,7]");
        assertThat(l1.compareTo(l2)).isEqualTo(1);
    }

    @Test
    public void testCompare6() {
        NestedList l1 = new NestedList("[7,7,7]");
        NestedList l2 = new NestedList("[7,7,7,7]");
        assertThat(l1.compareTo(l2)).isEqualTo(-1);
    }

}
