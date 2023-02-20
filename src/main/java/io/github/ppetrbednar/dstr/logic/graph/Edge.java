package io.github.ppetrbednar.dstr.logic.graph;

public class Edge<K, V extends Comparable<V>, E extends Comparable<E>> implements Comparable<Edge<K, V, E>> {
    private final K key;
    private final Vertex<K, V, E> left;
    private final Vertex<K, V, E> right;
    private final E value;

    public Edge(K key, Vertex<K, V, E> left, Vertex<K, V, E> right, E value) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.value = value;
        left.getConnections().add(this);
        right.getConnections().add(this);
    }

    public void clear(Vertex<K, V, E> vertex) {
        (vertex == left ? right : left).getConnections().remove(this);
    }

    public void clear() {
        left.getConnections().remove(this);
        right.getConnections().remove(this);
    }

    public Vertex<K, V, E> getNext(Vertex<K, V, E> current) {
        return left == current ? right : left;
    }

    public K getKey() {
        return key;
    }

    public Vertex<K, V, E> getLeft() {
        return left;
    }

    public Vertex<K, V, E> getRight() {
        return right;
    }

    public E getValue() {
        return value;
    }

    @Override
    public int compareTo(Edge<K, V, E> o) {
        return value.compareTo(o.getValue());
    }
}
