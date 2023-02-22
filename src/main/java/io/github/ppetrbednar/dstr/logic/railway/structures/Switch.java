package io.github.ppetrbednar.dstr.logic.railway.structures;

import java.util.*;

public class Switch implements Comparable<Switch> {
    private final String key;
    private final LinkedList<Direction> shortestPath;
    private final LinkedList<Transition> illegalTransitions;
    private final HashMap<Transition, LinkedList<Direction>> reversalPaths;
    private Integer distance = Integer.MAX_VALUE;

    public Switch(String key) {
        this.key = key;
        shortestPath = new LinkedList<>();
        illegalTransitions = new LinkedList<>();
        reversalPaths = new HashMap<>();
    }

    public void clear() {
        shortestPath.clear();
        reversalPaths.clear();
        distance = Integer.MAX_VALUE;
    }

    public String getKey() {
        return key;
    }

    public LinkedList<Direction> getShortestPath() {
        return shortestPath;
    }

    public LinkedList<Transition> getIllegalTransitions() {
        return illegalTransitions;
    }

    public HashMap<Transition, LinkedList<Direction>> getReversalPaths() {
        return reversalPaths;
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
