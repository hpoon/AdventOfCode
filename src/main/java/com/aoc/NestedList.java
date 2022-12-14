package com.aoc;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NestedList {

    private NestedListNode root;

    public NestedList(String line) {
        this.root = new NestedListNode(parse(line));
    }

    public int compareTo(NestedList other) {
        return root.compareTo(other.root);
    }

    private List<NestedListNode> parse(String line) {
        List<NestedListNode> list = new ArrayList<>();
        int idx = 0;
        while (line.length() > 0) {
            char c = line.charAt(idx);
            if (c == '[') {
                Stack<Integer> brackets = new Stack<>();
                int closingIdx = line.length() - 1;
                for (int i = idx; i < line.length(); i++) {
                    char d = line.charAt(i);
                    if (d == '[') {
                        brackets.add(i);
                    }
                    if (d == ']') {
                        closingIdx = i;
                        brackets.pop();
                    }
                    if (brackets.isEmpty()) {
                        break;
                    }
                }
                list.add(new NestedListNode(parse(line.substring(idx+1, closingIdx))));
                line = line.substring(closingIdx + 1);
            } else if (c == ',' && idx != 0) {
                list.add(new NestedListNode(Integer.parseInt(line.substring(0, idx))));
                line = line.substring(idx+1);
                idx = 0;
            } else if (c == ',') {
                line = line.substring(idx+1);
            } else if (idx == line.length() - 1) {
                list.add(new NestedListNode(Integer.parseInt(line.substring(0, idx+1))));
                return list;
            } else {
                idx++;
            }
        }
        return list;
    }

    @Getter
    private static class NestedListNode {

        private List<NestedListNode> list;

        private int val;

        public NestedListNode(List<NestedListNode> list) {
            this.list = list;
        }

        public NestedListNode(int val) {
            this.val = val;
        }

        public boolean isNested() {
            return list != null;
        }

        public int compareTo(NestedListNode other) {
            if (isNested() && other.isNested()) {
                for (int i = 0; i < Math.min(list.size(), other.getList().size()); i++) {
                    NestedListNode n1 = list.get(i);
                    NestedListNode n2 = other.list.get(i);
                    int compare = n1.compareTo(n2);
                    if (compare != 0) {
                        return compare;
                    }
                }
                return Integer.compare(list.size(), other.getList().size());
            } else if (isNested()) {
                return compareTo(new NestedListNode(ImmutableList.of(other)));
            } else if (other.isNested()) {
                return new NestedListNode(ImmutableList.of(this)).compareTo(other);
            } else {
                int v1 = val;
                int v2 = other.val;
                return Integer.compare(v1, v2);
            }
        }

    }

}
