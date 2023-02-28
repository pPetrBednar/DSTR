package io.github.ppetrbednar.dstr.logic.railway.pathfinding;

import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.structures.*;

import java.util.*;

public class EnhancedDijkstraAlgorithm {

    public static RailwayPath getShortestValidPath(Graph<String, Switch, String, Rail> graph, Switch source, Switch target, int length) throws NoPathFoundException {
        graph.getVertexValues().values().forEach(Switch::clear);
        calculateShortestPathFromSourceLengthCheck(graph, source, length);
        return new RailwayPath(graph, target);
    }

    public static RailwayPath getShortestValidPath(Graph<String, Switch, String, Rail> graph, String sourceKey, String targetKey, int length) throws NoPathFoundException {
        return getShortestValidPath(graph, graph.getVertexValue(sourceKey), graph.getVertexValue(targetKey), length);
    }

    private static void calculateShortestPathFromSourceLengthCheck(Graph<String, Switch, String, Rail> graph, Switch source, int length) {
        source.setDistance(0);

        Set<Switch> settledVertices = new HashSet<>();
        PriorityQueue<Direction> unsettledVertices = new PriorityQueue<>();

        unsettledVertices.add(new Direction(new Rail("", "", "", 0), source));

        while (unsettledVertices.size() != 0) {
            var current = unsettledVertices.poll();
            unsettledVertices.remove(current);

            for (Rail rail : graph.getEdgeValuesOfVertex(current.point().getKey())) {
                if (rail == current.rail()) {
                    continue;
                }
                if (!checkIfDirectionValid(current.rail(), current.point(), rail)) {
                    LinkedList<Direction> reversalPath = findReversalPath(graph, current, rail, length);
                    if (reversalPath == null) {
                        continue;
                    }
                    current.point().getReversalPaths().put(new Transition(current.rail(), current.point(), rail), reversalPath);
                }
                var adjacent = graph.getVertexValue(rail.left().equals(current.point().getKey()) ? rail.right() : rail.left());
                if (!settledVertices.contains(adjacent)) {
                    calculateMinimumDistance(current.point(), rail, adjacent);
                    unsettledVertices.add(new Direction(rail, adjacent));
                }
            }
            settledVertices.add(current.point());
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

    private static boolean checkIfDirectionValid(Rail last, Switch current, Rail next) {
        for (Transition transition : current.getIllegalTransitions()) {
            if ((transition.left() == last && transition.right() == next) || (transition.right() == last && transition.left() == next)) {
                return false;
            }
        }
        return true;
    }

    private static LinkedList<Direction> findReversalPath(Graph<String, Switch, String, Rail> graph, Direction current, Rail illegal, int length) {
        LinkedList<Direction> directions = new LinkedList<>();
        graph.getEdgeValuesOfVertex(current.point().getKey()).forEach(rail -> {
            if (rail == illegal || rail == current.rail()) {
                return;
            }
            directions.add(new Direction(rail, graph.getVertexValue(rail.getNext(current.point()))));
        });
        Collections.reverse(directions);

        int distance = 0;

        LinkedList<Direction> reversalPath = new LinkedList<>();

        for (Direction direction : directions) {
            if (!checkIfDirectionValid(current.rail(), current.point(), direction.rail())) {
                continue;
            }

            reversalPath.clear();
            if (distance + direction.rail().length() >= length) {
                reversalPath.add(direction);
                return reversalPath;
            }

            reversalPath.add(direction);
            LinkedList<Direction> newPath = findReversalPath(graph, new LinkedList<>(reversalPath), direction, distance + direction.rail().length(), length);
            if (newPath != null) {
                return newPath;
            }
        }
        return null;
    }

    private static LinkedList<Direction> findReversalPath(Graph<String, Switch, String, Rail> graph, LinkedList<Direction> reversalPath, Direction current, int distance, int length) {
        List<Direction> directions = new LinkedList<>();
        graph.getEdgeValuesOfVertex(current.point().getKey()).forEach(rail -> {
            if (rail == current.rail()) {
                return;
            }
            directions.add(new Direction(rail, graph.getVertexValue(rail.getNext(current.point()))));
        });
        Collections.reverse(directions);

        for (Direction direction : directions) {
            if (!checkIfDirectionValid(current.rail(), current.point(), direction.rail())) {
                continue;
            }

            if (checkIfDirectionVisited(reversalPath, direction)) {
                continue;
            }

            if (distance + direction.rail().length() >= length) {
                reversalPath.add(direction);
                return reversalPath;
            }
            LinkedList<Direction> reversalPathLast = new LinkedList<>(reversalPath);
            reversalPathLast.add(direction);
            LinkedList<Direction> newPath = findReversalPath(graph, reversalPathLast, direction, distance + direction.rail().length(), length);
            if (newPath != null) {
                return newPath;
            }
        }
        return null;
    }

    private static boolean checkIfDirectionVisited(LinkedList<Direction> reversalPath, Direction direction) {
        for (Direction dir : reversalPath) {
            if (dir.point().getKey().equals(direction.point().getKey()) || dir.rail().key().equals(direction.rail().key())) {
                return true;
            }
        }
        return false;
    }
}
