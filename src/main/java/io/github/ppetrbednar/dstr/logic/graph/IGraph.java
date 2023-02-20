package io.github.ppetrbednar.dstr.logic.graph;

import java.util.*;

public interface IGraph<K, V extends Comparable<V>, E extends Comparable<E>> {
    void clear();

    boolean isEmpty();

    int size();

    void addVertex(Vertex<K, V, E> vertex);

    void addEdge(Edge<K, V, E> edge);

    void addEdge(K edgeKey, K leftVertexKey, K rightVertexKey, E value);

    Vertex<K, V, E> removeVertex(K key);

    Edge<K, V, E> removeEdge(K key);

    Vertex<K, V, E> getVertex(K key);

    Edge<K, V, E> getEdge(K key);

    Map<K, Vertex<K, V, E>> getVertices();

    Map<K, Edge<K, V, E>> getEdges();
}

