package com.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Point2DL {

    private long x;
    private long y;

    public Point2DL(Point2DL other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public Point2DL minus(Point2DL other) {
        return new Point2DL(x - other.x, y - other.y);
    }

    public Point2DL translate(Point2DL increment) {
        return new Point2DL(this.x + increment.getX(), this.y + increment.getY());
    }

    public long manhattanDistance(Point2DL other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

}
