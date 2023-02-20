package io.github.ppetrbednar.dstr.logic.railway.structures;

import io.github.ppetrbednar.dstr.logic.graph.Edge;

public class IllegalTransition {
    private final Edge<String, Switch, Rail> left;
    private final Edge<String, Switch, Rail> right;

    public IllegalTransition(Edge<String, Switch, Rail> left, Edge<String, Switch, Rail> right) {
        this.left = left;
        this.right = right;
    }

    public Edge<String, Switch, Rail> getLeft() {
        return left;
    }

    public Edge<String, Switch, Rail> getRight() {
        return right;
    }
}
