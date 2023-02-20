package io.github.ppetrbednar.dstr.logic.structures;

import java.util.*;

public interface IGraph {
    void clear();

    boolean isEmpty();

    int size();

    void addVertex(Vertex vertex);

    void addEdge(Edge edge);

    void addEdge(Edge edge, String leftVertexKey, String rightVertexKey);

    Vertex removeVertex(String key);

    Edge removeEdge(String key);

    Vertex getVertex(String key);

    Edge getEdge(String key);

    Map<String, Vertex> getVertices();

    Map<String, Edge> getEdges();
}

