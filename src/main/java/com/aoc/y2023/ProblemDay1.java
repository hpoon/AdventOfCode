package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ProblemDay1 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        int sum = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char a = 0;
            for (int i = 0; i < line.length(); i++) {
                int num = line.charAt(i) - '0';
                if (num >= 0 && num < 10) {
                    a = line.charAt(i);
                    break;
                }
            }
            char b = 0;
            for (int i = line.length() - 1; i >= 0; i--) {
                int num = line.charAt(i) - '0';
                if (num >= 0 && num < 10) {
                    b = line.charAt(i);
                    break;
                }
            }
            sum += Integer.parseInt(String.valueOf(new char[] { a, b }));
        }
        return sum;
    }

    @Override
    public Integer solveB() {
        int sum = 0;
        Map<String, String> numbers = ImmutableMap.<String, String>builder()
                .put("one", "1")
                .put("two", "2")
                .put("three", "3")
                .put("four", "4")
                .put("five", "5")
                .put("six", "6")
                .put("seven", "7")
                .put("eight", "8")
                .put("nine", "9")
                .put("0", "0")
                .put("1", "1")
                .put("2", "2")
                .put("3", "3")
                .put("4", "4")
                .put("5", "5")
                .put("6", "6")
                .put("7", "7")
                .put("8", "8")
                .put("9", "9")
                .build();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int min = line.length();
            int max = 0;
            String a = "";
            String b = "";
            for (String num : numbers.keySet()) {
                int left = line.indexOf(num);
                int right = line.lastIndexOf(num);
                if (left == -1) {
                    continue;
                }
                if (left <= min) {
                    min = left;
                    a = numbers.get(num);
                }
                if (right == -1) {
                    continue;
                }
                if (right >= max) {
                    max = right;
                    b = numbers.get(num);
                }
            }
            sum += Integer.parseInt(a + b);
        }
        return sum;
    }

}
