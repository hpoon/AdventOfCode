package com.aoc.y2015;

import com.aoc.ProblemDay;

import java.util.regex.Pattern;

public class ProblemDay11 extends ProblemDay<String, String> {

    private static final Pattern THREE_CONSECUTIVE_CHARACTERS = Pattern.compile("(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)+");
    private static final Pattern IOL = Pattern.compile("[iol]");
    private static final Pattern TWO_PAIRS = Pattern.compile("([a-z\\d])\\1");

    @Override
    protected String solveA() {
        String input = parse();
        return solve(input);
    }

    @Override
    protected String solveB() {
        String input = parse();
        return solve(solve(input));
    }

    private String solve(String input) {
        String pw = input;
        while (true) {
            for (int i = pw.length() - 1; i >= 0; i--) {
                StringBuilder str = new StringBuilder(pw);
                char c = pw.charAt(i);
                if (c == 'z') {
                    str.setCharAt(i, 'a');
                    pw = str.toString();
                } else {
                    str.setCharAt(i, (char) (c+1));
                    pw = str.toString();
                    break;
                }
            }
            if (isPasswordValid(pw)) {
                return pw;
            }
        }
    }

    private boolean isPasswordValid(String pw) {
        return !containsIOL(pw) && hasThreeConsecutiveCharacters(pw) && containsAtLeastTwoPairs(pw);
    }

    private boolean hasThreeConsecutiveCharacters(String pw) {
        return THREE_CONSECUTIVE_CHARACTERS.matcher(pw).find();
    }

    private boolean containsIOL(String pw) {
        return IOL.matcher(pw).find();
    }

    private boolean containsAtLeastTwoPairs(String pw) {
        return TWO_PAIRS.matcher(pw).results().count() >= 2;
    }

    private String parse() {
        return scanner.nextLine();
    }
    
}
