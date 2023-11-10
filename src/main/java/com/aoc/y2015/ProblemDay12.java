package com.aoc.y2015;

import com.aoc.ProblemDay;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;

import java.util.Iterator;

public class ProblemDay12 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        JsonNode input = parse(scanner.nextLine());
        return solve(input, true, 0);
    }

    @Override
    protected Integer solveB() {
        JsonNode input = parse(scanner.nextLine());
        return solve(input, false, 0);
    }

    protected int solve(String str, boolean ignoreRed) {
        return solve(parse(str), ignoreRed, 0);
    }

    private int solve(JsonNode node, boolean ignoreRed, int sum) {
        if (node.isArray()) {
            ArrayNode array = (ArrayNode) node;
            Iterator<JsonNode> it = array.elements();
            while (it.hasNext()) {
                sum = solve(it.next(), ignoreRed, sum);
            }
        } else if (node.isObject()) {
            Iterator<JsonNode> it = node.elements();
            while (it.hasNext() && !ignoreRed) {
                JsonNode child = it.next();
                if (child.isTextual() && child.asText().equals("red")) {
                    return sum;
                }
            }
            it = node.elements();
            while (it.hasNext()) {
                sum = solve(it.next(), ignoreRed, sum);
            }
        }
        return sum + node.asInt();
    }

    @SneakyThrows
    private JsonNode parse(String str) {
        return new ObjectMapper().readTree(str);
    }
    
}
