package com.aoc;

import com.google.common.collect.Range;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Line2D {

    private Point2D p1;
    private Point2D p2;

    public Direction direction() {
        Point2D subtraction = p1.minus(p2);
        if (subtraction.getX() == 0 && subtraction.getY() == 0) {
            return Direction.POINT;
        } else if (subtraction.getX() != 0 && subtraction.getY() == 0) {
            return Direction.HORIZONTAL;
        } else if (subtraction.getX() == 0 && subtraction.getY() != 0) {
            return Direction.VERTICAL;
        } else {
            return Direction.DIAGONAL;
        }
    }

    public Range<Integer> xRange() {
        return Range.closed(
                Math.min(p1.getX(), p2.getX()),
                Math.max(p1.getX(), p2.getX()));
    }

    public Range<Integer> yRange() {
        return Range.closed(
                Math.min(p1.getY(), p2.getY()),
                Math.max(p1.getY(), p2.getY()));
    }

    public enum Direction {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL,
        POINT
    }

}
