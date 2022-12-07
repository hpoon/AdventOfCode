package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.aoc.TreeNode;
import com.google.common.base.Joiner;
import lombok.Value;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProblemDay7b implements ProblemDay<Integer> {

    private Scanner scanner;

    public Integer solve() {
        TreeNode<File> root = null;
        TreeNode<File> cwd = new TreeNode<>(new File("", 0));
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            char first = str.charAt(0);
            if (first == '$') {
                String[] cmds = str.split(" ");
                String cmd = cmds[1];
                if (cmd.equals("cd")) {
                    String dir = cmds[2];
                    if (dir.equals("/")) {
                        cwd = new TreeNode<>(new File("", 0));
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

        Map<String, Integer> dirToSizes = new HashMap<>();
        size(root, dirToSizes, "");

        int totalSpace = 70000000;
        int goalUsed = totalSpace - 30000000;
        int used = dirToSizes.get("");
        int toFree = used - goalUsed;

        return dirToSizes.values().stream()
                .filter(size -> size >= toFree)
                .mapToInt(Integer::intValue).min()
                .orElseThrow();
    }

    @Override
    public Scanner getProblemInputStream() throws IOException {
        this.scanner = new Scanner(Paths.get(".", "src/main/resources/y2022/day7.txt"));
        return scanner;
    }

    private int size(TreeNode<File> node, Map<String, Integer> dirToSizes, String path) {
        File file = node.getValue();
        int size = file.getSize();
        for (TreeNode<File> child : node.getNodes()) {
            size += size(child, dirToSizes, Joiner.on("/").join(path, child.getValue().getName()));
        }
        if (file.getSize() == 0) {
            dirToSizes.put(path, size);
        }
        return size;
    }

    @Value
    private static class File {

        private String name;

        private int size;

    }

}
