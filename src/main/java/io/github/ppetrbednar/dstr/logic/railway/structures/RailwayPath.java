package io.github.ppetrbednar.dstr.logic.railway.structures;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public record RailwayPath(LinkedList<Direction> path,
                          HashMap<Switch, LinkedList<Direction>> reversalPaths) {

    public void print(Switch source, Switch target) {
        System.out.println("Complete path:");
        StringBuilder out = new StringBuilder();
        for (Direction v : path) {
            out.append(v.point().getKey()).append(" -> ").append(v.rail() == null ? "" : (v.rail().key() + " -> "));
        }
        out.append(target.getKey());
        System.out.println(out);

        System.out.println("Reversal paths:");
        for (var key : reversalPaths.keySet().stream().sorted(Comparator.comparing(Switch::getKey)).toList()) {
            LinkedList<Direction> dirs = reversalPaths.get(key);
            StringBuilder pom = new StringBuilder(key.getKey() + " -> ");
            for (Direction v : dirs) {
                pom.append(v.rail() == null ? "" : (v.rail().key() + "(" + v.rail().length() + ")" + " -> ")).append(v.point().getKey()).append(" -> ");
            }
            pom = new StringBuilder(pom.substring(0, pom.length() - 3));
            System.out.println(pom);
        }
    }
}
