package com.aoc.y2015;

import com.aoc.ProblemDay;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProblemDay19 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<Pair<String, String>> transformations = parseTransformations();
        String molecule = parseMolecule();
        Set<String> solutions = solveA(transformations, molecule);
        return solutions.size();
    }

    @Override
    protected Integer solveB() {
        // Shrug
        // https://www.reddit.com/r/adventofcode/comments/3xflz8/comment/cy4f5w8/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
        parseTransformations();
        String target = parseMolecule();
        Pattern upper = Pattern.compile("[A-Z]");
        Pattern rn = Pattern.compile("Rn");
        Pattern ar = Pattern.compile("Ar");
        Pattern y = Pattern.compile("Y");
        return (int) (upper.matcher(target).results().count()
                - rn.matcher(target).results().count()
                - ar.matcher(target).results().count()
                - 2 * y.matcher(target).results().count()
                - 1);
    }

    private static Set<String> solveA(List<Pair<String, String>> transformations, String molecule) {
        Set<String> solutions = new HashSet<>();
        for (Pair<String, String> transformation : transformations) {
            Pattern regex = Pattern.compile(transformation.getKey());
            Matcher matcher = regex.matcher(molecule);
            matcher.results().forEach(r -> {
                StringBuilder sb = new StringBuilder(molecule);
                solutions.add(sb.replace(r.start(), r.end(), transformation.getValue()).toString());
            });
        }
        return solutions;
    }

    private List<Pair<String, String>> parseTransformations() {
        List<Pair<String, String>> transformations = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (StringUtils.isBlank(line)) {
                break;
            }
            String[] parts = line.split(" => ");
            transformations.add(ImmutablePair.of(parts[0], parts[1]));
        }
        return transformations;
    }

    private String parseMolecule() {
        String molecule = "";
        while (scanner.hasNextLine()) {
            molecule = scanner.nextLine();
            break;
        }
        return molecule;
    }

}
