package io.github.ppetrbednar.dstr.logic.railway.structures;

import io.github.ppetrbednar.dstr.logic.graph.Vertex;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public record RailwayPath(LinkedList<Direction> path,
                          HashMap<Vertex<String, Switch, Rail>, LinkedList<Direction>> reversalPaths) {

    public void print(Vertex<String, Switch, Rail> source, Vertex<String, Switch, Rail> target) {
        System.out.println("Complete path:");
        StringBuilder out = new StringBuilder();
        for (Direction v : path) {
            out.append(v.vertex().getKey()).append(" -> ").append(v.edge() == null ? "" : (v.edge().getKey() + " -> "));
        }
        out.append(target.getKey());
        System.out.println(out);

        System.out.println("Reversal paths:");
        for (var key : reversalPaths.keySet().stream().sorted(Comparator.comparing(Vertex::getKey)).toList()) {
            LinkedList<Direction> dirs = reversalPaths.get(key);
            StringBuilder pom = new StringBuilder(key.getKey() + " -> ");
            for (Direction v : dirs) {
                pom.append(v.edge() == null ? "" : (v.edge().getKey() + "(" + v.edge().getValue().length() + ")" + " -> ")).append(v.vertex().getKey()).append(" -> ");
            }
            pom = new StringBuilder(pom.substring(0, pom.length() - 3));
            System.out.println(pom);
        }
    }
}
