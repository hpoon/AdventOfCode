package com.aoc.y2023;

import com.aoc.ProblemDayTest;
import com.google.common.collect.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;

public class ProblemDay5Test implements ProblemDayTest {

    private ProblemDay5 problem;

    @BeforeEach
    public void before() {
        problem = new ProblemDay5();
    }

    @Test
    @Override
    public void testSolveA() {
        assertThat(problem.solveA()).isEqualTo(1181555926);
    }

    @Test
    @Override
    public void testSolveB() {
        assertThat(problem.solveB()).isEqualTo(37806486);
    }

    @Test
    public void testSolveB_simple() {
        RangeSet<Long> seeds = seeds("79 14 55 13");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of(
                "50 98 2", "52 50 48"));
        Map<Range<Long>, Range<Long>> soilToFertilizer = range(ImmutableList.of(
                "0 15 37", "37 52 2", "39 0 15"));
        Map<Range<Long>, Range<Long>> fertilizerToWater = range(ImmutableList.of(
                "49 53 8", "0 11 42", "42 0 7", "57 7 4"));
        Map<Range<Long>, Range<Long>> waterToLight = range(ImmutableList.of(
                "88 18 7", "18 25 70"));
        Map<Range<Long>, Range<Long>> lightToTemperature = range(ImmutableList.of(
                "45 77 23", "81 45 19", "68 64 13"));
        Map<Range<Long>, Range<Long>> temperatureToHumidity = range(ImmutableList.of(
                "0 69 1", "1 0 69"));
        Map<Range<Long>, Range<Long>> humidityToLocation = range(ImmutableList.of(
                "60 56 37", "56 93 4"));

        long actual = problem.solveB(
                seeds,
                seedToSoil,
                soilToFertilizer,
                fertilizerToWater,
                waterToLight,
                lightToTemperature,
                temperatureToHumidity,
                humidityToLocation);
        assertThat(actual).isEqualTo(46);
    }

    @Test
    public void testGetB_noOverlaps() {
        RangeSet<Long> seeds = seeds("364807853 408612163");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of("2069473506 3732587455 1483883"));
        RangeSet<Long> actual = problem.getB(seeds, seedToSoil);
        RangeSet<Long> expected = TreeRangeSet.create(ImmutableSet.of(
                Range.closedOpen(364807853L, 364807853L + 408612163L)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetB_seedsCompletelyEnclosesOther() {
        RangeSet<Long> seeds = seeds("364807853 408612163");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of("0 568494408 18343754"));
        RangeSet<Long> actual = problem.getB(seeds, seedToSoil);
        RangeSet<Long> expected = TreeRangeSet.create(ImmutableSet.of(
                Range.closedOpen(364807853L, 568494408L),
                Range.closedOpen(0L, 18343754L),
                Range.closedOpen(586838162L, 773420016L)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetB_otherCompletelyEnclosesSeeds() {
        RangeSet<Long> seeds = seeds("3692780593 17215731");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of("605476380 3544150009 188437446"));
        RangeSet<Long> actual = problem.getB(seeds, seedToSoil);
        RangeSet<Long> expected = TreeRangeSet.create(ImmutableSet.of(
                Range.closedOpen(3692780593L - 2938673629L, 3709996324L - 2938673629L)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetB_seedsLeftOffset() {
        RangeSet<Long> seeds = seeds("364807853 408612163");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of("3100147259 762191092 135543997"));
        RangeSet<Long> actual = problem.getB(seeds, seedToSoil);
        RangeSet<Long> expected = TreeRangeSet.create(ImmutableSet.of(
                Range.closedOpen(364807853L, 762191092L),
                Range.closedOpen(762191092L + 2337956167L, 773420016L + 2337956167L)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetB_seedsRightOffset() {
        RangeSet<Long> seeds = seeds("2593569946 345762334");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of("2821869347 2355540461 278277912"));
        RangeSet<Long> actual = problem.getB(seeds, seedToSoil);
        RangeSet<Long> expected = TreeRangeSet.create(ImmutableSet.of(
                Range.closedOpen(2593569946L + 466328886L, 2633818373L + 466328886L),
                Range.closedOpen(2633818373L, 2939332280L)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetB_seeds_rightOffset_seedsEnclosesOther() {
        RangeSet<Long> seeds = seeds("364807853 408612163");
        Map<Range<Long>, Range<Long>> seedToSoil = range(ImmutableList.of(
                "391285622 257757572 195552540",
                "335002083 512210869 56283539"));
        RangeSet<Long> actual = problem.getB(seeds, seedToSoil);
        RangeSet<Long> expected = TreeRangeSet.create(ImmutableSet.of(
                Range.closedOpen(364807853L + 133528050L, 453310112L + 133528050L),
                Range.closedOpen(453310112L, 512210869L),
                Range.closedOpen(512210869L - 177208786L, 568494408L - 177208786L),
                Range.closedOpen(568494408L, 773420016L)));
        assertThat(actual).isEqualTo(expected);
    }

    private RangeSet<Long> seeds(String str) {
        List<Long> tokens = Arrays.stream(str.split(" "))
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());
        RangeSet<Long> result = TreeRangeSet.create();
        for (int i = 0; i < tokens.size(); i += 2) {
            result.add(Range.closedOpen(tokens.get(i), tokens.get(i) + tokens.get(i + 1)));
        }
        return result;
    }

    private Map<Range<Long>, Range<Long>> range(List<String> strs) {
        Map<Range<Long>, Range<Long>> result = new HashMap<>();
        for (String str : strs) {
            List<Long> tokens = Arrays.stream(str.split(" "))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .collect(Collectors.toList());
            long diff = tokens.get(2);
            long srcStart = tokens.get(1);
            long dstStart = tokens.get(0);
            result.put(
                    Range.closedOpen(srcStart, srcStart + diff),
                    Range.closedOpen(dstStart, dstStart + diff));
        }
        return result;
    }

}
