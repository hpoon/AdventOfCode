package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.collect.*;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Predicate;

public class ProblemDay19 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        Pair<Map<String, Workflow>, List<Part>> inputs = parse();
        Map<String, Workflow> workflows = inputs.getKey();
        List<Part> parts = inputs.getValue();
        return solveA(workflows, parts);
    }

    @Override
    public Long solveB() {
        Pair<Map<String, Workflow>, List<Part>> inputs = parse();
        Map<String, Workflow> workflows = inputs.getKey();
        return solveB(workflows);
    }

    private long solveA(Map<String, Workflow> workflows, List<Part> parts) {
        int ratings = 0;
        for (Part part : parts) {
            String out = "in";
            while (!ImmutableSet.of("A", "R").contains(out)) {
                Workflow wf = workflows.get(out).clone();
                Queue<Pair<Predicate<Part>, String>> queue = wf.getPredicates();
                while (!queue.isEmpty()) {
                    Pair<Predicate<Part>, String> pair = queue.poll();
                    Predicate<Part> predicate = pair.getKey();
                    if (predicate.test(part)) {
                        out = pair.getValue();
                        break;
                    }
                    if (queue.isEmpty()) {
                        out = wf.getFail();
                    }
                }
            }
            if ("A".equals(out)) {
                ratings += part.rating();
            }
        }
        return ratings;
    }

    private long solveB(Map<String, Workflow> workflows) {
        Map<Character, Range<Integer>> startingRanges = new HashMap<>();
        startingRanges.put('x', Range.closed(1, 4000));
        startingRanges.put('m', Range.closed(1, 4000));
        startingRanges.put('a', Range.closed(1, 4000));
        startingRanges.put('s', Range.closed(1, 4000));
        State start = new State("in", startingRanges);
        Queue<State> queue = new LinkedList<>();
        queue.add(start);
        List<Map<Character, Range<Integer>>> sols = new ArrayList<>();
        long solutions = 0;
        while (!queue.isEmpty()) {
            State state = queue.poll();
            String wfStr = state.getWorkflowName();
            Map<Character, Range<Integer>> ranges = state.getValues();
            if ("A".equals(wfStr)) {
                Range<Integer> x = ranges.get('x');
                Range<Integer> m = ranges.get('m');
                Range<Integer> a = ranges.get('a');
                Range<Integer> s = ranges.get('s');
                sols.add(ranges);
                solutions += (long) (x.upperEndpoint() - x.lowerEndpoint() + 1)
                        * (m.upperEndpoint() - m.lowerEndpoint() + 1)
                        * (a.upperEndpoint() - a.lowerEndpoint() + 1)
                        * (s.upperEndpoint() - s.lowerEndpoint() + 1);
                continue;
            }
            if ("R".equals(wfStr)) {
                continue;
            }
            Workflow wf = workflows.get(wfStr);
            List<Condition> conditions = wf.getConditions();
            Map<Character, Range<Integer>> successRanges = new HashMap<>(ranges);
            Map<Character, Range<Integer>> failRanges = new HashMap<>(ranges);
            for (int i = 0; i < conditions.size(); i++) {
                Condition condition = conditions.get(i);
                char var = condition.getVar();
                char conditional = condition.getConditional();
                int threshold = condition.getThreshold();
                String success = condition.getSuccess();
                Range<Integer> currentSuccessRange;
                Range<Integer> currentFailRange;
                switch (conditional) {
                    case '<':
                        currentSuccessRange = Range.closed(ranges.get(var).lowerEndpoint(), threshold - 1);
                        currentFailRange = Range.closed(ranges.get(var).lowerEndpoint(), threshold - 1);
                        successRanges.put(
                                var,
                                i == 0
                                    ? successRanges.get(var).intersection(currentSuccessRange)
                                    : failRanges.get(var).intersection(currentFailRange));
                        failRanges.put(
                                var,
                                failRanges.get(var).intersection(Range.closed(threshold, (ranges.get(var).upperEndpoint()))));
                        queue.add(new State(success, new HashMap<>(successRanges)));
                        break;
                    case '>':
                        currentSuccessRange = Range.closed(threshold + 1, ranges.get(var).upperEndpoint());
                        currentFailRange = Range.closed(ranges.get(var).lowerEndpoint(), threshold);
                        successRanges.put(
                                var,
                                i == 0
                                    ? successRanges.get(var).intersection(currentSuccessRange)
                                    : failRanges.get(var).intersection(currentSuccessRange));
                        failRanges.put(
                                var,
                                failRanges.get(var).intersection(currentFailRange));
                        queue.add(new State(success, new HashMap<>(successRanges)));
                        break;
                    default: throw new RuntimeException(String.format("Unmatched conditional: %c", conditional));
                }
                successRanges = new HashMap<>(failRanges);
                if (i == conditions.size() - 1) {
                    queue.add(new State(wf.getFail(), new HashMap<>(failRanges)));
                }
            }
        }
        return solutions;
    }

    private Pair<Map<String, Workflow>, List<Part>> parse() {
        Map<String, Workflow> workflows = new HashMap<>();
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            if (StringUtils.isBlank(str)) {
                break;
            }
            String[] tokens = str.split("\\{");
            String name = tokens[0];
            tokens = tokens[1]
                    .replace("{", "")
                    .replace("}", "")
                    .replace(":", ",")
                    .split(",");
            Queue<Pair<Predicate<Part>, String>> predicates = new LinkedList<>();
            List<Condition> conditions = new ArrayList<>();
            for (int i = 0; i < tokens.length - 2; i += 2) {
                String statement = tokens[i];
                String success = tokens[i+1];
                char var = statement.charAt(0);
                char conditional = statement.charAt(1);
                int threshold = Integer.parseInt(statement.substring(2));
                Predicate<Part> predicate = part -> {
                    int value = part.get(var);
                    switch (conditional) {
                        case '>': return value > threshold;
                        case '<': return value < threshold;
                        default: throw new RuntimeException(String.format("Unmatched conditional: %c", conditional));
                    }
                };
                predicates.add(ImmutablePair.of(predicate, success));
                conditions.add(new Condition(var, conditional, threshold, success));
            }
            String fail = tokens[tokens.length - 1];
            workflows.put(name, new Workflow(name, predicates, conditions, fail));
        }

        List<Part> parts = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine()
                    .replace("{", "")
                    .replace("}", "")
                    .split(",");
            Map<Character, Integer> values = new HashMap<>();
            for (String token : tokens) {
                values.put(token.charAt(0), Integer.parseInt(token.substring(2)));
            }
            parts.add(new Part(values.get('x'), values.get('m'), values.get('a'), values.get('s')));
        }

        return ImmutablePair.of(workflows, parts);
    }

    @Value
    @EqualsAndHashCode
    private static class State {
        private String workflowName;
        private Map<Character, Range<Integer>> values;
    }

    @Value
    @EqualsAndHashCode
    private static class Part {
        private int x;
        private int m;
        private int a;
        private int s;

        public int get(char var) {
            switch (var) {
                case 'x': return x;
                case 'm': return m;
                case 'a': return a;
                case 's': return s;
                default: throw new RuntimeException(String.format("Unmatched char: %c", var));
            }
        }

        public int rating() {
            return x + m + a + s;
        }
    }

    @Value
    private static class Workflow {
        private String name;
        private Queue<Pair<Predicate<Part>, String>> predicates;
        private List<Condition> conditions;
        private String fail;

        public Workflow clone() {
            return new Workflow(name, new LinkedList<>(predicates), new ArrayList<>(conditions), fail);
        }
    }

    @Value
    private static class Condition {
        private char var;
        private char conditional;
        private int threshold;
        private String success;
    }

}
