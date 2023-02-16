package io.github.ppetrbednar.dstr.logic.structures;

import java.util.HashMap;

public class Graph<K, V, E> implements SuperADTGraph<K, V, E> {
    private HashMap<K, Vertex<K, V, E>> vertices;
    private HashMap<K, Edge<K, V, E>> edges;

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
    public void addVertex(K key, Vertex<K, V, E> vertex) {
        vertices.put(key, vertex);
    }

    @Override
    public void addEdge(K edgeKey, Edge<K, V, E> edge, K vertexKeyLeft, K vertexKeyRight) {
        edge.setLink(vertices.get(vertexKeyLeft), vertices.get(vertexKeyRight));
        edges.put(edgeKey, edge);
    }

    @Override
    public Vertex<K, V, E> removeVertex(K key) {
        Vertex<K, V, E> vertex = vertices.remove(key);
        vertex.getConnections().forEach(edge -> {
            edges.remove(edge.getKey()).clear(vertex);
        });
        return vertex;
    }

    @Override
    public Edge<K, V, E> removeEdge(K key) {
        Edge<K, V, E> edge = edges.remove(key);
        edge.clear();
        return edge;
    }

    @Override
    public Vertex<K, V, E> getVertex(K key) {
        return vertices.get(key);
    }

    @Override
    public Edge<K, V, E> getEdge(K key) {
        return edges.get(key);
    }
}
