package com.aoc.y2022;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProblemDay5b extends ProblemDay<String> {

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
                List<String> order = new ArrayList<>();
                for (int i = 0; i < moves; i++) {
                    order.add(stacks.get(from).pop());
                }
                for (int i = moves - 1; i >= 0; i--) {
                    stacks.get(to).add(order.get(i));
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

}
