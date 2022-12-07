package com.aoc.y2022;

import com.aoc.ProblemDay;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ProblemDay6a implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        String str = scanner.nextLine();
        Queue<Character> queue = new LinkedList<>();
        Map<Character, Integer> freqs = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            queue.add(c);
            if (queue.size() > 4) {
                char out = queue.poll();
                int freq = freqs.get(out) - 1;
                if (freq == 0) {
                    freqs.remove(out);
                } else {
                    freqs.put(out, freq);
                }
            }
            if (freqs.size() < 4) {
                freqs.put(c, freqs.getOrDefault(c, 0) + 1);
            }
            if (freqs.size() == 4) {
                return i + 1;
            }
        }
        return -1;
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day6.txt"));
        return scanner;
    }

}
