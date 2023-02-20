package io.github.ppetrbednar.dstr.logic.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Vertex<K, V extends Comparable<V>, E extends Comparable<E>> implements Comparable<Vertex<K, V, E>> {
    private final K key;
    private final PriorityQueue<Edge<K, V, E>> connections;
    private final V value;

    public Vertex(K key, V value) {
        this.key = key;
        this.value = value;
        connections = new PriorityQueue<>();
    }

    public void clear() {
        connections.clear();
    }

    public K getKey() {
        return key;
    }

    public PriorityQueue<Edge<K, V, E>> getConnections() {
        return connections;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(Vertex<K, V, E> o) {
        return value.compareTo(o.getValue());
    }
}
