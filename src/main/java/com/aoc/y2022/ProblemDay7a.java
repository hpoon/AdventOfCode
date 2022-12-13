package com.aoc.y2022;

import com.aoc.NTree;
import com.aoc.ProblemDay;
import lombok.Value;

import java.util.*;

public class ProblemDay7a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        NTree<File> tree = parseTree();
        Map<String, Integer> dirToSizes = tree.score(File::getSize);
        return dirToSizes.values().stream().filter(size -> size <= 100000).mapToInt(Integer::intValue).sum();
    }

    private NTree<File> parseTree() {
        NTree.NTreeNode<File> root = null;
        NTree.NTreeNode<File> cwd = new NTree.NTreeNode<>(new File("", 0));
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            char first = str.charAt(0);
            if (first == '$') {
                String[] cmds = str.split(" ");
                String cmd = cmds[1];
                if (cmd.equals("cd")) {
                    String dir = cmds[2];
                    if (dir.equals("/")) {
                        cwd = new NTree.NTreeNode<>(new File("", 0));
                        root = cwd;
                    } else if (dir.equals("..")) {
                        cwd = cwd.getParent().orElseThrow();
                    } else {
                        cwd = cwd.getNode(new File(dir, 0)).orElseThrow();
                    }
                }
            } else {
                String[] entries = str.split(" ");
                String name = entries[1];
                try {
                    int size = Integer.parseInt(entries[0]);
                    cwd.addNode(new File(name, size));
                } catch (NumberFormatException e) {
                    cwd.addNode(new File(name, 0));
                }
            }
        }
        return new NTree<>(root);
    }

    @Value
    private static class File {

        private String name;

        private int size;

        @Override
        public String toString() {
            return name;
        }

    }

}
