package com.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point2D {

    private int x;
    private int y;

    public Point2D minus(Point2D other) {
        return new Point2D(x - other.x, y - other.y);
    }

    public int manhattanDistance(Point2D other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

}
