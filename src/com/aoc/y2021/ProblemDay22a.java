
package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemDay22a implements ProblemDay<Integer> {

    private Scanner scanner;

    @Override
    public Integer solve() {
        boolean reactor[][][] = new boolean[101][101][101];
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final String[] split1 = line.split(" ");
            final boolean state = split1[0].equals("on");

            final String[] split2 = split1[1]
                    .replace("x=", "")
                    .replace("y=", "")
                    .replace("z=", "")
                    .split(",");

            final int[] x = Arrays.stream(split2[0].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            final int[] y = Arrays.stream(split2[1].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            final int[] z = Arrays.stream(split2[2].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            if (x[0] < -50 || x[1] > 50) {
                continue;
            }
            if (y[0] < -50 || y[1] > 50) {
                continue;
            }
            if (z[0] < -50 || z[1] > 50) {
                continue;
            }
            set(x, y, z, state, reactor);
        }

        return count(reactor);
    }

    private void set(final int[] x, final int[]y, final int[] z, final boolean state, final boolean[][][] reactor) {
        for (int i = x[0]; i <= x[1]; i++) {
            for (int j = y[0]; j <= y[1]; j++) {
                for (int k = z[0]; k <= z[1]; k++) {
                    reactor[i+50][j+50][k+50] = state;
                }
            }
        }
    }

    private int count(final boolean[][][] reactor) {
        int count = 0;
        for (int i = 0; i < reactor.length; i++) {
            for (int j = 0; j < reactor[i].length; j++) {
                for (int k = 0; k < reactor[i][j].length; k++) {
                    if (reactor[i][j][k]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day22.txt"));
        return scanner;
    }
}
