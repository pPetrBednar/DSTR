package io.github.ppetrbednar.dstr.logic.graph;

import java.util.HashMap;
import java.util.Map;

public class Graph<K, V extends Comparable<V>, E extends Comparable<E>> implements IGraph<K, V, E> {
    private HashMap<K, Vertex<K, V, E>> vertices;
    private HashMap<K, Edge<K, V, E>> edges;

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
    public void addVertex(Vertex<K, V, E> vertex) {
        vertices.put(vertex.getKey(), vertex);
    }

    @Override
    public void addEdge(K edgeKey, K leftVertexKey, K rightVertexKey, E value) {
        var left = getVertex(leftVertexKey);
        var right = getVertex(rightVertexKey);

        edges.put(edgeKey, new Edge<>(edgeKey, left, right, value));
    }

    @Override
    public void addEdge(Edge<K, V, E> edge) {
        edges.put(edge.getKey(), edge);
    }

    @Override
    public Vertex<K, V, E> removeVertex(K key) {
        var vertex = vertices.remove(key);
        vertex.getConnections().forEach(edge -> {
            edges.remove(edge.getKey()).clear(vertex);
        });
        return vertex;
    }

    @Override
    public Edge<K, V, E> removeEdge(K key) {
        var edge = edges.remove(key);
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

    @Override
    public Map<K, Vertex<K, V, E>> getVertices() {
        return vertices;
    }

    @Override
    public Map<K, Edge<K, V, E>> getEdges() {
        return edges;
    }
}
