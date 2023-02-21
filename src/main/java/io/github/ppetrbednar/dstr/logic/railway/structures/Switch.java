package io.github.ppetrbednar.dstr.logic.railway.structures;

import java.util.*;

public class Switch implements Comparable<Switch> {
    private final String key;
    private final PriorityQueue<Rail> connections;
    private final LinkedList<Direction> shortestPath;
    private final List<IllegalTransition> illegalPath;
    private Integer distance = Integer.MAX_VALUE;

    public Switch(String key) {
        this.key = key;
        connections = new PriorityQueue<>();
        shortestPath = new LinkedList<>();
        illegalPath = new LinkedList<>();
    }

    public void clear() {
        shortestPath.clear();
        distance = Integer.MAX_VALUE;
    }

    public String getKey() {
        return key;
    }

    public PriorityQueue<Rail> getConnections() {
        return connections;
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
