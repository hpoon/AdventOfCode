package com.aoc.y2016;

import com.aoc.ProblemDay;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProblemDay4 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
        List<Room> rooms = parse();
        return solveA(rooms);
    }

    @Override
    public Integer solveB() {
        List<Room> rooms = parse();
        return solveB(rooms);
    }

    private int solveA(List<Room> rooms) {
        int answer = 0;
        for (Room room : rooms) {
            if (isRealRoom(room)) {
                answer += room.sectorId;
            }
        }
        return answer;
    }

    private int solveB(List<Room> rooms) {
        for (Room room : rooms) {
            String decrypted = decryptRoom(room);
            if ("northpole4object4storage".equals(decrypted)) {
                return room.sectorId;
            }
        }
        throw new RuntimeException("Shouldn't happen");
    }

    private boolean isRealRoom(Room room) {
        String name = room.name.toLowerCase().replaceAll("[0-9\\-]", "");
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        Map<Integer, Set<Character>> grouped = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            Set<Character> chars = grouped.getOrDefault(entry.getValue(), new TreeSet<>());
            chars.add(entry.getKey());
            grouped.put(entry.getValue(), chars);
        }
        String expected = room.checksum;
        String actual = "";
        Iterator<Map.Entry<Integer, Set<Character>>> iterator = grouped.entrySet().iterator();
        while (actual.length() != expected.length()) {
            Set<Character> chars = iterator.next().getValue();
            for (char c : chars) {
                actual += c;
                if (actual.length() == expected.length()) {
                    break;
                }
            }
        }
        return actual.equals(expected);
    }

    private String decryptRoom(Room room) {
        int cipher = room.sectorId;
        int rotation = cipher % 26;
        String name = room.name.toLowerCase();
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == ' ') {
                decrypted.append(" ");
            } else {
                int v = c - 'a' + rotation;
                v = v >= 26 ? v - 26 : v;
                decrypted.append((char)(v + 'a'));
            }
        }
        return decrypted.toString();
    }

    private List<Room> parse() {
        List<Room> rooms = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens1 = scanner.nextLine().split("\\[");
            String[] tokens2 = tokens1[0].split("-");
            rooms.add(new Room(
                    tokens1[0].replace("-" + tokens2[tokens2.length - 1], ""),
                    Integer.parseInt(tokens2[tokens2.length - 1]),
                    tokens1[1].replace("]", "")));
        }
        return rooms;
    }

    @AllArgsConstructor
    private static class Room {
        private String name;
        private int sectorId;
        private String checksum;
    }

}
