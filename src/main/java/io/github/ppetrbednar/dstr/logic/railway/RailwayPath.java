package io.github.ppetrbednar.dstr.logic.railway;

import io.github.ppetrbednar.dstr.logic.structures.Direction;
import io.github.ppetrbednar.dstr.logic.structures.Vertex;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class RailwayPath {
    private final LinkedList<Direction> path;
    private final HashMap<Vertex, LinkedList<Direction>> reversalPaths;

    public RailwayPath(Stack<Direction> path, HashMap<Vertex, LinkedList<Direction>> reversalPaths) {
        this.path = new LinkedList<>();
        int size = path.size();
        for (int i = 0; i < size; i++) {
            this.path.add(path.pop());
        }

        this.reversalPaths = reversalPaths;
    }

    public LinkedList<Direction> getPath() {
        return path;
    }

    public HashMap<Vertex, LinkedList<Direction>> getReversalPaths() {
        return reversalPaths;
    }

    public void print(Vertex source, Vertex target) {
        System.out.println("Complete path:");
        String out = "";
        for (Direction v : path) {
            out += v.getVertex().getKey() + " -> " + (v.getEdge() == null ? "" : (v.getEdge().getKey() + " -> "));
        }
        out += target.getKey();
        System.out.println(out);

        System.out.println("Reversal paths:");
        for (var key : reversalPaths.keySet().stream().sorted(Comparator.comparing(Vertex::getKey)).toList()) {
            LinkedList<Direction> dirs = reversalPaths.get(key);
            String pom = key.getKey() + " -> ";
            for (Direction v : dirs) {
                pom += (v.getEdge() == null ? "" : (v.getEdge().getKey() + "(" + v.getEdge().getWeight() + ")" + " -> ")) + v.getVertex().getKey() + " -> ";
            }
            pom = pom.substring(0, pom.length() - 3);
            System.out.println(pom);
        }
    }
}
