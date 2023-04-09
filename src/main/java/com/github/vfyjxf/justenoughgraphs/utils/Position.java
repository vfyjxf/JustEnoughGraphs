package com.github.vfyjxf.justenoughgraphs.utils;


import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Position {

    public static final Position ORIGIN = new Position(0, 0);

    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    public Position subtract(Position other) {
        return new Position(x - other.x, y - other.y);
    }

    public Position add(int width, int height) {
        return new Position(x + width, y + height);
    }

    public Position addX(int x) {
        return new Position(this.x + x, y);
    }

    public Position addY(int y) {
        return new Position(x, this.y + y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .toString();
    }
}
