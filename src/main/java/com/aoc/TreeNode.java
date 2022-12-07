package com.aoc;

import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public class TreeNode<T> {

    @NonNull private T value;

    @Nullable private TreeNode<T> parent;

    @Nullable private Set<TreeNode<T>> nodes;

    public TreeNode(@NonNull T value) {
        this.value = value;
        this.parent = null;
        this.nodes = new HashSet<>();
    }

    private TreeNode(@NonNull T value, @NonNull TreeNode<T> parent) {
        this.value = value;
        this.parent = parent;
        this.nodes = new HashSet<>();
    }

    public Set<TreeNode<T>> getNodes() {
        return nodes != null ? nodes : new HashSet<>();
    }

    public void addNode(@NonNull T node) {
        if (nodes == null) {
            nodes = new HashSet<>();
        }
        nodes.add(new TreeNode<>(node, this));
    }

    public Optional<TreeNode<T>> getNode(@NonNull T value) {
        return Optional.ofNullable(nodes)
                .stream().flatMap(Collection::stream)
                .filter(node -> value.equals(node.getValue()))
                .findFirst();
    }

    public Optional<TreeNode<T>> getParent() {
        return Optional.ofNullable(parent);
    }

}
