package com.aoc.y2015;

import com.aoc.ProblemDay;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay16 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Sue> input = parse();
        Set<Sue> children = input.stream().filter(s -> s.children == null || s.children == 3).collect(Collectors.toSet());
        Set<Sue> cats = children.stream().filter(s -> s.cats == null || s.cats == 7).collect(Collectors.toSet());
        Set<Sue> samoyeds = cats.stream().filter(s -> s.samoyeds == null || s.samoyeds == 2).collect(Collectors.toSet());
        Set<Sue> pomeranians = samoyeds.stream().filter(s -> s.pomeranians == null || s.pomeranians == 3).collect(Collectors.toSet());
        Set<Sue> akitas = pomeranians.stream().filter(s -> s.akitas == null || s.akitas == 0).collect(Collectors.toSet());
        Set<Sue> vizslas = akitas.stream().filter(s -> s.vizslas == null || s.vizslas == 0).collect(Collectors.toSet());
        Set<Sue> goldfish = vizslas.stream().filter(s -> s.goldfish == null || s.goldfish == 5).collect(Collectors.toSet());
        Set<Sue> trees = goldfish.stream().filter(s -> s.trees == null || s.trees == 3).collect(Collectors.toSet());
        Set<Sue> cars = trees.stream().filter(s -> s.cars == null || s.cars == 2).collect(Collectors.toSet());
        Set<Sue> perfumes = cars.stream().filter(s -> s.perfumes == null || s.perfumes == 1).collect(Collectors.toSet());
        return input.indexOf(perfumes.iterator().next()) + 1;
    }

    @Override
    protected Integer solveB() {
        List<Sue> input = parse();
        Set<Sue> children = input.stream().filter(s -> s.children == null || s.children == 3).collect(Collectors.toSet());
        Set<Sue> cats = children.stream().filter(s -> s.cats == null || s.cats > 7).collect(Collectors.toSet());
        Set<Sue> samoyeds = cats.stream().filter(s -> s.samoyeds == null || s.samoyeds == 2).collect(Collectors.toSet());
        Set<Sue> pomeranians = samoyeds.stream().filter(s -> s.pomeranians == null || s.pomeranians < 3).collect(Collectors.toSet());
        Set<Sue> akitas = pomeranians.stream().filter(s -> s.akitas == null || s.akitas == 0).collect(Collectors.toSet());
        Set<Sue> vizslas = akitas.stream().filter(s -> s.vizslas == null || s.vizslas == 0).collect(Collectors.toSet());
        Set<Sue> goldfish = vizslas.stream().filter(s -> s.goldfish == null || s.goldfish < 5).collect(Collectors.toSet());
        Set<Sue> trees = goldfish.stream().filter(s -> s.trees == null || s.trees > 3).collect(Collectors.toSet());
        Set<Sue> cars = trees.stream().filter(s -> s.cars == null || s.cars == 2).collect(Collectors.toSet());
        Set<Sue> perfumes = cars.stream().filter(s -> s.perfumes == null || s.perfumes == 1).collect(Collectors.toSet());
        return input.indexOf(perfumes.iterator().next()) + 1;
    }

    private List<Sue> parse() {
        List<Sue> sues = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] strings = scanner.nextLine().split("[: ,]");
            int i = 3;
            Sue.SueBuilder b = Sue.builder();
            while (true) {
                if (i >= strings.length) {
                    break;
                }
                String property = strings[i];
                int value = Integer.parseInt(strings[i+2]);
                switch (property) {
                    case "children": b.children(value); break;
                    case "cats": b.cats(value); break;
                    case "samoyeds": b.samoyeds(value); break;
                    case "pomeranians": b.pomeranians(value); break;
                    case "akitas": b.akitas(value); break;
                    case "vizslas": b.vizslas(value); break;
                    case "goldfish": b.goldfish(value); break;
                    case "trees": b.trees(value); break;
                    case "cars": b.cars(value); break;
                    case "perfumes": b.perfumes(value); break;
                    default: throw new RuntimeException("unmatched");
                }
                i += 4;
            }
            sues.add(b.build());
        }
        return sues;
    }

    @Builder
    private static class Sue {
        private Integer children;
        private Integer cats;
        private Integer samoyeds;
        private Integer pomeranians;
        private Integer akitas;
        private Integer vizslas;
        private Integer goldfish;
        private Integer trees;
        private Integer cars;
        private Integer perfumes;
    }

}
