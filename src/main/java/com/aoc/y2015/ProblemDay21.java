package com.aoc.y2015;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemDay21 extends ProblemDay<Integer, Integer> {

    private List<Item> ITEMS = ImmutableList.<Item>builder()
            .add(new Item("Dagger", 8, 4, 0, ItemType.WEAPON))
            .add(new Item("Shortsword", 10, 5, 0, ItemType.WEAPON))
            .add(new Item("Warhammer", 25, 6, 0, ItemType.WEAPON))
            .add(new Item("Longsword", 40, 7, 0, ItemType.WEAPON))
            .add(new Item("Greataxe", 74, 8, 0, ItemType.WEAPON))
            .add(new Item("Leather", 13, 0, 1, ItemType.ARMOUR))
            .add(new Item("Chainmail", 31, 0, 2, ItemType.ARMOUR))
            .add(new Item("Splintmail", 53, 0, 3, ItemType.ARMOUR))
            .add(new Item("Bandedmail", 75, 0, 4, ItemType.ARMOUR))
            .add(new Item("Platemail", 102, 0, 5, ItemType.ARMOUR))
            .add(new Item("Damage +1", 25, 1, 0, ItemType.RING))
            .add(new Item("Damage +2", 50, 2, 0, ItemType.RING))
            .add(new Item("Damage +3", 100, 3, 0, ItemType.RING))
            .add(new Item("Defense +1", 20, 0, 1, ItemType.RING))
            .add(new Item("Defense +2", 40, 0, 2, ItemType.RING))
            .add(new Item("Defense +3", 80, 0, 3, ItemType.RING))
            .build();

    @Override
    protected Integer solveA() {
        Boss boss = parse();
        List<Player> players = combinations();
        List<Player> winners = players.stream().filter(p -> fight(p, boss)).collect(Collectors.toList());
        return winners.stream()
                .map(Player::cost)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    @Override
    protected Integer solveB() {
        Boss boss = parse();
        List<Player> players = combinations();
        List<Player> losers = players.stream().filter(p -> !fight(p, boss)).collect(Collectors.toList());
        return losers.stream()
                .map(Player::cost)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Shouldn't happen"));
    }

    private Boss parse() {
        int hp = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        int atk = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        int def = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        return new Boss(hp, atk, def);
    }

    private boolean fight(Player player, Boss boss) {
        final Boss bossCopy = new Boss(boss.getHp(), boss.getAtk(), boss.getDef());
        final Player playerCopy = new Player(player.getHp(), player.getWeapon(), player.getArmour(), player.getRings());
        while (true) {
            int playerDmg = Math.max(playerCopy.attack() - bossCopy.getDef(), 1);
            bossCopy.setHp(bossCopy.getHp() - playerDmg);
            if (bossCopy.dead()) {
                return true;
            }
            int bossDmg = Math.max(bossCopy.getAtk() - playerCopy.armour(), 1);
            playerCopy.setHp(playerCopy.getHp() - bossDmg);
            if (playerCopy.dead()) {
                return false;
            }
        }
    }

    private List<Player> combinations() {
        List<Item> weapons = ITEMS.stream().filter(i -> i.getType().equals(ItemType.WEAPON)).collect(Collectors.toList());
        List<Item> armour = ITEMS.stream().filter(i -> i.getType().equals(ItemType.ARMOUR)).collect(Collectors.toList());
        armour.add(null);  // Armour optional
        List<Item> rings = ITEMS.stream().filter(i -> i.getType().equals(ItemType.RING)).collect(Collectors.toList());
        List<Player> players = new ArrayList<>();
        for (Item w : weapons) {
            for (Item a : armour) {
                players.add(new Player(100, w, a, ImmutableList.of()));
                for (Item r : rings) {
                    players.add(new Player(100, w, a, ImmutableList.of(r)));
                }
                Set<Set<Integer>> visited = new HashSet<>();
                for (int i = 0; i < rings.size(); i++) {
                    for (int j = 0; j < rings.size(); j++) {
                        if (i == j) {
                            continue;
                        }
                        if (!visited.contains(ImmutableSet.of(i, j))) {
                            visited.add(ImmutableSet.of(i, j));
                            players.add(new Player(100, w, a, ImmutableList.of(rings.get(i), rings.get(j))));
                        }
                    }
                }
            }
        }
        return players;
    }

    @Getter
    @AllArgsConstructor
    private static class Player {
        @Setter int hp;
        Item weapon;
        Item armour;
        List<Item> rings;

        public boolean dead() {
            return hp <= 0;
        }

        public int attack() {
            return rings.stream().mapToInt(Item::getAtk).sum() + (weapon != null ? weapon.getAtk() : 0);
        }

        public int armour() {
            return rings.stream().mapToInt(Item::getDef).sum() + (armour != null ? armour.getDef() : 0);
        }

        public int cost() {
            return (weapon != null ? weapon.getCost() : 0)
                    + (armour != null ? armour.getCost() : 0)
                    + rings.stream().mapToInt(Item::getCost).sum();
        }
    }

    @Getter
    @AllArgsConstructor
    private static class Boss {
        @Setter private int hp;
        private int atk;
        private int def;

        public boolean dead() {
            return hp <= 0;
        }
    }

    @Value
    private static class Item {
        String name;
        int cost;
        int atk;
        int def;
        ItemType type;
    }

    private enum ItemType {
        WEAPON, ARMOUR, RING
    }

}
