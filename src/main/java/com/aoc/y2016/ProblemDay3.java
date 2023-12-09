package com.aoc.y2016;

import com.aoc.ProblemDay;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay3 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<List<Integer>> triangles = parseA();
        return solve(triangles);
    }

    @Override
    public Integer solveB() {
        List<List<Integer>> triangles = parseB();
        return solve(triangles);
    }

    private int solve(List<List<Integer>> triangles) {
        int sum = 0;
        for (List<Integer> triangle : triangles) {
            sum += isTriangle(triangle) ? 1 : 0;
        }
        return sum;
    }

    @VisibleForTesting
    protected boolean isTriangle(List<Integer> nums) {
        if (nums.size() != 3) {
            return false;
        }
        nums.sort(Comparator.naturalOrder());
        return nums.get(0) + nums.get(1) > nums.get(2);
    }

    public List<List<Integer>> parseA() {
        List<List<Integer>> triangles = new ArrayList<>();
        while (scanner.hasNextLine()) {
            triangles.add(Arrays.stream(scanner.nextLine().split(" "))
                    .filter(StringUtils::isNotBlank)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
        }
        return triangles;
    }

    public List<List<Integer>> parseB() {
        List<List<Integer>> triangles = new ArrayList<>();
        int i = 0;
        List<Integer> t1 = new ArrayList<>();
        List<Integer> t2 = new ArrayList<>();
        List<Integer> t3 = new ArrayList<>();
        while (scanner.hasNextLine()) {
            List<Integer> row = Arrays.stream(scanner.nextLine().split(" "))
                    .filter(StringUtils::isNotBlank)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            t1.add(row.get(0));
            t2.add(row.get(1));
            t3.add(row.get(2));
            i++;
            if (i == 3) {
                i = 0;
                triangles.add(t1);
                triangles.add(t2);
                triangles.add(t3);
                t1 = new ArrayList<>();
                t2 = new ArrayList<>();
                t3 = new ArrayList<>();
            }
        }
        return triangles;
    }

}
