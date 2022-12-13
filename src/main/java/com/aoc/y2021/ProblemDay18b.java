package com.aoc.y2021;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class ProblemDay18b extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        final List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        int max = 0;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.size(); j++) {
                if (i == j) { continue; }
                final Fish fish1 = findPair(lines.get(i));
                final Fish fish2 = findPair(lines.get(j));
                final Fish sum = new Fish(fish1, fish2);
                final int magnitude = add(sum);
                if (magnitude > max) {
                    max = magnitude;
                }
            }
        }

        return max;
    }

    private int add(final Fish fish) {
        boolean splitted = true;
        while (splitted) {
            // Explode
            Pair<Fish, Stack<Fish>> deepest = null;
            while (true) {
                deepest = excess(ImmutablePair.of(fish, new Stack<>()));
                if (deepest.getValue().size() <= 4) {
                    break;
                }

                final Fish curFish = deepest.getKey();
                final int curLeft = curFish.getLeftInt();
                final int curRight = curFish.getRightInt();
                final Stack<Fish> curPath = deepest.getValue();
                curPath.pop();
                Fish parent = curPath.pop();
                final boolean isCurLeft = curFish.equals(parent.getLeftPair());

                // Reduce existing pair
                if (isCurLeft) {
                    parent.setLeftInt(0);

                    // Add left
                    final Stack<Fish> leftPath = new Stack<>();
                    leftPath.addAll(curPath);
                    Fish prev = parent;
                    Fish cur = leftPath.peek();
                    if (cur.getLeftPair() == null) {
                        cur.setLeftInt(cur.getLeftInt() + curLeft);
                    } else {
                        boolean found = false;
                        while (!leftPath.isEmpty()) {
                            cur = leftPath.pop();
                            if (!prev.equals(cur.getLeftPair())) {
                                found = true;
                                break;
                            }
                            prev = cur;
                        }
                        if (found) {
                            if (cur.getLeftPair() == null) {
                                cur.setLeftInt(cur.getLeftInt() + curLeft);
                            } else {
                                cur = getRightMost(cur.getLeftPair());
                                cur.setRightInt(cur.getRightInt() + curLeft);
                            }
                        }
                    }

                    // Add right
                    if (parent.getRightPair() == null) {
                        parent.setRightInt(parent.getRightInt() + curRight);
                    } else {
                        cur = getLeftMost(parent.getRightPair());
                        cur.setLeftInt(cur.getLeftInt() + curRight);
                    }
                } else {
                    parent.setRightInt(0);

                    // Add left
                    if (parent.getLeftPair() == null) {
                        parent.setLeftInt(parent.getLeftInt() + curLeft);
                    } else {
                        Fish cur = getRightMost(parent.getLeftPair());
                        cur.setRightInt(cur.getRightInt() + curLeft);
                    }

                    // Add right
                    final Stack<Fish> rightPath = new Stack<>();
                    rightPath.addAll(curPath);
                    Fish prev = parent;
                    Fish cur = rightPath.peek();
                    if (cur.getRightPair() == null) {
                        cur.setRightInt(cur.getRightInt() + curRight);
                    } else {
                        boolean found = false;
                        while (!rightPath.isEmpty()) {
                            cur = rightPath.pop();
                            if (!prev.equals(cur.getRightPair())) {
                                found = true;
                                break;
                            }
                            prev = cur;
                        }
                        if (found) {
                            if (cur.getRightPair() == null) {
                                cur.setRightInt(cur.getRightInt() + curRight);
                            } else {
                                cur = getLeftMost(cur.getRightPair());
                                cur.setLeftInt(cur.getLeftInt() + curRight);
                            }
                        }
                    }
                }
            }

            // Split
            splitted = split(fish);
        }

        return magnitude(fish);
    }

    private int magnitude(final Fish pair) {
        if (pair.getLeftPair() == null && pair.getRightPair() == null) {
            return 3 * pair.getLeftInt() + 2 * pair.getRightInt();
        } else if (pair.getLeftPair() != null && pair.getRightPair() == null) {
            return 3 * magnitude(pair.getLeftPair()) + 2 * pair.getRightInt();
        } else if (pair.getLeftPair() == null && pair.getRightPair() != null) {
            return 3 * pair.getLeftInt() + 2 * magnitude(pair.getRightPair());
        } else {
            return 3 * magnitude(pair.getLeftPair()) + 2 * magnitude(pair.getRightPair());
        }
    }

    private boolean split(final Fish fish) {
        boolean splitted;
        if (fish.getLeftPair() == null) {
            final int left = fish.getLeftInt();
            if (left >= 10) {
                final int newLeft = left / 2;
                final int newRight = (left % 2 == 0) ? left / 2 : left / 2 + 1;
                fish.setLeftPair(new Fish(newLeft, newRight));
                return true;
            }
        } else {
            splitted = split(fish.getLeftPair());
            if (splitted) {
                return splitted;
            }
        }

        if (fish.getRightPair() == null) {
            final int right = fish.getRightInt();
            if (right >= 10) {
                final int newLeft = right / 2;
                final int newRight = (right % 2 == 0) ? right / 2 : right / 2 + 1;
                fish.setRightPair(new Fish(newLeft, newRight));
                return true;
            }
        } else {
            splitted = split(fish.getRightPair());
            if (splitted) {
                return splitted;
            }
        }

        return false;
    }

    private Fish getLeftMost(final Fish pair) {
        return (pair.getLeftPair() != null)
                ? getLeftMost(pair.getLeftPair())
                : pair;
    }

    private Fish getRightMost(final Fish pair) {
        return (pair.getRightPair() != null)
                ? getRightMost(pair.getRightPair())
                : pair;
    }

    private Pair<Fish, Stack<Fish>> excess(final Pair<Fish, Stack<Fish>> pairCount) {
        final Fish fish = pairCount.getKey();
        final Stack<Fish> path = pairCount.getValue();

        final Stack<Fish> newPath = new Stack<>();
        newPath.addAll(path);
        newPath.add(fish);

        if (fish.getLeftPair() == null && fish.getRightPair() == null) {
            return ImmutablePair.of(fish, newPath);
        }

        Pair<Fish, Stack<Fish>> candidate;
        if (fish.getLeftPair() != null) {
             candidate = excess(ImmutablePair.of(fish.getLeftPair(), newPath));
             if (candidate.getValue().size() > 4) {
                 return candidate;
             }
        }
        if (fish.getRightPair() != null) {
            candidate = excess(ImmutablePair.of(fish.getRightPair(), newPath));
            if (candidate.getValue().size() > 4) {
                return candidate;
            }
        }

        return ImmutablePair.of(fish, newPath);
    }

    private Fish findPair(final String line) {
        final char first = line.charAt(0);
        String left = null;
        boolean leftIsInt = true;
        if (first == '[') {
            int unmatched = 0;
            for (int i = 1; i < line.length(); i++) {
                final char c = line.charAt(i);
                if (c == '[') {
                    unmatched++;
                    leftIsInt = false;
                } else if (c == ']') {
                    unmatched--;
                    leftIsInt = false;
                } else if (c == ',') {
                    if (unmatched == 0) {
                        left = line.substring(1, i);
                        break;
                    }
                }
            }
        }

        final char last = line.charAt(line.length() - 1);
        String right = null;
        boolean rightIsInt = true;
        if (last == ']') {
            int unmatched = 0;
            for (int i = line.length() - 2; i >= 0; i--) {
                final char c = line.charAt(i);
                if (c == ']') {
                    unmatched++;
                    rightIsInt = false;
                } else if (c == '[') {
                    unmatched--;
                    rightIsInt = false;
                } else if (c == ',') {
                    if (unmatched == 0) {
                        right = line.substring(i + 1, line.length() - 1);
                        break;
                    }
                }
            }
        }

        if (!leftIsInt && !rightIsInt) {
            return new Fish(findPair(left), findPair(right));
        } else if (!leftIsInt) {
            return new Fish(findPair(left), Integer.parseInt(right));
        } else if (!rightIsInt) {
            return new Fish(Integer.parseInt(left), findPair(right));
        } else {
            return new Fish(Integer.parseInt(left), Integer.parseInt(right));
        }
    }

    private static class Fish {

        private Integer leftInt;

        private Integer rightInt;

        private Fish leftPair;

        private Fish rightPair;

        public Fish(final int left, final int right) {
            this.leftInt = left;
            this.rightInt = right;
        }

        public Fish(final Fish left, final Fish right) {
            this.leftPair = left;
            this.rightPair = right;
        }

        public Fish(final int left, final Fish right) {
            this.leftInt = left;
            this.rightPair = right;
        }

        public Fish(final Fish left, final int right) {
            this.leftPair = left;
            this.rightInt = right;
        }

        public int getLeftInt() {
            return this.leftInt;
        }

        public int getRightInt() {
            return this.rightInt;
        }

        public Fish getLeftPair() {
            return this.leftPair;
        }

        public Fish getRightPair() {
            return this.rightPair;
        }

        public void setLeftInt(final int left) {
            this.leftInt = left;
            this.leftPair = null;
        }

        public void setLeftPair(final Fish left) {
            this.leftInt = null;
            this.leftPair = left;
        }

        public void setRightInt(final int right) {
            this.rightInt = right;
            this.rightPair = null;
        }

        public void setRightPair(final Fish right) {
            this.rightInt = null;
            this.rightPair = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Fish fish = (Fish) o;
            return Objects.equals(leftInt, fish.leftInt) && Objects.equals(rightInt, fish.rightInt) && Objects.equals(leftPair, fish.leftPair) && Objects.equals(rightPair, fish.rightPair);
        }

        @Override
        public int hashCode() {
            return Objects.hash(leftInt, rightInt, leftPair, rightPair);
        }
    }
}
