package io.github.ppetrbednar.dstr.logic.structures;

public class Edge {
    private final String key;
    private Vertex left;
    private Vertex right;
    private int weight;

    public Edge(String key, int weight) {
        this.key = key;
        this.weight = weight;
    }

    public void setLeft(Vertex left) {
        this.left = left;
        left.getConnections().add(this);
    }

    public void setRight(Vertex right) {
        this.right = right;
        right.getConnections().add(this);
    }

    public Edge(String key, Vertex left, Vertex right, int weight) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.weight = weight;
        left.getConnections().add(this);
        right.getConnections().add(this);
    }

    public void clear(Vertex vertex) {
        (vertex == left ? right : left).getConnections().remove(this);
        left = right = null;
    }

    public void clear() {
        left.getConnections().remove(this);
        right.getConnections().remove(this);
        left = right = null;
    }

    public Vertex getNext(Vertex current) {
        return left == current ? right : left;
    }

    public String getKey() {
        return key;
    }

    public Vertex getLeft() {
        return left;
    }

    public Vertex getRight() {
        return right;
    }

    public int getWeight() {
        return weight;
    }
}
