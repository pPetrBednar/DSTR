package io.github.ppetrbednar.dstr.logic.structures;

import java.util.List;

public class Vertex<K, V, E> {
    private K key;
    private V value;
    private List<Edge<K, V, E>> connections;

    public Vertex(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public List<Edge<K, V, E>> getConnections() {
        return connections;
    }

    public void setConnections(List<Edge<K, V, E>> connections) {
        this.connections = connections;
    }
}
