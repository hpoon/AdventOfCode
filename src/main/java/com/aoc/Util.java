package com.aoc;

public class Util {

    public static long bytesToLong(final long[] bits) {
        return bytesToLong(bits, 2);
    }

    public static int bytesToInt(final int[] bits) {
        return bytesToInt(bits, 2);
    }

    private static long bytesToLong(final long[] bits, final int base) {
        long value = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            long b = bits[i];
            value += b * Math.pow(base, bits.length - i - 1);
        }
        return value;
    }

    private static int bytesToInt(final int[] bits, final int base) {
        int value = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            int b = bits[i];
            value += b * Math.pow(base, bits.length - i - 1);
        }
        return value;
    }

    public static boolean isBlank(final String str) {
        return str == null || str.length() == 0;
    }
}
