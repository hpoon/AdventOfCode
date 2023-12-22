package com.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class CubeSet3D {

    private List<Point3D> cubes;

    public int minZ() {
        return cubes.stream()
                .map(Point3D::getZ)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Empty set"));
    }

    public int maxZ() {
        return cubes.stream()
                .map(Point3D::getZ)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Empty set"));
    }

    public int height() {
        return maxZ() - minZ() + 1;
    }

}
