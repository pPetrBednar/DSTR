package io.github.ppetrbednar.dstr.logic.structures;

public class Edge<K, V, E> {
    private K key;
    private Vertex<K, V, E> left;
    private Vertex<K, V, E> right;
    private E value;

    public Edge(K key, E value) {
        this.key = key;
        this.value = value;
    }

    public Edge(K key, Vertex<K, V, E> left, Vertex<K, V, E> right, E value) {
        this.key = key;
        this.left = left;
        left.getConnections().add(this);
        this.right = right;
        right.getConnections().add(this);
        this.value = value;
    }

    public Vertex<K, V, E> getLeft() {
        return left;
    }

    public Vertex<K, V, E> getRight() {
        return right;
    }

    public void setLink(Vertex<K, V, E> left, Vertex<K, V, E> right) {
        this.left = left;
        left.getConnections().add(this);
        this.right = right;
        right.getConnections().add(this);
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void clear(Vertex<K, V, E> vertex) {
        (vertex == left ? right : left).getConnections().remove(this);
        left = right = null;
    }

    public void clear() {
        left.getConnections().remove(this);
        right.getConnections().remove(this);
        left = right = null;
    }
}
