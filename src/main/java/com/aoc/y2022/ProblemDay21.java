package com.aoc.y2022;

import com.aoc.ProblemDay;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.HashMap;
import java.util.Map;

public class ProblemDay21 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        Map<String, String> nodeToOperationStrings = parse();
        String node = "root";
        return solveA(node, nodeToOperationStrings, false).getNumeratorAsLong();
    }

    @Override
    public Long solveB() {
        Map<String, String> nodeToOperationStrings = parse();
        String node = "root";
        String[] operands = nodeToOperationStrings.get(node).split("\\+");

        // The right side is the side that has no variable
        BigFraction left = solveA(operands[0], nodeToOperationStrings, true);
        BigFraction right = solveA(operands[1], nodeToOperationStrings, true);

        BigFraction ans;
        if (left == null) {
            ans = solveB(operands[0], right, nodeToOperationStrings);
        } else {
            ans = solveB(operands[1], left, nodeToOperationStrings);
        }

        nodeToOperationStrings.put("humn", String.valueOf(ans.doubleValue()));
        left = solveA(operands[0], nodeToOperationStrings, false);
        right = solveA(operands[1], nodeToOperationStrings, false);
        System.out.println(ans);
        System.out.println(left.equals(right));
        return ans.getNumeratorAsLong();
    }

    private BigFraction solveB(String node,
                               BigFraction target,
                               Map<String, String> nodeToOperationStrings) {
        String str = nodeToOperationStrings.get(node);
        String[] operands = str.split("[+\\-*/]");
        if (operands.length > 1) {
            BigFraction left = solveA(operands[0], nodeToOperationStrings, true);
            BigFraction right = solveA(operands[1], nodeToOperationStrings, true);
            if (left == null) {
                if (str.contains("+")) {
                    return solveB(operands[0], target.subtract(right), nodeToOperationStrings);
                } else if (str.contains("-")) {
                    return solveB(operands[0], target.add(right), nodeToOperationStrings);
                } else if (str.contains("*")) {
                    return solveB(operands[0], target.divide(right), nodeToOperationStrings);
                } else if (str.contains("/")) {
                    return solveB(operands[0], target.multiply(right), nodeToOperationStrings);
                } else {
                    throw new RuntimeException("Shouldn't go here");
                }
            } else {
                if (str.contains("+")) {
                    return solveB(operands[1], target.subtract(left), nodeToOperationStrings);
                } else if (str.contains("-")) {
                    return solveB(operands[1], left.subtract(target), nodeToOperationStrings);
                } else if (str.contains("*")) {
                    return solveB(operands[1], target.divide(left), nodeToOperationStrings);
                } else if (str.contains("/")) {
                    return solveB(operands[1], left.divide(target), nodeToOperationStrings);
                } else {
                    throw new RuntimeException("Shouldn't go here");
                }
            }
        } else {
            return target;
        }
    }

    BigFraction solveA(String node,
                       Map<String, String> nodeToOperationStrings,
                       boolean solveForUnknown) {
        String str = nodeToOperationStrings.get(node);
        String[] operands = str.split("[+\\-*/]");
        if (operands.length > 1) {
            BigFraction left = solveA(operands[0], nodeToOperationStrings, solveForUnknown);
            BigFraction right = solveA(operands[1], nodeToOperationStrings, solveForUnknown);
            if (str.contains("+")) {
                return (left == null || right == null) ? null : left.add(right);
            } else if (str.contains("-")) {
                return (left == null || right == null) ? null : left.subtract(right);
            } else if (str.contains("*")) {
                return (left == null || right == null) ? null : left.multiply(right);
            } else if (str.contains("/")) {
                return (left == null || right == null) ? null : left.divide(right);
            } else {
                throw new RuntimeException("Shouldn't go here");
            }
        } else {
            return (solveForUnknown && node.equals("humn")) ? null : new BigFraction(str.contains(".") ? Double.parseDouble(str) : Long.parseLong(str));
        }
    }

    private Map<String, String> parse() {
        Map<String, String> nodeToOperationStrings = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] strs = scanner.nextLine().split(": ");
            nodeToOperationStrings.put(strs[0], strs[1].replace(" ", ""));
        }
        return nodeToOperationStrings;
    }

}
