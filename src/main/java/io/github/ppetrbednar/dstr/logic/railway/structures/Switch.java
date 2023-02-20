package io.github.ppetrbednar.dstr.logic.railway.structures;

import java.util.*;

public class Switch implements Comparable<Switch> {
    private final LinkedList<Direction> shortestPath;
    private final List<IllegalTransition> illegalPath;
    private Integer distance = Integer.MAX_VALUE;

    public Switch() {
        this.shortestPath = new LinkedList<>();
        this.illegalPath = new LinkedList<>();
    }

    public void clear() {
        shortestPath.clear();
        distance = Integer.MAX_VALUE;
    }

    public LinkedList<Direction> getShortestPath() {
        return shortestPath;
    }

    public List<IllegalTransition> getIllegalTransitions() {
        return illegalPath;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Switch o) {
        return distance - o.getDistance();
    }
}
