package com.aoc.y2021;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProblemDay3a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final int zeros[] = new int[12];
        final int ones[] = new int[12];
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final byte[] bytes = line.getBytes();
            for (int i = 0; i < line.length(); i++) {
                final byte b = bytes[i];
                if (b == 49) {
                    ones[i]++;
                } else {
                    zeros[i]++;
                }
            }
        }

        final int gammas[] = new int[12];
        final int epsilons[] = new int[12];
        for (int i = 0; i < 12; i++) {
            final int zero = zeros[i];
            final int one = ones[i];
            if (one > zero) {
                gammas[i] = 1;
                epsilons[i] = 0;
            } else if (zero > one) {
                gammas[i] = 0;
                epsilons[i] = 1;
            } else {
                throw new RuntimeException("one == zero");
            }
        }

        int gamma = Util.bytesToInt(gammas);
        int epsilon = Util.bytesToInt(epsilons);
        return gamma * epsilon;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src/main/resources/y2021/day3.txt"));
        return scanner;
    }
}
