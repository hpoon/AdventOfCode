package com.aoc.y2021;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ProblemDay24a extends ProblemDay<Long> {

    private static final Set<String> VARIABLES = new HashSet<String>() {{
        add("w"); add("x"); add("y"); add("z");
    }};

    private static final Map<String, Integer> MAPPING = new HashMap<String, Integer>() {{
        put("w", 0); put("x", 1); put("y", 2); put("z", 3);
    }};

    @Override
    public Long solve() {
        final List<Block> instructions = new ArrayList<>();
        for (int block = 0; block < 14; block++) {
            int check = 0;
            int div = 0;
            int add = 0;
            for (int line = 0; line < 18; line++) {
                final String ins = scanner.nextLine();
                if (ins.contains("div")) {
                    div = Integer.parseInt(ins.split(" ")[2]);
                }
                if (ins.contains("add x ") && !ins.contains("add x z")) {
                    check = Integer.parseInt(ins.split(" ")[2]);
                }
                if (ins.contains("add y ") && !ins.contains("add y 25") && !ins.contains("add y 1") && !ins.contains("add y w")) {
                    add = Integer.parseInt(ins.split(" ")[2]);
                }
            }
            instructions.add(new Block(check, div, add));
        }

//    if z % 26 + chk != inp:
//        z //= div
//        z *= 26
//        z += inp + add
//    else:
//        z //= div

        return 0L;
    }

    private static class Block {

        private final int check;

        private final int div;

        private final int add;

        public Block(final int check, final int div, final int add) {
            this.check = check;
            this.div = div;
            this.add = add;
        }
    }

    private boolean execute(final String line, final int[] vars, final int digit) {
        final String[] split = line.split(" ");
        final String cmd = split[0];
        final String var1 = split[1];

        if ("inp".equals(cmd)) {
            vars[MAPPING.get(var1)] = digit;
        } else if ("mul".equals(cmd)) {
            final String var2 = split[2];
            final int num1 = vars[MAPPING.get(var1)];
            final int num2;
            if (VARIABLES.contains(var2)) {
                num2 = vars[MAPPING.get(var2)];
            } else {
                num2 = Integer.parseInt(var2);
            }
            vars[MAPPING.get(var1)] = num1 * num2;
        } else if ("add".equals(cmd)) {
            final String var2 = split[2];
            final int num1 = vars[MAPPING.get(var1)];
            final int num2;
            if (VARIABLES.contains(var2)) {
                num2 = vars[MAPPING.get(var2)];
            } else {
                num2 = Integer.parseInt(var2);
            }
            vars[MAPPING.get(var1)] = num1 + num2;
        } else if ("mod".equals(cmd)) {
            final String var2 = split[2];
            final int num1 = vars[MAPPING.get(var1)];
            final int num2;
            if (VARIABLES.contains(var2)) {
                num2 = vars[MAPPING.get(var2)];
            } else {
                num2 = Integer.parseInt(var2);
            }
            if (num2 == 0) {
                return false;
            }
            vars[MAPPING.get(var1)] = num1 % num2;
        } else if ("div".equals(cmd)) {
            final String var2 = split[2];
            final int num1 = vars[MAPPING.get(var1)];
            final int num2;
            if (VARIABLES.contains(var2)) {
                num2 = vars[MAPPING.get(var2)];
            } else {
                num2 = Integer.parseInt(var2);
            }
            if (num2 == 0) {
                return false;
            }
            vars[MAPPING.get(var1)] = num1 / num2;
        } else if ("eql".equals(cmd)) {
            final String var2 = split[2];
            final int num1 = vars[MAPPING.get(var1)];
            final int num2;
            if (VARIABLES.contains(var2)) {
                num2 = vars[MAPPING.get(var2)];
            } else {
                num2 = Integer.parseInt(var2);
            }
            vars[MAPPING.get(var1)] = num1 == num2 ? 1 : 0;
        }

        return true;
    }

    private static class State {

        private final int place;

        private final int[] vars;

        public State(final int place, final int[] vars) {
            this.place = place;
            this.vars = Arrays.copyOf(vars, vars.length);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return place == state.place && Arrays.equals(vars, state.vars);
        }

        @Override
        public int hashCode() {
            return Objects.hash(place, Arrays.hashCode(vars));
        }

    }
}
