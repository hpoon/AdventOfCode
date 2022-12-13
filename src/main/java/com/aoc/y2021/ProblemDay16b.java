package com.aoc.y2021;

import com.aoc.ProblemDay;
import com.aoc.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.LongStream;

public class ProblemDay16b extends ProblemDay<Long> {

    public Long solve() {
        final Map<String, long[]> letterToBinary = new HashMap<>() {{
            put("0", new long[]{0, 0, 0, 0});
            put("1", new long[]{0, 0, 0, 1});
            put("2", new long[]{0, 0, 1, 0});
            put("3", new long[]{0, 0, 1, 1});
            put("4", new long[]{0, 1, 0, 0});
            put("5", new long[]{0, 1, 0, 1});
            put("6", new long[]{0, 1, 1, 0});
            put("7", new long[]{0, 1, 1, 1});
            put("8", new long[]{1, 0, 0, 0});
            put("9", new long[]{1, 0, 0, 1});
            put("A", new long[]{1, 0, 1, 0});
            put("B", new long[]{1, 0, 1, 1});
            put("C", new long[]{1, 1, 0, 0});
            put("D", new long[]{1, 1, 0, 1});
            put("E", new long[]{1, 1, 1, 0});
            put("F", new long[]{1, 1, 1, 1});
        }};

        final String[] hex = scanner.nextLine().split("(?!^)");
        final Queue<Long> queue = new LinkedList<>();
        for (final String h : hex) {
            final long[] longs = letterToBinary.get(h);
            for (long bit : longs) {
                queue.add(bit);
            }
        }

        return parsePacket(queue);
    }

    private long parsePacket(final Queue<Long> queue) {
        long res = 0;
        final List<Long> operands = new ArrayList<>();
        while (!queue.isEmpty()) {
            long[] bits = new long[] { queue.poll(), queue.poll(), queue.poll() };

            bits = new long[] { queue.poll(), queue.poll(), queue.poll() };
            final long typeId = Util.bytesToLong(bits);

            if (typeId == 4) {
                long separator;
                long subpackets = 0;
                long[] numBits = new long[4];
                do {
                    separator = queue.poll();
                    final long[] subpacket = new long[4];
                    LongStream.range(0, subpacket.length).forEach(i -> subpacket[(int) i] = queue.poll());
                    subpackets++;
                    final long[] temp = new long[(int) subpackets * 4];
                    System.arraycopy(numBits, 0, temp, 0, numBits.length);
                    System.arraycopy(subpacket, 0, temp, ((int) subpackets - 1) * 4, subpacket.length);
                    numBits = temp;
                } while (separator == 1);
                return Util.bytesToLong(numBits);
            } else {
                final long lengthTypeId = queue.poll();
                if (lengthTypeId == 0) {
                    final long[] lengthBits = new long[15];
                    LongStream.range(0, lengthBits.length).forEach(i -> lengthBits[(int) i] = queue.poll());
                    final long length = Util.bytesToLong(lengthBits);
                    final Queue<Long> subQueue = new LinkedList<>();
                    LongStream.range(0, length).forEach(i -> subQueue.add(queue.poll()));
                    while (!subQueue.isEmpty()) {
                        operands.add(parsePacket(subQueue));
                    }
                } else if (lengthTypeId == 1) {
                    final long[] subpacketBits = new long[11];
                    LongStream.range(0, subpacketBits.length).forEach(i -> subpacketBits[(int) i] = queue.poll());
                    final long subpackets = Util.bytesToLong(subpacketBits);
                    LongStream.range(0, subpackets).forEach(i -> operands.add(parsePacket(queue)));
                } else {
                    throw new RuntimeException("bad packet");
                }

                switch ((int) typeId) {
                    case 0:
                        return operands.stream().reduce(Long::sum).orElse(0L);
                    case 1:
                        return operands.stream().reduce((a, b) -> a * b).orElse(0L);
                    case 2:
                        return operands.stream().min(Long::compare).orElseThrow(() -> new RuntimeException("empty"));
                    case 3:
                        return operands.stream().max(Long::compare).orElseThrow(() -> new RuntimeException("empty"));
                    case 5:
                        if (operands.size() != 2) {
                            throw new RuntimeException("wrong size");
                        }
                        return operands.get(0) > operands.get(1) ? 1 : 0;
                    case 6:
                        if (operands.size() != 2) {
                            throw new RuntimeException("wrong size");
                        }
                        return operands.get(0) < operands.get(1) ? 1 : 0;
                    case 7:
                        if (operands.size() != 2) {
                            throw new RuntimeException("wrong size");
                        }
                        return Objects.equals(operands.get(0), operands.get(1)) ? 1 : 0;
                    default:
                        break;
                }
            }
        }
        return res;
    }
}
