
package com.aoc.y2021;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProblemDay22b implements ProblemDay<Long> {

    private Scanner scanner;

    @Override
    public Long solve() {
        final Instant t1 = Instant.now();

        Cuboid bounds = null;
        final List<Cuboid> cuboids = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final String[] split1 = line.split(" ");
            final boolean isAdding = split1[0].equals("on");

            final String[] split2 = split1[1]
                    .replace("x=", "")
                    .replace("y=", "")
                    .replace("z=", "")
                    .split(",");

            final int[] x = Arrays.stream(split2[0].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            final int[] y = Arrays.stream(split2[1].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            final int[] z = Arrays.stream(split2[2].split("\\.\\.")).mapToInt(Integer::parseInt).toArray();

            final Cuboid current = new Cuboid(x[0], x[1]+1, y[0], y[1]+1, z[0], z[1]+1);
            if (bounds == null) {
                bounds = current;
                cuboids.add(current);
            } else {
                if (!current.intersects(bounds)) {
                    cuboids.add(current);
                } else {
                    final Set<Cuboid> relevant = cuboids.stream()
                            .filter(c -> c.intersects(current))
                            .collect(Collectors.toSet());
                    relevant.add(current);

                    final List<Integer> xs = Stream.concat(
                                    relevant.stream().map(Cuboid::getX1),
                                    relevant.stream().map(Cuboid::getX2))
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());
                    final List<Integer> ys = Stream.concat(
                                    relevant.stream().map(Cuboid::getY1),
                                    relevant.stream().map(Cuboid::getY2))
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());
                    final List<Integer> zs = Stream.concat(
                                    relevant.stream().map(Cuboid::getZ1),
                                    relevant.stream().map(Cuboid::getZ2))
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());

                    final List<Interval> xis = IntStream.range(0, xs.size() - 1)
                            .mapToObj(i -> new Interval(xs.get(i), xs.get(i+1) - xs.get(i)))
                            .collect(Collectors.toList());
                    final List<Interval> yis = IntStream.range(0, ys.size() - 1)
                            .mapToObj(i -> new Interval(ys.get(i), ys.get(i+1) - ys.get(i)))
                            .collect(Collectors.toList());
                    final List<Interval> zis = IntStream.range(0, zs.size() - 1)
                            .mapToObj(i -> new Interval(zs.get(i), zs.get(i+1) - zs.get(i)))
                            .collect(Collectors.toList());

                    cuboids.removeAll(relevant);
                    for (final Interval xi : xis) {
                        for (final Interval yi : yis) {
                            for (final Interval zi : zis) {
                                final Cuboid cc = new Cuboid(xi, yi, zi);
                                final boolean intersects = relevant.stream().map(cc::intersects).reduce((c1, c2) -> c1 || c2).orElse(false);
                                if (isAdding && intersects) {
                                    cuboids.add(cc);
                                } else if (!isAdding && !cc.intersects(current) && intersects) {
                                    cuboids.add(cc);
                                }
                            }
                        }
                    }
                }
                bounds = new Cuboid(
                        Math.min(bounds.getX1(), current.getX1()), Math.max(bounds.getX2(), current.getX2()),
                        Math.min(bounds.getY1(), current.getY1()), Math.max(bounds.getY2(), current.getY2()),
                        Math.min(bounds.getZ1(), current.getZ1()), Math.max(bounds.getZ2(), current.getZ2()));
            }
        }

        final long volume = cuboids.stream().map(Cuboid::getVolume).reduce(Long::sum).orElse(0L);
        final Instant t2 = Instant.now();
        System.out.println(Duration.between(t1, t2).getSeconds());
        return volume;
    }

    private static class Interval {

        private final int p;

        private final int v;

        public Interval(final int p, final int v) {
            this.p = p;
            this.v = v;
        }

        public int getP() {
            return p;
        }

        public int getV() {
            return v;
        }

    }

    private static class Cuboid {

        private final int x1;

        private final int x2;

        private final int y1;

        private final int y2;

        private final int z1;

        private final int z2;

        public Cuboid(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.z1 = z1;
            this.z2 = z2;
        }

        public Cuboid(final Interval x, final Interval y, final Interval z) {
            this.x1 = x.getP();
            this.x2 = this.x1 + x.getV();
            this.y1 = y.getP();
            this.y2 = this.y1 + y.getV();
            this.z1 = z.getP();
            this.z2 = this.z1 + z.getV();
        }

        public int getX1() {
            return x1;
        }

        public int getX2() {
            return x2;
        }

        public int getY1() {
            return y1;
        }

        public int getY2() {
            return y2;
        }

        public int getZ1() {
            return z1;
        }

        public int getZ2() {
            return z2;
        }

        public long getVolume() {
            final long x = Math.abs(x2 - x1);
            final long y = Math.abs(y2 - y1);
            final long z = Math.abs(z2 - z1);
            return x * y * z;
        }

        public boolean intersects(final Cuboid other) {
            final boolean x = intersects(x1, x2, other.getX1(), other.getX2());
            final boolean y = intersects(y1, y2, other.getY1(), other.getY2());
            final boolean z = intersects(z1, z2, other.getZ1(), other.getZ2());
            return x && y && z;
        }

        private boolean intersects(final int a1, final int a2, final int b1, final int b2) {
            return (b1 <= a1 && a1 < b2) || (a1 <= b1 && b1 < a2);
        }

    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day22.txt"));
        return scanner;
    }
}
