package io.github.ppetrbednar.dstr.logic.railway;

import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.structures.Direction;
import io.github.ppetrbednar.dstr.logic.railway.structures.Rail;
import io.github.ppetrbednar.dstr.logic.railway.structures.RailwayPath;
import io.github.ppetrbednar.dstr.logic.railway.structures.Switch;

import java.util.*;

public class PathFinder {

    public static void calculateShortestPathFromSource(Graph<String, Switch, String, Rail> graph, Switch source) {
        graph.getVertexValues().values().forEach(Switch::clear);
        source.setDistance(0);

        Set<Switch> settledVertices = new HashSet<>();
        PriorityQueue<Switch> unsettledVertices = new PriorityQueue<>();

        unsettledVertices.add(source);

        while (unsettledVertices.size() != 0) {
            var current = unsettledVertices.poll();
            unsettledVertices.remove(current);

            for (Rail rail : current.getConnections()) {
                var adjacent = graph.getVertexValue(rail.left().equals(current.getKey()) ? rail.right() : rail.left());
                if (!settledVertices.contains(adjacent)) {
                    calculateMinimumDistance(current, rail, adjacent);
                    unsettledVertices.add(adjacent);
                }
            }
            settledVertices.add(current);
        }
    }

    private static void calculateMinimumDistance(Switch source, Rail rail, Switch adjacent) {

        int distance = source.getDistance();

        if (distance + rail.length() < adjacent.getDistance()) {
            adjacent.setDistance(distance + rail.length());
            adjacent.getShortestPath().clear();
            adjacent.getShortestPath().addAll(source.getShortestPath());
            adjacent.getShortestPath().add(new Direction(rail, source));
        }
    }

    private static RailwayPath getShortestValidPath(Graph<String, Switch, String, Rail> graph, Switch source, Switch target, int length) throws NoPathFoundException {
        calculateShortestPathFromSource(graph, source);

        if (target.getShortestPath().isEmpty()) {
            throw new NoPathFoundException();
        }

        Traveller traveller = new Traveller(graph, source, target, length);
        boolean found = traveller.findTraversablePath();

        if (!found) {
            throw new NoPathFoundException();
        }

        var traversedPath = traveller.getTraversedPath();
        LinkedList<Direction> path = new LinkedList<>(traversedPath);
        Collections.reverse(path);

        return new RailwayPath(path, traveller.getReversalPaths());
    }

    public static RailwayPath getShortestValidPath(Graph<String, Switch, String, Rail> graph, String sourceKey, String targetKey, int length) throws NoPathFoundException {
        return getShortestValidPath(graph, graph.getVertexValue(sourceKey), graph.getVertexValue(targetKey), length);
    }
}
