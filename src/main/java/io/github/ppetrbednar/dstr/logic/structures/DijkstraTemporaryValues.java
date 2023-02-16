package io.github.ppetrbednar.dstr.logic.structures;

import java.util.LinkedList;
import java.util.List;

public class DijkstraTemporaryValues {
    private List<Vertex> shortestPath;
    private int distance;

    public DijkstraTemporaryValues() {
        this.shortestPath = new LinkedList<>();
        this.distance = Integer.MAX_VALUE;
    }

    public void clear() {
        shortestPath.clear();
        distance = Integer.MAX_VALUE;
    }

    public List<Vertex> getShortestPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
