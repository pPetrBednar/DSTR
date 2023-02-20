package io.github.ppetrbednar.dstr.logic.structures;

import io.github.ppetrbednar.dstr.logic.railway.Pathfinder;

import java.util.LinkedList;
import java.util.List;

public class Vertex {
    private final String key;
    private final List<Edge> connections;
    private final Pathfinder values;

    public Vertex(String key) {
        this.key = key;
        connections = new LinkedList<>();
        values = new Pathfinder();
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

    public Pathfinder getValues() {
        return values;
    }
}
