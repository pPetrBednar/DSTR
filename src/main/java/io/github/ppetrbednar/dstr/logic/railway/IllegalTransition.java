package io.github.ppetrbednar.dstr.logic.railway;

import io.github.ppetrbednar.dstr.logic.structures.Edge;

public class IllegalTransition {
    private final Edge left;
    private final Edge right;

    public IllegalTransition(Edge left, Edge right) {
        this.left = left;
        this.right = right;
    }

    public Edge getLeft() {
        return left;
    }

    public Edge getRight() {
        return right;
    }
}
