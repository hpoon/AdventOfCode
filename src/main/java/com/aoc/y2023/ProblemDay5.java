package com.aoc.y2023;

import com.aoc.ProblemDay;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemDay5 extends ProblemDay<Long, Long> {

    @Override
    public Long solveA() {
        List<Long> seeds = seedsA();
        Map<Range<Long>, Range<Long>> seedToSoil = range("seed-to-soil map:");
        Map<Range<Long>, Range<Long>> soilToFertilizer = range("soil-to-fertilizer map:");
        Map<Range<Long>, Range<Long>> fertilizerToWater = range("fertilizer-to-water map:");
        Map<Range<Long>, Range<Long>> waterToLight = range("water-to-light map:");
        Map<Range<Long>, Range<Long>> lightToTemperature = range("light-to-temperature map:");
        Map<Range<Long>, Range<Long>> temperatureToHumidity = range("temperature-to-humidity map:");
        Map<Range<Long>, Range<Long>> humidityToLocation = range("humidity-to-location map:");

        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            long soil = getA(seedToSoil, seed);
            long fertilizer = getA(soilToFertilizer, soil);
            long water = getA(fertilizerToWater, fertilizer);
            long light = getA(waterToLight, water);
            long temperature = getA(lightToTemperature, light);
            long humidity = getA(temperatureToHumidity, temperature);
            long location = getA(humidityToLocation, humidity);
            if (location < min) {
                min = location;
            }
        }
        return min;
    }

    @Override
    public Long solveB() {
        RangeSet<Long> seeds = seedsB();
        Map<Range<Long>, Range<Long>> seedToSoil = range("seed-to-soil map:");
        Map<Range<Long>, Range<Long>> soilToFertilizer = range("soil-to-fertilizer map:");
        Map<Range<Long>, Range<Long>> fertilizerToWater = range("fertilizer-to-water map:");
        Map<Range<Long>, Range<Long>> waterToLight = range("water-to-light map:");
        Map<Range<Long>, Range<Long>> lightToTemperature = range("light-to-temperature map:");
        Map<Range<Long>, Range<Long>> temperatureToHumidity = range("temperature-to-humidity map:");
        Map<Range<Long>, Range<Long>> humidityToLocation = range("humidity-to-location map:");
        return solveB(
                seeds,
                seedToSoil,
                soilToFertilizer,
                fertilizerToWater,
                waterToLight,
                lightToTemperature,
                temperatureToHumidity,
                humidityToLocation);
    }

    @VisibleForTesting
    protected Long solveB(RangeSet<Long> seeds, Map<Range<Long>, Range<Long>> seedToSoil, Map<Range<Long>, Range<Long>> soilToFertilizer, Map<Range<Long>, Range<Long>> fertilizerToWater, Map<Range<Long>, Range<Long>> waterToLight, Map<Range<Long>, Range<Long>> lightToTemperature, Map<Range<Long>, Range<Long>> temperatureToHumidity, Map<Range<Long>, Range<Long>> humidityToLocation) {
        RangeSet<Long> soils = getB(seeds, seedToSoil);
        RangeSet<Long> fertilizers = getB(soils, soilToFertilizer);
        RangeSet<Long> water = getB(fertilizers, fertilizerToWater);
        RangeSet<Long> light = getB(water, waterToLight);
        RangeSet<Long> temperature = getB(light, lightToTemperature);
        RangeSet<Long> humidity = getB(temperature, temperatureToHumidity);
        RangeSet<Long> location = getB(humidity, humidityToLocation);
        return location.span().lowerEndpoint();
    }

    @VisibleForTesting
    protected RangeSet<Long> getB(RangeSet<Long> srcSet,
                                  Map<Range<Long>, Range<Long>> dstMap) {
        RangeSet<Long> result = TreeRangeSet.create();
        Queue<Range<Long>> queue = new LinkedList<>();
        queue.addAll(srcSet.asRanges());
        while (!queue.isEmpty()) {
            Range<Long> src = queue.poll();
            boolean hasAnyConnection = dstMap.keySet()
                    .stream()
                    .filter(srcKey -> srcKey.isConnected(src))
                    .anyMatch(srcKey -> !srcKey.intersection(src).isEmpty());
            if (!hasAnyConnection) {
                result.add(src);
                continue;
            }
            for (Range<Long> srcKey : dstMap.keySet()) {
                if (srcKey.encloses(src)) {
                    Range<Long> dst = dstMap.get(srcKey);
                    long diff = dst.lowerEndpoint() - srcKey.lowerEndpoint();
                    result.add(Range.closedOpen(src.lowerEndpoint() + diff, src.upperEndpoint() + diff));
                    break;
                } else if (src.encloses(srcKey)) {
                    Range<Long> dst = dstMap.get(srcKey);
                    result.add(dst);
                    queue.add(Range.closedOpen(src.lowerEndpoint(), srcKey.lowerEndpoint()));
                    queue.add(Range.closedOpen(srcKey.upperEndpoint(), src.upperEndpoint()));
                    break;
                } else {
                    if (!srcKey.isConnected(src)) {
                        continue;
                    }
                    Range<Long> intersection = src.intersection(srcKey);
                    if (intersection.isEmpty()) {
                        continue;
                    }
                    Range<Long> dst = dstMap.get(srcKey);
                    long diff = dst.lowerEndpoint() - srcKey.lowerEndpoint();
                    // Left offset
                    if (src.lowerEndpoint() < srcKey.lowerEndpoint() && src.upperEndpoint() < srcKey.upperEndpoint()) {
                        result.add(Range.closedOpen(
                                intersection.lowerEndpoint() + diff,
                                src.upperEndpoint() + diff));
                        queue.add(Range.closedOpen(src.lowerEndpoint(), srcKey.lowerEndpoint()));
                    // Right offset
                    } else if (src.lowerEndpoint() > srcKey.lowerEndpoint() && src.upperEndpoint() > srcKey.upperEndpoint()) {
                        result.add(Range.closedOpen(
                                src.lowerEndpoint() + diff,
                                intersection.upperEndpoint() + diff));
                        queue.add(Range.closedOpen(srcKey.upperEndpoint(), src.upperEndpoint()));
                    } else {
                        if (src.upperEndpoint() < srcKey.upperEndpoint()) {
                            continue;
                        } else if (src.upperEndpoint() > srcKey.upperEndpoint()) {
                            result.add(Range.closedOpen(srcKey.upperEndpoint(), src.upperEndpoint()));
                        } else {
                            continue;
                        }
                    }
//                    result.asRanges().stream().filter(r -> r.lowerEndpoint() == 0).findAny().isPresent()
                }
            }
        }
        return result;
    }

    long getA(Map<Range<Long>, Range<Long>> map, long key) {
        for (Range<Long> range : map.keySet()) {
            if (range.contains(key)) {
                long diff = key - range.lowerEndpoint();
                return map.get(range).lowerEndpoint() + diff;
            }
        }
        return key;
    }

    List<Long> seedsA() {
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            if (tokens[0].equals("seeds:")) {
                return IntStream.range(1, tokens.length)
                        .mapToObj(i -> Long.parseLong(tokens[i]))
                        .collect(Collectors.toList());
            }
        }
        throw new RuntimeException("Shouldn't happen");
    }

    RangeSet<Long> seedsB() {
        RangeSet<Long> seeds = TreeRangeSet.create();
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            if (StringUtils.isBlank(str)) {
                break;
            }
            String[] tokens = str.split(" ");
            if (tokens[0].equals("seeds:")) {
                for (int i = 1; i < tokens.length; i += 2) {
                    long start = Long.parseLong(tokens[i]);
                    seeds.add(Range.closedOpen(start, start + Long.parseLong(tokens[i+1])));
                }
            }
        }
        return seeds;
    }

    Map<Range<Long>, Range<Long>> range(String token) {
        Map<Range<Long>, Range<Long>> map = new HashMap<>();
        boolean started = false;
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            if (StringUtils.isBlank(str)) {
                if (started) {
                    break;
                }
                continue;
            }
            if (str.equals(token)) {
                started = true;
                continue;
            }
            String[] tokens = str.split(" ");
            long range = Long.parseLong(tokens[2]);
            long src = Long.parseLong(tokens[1]);
            long dest = Long.parseLong(tokens[0]);
            map.put(Range.closedOpen(src, src + range), Range.closedOpen(dest, dest + range));
        }
        return map;
    }

}
