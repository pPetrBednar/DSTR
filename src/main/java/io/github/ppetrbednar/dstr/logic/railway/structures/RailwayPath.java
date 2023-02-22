package io.github.ppetrbednar.dstr.logic.railway.structures;

import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class RailwayPath {
    private final LinkedList<Direction> path;
    private final HashMap<Transition, LinkedList<Direction>> reversalPaths;

    public RailwayPath(Graph<String, Switch, String, Rail> network, String target) throws NoPathFoundException {
        LinkedList<Direction> dijkstraPath = network.getVertexValue(target).getShortestPath();
        if (dijkstraPath.isEmpty()) {
            throw new NoPathFoundException();
        }
        path = new LinkedList<>(dijkstraPath);
        reversalPaths = new HashMap<>();

        Direction last = null;
        for (Direction direction : path) {
            if (last != null) {
                Transition transition = new Transition(last.rail(), direction.point(), direction.rail());
                LinkedList<Direction> reversalPath = direction.point().getReversalPaths().get(transition);
                if (reversalPath != null) {
                    reversalPaths.put(transition, reversalPath);
                }
            }
            last = direction;
        }
    }

    public void print(Switch source, Switch target) {
        System.out.println("Complete path:");
        StringBuilder out = new StringBuilder();
        for (Direction v : path) {
            out.append(v.point().getKey()).append(" -> ").append(v.rail() == null ? "" : (v.rail().key() + " -> "));
        }
        out.append(target.getKey());
        System.out.println(out);

        System.out.println("Reversal paths:");
        for (var key : reversalPaths.keySet()) {
            LinkedList<Direction> dirs = reversalPaths.get(key);
            StringBuilder pom = new StringBuilder(key.point().getKey() + " -> ");
            for (Direction v : dirs) {
                pom.append(v.rail() == null ? "" : (v.rail().key() + "(" + v.rail().length() + ")" + " -> ")).append(v.point().getKey()).append(" -> ");
            }
            pom = new StringBuilder(pom.substring(0, pom.length() - 3));
            System.out.println(pom);
        }
    }
}
