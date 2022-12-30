package com.aoc.y2015;

import com.aoc.ProblemDay;
import org.apache.commons.codec.digest.DigestUtils;

public class ProblemDay4 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        return solve("00000");
    }

    @Override
    protected Integer solveB() {
        return solve("000000");
    }

    private int solve(String startsWith) {
        String prefix = parse();
        int i = 0;
        while (true) {
            String str = prefix + i;
            String hash = DigestUtils.md5Hex(str);
            if (hash.startsWith(startsWith)) {
                break;
            }
            i++;
        }
        return i;
    }

    private String parse() {
        return scanner.nextLine();
    }

}
