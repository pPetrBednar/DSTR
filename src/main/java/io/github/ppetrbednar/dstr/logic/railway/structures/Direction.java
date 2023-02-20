package io.github.ppetrbednar.dstr.logic.railway.structures;

import io.github.ppetrbednar.dstr.logic.graph.Edge;
import io.github.ppetrbednar.dstr.logic.graph.Vertex;

public record Direction(Edge<String, Switch, Rail> edge, Vertex<String, Switch, Rail> vertex) {
}
