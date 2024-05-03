package com.aoc.y2016;

import com.aoc.ProblemDay;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashSet;
import java.util.Set;

public class ProblemDay5 extends ProblemDay<String, String> {

    @Override
    public String solveA() {
        String id = parse();
        return solveA(id);
    }

    @Override
    public String solveB() {
        String id = parse();
        return solveB(id);
    }

    private String solveA(String id) {
        int i = 0;
        String password = "";
        while (true) {
            String hash = DigestUtils.md5Hex(id + i);
            if (hash.startsWith("00000")) {
                password += hash.charAt(5);
            }
            if (password.length() == 8) {
                return password;
            }
            i++;
        }
    }

    private String solveB(String id) {
        int i = 0;
        char[] password = new char[8];
        Set<Integer> indices = new HashSet<>();
        while (true) {
            String hash = DigestUtils.md5Hex(id + i);
            int index = hash.charAt(5) - '0';
            if (hash.startsWith("00000") && index < 8 && index >= 0 && !indices.contains(index)) {
                password[index] = hash.charAt(6);
                indices.add(index);
            }
            if (indices.size() == 8) {
                return new String(password);
            }
            i++;
        }
    }

    private String parse() {
        return scanner.nextLine();
    }

}
