package com.aoc.y2022;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import lombok.Value;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemDay19 extends ProblemDay<Integer, Integer> {

    private static final int MAX_MINUTES_A = 24;
    private static final int MAX_MINUTES_B = 32;

    @Override
    public Integer solveA() {
        List<Blueprint> blueprints = parse(-1);
        int sum = 0;
        for (Blueprint blueprint : blueprints) {
            long max = dfs(blueprint, MAX_MINUTES_A);
            int id = blueprint.getId();
            sum += id * max;
        }
        return sum;
    }

    @Override
    public Integer solveB() {
        List<Blueprint> blueprints = parse(3);
        int product = 1;
        for (Blueprint blueprint : blueprints) {
            long max = dfs(blueprint, MAX_MINUTES_B);
            product *= max;
        }
        return product;
    }

    private long dfs(Blueprint blueprint, int maxMinutes) {
        State initialState = new State(
                ImmutableSortedMap.of(0, MineralType.ORE),
                ImmutableMap.of());
        Stack<State> stack = new Stack<>();
        stack.add(initialState);
        long maxGeodes = 0;
        Map<MineralType, Integer> maxRobotsNeeded = blueprint.maxRobotsNeeded();
        while (!stack.isEmpty()) {
            State state = stack.pop();
            Map<Integer, MineralType> timeline = state.getTimeline();
            int minute = timeline.keySet().stream().max(Comparator.naturalOrder()).orElseThrow();

            if (minute > maxMinutes) {
                continue;
            }

            Map<MineralType, Integer> resources = state.getResources();
            Map<MineralType, Long> robots = timeline
                    .entrySet()
                    .stream()
                    .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));

            // No point to continue if there's no way to get the max that assumes you build geode bot every minute
            int remaining = maxMinutes - minute;
            long geodeRobots = robots.getOrDefault(MineralType.GEODE, 0L);
            int theoreticalMax = resources.getOrDefault(MineralType.GEODE, 0)
                    + (int) Math.round(1.0 * (geodeRobots + geodeRobots + remaining - 1) / 2 * remaining);
            if (theoreticalMax < maxGeodes) {
                continue;
            }

            // What do we get if we don't build at all
            // How much minerals would have been collected up to the end
            if (resources.containsKey(MineralType.GEODE)) {
                long geodes = resources.get(MineralType.GEODE) + geodeRobots * (maxMinutes - minute);
                if (geodes > maxGeodes) {
                    maxGeodes = geodes;
                }
            }

            for (MineralType type : MineralType.values()) {
                Robot robot = blueprint.getRobot(type);
                Map<MineralType, Integer> cost = robot.getCost();
                // If you have the robot for it, you have access to the resources that it mines to build a thing
                Set<MineralType> requiredRobotsToBuild = cost.keySet();
                if (!robots.keySet().containsAll(requiredRobotsToBuild)) {
                    continue;
                }

                // How many turns to build a robot with current resourcing
                int turnsToBuild = robot.turnsToResource(resources, robots);
                Map<Integer, MineralType> updatedTimeline = new LinkedHashMap<>(timeline);

                // Cannot build on same turn, so add 1
                int nextMinute = minute + turnsToBuild + 1;

                // Don't build robot if we already have enough.  No limit for Geode robots
                if (!type.equals(MineralType.GEODE)
                        && robots.getOrDefault(type, 0L) == (long) maxRobotsNeeded.get(type)) {
                    continue;
                }

                updatedTimeline.put(nextMinute, type);

                // How much minerals would have been collected within those turns
                Map<MineralType, Integer> collectedResources = new HashMap<>(resources);
                robots.forEach((k, v) -> collectedResources.put(
                        k, (int)(collectedResources.getOrDefault(k, 0) + turnsToBuild * v)));

                // Build robot to subtract the resources
                Map<MineralType, Integer> updatedResources = robot.build(collectedResources);

                // Collect resources for current turn
                robots.forEach((k, v) -> updatedResources.put(
                        k, (int)(updatedResources.getOrDefault(k, 0) + v)));

                State next = new State(updatedTimeline, updatedResources);
                stack.add(next);
            }
        }
        return maxGeodes;
    }

    private List<Blueprint> parse(int num) {
        List<Blueprint> blueprints = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().replace(":", ".").split("\\. ");
            int id = Integer.parseInt(line[0].replace("Blueprint ", ""));
            Robot oreRobot = new Robot(
                    MineralType.ORE,
                    ImmutableMap.of(
                            MineralType.ORE,
                            Integer.parseInt(line[1]
                                    .replace("Each ore robot costs ", "")
                                    .replace(" ore", ""))));
            Robot clayRobot = new Robot(
                    MineralType.CLAY,
                    ImmutableMap.of(
                            MineralType.ORE,
                            Integer.parseInt(line[2]
                                    .replace("Each clay robot costs ", "")
                                    .replace(" ore", ""))));
            String[] obsidianLine = line[3].split(" ore and ");
            Robot obsidianRobot = new Robot(
                    MineralType.OBSIDIAN,
                    ImmutableMap.of(
                            MineralType.ORE,
                            Integer.parseInt(obsidianLine[0]
                                    .replace("Each obsidian robot costs ", "")),
                            MineralType.CLAY,
                            Integer.parseInt(obsidianLine[1]
                                    .replace(" clay", ""))));
            String[] geodeLine = line[4].split(" ore and ");
            Robot geodeRobot = new Robot(
                    MineralType.GEODE,
                    ImmutableMap.of(
                            MineralType.ORE,
                            Integer.parseInt(geodeLine[0]
                                    .replace("Each geode robot costs ", "")),
                            MineralType.OBSIDIAN,
                            Integer.parseInt(geodeLine[1]
                                    .replace(" obsidian.", ""))));
            blueprints.add(new Blueprint(id, ImmutableSet.of(oreRobot, clayRobot, obsidianRobot, geodeRobot).stream()
                    .collect(Collectors.toMap(Robot::getMineralType, Function.identity()))));
            if (blueprints.size() == num) {
                break;
            }
        }
        return blueprints;
    }

    private enum MineralType {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    @Value
    private static class Robot {

        private MineralType mineralType;

        private Map<MineralType, Integer> cost;

        public int turnsToResource(Map<MineralType, Integer> resources,
                                   Map<MineralType, Long> robots) {
            Map<MineralType, Integer> costToBuild = getCost();
            Map<MineralType, Integer> turnsForResource = new HashMap<>();
            for (Map.Entry<MineralType, Integer> entry : costToBuild.entrySet()) {
                MineralType type = entry.getKey();
                int cost = entry.getValue();
                int onHand = resources.getOrDefault(type, 0);
                int needed = cost - onHand;
                long producers = robots.get(type);
                if (needed < 0) {
                    // If you already have resources, then you can do it now
                    turnsForResource.put(type, 0);
                } else {
                    turnsForResource.put(type, (int)(Math.ceil(1.0 * needed / producers)));
                }
            }
            return turnsForResource.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        }

        public Map<MineralType, Integer> build(Map<MineralType, Integer> resources) {
            return resources.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> {
                                MineralType type = e.getKey();
                                return resources.getOrDefault(type, 0) - cost.getOrDefault(type, 0);
                            }));
        }

    }

    @Value
    private static class Blueprint {

        private int id;

        private Map<MineralType, Robot> mineralTypeToRobot;

        public Robot getRobot(MineralType type) {
            return mineralTypeToRobot.get(type);
        }

        public Map<MineralType, Integer> maxRobotsNeeded() {
            Map<MineralType, Map<MineralType, Integer>> costs = mineralTypeToRobot
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getCost()));
            return Arrays.stream(MineralType.values())
                    .filter(type -> !type.equals(MineralType.GEODE))
                    .map(type -> ImmutablePair.of(type, costs.values().stream()
                            .map(map -> map.getOrDefault(type, 0))
                            .max(Comparator.naturalOrder()).orElse(0)))
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        }

    }

    @Value
    private static class State {

        private Map<Integer, MineralType> timeline;
        private Map<MineralType, Integer> resources;

    }

}
