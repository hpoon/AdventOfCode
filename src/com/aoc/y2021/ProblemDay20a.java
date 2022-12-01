
package com.aoc.y2021;

import com.aoc.ProblemDay;
import com.aoc.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProblemDay20a implements ProblemDay<Integer> {

    private Scanner scanner;

    @Override
    public Integer solve() {
        final Algorithm algo = new Algorithm(scanner.nextLine());
        scanner.nextLine();
        Image image = new Image(scanner);
        image.print();
        for (int i = 0; i < 2; i++) {
            image = image.apply(algo);
            image.print();
        }
        return image.lit();
    }

    private static class Image {

        private static final Map<Character, Integer> LUT =  new HashMap<Character, Integer>() {{
            put('.', 0);
            put('#', 1);
        }};

        private final char[][] image;

        private final int width;

        private final int height;

        private final char infinity;

        public Image(final Scanner scanner) {
            image = new char[100][100];
            int width = 0;
            int height = 0;
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (width == 0) {
                    width = line.length();
                }
                if (line.length() != width) {
                    throw new RuntimeException("malformed");
                }
                image[height] = line.toCharArray();
                height++;
            }
            this.width = width;
            this.height = height;
            this.infinity = '.';
        }

        public Image(final char[][] image, final char infinity) {
            this.image = image;
            this.width = image[0].length;
            this.height = image.length;
            this.infinity = infinity;
        }

        public Image apply(final Algorithm algo) {
            final char[][] image = new char[height + 2][width + 2];
            for (int row = -1; row < height + 1; row++) {
                for (int col = -1; col < width + 1; col++) {
                    image[row + 1][col + 1] = algo.getChar(getKernel(row, col));
                }
            }
            final char infinity = algo.getChar(getKernel(Integer.MIN_VALUE, Integer.MIN_VALUE));
            return new Image(image, infinity);
        }

        public int lit() {
            int count = 0;
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (image[row][col] == '#') {
                        count++;
                    }
                }
            }
            return count;
        }

        public void print() {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    System.out.print(image[row][col]);
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }

        private int getKernel(final int row, final int col) {
            return Util.bytesToInt(new int[] {
                    getPixel(row - 1, col - 1),
                    getPixel(row - 1, col),
                    getPixel(row - 1, col + 1),
                    getPixel(row, col - 1),
                    getPixel(row, col),
                    getPixel(row, col + 1),
                    getPixel(row + 1, col - 1),
                    getPixel(row + 1, col),
                    getPixel(row + 1, col + 1)
            });
        }

        private int getPixel(final int row, final int col) {
            return LUT.get(
                    (row >= 0 && row < height && col >= 0 && col < width)
                        ? image[row][col]
                        : infinity);

        }
    }

    private static class Algorithm {

        private final char[] algorithm;

        public Algorithm(final String line) {
            algorithm = new char[line.length()];
            for (int i = 0; i < line.length(); i++) {
                algorithm[i] = line.charAt(i);
            }
        }

        public char getChar(final int position) {
            if (position >= algorithm.length) {
                throw new RuntimeException("out of bounds");
            }
            return algorithm[position];
        }
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        scanner = new Scanner(Paths.get(".", "src", "com/aoc/y2021/day20.txt"));
        return scanner;
    }
}
