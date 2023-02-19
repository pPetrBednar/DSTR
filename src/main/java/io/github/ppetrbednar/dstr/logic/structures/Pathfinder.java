package io.github.ppetrbednar.dstr.logic.structures;

import java.util.*;

public class Pathfinder {
    private final LinkedList<Direction> shortestPath;
    private final List<IllegalTransition> illegalPath;
    private int distance;

    public Pathfinder() {
        this.shortestPath = new LinkedList<>();
        this.illegalPath = new LinkedList<>();
        this.distance = Integer.MAX_VALUE;
    }

    public void clear() {
        shortestPath.clear();
        distance = Integer.MAX_VALUE;
    }

    public LinkedList<Direction> getShortestPath() {
        return shortestPath;
    }

    public List<IllegalTransition> getIllegalTransitions() {
        return illegalPath;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public static Graph calculateShortestPathFromSource(Graph graph, Vertex source) {
        graph.getVertices().values().forEach(vertex -> vertex.getValues().clear());
        source.getValues().setDistance(0);

        Set<Vertex> settledVertices = new HashSet<>();
        Set<Vertex> unsettledVertices = new HashSet<>();

        unsettledVertices.add(source);

        while (unsettledVertices.size() != 0) {
            Vertex current = getLowestDistanceVertex(unsettledVertices);
            unsettledVertices.remove(current);

            for (Edge edge : current.getConnections()) {
                Vertex adjacent = edge.getLeft() == current ? edge.getRight() : edge.getLeft();
                if (!settledVertices.contains(adjacent)) {
                    calculateMinimumDistance(current, edge, adjacent);
                    unsettledVertices.add(adjacent);
                }
            }
            settledVertices.add(current);
        }

        return graph;
    }

    private static Vertex getLowestDistanceVertex(Set<Vertex> unsettledVertices) {
        Vertex lowestDistanceVertex = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (Vertex vertex : unsettledVertices) {
            int distance = vertex.getValues().getDistance();
            if (distance < lowestDistance) {
                lowestDistance = distance;
                lowestDistanceVertex = vertex;
            }
        }

        return lowestDistanceVertex;
    }

    private static void calculateMinimumDistance(Vertex source, Edge edge, Vertex adjacent) {

        int distance = source.getValues().getDistance();

        if (distance + edge.getWeight() < adjacent.getValues().getDistance()) {
            adjacent.getValues().setDistance(distance + edge.getWeight());
            adjacent.getValues().getShortestPath().clear();
            adjacent.getValues().getShortestPath().addAll(source.getValues().getShortestPath());
            adjacent.getValues().getShortestPath().add(new Direction(edge, source));
        }
    }

    private static RailwayPath getShortestPathWithTraveller(Graph graph, Vertex source, Vertex target, int length) {
        if (graph.getVertex(source.getKey()).getValues().getDistance() != 0) {
            graph = Pathfinder.calculateShortestPathFromSource(graph, source);
        }

        Traveller traveller = new Traveller(graph, source, target, length);
        traveller.findTraversablePath();
        return new RailwayPath(traveller.getTraversedPath(), traveller.getReversalPaths());
    }

    public static LinkedList<Direction> getShortestPath(Graph graph, Vertex source, Vertex target) {
        if (graph.getVertex(source.getKey()).getValues().getDistance() != 0) {
            graph = Pathfinder.calculateShortestPathFromSource(graph, source);
        }

        LinkedList<Direction> shortestPath = graph.getVertex(target.getKey()).getValues().getShortestPath();
        shortestPath.add(new Direction(null, target));


        return shortestPath;
    }

    public static LinkedList<Direction> getShortestPath(Graph graph, String sourceKey, String targetKey) {
        return getShortestPath(graph, graph.getVertex(sourceKey), graph.getVertex(targetKey));
    }

    public static RailwayPath getShortestPathWithTraveller(Graph graph, String sourceKey, String targetKey, int length) {
        return getShortestPathWithTraveller(graph, graph.getVertex(sourceKey), graph.getVertex(targetKey), length);
    }
}
