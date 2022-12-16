package com.aoc.y2021;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class ProblemDay21a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final Player p1 = new Player(Integer.parseInt(scanner.nextLine().split(": ")[1]));
        final Player p2 = new Player(Integer.parseInt(scanner.nextLine().split(": ")[1]));
        final Dice dice = new Dice();

        Player loser = null;
        while (true) {
            p1.move(dice.roll(3));
            if (p1.won()) {
                loser = p2;
                break;
            }
            p2.move(dice.roll(3));
            if (p2.won()) {
                loser = p1;
                break;
            }
        }

        return loser.score() * dice.total();
    }

    private static class Player {

        private int pos;

        private int score;

        public Player(final int pos) {
            this.pos = pos;
            this.score = 0;
        }

        public void move(final int roll) {
            pos += roll;
            pos = (pos - 1) % 10 + 1;
            score += pos;
        }

        public int score() {
            return score;
        }

        public boolean won() {
            return score >= 1000;
        }
    }

    private static class Dice {

        private Queue<Integer> dice;

        private int total = 0;

        public Dice() {
            dice = new LinkedList<>();
            total = 0;
            reset();
        }

        public int roll(final int num) {
            return IntStream.range(0, num).map(i -> {
                if (dice.isEmpty()) {
                    reset();
                }
                total++;
                return dice.poll();
            }).sum();
        }

        public int total() {
            return total;
        }

        private void reset() {
            IntStream.range(1, 101).forEach(i -> dice.add(i));
        }
    }
}
