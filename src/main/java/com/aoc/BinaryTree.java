package com.aoc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;

@AllArgsConstructor
public class BinaryTree<T> {

    private final TreeNode<T> head;

    @Getter
    public static class TreeNode<T> {

        @NonNull
        private final T value;

        @Nullable
        private TreeNode<T> left;

        @Nullable
        private TreeNode<T> right;

        public TreeNode(@NonNull T value) {
            this.value = value;
        }

        public void addLeft(@NonNull T node) {
            left = new TreeNode<>(node);
        }

        public void addRight(@NonNull T node) {
            right = new TreeNode<>(node);
        }

    }
}
