package com.aoc.y2021;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProblemDay3b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final List<byte[]> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final byte[] bytes = line.getBytes();
            list.add(bytes);
        }

        List<byte[]> o2 = new ArrayList<>(list);
        for (int i = 0; i < 12; i++) {
            int ones = 0;
            int zeros = 0;
            for (byte[] line : o2) {
                final int digit = line[i];
                if (digit == 49) {
                    ones++;
                } else {
                    zeros++;
                }
            }

            int finalOnes = ones;
            int finalZeros = zeros;
            int finalI = i;
            o2 = o2.stream()
                    .filter(bytes -> finalOnes >= finalZeros ? bytes[finalI] == 49 : bytes[finalI] == 48)
                    .collect(Collectors.toList());
            if (o2.size() == 1) {
                break;
            }
        }

        List<byte[]> co2 = new ArrayList<>(list);
        for (int i = 0; i < 12; i++) {
            int ones = 0;
            int zeros = 0;
            for (byte[] line : co2) {
                final int digit = line[i];
                if (digit == 49) {
                    ones++;
                } else {
                    zeros++;
                }
            }

            int finalOnes = ones;
            int finalZeros = zeros;
            int finalI = i;
            co2 = co2.stream()
                    .filter(bytes -> finalOnes < finalZeros ? bytes[finalI] == 49 : bytes[finalI] == 48)
                    .collect(Collectors.toList());
            if (co2.size() == 1) {
                break;
            }
        }

        final int o2Value = Util.bytesToInt(o2.stream()
                .map(bytes -> {
                    final int bits[] = new int[bytes.length];
                    for (int i = 0; i < bytes.length; i++) {
                        bits[i] = bytes[i] - 48;
                    }
                    return bits;
                })
                .findFirst()
                .get());
        final int co2Value = Util.bytesToInt(co2.stream()
                .map(bytes -> {
                    final int bits[] = new int[bytes.length];
                    for (int i = 0; i < bytes.length; i++) {
                        bits[i] = bytes[i] - 48;
                    }
                    return bits;
                })
                .findFirst()
                .get());

        return o2Value * co2Value;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day3.txt"));
        return scanner;
    }
}
