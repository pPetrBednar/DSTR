package io.github.ppetrbednar.dstr.logic.railway;

import io.github.ppetrbednar.dstr.logic.graph.Edge;
import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.graph.Vertex;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.structures.Direction;
import io.github.ppetrbednar.dstr.logic.railway.structures.Rail;
import io.github.ppetrbednar.dstr.logic.railway.structures.RailwayPath;
import io.github.ppetrbednar.dstr.logic.railway.structures.Switch;

import java.util.*;

public class PathFinder {

    public static void calculateShortestPathFromSource(Graph<String, Switch, Rail> graph, Vertex<String, Switch, Rail> source) {
        clearGraph(graph);
        graph.getVertices().values().forEach(vertex -> vertex.getValue().clear());
        source.getValue().setDistance(0);

        Set<Vertex<String, Switch, Rail>> settledVertices = new HashSet<>();
        PriorityQueue<Vertex<String, Switch, Rail>> unsettledVertices = new PriorityQueue<>();

        unsettledVertices.add(source);

        while (unsettledVertices.size() != 0) {
            var current = unsettledVertices.poll();
            unsettledVertices.remove(current);

            for (Edge<String, Switch, Rail> edge : current.getConnections()) {
                var adjacent = edge.getLeft() == current ? edge.getRight() : edge.getLeft();
                if (!settledVertices.contains(adjacent)) {
                    calculateMinimumDistance(current, edge, adjacent);
                    unsettledVertices.add(adjacent);
                }
            }
            settledVertices.add(current);
        }
    }

    private static void calculateMinimumDistance(Vertex<String, Switch, Rail> source, Edge<String, Switch, Rail> edge, Vertex<String, Switch, Rail> adjacent) {

        int distance = source.getValue().getDistance();

        if (distance + edge.getValue().length() < adjacent.getValue().getDistance()) {
            adjacent.getValue().setDistance(distance + edge.getValue().length());
            adjacent.getValue().getShortestPath().clear();
            adjacent.getValue().getShortestPath().addAll(source.getValue().getShortestPath());
            adjacent.getValue().getShortestPath().add(new Direction(edge, source));
        }
    }

    private static void clearGraph(Graph<String, Switch, Rail> graph) {
        graph.getVertices().forEach((s, vertex) -> {
            vertex.getValue().clear();
        });
    }

    private static RailwayPath getShortestValidPath(Graph<String, Switch, Rail> graph, Vertex<String, Switch, Rail> source, Vertex<String, Switch, Rail> target, int length) throws NoPathFoundException {
        calculateShortestPathFromSource(graph, source);

        if (target.getValue().getShortestPath().isEmpty()) {
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

    public static RailwayPath getShortestValidPath(Graph<String, Switch, Rail> graph, String sourceKey, String targetKey, int length) throws NoPathFoundException {
        return getShortestValidPath(graph, graph.getVertex(sourceKey), graph.getVertex(targetKey), length);
    }
}
