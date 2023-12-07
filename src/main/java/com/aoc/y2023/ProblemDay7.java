package com.aoc.y2023;

import com.aoc.ProblemDay;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay7 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        return solve(false);
    }

    @Override
    public Integer solveB() {
        return solve(true);
    }

    private int solve(boolean useJoker) {
        Map<Hand, Integer> handToBid = parse(useJoker);
        List<Hand> rank = new ArrayList<>(handToBid.keySet());
        int score = 0;
        for (int i = 0; i < rank.size(); i++) {
            score += (i + 1) * handToBid.get(rank.get(i));
        }
        return score;
    }

    private Map<Hand, Integer> parse(boolean useJoker) {
        Map<Hand, Integer> handToBid = new TreeMap<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            handToBid.put(new Hand(tokens[0], useJoker), Integer.parseInt(tokens[1]));
        }
        return handToBid;
    }

    private enum Combo {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    @AllArgsConstructor
    private enum Card {
        JOKER("J"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("T"),
        JACK("J"),
        QUEEN("Q"),
        KING("K"),
        ACE("A");

        @Getter
        private String card;

        public static Card fromString(String str, boolean useJoker) {
            for (Card card : values()) {
                if (card.getCard().equals(str)) {
                    if (card.equals(Card.JOKER)) {
                       return useJoker ? Card.JOKER : Card.JACK;
                    }
                    return card;
                }
            }
            throw new RuntimeException("No match");
        }

    }

    @EqualsAndHashCode
    private static class Hand implements Comparable<Hand> {

        @Getter
        private List<Card> hand;

        private boolean useJoker;

        public Hand(String line, boolean useJoker) {
            this.hand = new ArrayList<>();
            this.useJoker = useJoker;
            for (int i = 0; i < line.length(); i++) {
                this.hand.add(Card.fromString(line.substring(i, i+1), useJoker));
            }
        }

        public Combo combo() {
            Map<Card, Long> freq = hand.stream()
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            Collectors.counting()));
            long jokers = useJoker
                    ? freq.entrySet()
                            .stream()
                            .filter(e -> e.getKey().equals(Card.JOKER))
                            .map(Map.Entry::getValue)
                            .reduce(Long::sum)
                            .orElse(0L)
                    : 0L;
            Set<Card> used = new HashSet<>();
            if (freq.containsKey(Card.JOKER)) {
                used.add(Card.JOKER);
            }
            if (freq.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(Card.JOKER))
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder())
                    .orElse(0L)
                    + jokers == 5) {
                return Combo.FIVE_OF_A_KIND;
            } else if (freq.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(Card.JOKER))
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder())
                    .orElse(0L)
                    + jokers == 4) {
                return Combo.FOUR_OF_A_KIND;
            } else if (freq.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(Card.JOKER))
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder())
                    .orElse(0L)
                    + jokers == 3) {
                used.add(Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey());
                if (freq.entrySet().stream().filter(e -> !used.contains(e.getKey())).anyMatch(e -> e.getValue() == 2)) {
                    return Combo.FULL_HOUSE;
                } else {
                    return Combo.THREE_OF_A_KIND;
                }
            } else if (freq.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(Card.JOKER))
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder())
                    .orElse(0L)
                    + jokers == 2) {
                used.add(Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey());
                if (freq.entrySet().stream().filter(e -> !used.contains(e.getKey())).anyMatch(e -> e.getValue() == 2)) {
                    return Combo.TWO_PAIR;
                } else {
                    return Combo.ONE_PAIR;
                }
            } else {
                return Combo.HIGH_CARD;
            }
        }

        @Override
        public int compareTo(Hand other) {
            if (Objects.equals(hand, other)) {
                return 0;
            }
            if (hand.equals(other)) {
                return 0;
            }
            Combo handCombo = combo();
            Combo otherCombo = other.combo();
            if (handCombo != otherCombo) {
                return handCombo.compareTo(otherCombo);
            }
            for (int i = 0; i < hand.size(); i++) {
                int comp = hand.get(i).compareTo(other.getHand().get(i));
                if (comp != 0) {
                    return comp;
                }
            }
            return 0;
        }
    }

}
