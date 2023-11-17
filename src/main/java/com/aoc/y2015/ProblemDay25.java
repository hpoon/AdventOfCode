
package com.aoc.y2015;

import com.aoc.*;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay25 extends ProblemDay<Long, Integer> {

    private static int ROW = 2981;
    private static int COLUMN = 3075;

    @Override
    protected Long solveA() {
        return solve(ROW, COLUMN);
    }

    @Override
    public Integer solveB() {
        throw new NotImplementedException("Day 25 has no part B");
    }

    private long solve(int row, int col) {
        int curRow = 1;
        int curCol = 1;
        long val = 20151125;
        while (curRow != row || curCol != col) {
            curRow = curRow - 1;
            curCol = curCol + 1;
            if (curRow == 0) {
                curRow = curCol;
                curCol = 1;
            }
            val = (val * 252533) % 33554393;
        }
        return val;
    }

}
