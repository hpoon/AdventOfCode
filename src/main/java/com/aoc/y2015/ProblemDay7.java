package com.aoc.y2015;

import com.aoc.ProblemDay;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ProblemDay7 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        Map<String, Operation> nodeToOperation = parse();
        return solve(nodeToOperation);
    }

    @Override
    protected Integer solveB() {
        Map<String, Operation> nodeToOperation = parse();
        // Apply overrides for part B
        nodeToOperation.put(
                "b",
                new Operation(
                        OperationType.CONSTANT,
                        String.valueOf(solve(nodeToOperation)),
                        null));
        return solve(nodeToOperation);
    }

    private int solve(Map<String, Operation> nodeToOperation) {
        Map<String, Integer> memoization = new HashMap<>();
        return operate(nodeToOperation, "a", memoization);
    }

    private int operate(Map<String, Operation> nodeToOperation,
                        String node,
                        Map<String, Integer> memoization) {
        if (memoization.containsKey(node)) {
            return memoization.get(node);
        }
        if (StringUtils.isNumeric(node)) {
            int result = Integer.parseInt(node);
            memoization.put(node, result);
            return result;
        }
        Operation operation = nodeToOperation.get(node);
        OperationType type = operation.getType();
        String left = operation.getLeft();
        String right = operation.getRight();
        int result;
        switch (type) {
            case NOT:
                result = ~operate(nodeToOperation, left, memoization);
                break;
            case OR:
                result = operate(nodeToOperation, left, memoization) | operate(nodeToOperation, right, memoization);
                break;
            case AND:
                result = operate(nodeToOperation, left, memoization) & operate(nodeToOperation, right, memoization);
                break;
            case LSHIFT:
                result = operate(nodeToOperation, left, memoization) << Integer.parseInt(right);
                break;
            case RSHIFT:
                result = operate(nodeToOperation, left, memoization) >> Integer.parseInt(right);
                break;
            case ASSIGNMENT:
                result = operate(nodeToOperation, left, memoization);
                break;
            case CONSTANT:
                result = Integer.parseInt(left);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported bitwise operation");
        }
        memoization.put(node, result);
        return result;
    }

    private Map<String, Operation> parse() {
        Map<String, Operation> resultToOperation = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] components = line.split(" ");
            String result = components[components.length - 1];
            OperationType type;
            String left;
            String right = null;
            if (line.contains("NOT")) {
                type = OperationType.NOT;
                left = components[1];
            } else if (line.contains("OR")) {
                type = OperationType.OR;
                left = components[0];
                right = components[2];
            } else if (line.contains("AND")) {
                type = OperationType.AND;
                left = components[0];
                right = components[2];
            } else if (line.contains("LSHIFT")) {
                type = OperationType.LSHIFT;
                left = components[0];
                right = components[2];
            } else if (line.contains("RSHIFT")) {
                type = OperationType.RSHIFT;
                left = components[0];
                right = components[2];
            } else if (!StringUtils.isNumeric(components[0])) {
                type = OperationType.ASSIGNMENT;
                left = components[0];
            } else {
                type = OperationType.CONSTANT;
                left = components[0];
            }
            resultToOperation.put(result, new Operation(type, left, right));
        }
        return resultToOperation;
    }

    @Value
    private static class Operation {
        private final OperationType type;
        private final String left;
        private final String right;
    }

    private enum OperationType {
        NOT,
        OR,
        AND,
        LSHIFT,
        RSHIFT,
        CONSTANT,
        ASSIGNMENT
    }

}
