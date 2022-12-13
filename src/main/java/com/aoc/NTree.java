package com.aoc;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

@AllArgsConstructor
public class NTree<T> {

    private final NTreeNode<T> head;

    public Map<String, Integer> score(Function<T, Integer> scoringFunction) {
        Map<String, Integer> nodeToScore = new HashMap<>();
        score(head, nodeToScore, scoringFunction, "");
        return nodeToScore;
    }

    private int score(NTreeNode<T> node,
                      Map<String, Integer> nodeToScore,
                      Function<T, Integer> scoringFunction,
                      String path) {
        T val = node.getValue();
        int score = scoringFunction.apply(val);
        for (NTreeNode<T> child : node.getNodes()) {
            score += score(
                    child,
                    nodeToScore,
                    scoringFunction,
                    Joiner.on("/").join(val.toString(), child.getValue().toString()));
        }
        if (scoringFunction.apply(node.getValue()) == 0) {
            nodeToScore.put(path, score);
        }
        return score;
    }

    @Getter
    public static class NTreeNode<T> {

        @NonNull
        private final T value;

        @Nullable
        private final NTreeNode<T> parent;

        @Nullable private Set<NTreeNode<T>> nodes;

        public NTreeNode(@NonNull T value) {
            this.value = value;
            this.parent = null;
            this.nodes = new HashSet<>();
        }

        private NTreeNode(@NonNull T value, @NonNull NTreeNode<T> parent) {
            this.value = value;
            this.parent = parent;
            this.nodes = new HashSet<>();
        }

        public Set<NTreeNode<T>> getNodes() {
            return nodes != null ? nodes : new HashSet<>();
        }

        public void addNode(@NonNull T node) {
            if (nodes == null) {
                nodes = new HashSet<>();
            }
            nodes.add(new NTreeNode<>(node, this));
        }

        public Optional<NTreeNode<T>> getNode(@NonNull T value) {
            return Optional.ofNullable(nodes)
                    .stream().flatMap(Collection::stream)
                    .filter(node -> value.equals(node.getValue()))
                    .findFirst();
        }

        public Optional<NTreeNode<T>> getParent() {
            return Optional.ofNullable(parent);
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }
}
