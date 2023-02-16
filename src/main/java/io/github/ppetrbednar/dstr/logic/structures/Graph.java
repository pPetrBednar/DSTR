package io.github.ppetrbednar.dstr.logic.structures;

import java.util.HashMap;
import java.util.Map;

public class Graph implements IGraph {
    private HashMap<String, Vertex> vertices;
    private HashMap<String, Edge> edges;

    public Graph() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    @Override
    public void clear() {
        vertices.clear();
        edges.clear();
    }

    @Override
    public boolean isEmpty() {
        return vertices.isEmpty() & edges.isEmpty();
    }

    @Override
    public int size() {
        return vertices.size() + edges.size();
    }

    @Override
    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getKey(), vertex);
    }

    @Override
    public void addEdge(Edge edge) {
        edges.put(edge.getKey(), edge);
    }

    @Override
    public Vertex removeVertex(String key) {
        Vertex vertex = vertices.remove(key);
        vertex.getConnections().forEach(edge -> {
            edges.remove(edge.getKey()).clear(vertex);
        });
        return vertex;
    }

    @Override
    public Edge removeEdge(String key) {
        Edge edge = edges.remove(key);
        edge.clear();
        return edge;
    }

    @Override
    public Vertex getVertex(String key) {
        return vertices.get(key);
    }

    @Override
    public Edge getEdge(String key) {
        return edges.get(key);
    }

    @Override
    public Map<String, Vertex> getVertices() {
        return vertices;
    }

    @Override
    public Map<String, Edge> getEdges() {
        return edges;
    }
}
