package io.github.ppetrbednar.dstr.logic.structures;

public class Direction {
    private final Edge edge;
    private final Vertex vertex;

    public Direction(Edge edge, Vertex vertex) {
        this.edge = edge;
        this.vertex = vertex;
    }

    public Edge getEdge() {
        return edge;
    }

    public Vertex getVertex() {
        return vertex;
    }
}
