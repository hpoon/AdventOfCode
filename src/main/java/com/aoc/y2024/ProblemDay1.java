package com.aoc.y2024;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ProblemDay1 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        Pair<PriorityQueue<Integer>, PriorityQueue<Integer>> queues = parseA();
        PriorityQueue<Integer> left = queues.getLeft();
        PriorityQueue<Integer> right = queues.getRight();
        int sum = 0;
        while (!left.isEmpty() && !right.isEmpty()) {
            sum += distance(left.poll(), right.poll());
        }
        return sum;
    }

    @Override
    public Integer solveB() {
        Pair<List<Integer>, Map<Integer, Integer>> lists = parseB();
        List<Integer> left = lists.getLeft();
        Map<Integer, Integer> right = lists.getRight();
        int score = 0;
        for (int lf : left) {
            score += lf * right.getOrDefault(lf, 0);
        }
        return score;
    }

    private Pair<PriorityQueue<Integer>, PriorityQueue<Integer>> parseA() {
        PriorityQueue<Integer> left = new PriorityQueue<>();
        PriorityQueue<Integer> right = new PriorityQueue<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            left.add(Integer.parseInt(tokens[0]));
            right.add(Integer.parseInt(tokens[tokens.length - 1]));
        }
        return ImmutablePair.of(left, right);
    }

    private Pair<List<Integer>, Map<Integer, Integer>> parseB() {
        List<Integer> left = new ArrayList<>();
        Map<Integer, Integer> right = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            left.add(Integer.parseInt(tokens[0]));
            int rt = Integer.parseInt(tokens[tokens.length - 1]);
            right.put(rt, right.getOrDefault(rt, 0) + 1);
        }
        return ImmutablePair.of(left, right);
    }

    private int distance(int a, int b) {
        return Math.abs(a - b);
    }

}
