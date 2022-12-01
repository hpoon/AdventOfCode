
package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProblemDay21b implements ProblemDay<Long> {

    private Scanner scanner;

    private Long[] count(final int p1,
                         final int p2,
                         final int s1,
                         final int s2,
                         final int turn,
                         final Long[][][][][][] state) {
        Long[] w = state[p1-1][p2-1][s1][s2][turn];
        if (w[0] != null && w[1] != null) {
            return w;
        }

        w = new Long[] { 0L, 0L };
        for (int r1 = 1; r1 <= 3; r1++) {
            for (int r2 = 1; r2 <= 3; r2++) {
                for (int r3 = 1; r3 <= 3; r3++) {
                    final int roll = r1 + r2 + r3;

                    final int[] p = new int[] { p1, p2 };
                    final int[] s = new int[] { s1, s2 };

                    p[turn] = (p[turn] + roll - 1) % 10 + 1;
                    s[turn] = s[turn] + p[turn];

                    if (s[turn] >= 21) {
                        w[turn] += 1;
                    } else {
                        final Long[] ww = count(p[0], p[1], s[0], s[1], (turn == 0) ? 1 : 0, state);
                        w[0] += ww[0];
                        w[1] += ww[1];
                    }
                }
            }
        }

        state[p1-1][p2-1][s1][s2][turn] = w;
        return w;
    }

    @Override
    public Long solve() {
        final Long[][][][][][] state = new Long[10][10][22][22][2][2];

        int p1 = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        int p2 = Integer.parseInt(scanner.nextLine().split(": ")[1]);

        final Long[] count = count(p1, p2, 0, 0, 0, state);

        return Math.max(count[0], count[1]);
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day21.txt"));
        return scanner;
    }
}
