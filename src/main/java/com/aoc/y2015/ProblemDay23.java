package com.aoc.y2015;

import com.aoc.ProblemDay;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

public class ProblemDay23 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Instruction> instructions = parse();
        int[] registers = new int[]{ 0, 0 };
        return solve(instructions, registers);
    }

    @Override
    protected Integer solveB() {
        List<Instruction> instructions = parse();
        int[] registers = new int[]{ 1, 0 };
        return solve(instructions, registers);
    }

    private int solve(List<Instruction> instructions, int[] registers) {
        int i = 0;
        do {
            Instruction instruction = instructions.get(i);
            registers = instruction.operate(registers[0], registers[1]);
            i = instruction.nextInstruction(registers[0], registers[1]);
        } while (i >= 0 && i < instructions.size());
        return registers[1];
    }

    private List<Instruction> parse() {
        int i = 0;
        List<Instruction> instructions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            String ins = tokens[0];
            String t1 = tokens[1];
            String t2 = tokens.length == 3 ? tokens[2] : null;
            switch (ins) {
                case "hlf": instructions.add(new Half(i, t1.charAt(0))); break;
                case "tpl": instructions.add(new Triple(i, t1.charAt(0))); break;
                case "inc": instructions.add(new Increment(i, t1.charAt(0))); break;
                case "jmp": instructions.add(new Jump(i, Integer.parseInt(t1))); break;
                case "jie": instructions.add(new JumpEven(i, t1.charAt(0), Integer.parseInt(t2))); break;
                case "jio": instructions.add(new JumpOne(i, t1.charAt(0), Integer.parseInt(t2))); break;
            }
            i++;
        }
        return instructions;
    }

    private interface Instruction {
        int[] operate(int a, int b);
        int nextInstruction(int a, int b);
    }

    @Value
    private static class Half implements Instruction {

        private int id;
        private char register;

        @Override
        public int[] operate(int a, int b) {
            if (register == 'a') {
                return new int[] { a / 2, b };
            } else {
                return new int[] { a, b / 2 };
            }
        }

        @Override
        public int nextInstruction(int a, int b) {
            return id + 1;
        }
    }

    @Value
    private static class Triple implements Instruction {

        private int id;
        private char register;

        @Override
        public int[] operate(int a, int b) {
            if (register == 'a') {
                return new int[] { 3 * a, b };
            } else {
                return new int[] { a, 3 * b };
            }
        }

        @Override
        public int nextInstruction(int a, int b) {
            return id + 1;
        }
    }

    @Value
    private static class Increment implements Instruction {

        private int id;
        private char register;

        @Override
        public int[] operate(int a, int b) {
            if (register == 'a') {
                return new int[] { a + 1, b };
            } else {
                return new int[] { a, b + 1 };
            }
        }

        @Override
        public int nextInstruction(int a, int b) {
            return id + 1;
        }
    }

    @Value
    private static class Jump implements Instruction {

        private int id;
        private int offset;

        @Override
        public int[] operate(int a, int b) {
            return new int[] { a, b };
        }

        @Override
        public int nextInstruction(int a, int b) {
            return id + offset;
        }

    }

    @Value
    private static class JumpEven implements Instruction {

        private int id;
        private char register;
        private int offset;

        @Override
        public int[] operate(int a, int b) {
            return new int[] { a, b };
        }

        @Override
        public int nextInstruction(int a, int b) {
            if (register == 'a') {
                return (a % 2 == 0) ? id + offset : id + 1;
            } else {
                return (b % 2 == 0) ? id + offset : id + 1;
            }
        }

    }

    @Value
    private static class JumpOne implements Instruction {

        private int id;
        private char register;
        private int offset;

        @Override
        public int[] operate(int a, int b) {
            return new int[] { a, b };
        }

        @Override
        public int nextInstruction(int a, int b) {
            if (register == 'a') {
                return (a == 1) ? id + offset : id + 1;
            } else {
                return (b == 1) ? id + offset : id + 1;
            }
        }

    }

}
