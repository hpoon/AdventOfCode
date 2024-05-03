package com.aoc.y2016;

import com.aoc.ProblemDay;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemDay7 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<IP> ips = parse();
        return solveA(ips);
    }

    @Override
    public Integer solveB() {
        List<IP> ips = parse();
        return solveB(ips);
    }

    private int solveA(List<IP> ips) {
        int count = 0;
        for (IP ip : ips) {
            count += ip.sqs.stream().anyMatch(this::abba) && ip.hyperSqs.stream().noneMatch(this::abba) ? 1 : 0;
        }
        return count;
    }

    private int solveB(List<IP> ips) {
        int count = 0;
        for (IP ip : ips) {
            List<String> abas = ip.sqs.stream().flatMap(sq -> aba(sq).stream()).collect(Collectors.toList());
            if (abas.isEmpty()) {
                continue;
            }
            boolean bab = ip.hyperSqs.stream().anyMatch(sq -> bab(sq, abas));
            count += !abas.isEmpty() && bab ? 1 : 0;
        }
        return count;
    }

    private List<String> aba(String string) {
        return palindromeN(string, 3);
    }

    private boolean bab(String string, List<String> testAbas) {
        List<String> abas = palindromeN(string, 3);
        for (String aba : abas) {
            for (String testAba : testAbas) {
                if (aba.charAt(0) == testAba.charAt(1) && aba.charAt(1) == testAba.charAt(0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean abba(String string) {
        return !palindromeN(string, 4).isEmpty();
    }

    private List<String> palindromeN(String string, int n) {
        List<String> matches = new ArrayList<>();
        for (int i = 0; i < string.length() - (n - 1); i++) {
            String test = string.substring(i, i + n);
            if (test.charAt(0) != test.charAt(1) && palindrome(test)) {
                matches.add(test);
            }
        }
        return matches;
    }

    private boolean palindrome(String string) {
        int lf = 0;
        int rt = string.length() - 1;
        while (lf < rt) {
            if (string.charAt(lf) != string.charAt(rt)) {
                return false;
            }
            lf++;
            rt--;
        }
        return true;
    }

    private List<IP> parse() {
        List<IP> ips = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            StringBuilder sb = new StringBuilder();
            List<String> sqs = new ArrayList<>();
            List<String> hyperSqs = new ArrayList<>();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '[') {
                    sqs.add(sb.toString());
                    sb = new StringBuilder();
                } else if (c == ']') {
                    hyperSqs.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }
            }
            sqs.add(sb.toString());
            ips.add(new IP(sqs, hyperSqs));
        }
        return ips;
    }

    @AllArgsConstructor
    private static class IP {
        private List<String> sqs;
        private List<String> hyperSqs;
    }

}
