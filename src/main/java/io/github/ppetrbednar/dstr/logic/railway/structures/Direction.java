package io.github.ppetrbednar.dstr.logic.railway.structures;

public record Direction(Rail rail, Switch point) implements Comparable<Direction> {
    @Override
    public int compareTo(Direction direction) {
        return rail.compareTo(direction.rail);
    }
}
