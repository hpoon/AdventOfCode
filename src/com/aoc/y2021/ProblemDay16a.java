
package com.aoc.y2021;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ProblemDay16a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        final Map<String, int[]> letterToBinary = new HashMap<String, int[]>() {{
            put("0", new int[]{0, 0, 0, 0});
            put("1", new int[]{0, 0, 0, 1});
            put("2", new int[]{0, 0, 1, 0});
            put("3", new int[]{0, 0, 1, 1});
            put("4", new int[]{0, 1, 0, 0});
            put("5", new int[]{0, 1, 0, 1});
            put("6", new int[]{0, 1, 1, 0});
            put("7", new int[]{0, 1, 1, 1});
            put("8", new int[]{1, 0, 0, 0});
            put("9", new int[]{1, 0, 0, 1});
            put("A", new int[]{1, 0, 1, 0});
            put("B", new int[]{1, 0, 1, 1});
            put("C", new int[]{1, 1, 0, 0});
            put("D", new int[]{1, 1, 0, 1});
            put("E", new int[]{1, 1, 1, 0});
            put("F", new int[]{1, 1, 1, 1});
        }};

        final String[] hex = scanner.nextLine().split("(?!^)");
        final Queue<Integer> queue = new LinkedList<>();
        for (final String h : hex) {
            final int[] ints = letterToBinary.get(h);
            for (int bit : ints) {
                queue.add(bit);
            }
        }

        return parsePacket(queue, 1);
    }

    private int parsePacket(final Queue<Integer> queue, final int packetsToRead) {
        int versions = 0;
        int subPacketsRead = 0;
        while (!queue.isEmpty()) {
            int[] bits = new int[] { queue.poll(), queue.poll(), queue.poll() };
            versions += Util.bytesToInt(bits);

            bits = new int[] { queue.poll(), queue.poll(), queue.poll() };
            final int typeId = Util.bytesToInt(bits);

            if (typeId == 4) {
                int separator;
                int subpackets = 0;
                int[] numBits = new int[4];
                do {
                    separator = queue.poll();
                    final int[] subpacket = new int[4];
                    IntStream.range(0, subpacket.length).forEach(i -> subpacket[i] = queue.poll());
                    subpackets++;
                    final int[] temp = new int[subpackets * 4];
                    System.arraycopy(numBits, 0, temp, 0, numBits.length);
                    System.arraycopy(subpacket, 0, temp, (subpackets - 1) * 4, subpacket.length);
                    numBits = temp;
                } while (separator == 1);
                final int num = Util.bytesToInt(numBits);
            } else {
                final int lengthTypeId = queue.poll();
                if (lengthTypeId == 0) {
                    final int[] lengthBits = new int[15];
                    IntStream.range(0, lengthBits.length).forEach(i -> lengthBits[i] = queue.poll());
                    final int length = Util.bytesToInt(lengthBits);
                    final Queue<Integer> subQueue = new LinkedList<>();
                    IntStream.range(0, length).forEach(i -> subQueue.add(queue.poll()));
                    versions += parsePacket(subQueue, Integer.MAX_VALUE);
                } else if (lengthTypeId == 1) {
                    final int[] subpacketBits = new int[11];
                    IntStream.range(0, subpacketBits.length).forEach(i -> subpacketBits[i] = queue.poll());
                    final int subpackets = Util.bytesToInt(subpacketBits);
                    versions += parsePacket(queue, subpackets);
                } else {
                    throw new RuntimeException("bad packet");
                }
            }
            subPacketsRead++;

            if (subPacketsRead >= packetsToRead) {
                break;
            }
        }
        return versions;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day16.txt"));
        return scanner;
    }
}
