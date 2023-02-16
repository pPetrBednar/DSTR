package io.github.ppetrbednar.dstr.logic.structures;

import java.util.LinkedList;
import java.util.List;

public class Vertex {
    private final String key;
    private final List<Edge> connections;
    private final DijkstraTemporaryValues values;

    public Vertex(String key) {
        this.key = key;
        connections = new LinkedList<>();
        values = new DijkstraTemporaryValues();
    }

    public void clear() {
        connections.clear();
    }

    public String getKey() {
        return key;
    }

    public List<Edge> getConnections() {
        return connections;
    }

    public DijkstraTemporaryValues getValues() {
        return values;
    }
}
