package com.aoc.y2022;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ProblemDay5a implements ProblemDay<String> {

    private Scanner scanner;

    public String solve() {
        int line = 1;
        List<List<String>> initialState = new ArrayList<>();
        List<Stack<String>> stacks = new ArrayList<>();
        for (int col = 0; col < 9; col++) {
            initialState.add(new ArrayList<>());
            stacks.add(new Stack<>());
        }
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            if (line < 9) {
                for (int col = 0; col < initialState.size(); col++) {
                    if (col * 4 + 3 > str.length()) {
                        continue;
                    }
                    String colStr = str.substring(col * 4, col * 4 + 3);
                    if (StringUtils.isBlank(colStr)) {
                        continue;
                    }
                    initialState.get(col).add(colStr);
                }
            } else if (line == 9) {
                for (int col = 0; col < initialState.size(); col++) {
                    for (int i = initialState.get(col).size() - 1; i >= 0; i--) {
                        stacks.get(col).add(initialState.get(col).get(i));
                    }
                }
            } else if (line == 10) {
                // Nothing
            } else {
                String[] cmd = str.split(" ");
                int moves = Integer.parseInt(cmd[1]);
                int from = Integer.parseInt(cmd[3]) - 1;
                int to = Integer.parseInt(cmd[5]) - 1;
                for (int i = 0; i < moves; i++) {
                    stacks.get(to).add(stacks.get(from).pop());
                }
            }
            line++;
        }
        String ans = "";
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).isEmpty()) {
                continue;
            }
            ans += stacks.get(i).peek();
        }
        return ans.replace("[", "").replace("]", "");
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day5.txt"));
        return scanner;
    }

}
