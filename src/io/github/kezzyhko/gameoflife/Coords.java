package io.github.kezzyhko.gameoflife;

public class Coords implements Comparable<Coords> {
    public int x, y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Coords(Coords p) {
        this(p.x, p.y);
    }

    public int compareTo(Coords that) {
        return (this.y == that.y) ? (this.x - that.x) : (this.y - that.y);
    }
    public String toString() {
        return "("+x+", "+y+")";
    }
}
