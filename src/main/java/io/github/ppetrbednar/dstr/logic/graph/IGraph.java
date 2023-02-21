package io.github.ppetrbednar.dstr.logic.graph;

import java.util.*;

public interface IGraph<VK, VV extends Comparable<VV>, EK, EV extends Comparable<EV>> {
    void clear();

    boolean isEmpty();

    int size();

    void addVertex(VK key, VV value);

    void addEdge(EK key, VK left, VK right, EV value);

    void removeVertex(VK key);

    void removeEdge(EK key);

    VV getVertexValue(VK key);

    EV getEdgeValue(EK key);

    Map<VK, VV> getVertexValues();

    Map<EK, EV> getEdgeValues();
}

