package com.aoc.y2024;

import com.aoc.ProblemDay;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ProblemDay9 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Block> blocks = parse(true);
        defrag(blocks);
        return score(blocks);
    }

    @Override
    public Long solveB() {
        List<Block> blocks = parse(false);
        defrag(blocks);
        return score(blocks);
    }

    private long score(List<Block> blocks) {
        long score = 0;
        int n = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).isEmpty()) {
                n += blocks.get(i).getSize();
                continue;
            }
            for (int j = 0; j < blocks.get(i).getSize(); j++) {
                score = Math.addExact(score, n * blocks.get(i).score());
                n++;
            }
        }
        return score;
    }

    private void defrag(List<Block> blocks) {
        int defrag = blocks.size() - 1;
        while (defrag >= 0) {
            defrag = findLastFull(blocks, defrag);
            Block toDefrag = blocks.get(defrag);

            int next = findNextEmpty(blocks, 0, toDefrag.getSize());
            if (next == -1) {
                defrag--;
                continue;
            }

            if (next >= defrag) {
                defrag--;
                continue;
            }

            Block temp = blocks.get(next);
            if (temp.getSize() > toDefrag.getSize()) {
                blocks.set(defrag, new Block(toDefrag.getSize()));
                blocks.set(next, toDefrag);
                blocks.add(next + 1, new Block(temp.getSize() - toDefrag.getSize()));
            } else {
                blocks.set(next, blocks.get(defrag));
                blocks.set(defrag, temp);
                defrag--;
            }
        }
    }

    private int findNextEmpty(List<Block> blocks, int index, int minSize) {
        for (int i = index; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.isEmpty() && block.getSize() >= minSize) {
                return i;
            }
        }
        return -1;
    }

    private int findLastFull(List<Block> blocks, int index) {
        for (int i = index; i >= 0; i--) {
            Block block = blocks.get(i);
            if (!block.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private List<Block> parse(boolean individualBlocks) {
        String line = scanner.nextLine();
        List<Block> blocks = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < line.length(); i++) {
            boolean isFile = i % 2 == 0;
            int size = line.charAt(i) - '0';
            if (size == 0) {
                continue;
            }
            if (individualBlocks) {
                Block block = isFile ? new FileBlock(1, id) : new Block(1);
                for (int j = 0; j < size; j++) {
                    blocks.add(block);
                }
            } else {
                Block block = isFile ? new FileBlock(size, id) : new Block(size);
                blocks.add(block);
            }
            if (isFile) {
                id++;
            }
        }
        return blocks;
    }

    @Getter
    @AllArgsConstructor
    private static class Block {
        private int size;

        public boolean isEmpty() {
            return true;
        }

        public long score() {
            return 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < getSize(); i++) {
                sb.append(".");
            }
            return sb.toString();
        }
    }

    @Getter
    private static class FileBlock extends Block {
        private long id;

        public FileBlock(int size, long id) {
            super(size);
            this.id = id;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long score() {
            return id;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < getSize(); i++) {
                sb.append(id);
            }
            return sb.toString();
        }
    }
}
