package com.datasite;

import java.util.Collection;

public class Ship {
    private final static int HORIZONTAL = 0;
    private final static int VERTICAL = 1;
    private String name;
    private int size;
    private int workingSize;
    private int x;
    private int y;
    private int direction;
    private boolean content[];

    public Ship(String name, int size, int x, int y, int direction) {
        if (!isValid(size, x, y, direction))
            throw  new IllegalArgumentException("Ship is not valid.");

        this.name = name;
        this.size = this.workingSize = size;
        this.x = x;
        this.y = y;
        this.direction = direction;

        content = new boolean[size];
        for (int i = 0; i < size; i++) {
            content[i] = true;
        }
    }

    public static boolean isValid(int tsize, int tx, int ty, int tdirection) {
        if (!(0 <= tx && tx < 10 && 0 <=  ty && ty < 10))
            return false;

        if (tdirection == VERTICAL) {
            return ty + tsize < 10;
        } else {
            return tx + tsize < 10;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }

    public void setSize(int size) {
        this.size = this.workingSize = size;
    }
    int getSize() {
        return size;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void flipDirection() {
        direction ^= 1;
    }

    public boolean contains(int tx, int ty) {
        if (direction == VERTICAL) {
            int dy = ty - y;
            return (tx == x) && (dy >= 0 && dy < size);
        } else {
            int dx = tx - x;
            return (ty == y) && (dx >= 0 && dx < size);
        }
    }

    public boolean isAliveXY(int tx, int ty) {
        if (!contains(tx, ty)) throw new IllegalArgumentException("Not lie in this ship.");

        if (direction == VERTICAL)
            return content[ty - y];
        else
            return content[tx - x];
    }

    public boolean collides(Ship other) {
        if (direction == VERTICAL) {
            for (int off = -1; off <= 1; off++)
                for (int ty = y - 1; ty <= y + size; ty++) {
                    if (other.contains(x + off, ty))
                        return true;
                }
        } else {
            for (int off = -1; off <= 1; off++)
                for (int tx = x - 1; tx <= x + size; tx++) {
                    if (other.contains(tx, y + off))
                        return true;
                }
        }

        return false;
    }

    public boolean hit(int tx, int ty) {
        if (!contains(tx, ty))
            return false;

        if (direction == VERTICAL)
            content[ty - y] = false;
        else
            content[tx - x] = false;

        return true;
    }

    public boolean isSank() {
        return workingSize == 0;
    }

    public boolean isOverlap(Ship o) {
        if (direction == VERTICAL) {
            for (int ty = 0; ty < size; ty++){
                if (o.contains(x, y + ty))
                    return true;
            }
        } else {
            for (int tx = 0; tx < size; tx++) {
                if (o.contains(x + tx, y))
                    return true;
            }
        }

        return false;
    }
}
