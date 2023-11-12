package com.aoc.y2015;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import lombok.Value;

import java.util.*;

public class ProblemDay15 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Ingredient> ingredients = parse();
        int max = 0;
        for (int i = 0; i <= 100; i++) {
            for (int j = 0; j <= 100; j++) {
                for (int k = 0; k <= 100; k++) {
                    for (int l = 0; l <= 100; l++) {
                        if (i + j + k + l != 100) {
                            continue;
                        }
                        Map<Ingredient, Integer> counts = ImmutableMap.of(
                                ingredients.get(0), i,
                                ingredients.get(1), j,
                                ingredients.get(2), k,
                                ingredients.get(3), l);
                        max = Math.max(
                                max,
                                score(counts));
                    }
                }
            }
        }
        return max;
    }

    @Override
    protected Integer solveB() {
        List<Ingredient> ingredients = parse();
        int max = 0;
        for (int i = 0; i <= 100; i++) {
            for (int j = 0; j <= 100; j++) {
                for (int k = 0; k <= 100; k++) {
                    for (int l = 0; l <= 100; l++) {
                        if (i + j + k + l != 100) {
                            continue;
                        }
                        Map<Ingredient, Integer> counts = ImmutableMap.of(
                                ingredients.get(0), i,
                                ingredients.get(1), j,
                                ingredients.get(2), k,
                                ingredients.get(3), l);
                        if (cals(counts) != 500) {
                            continue;
                        }
                        max = Math.max(
                                max,
                                score(counts));
                    }
                }
            }
        }
        return max;
    }

    private int score(Map<Ingredient, Integer> counts) {
        int capacity = 0;
        int durability = 0;
        int flavour = 0;
        int texture = 0;
        for (Map.Entry<Ingredient, Integer> count : counts.entrySet()) {
            Ingredient ingredient = count.getKey();
            int spoons = count.getValue();
            capacity += ingredient.getCapacity() * spoons;
            durability += ingredient.getDurability() * spoons;
            flavour += ingredient.getFlavour() * spoons;
            texture += ingredient.getTexture() * spoons;
        }
        return Math.min(Math.min(capacity, durability), Math.min(flavour, texture)) > 0
                ? capacity * durability * flavour * texture
                : 0;
    }

    private int cals(Map<Ingredient, Integer> counts) {
        int cals = 0;
        for (Map.Entry<Ingredient, Integer> count : counts.entrySet()) {
            Ingredient ingredient = count.getKey();
            int spoons = count.getValue();
            cals += ingredient.getCalories() * spoons;
        }
        return cals;
    }

    private List<Ingredient> parse() {
        List<Ingredient> ingredients = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] strings = scanner.nextLine().split("[: ,]");
            ingredients.add(
                    new Ingredient(
                            strings[0],
                            Integer.parseInt(strings[3]),
                            Integer.parseInt(strings[6]),
                            Integer.parseInt(strings[9]),
                            Integer.parseInt(strings[12]),
                            Integer.parseInt(strings[15])));
        }
        return ingredients;
    }

    @Value
    private static class Ingredient {
        private String name;
        private int capacity;
        private int durability;
        private int flavour;
        private int texture;
        private int calories;
    }
}
