package com.aoc;

import com.google.common.collect.Range;
import lombok.Getter;

public class Diamond2D {

    @Getter private final int top;
    @Getter private final int bottom;
    @Getter private final int left;
    @Getter private final int right;

    private final Point2D center;

    private final Range<Integer> vertical;
    private final Range<Integer> horizontal;

    public Diamond2D(Point2D center, int centerToEdge) {
        this.top = center.getY() - centerToEdge;
        this.bottom = center.getY() + centerToEdge;
        this.left = center.getX() - centerToEdge;
        this.right = center.getX() + centerToEdge;
        this.center = center;
        this.vertical = Range.closed(top, bottom);
        this.horizontal = Range.closed(left, right);
    }

    public Range<Integer> xRangeAtRow(int y) {
        int vDist = Math.abs(center.getY() - y);
        return Range.closed(left + vDist, right - vDist);
    }

    public Range<Integer> yRangeAtCol(int x) {
        int hDist = Math.abs(center.getX() - x);
        return Range.closed(top + hDist, bottom - hDist);
    }

    public boolean intersects(int x, int y) {
        if (!horizontal.contains(x)) {
            return false;
        }
        if (!vertical.contains(y)) {
            return false;
        }
        return xRangeAtRow(y).contains(x) && yRangeAtCol(x).contains(y);
    }

}
