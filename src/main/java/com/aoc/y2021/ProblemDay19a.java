package com.aoc.y2021;

import com.aoc.ProblemDay;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ProblemDay19a extends ProblemDay<Integer> {

    private static final Comparator<Beacon> COMPARATOR = Comparator.comparing(Beacon::getX)
            .thenComparingInt(Beacon::getY)
            .thenComparingInt(Beacon::getZ);

    @Override
    public Integer solve() {
        final Queue<BeaconScanner> scanners = new LinkedList<>();
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            final Set<Beacon> beacons = new TreeSet<>(COMPARATOR);
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                final String[] split = line.split(",");
                final Beacon beacon = new Beacon(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2]));
                beacons.add(beacon);
            }
            final BeaconScanner beaconScanner = new BeaconScanner(beacons);
            scanners.add(beaconScanner);
        }

        final Set<Beacon> beacons = new TreeSet<>(COMPARATOR);
        while (!scanners.isEmpty()) {
            final BeaconScanner s = scanners.poll();
            if (beacons.isEmpty()) {
                beacons.addAll(s.getBeacons());
                continue;
            }
            final Set<Beacon> aligned = align(beacons, s.getBeacons());
            if (aligned.size() > 0) {
                beacons.addAll(aligned);
            } else {
                scanners.add(s);
            }
        }

        return beacons.size();
    }

    private Set<Beacon> align(final Set<Beacon> matches, final Set<Beacon> current) {
        for (int ii = 0; ii < 4; ii++) {
            for (int jj = 0; jj < 4; jj++) {
                for (int kk = 0; kk < 4; kk++) {
                    final int rotX = 90 * ii;
                    final int rotY = 90 * jj;
                    final int rotZ = 90 * kk;
                    final List<Beacon> rotated = current.stream()
                            .map(b -> b.rotate(rotX, rotY, rotZ))
                            .sorted(COMPARATOR)
                            .collect(Collectors.toList());
                    final int threshold = 12;
                    for (int rr = 0; rr <= rotated.size() - threshold; rr++) {
                        final Beacon r = rotated.get(rr);
                        for (final Beacon m : matches) {
                            final int moveX = m.getX() - r.getX();
                            final int moveY = m.getY() - r.getY();
                            final int moveZ = m.getZ() - r.getZ();
                            final Set<Beacon> shifted = rotated.stream()
                                    .map(s -> s.shift(moveX, moveY, moveZ))
                                    .collect(Collectors.toCollection(() -> new TreeSet<>(COMPARATOR)));
                            final Set<Beacon> intersection = new HashSet<>(matches);
                            intersection.retainAll(shifted);
                            if (intersection.size() >= threshold) {
                                return shifted;
                            }
                        }
                    }
                }
            }
        }

        return new HashSet<>();
    }

    private static class BeaconScanner {

        private final Set<Beacon> beacons;

        public BeaconScanner(final Set<Beacon> beacons) {
            this.beacons = beacons;
        }

        public Set<Beacon> getBeacons() {
            return beacons;
        }

    }

    private static class Beacon {

        private final int x;

        private final int y;

        private final int z;

        public Beacon(final int x, final int y, final int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public Beacon shift(final int x, final int y, final int z) {
            return new Beacon(this.x + x, this.y + y, this.z + z);
        }

        public Beacon rotate(final int x, final int y, final int z) {
            final int[][][] mtx = {
                {
                    { 1, 0, 0 },
                    { 0, ezCos(x), -ezSin(x) },
                    { 0, ezSin(x), ezCos(x) }
                },
                {
                    { ezCos(y), 0, ezSin(y) },
                    { 0, 1, 0 },
                    { -ezSin(y), 0, ezCos(y) }
                },
                {
                    { ezCos(z), -ezSin(z), 0 },
                    { ezSin(z), ezCos(z), 0 },
                    { 0, 0, 1 }
                }
            };

            final int[][] p = { { this.x }, { this.y }, { this.z } };
            final int[][] first = mmult(mtx[0], p);
            final int[][] second = mmult(mtx[1], first);
            final int[][] third = mmult(mtx[2], second);
            return new Beacon(third[0][0], third[1][0], third[2][0]);
        }

        private int[][] mmult(int[][] a, int[][] b) {
            if (a[0].length != b.length) {
                throw new RuntimeException("bad dims");
            }
            final int m = a.length;
            final int n = b[0].length;
            final int[][] res = new int[m][n];

            for (int row = 0; row < m; row++) {
                for (int col = 0; col < n; col++) {
                    final int[] aRow = a[row];
                    final int[] bCol = new int[b.length];
                    for (int i = 0; i < b.length; i++) {
                        bCol[i] = b[i][col];
                    }
                    int dot = 0;
                    for (int i = 0; i < aRow.length; i++) {
                        dot += aRow[i] * bCol[i];
                    }
                    res[row][col] = dot;
                }
            }
            return res;
        }

        private int ezCos(final int v) {
            switch (v) {
                case 0:
                    return 1;
                case 90:
                case 270:
                    return 0;
                case 180:
                    return -1;
                default: throw new RuntimeException("illegal");
            }
        }

        private int ezSin(final int v) {
            switch (v) {
                case 0:
                case 180:
                    return 0;
                case 90:
                    return 1;
                case 270:
                    return -1;
                default: throw new RuntimeException("illegal");
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Beacon beacon = (Beacon) o;
            return x == beacon.x && y == beacon.y && z == beacon.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
