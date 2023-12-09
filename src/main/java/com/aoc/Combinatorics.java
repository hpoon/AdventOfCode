package com.aoc;

import java.util.ArrayList;
import java.util.List;

public class Combinatorics {

    public static <T> void permutations(List<T> list, List<T> permutation, List<List<T>> permutations) {
        if (list.isEmpty()) {
            permutations.add(permutation);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            List<T> newCombo = new ArrayList<>(permutation);
            newCombo.add(list.get(i));
            List<T> newList = new ArrayList<>(list);
            newList.remove(i);
            permutations(newList, newCombo, permutations);
        }
    }

}
