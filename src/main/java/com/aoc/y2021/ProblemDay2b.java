package com.aoc.y2021;


public class ProblemDay2b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        int surface = 0;
        int aim = 0;
        int depth = 0;
        while (scanner.hasNextLine()) {
            final String[] line = scanner.nextLine().split(" ");
            final String command = line[0];
            final int value = Integer.parseInt(line[1]);
            if (command.equals("forward")) {
                surface += value;
                depth += value * aim;
            } else if (command.equals("up")) {
                aim -= value;
            } else if (command.equals("down")) {
                aim += value;
            }
        }
        return surface * depth;
    }

}
