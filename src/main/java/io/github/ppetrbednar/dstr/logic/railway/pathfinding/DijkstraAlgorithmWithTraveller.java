package io.github.ppetrbednar.dstr.logic.railway.pathfinding;

import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPossiblePathException;
import io.github.ppetrbednar.dstr.logic.railway.structures.*;

import java.util.*;

public class DijkstraAlgorithmWithTraveller {

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

        return new RailwayPath(path);
    }

    public static RailwayPath getShortestValidPath(Graph<String, Switch, String, Rail> graph, String sourceKey, String targetKey, int length) throws NoPathFoundException {
        return getShortestValidPath(graph, graph.getVertexValue(sourceKey), graph.getVertexValue(targetKey), length);
    }

    private static void calculateShortestPathFromSource(Graph<String, Switch, String, Rail> graph, Switch source) {
        graph.getVertexValues().values().forEach(Switch::clear);
        source.setDistance(0);

        Set<Switch> settledVertices = new HashSet<>();
        PriorityQueue<Switch> unsettledVertices = new PriorityQueue<>();

        unsettledVertices.add(source);

        while (unsettledVertices.size() != 0) {
            var current = unsettledVertices.poll();
            unsettledVertices.remove(current);

            for (Rail rail : graph.getEdgeValuesOfVertex(current.getKey())) {
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

    private static class Traveller {
        private final Graph<String, Switch, String, Rail> graph;
        private final Switch source;
        private final Stack<Direction> traversedPath;
        private final HashMap<Rail, Direction> bannedDirections;
        private Direction position;
        private Direction planned;
        private boolean searchingNewPath;
        private final int length;

        private Traveller(Graph<String, Switch, String, Rail> graph, Switch source, Switch target, int length) {
            this.graph = graph;
            this.source = source;
            position = new Direction(null, target);
            bannedDirections = new HashMap<>();
            traversedPath = new Stack<>();
            searchingNewPath = false;
            this.length = length;
        }

        private boolean findTraversablePath() {
            try {
                while (position.point() != source) {
                    if (!anyValidPathsExceptLast(null)) {
                        throw new NoPossiblePathException();
                    }

                    if (searchingNewPath) {
                        planned = findShortestValidDirection();
                        if (planned == null) {
                            backtrackingStep();
                            backtrackToValid();
                            continue;
                        }
                    } else {
                        planned = findValidDirectionByDijkstra();
                    }

                    if (!checkIfDirectionValid(traversedPath.isEmpty() ? null : traversedPath.peek().rail(), position.point(), planned.rail())
                            && !reversalPathFound()) {
                        continue;
                    }

                    step();
                    searchingNewPath = false;
                }
            } catch (NoPossiblePathException ex) {
                return false;
            }
            return true;
        }

        private Stack<Direction> getTraversedPath() {
            return traversedPath;
        }

        private boolean reversalPathFound() throws NoPossiblePathException {
            LinkedList<Direction> reversalPath = findReversalPath();
            if (reversalPath == null) {
                if (anyValidPathsExceptLast(planned.rail())) {
                    bannedDirections.put(planned.rail(), planned);
                    searchingNewPath = true;
                } else {
                    backtrackingStep();
                    backtrackToValid();
                }
                return false;
            }

            position.point().getReversalPaths().put(new Transition(planned.rail(), position.point(), position.rail()), reversalPath);
            return true;
        }

        private LinkedList<Direction> findReversalPath() {
            List<Direction> directions = findShortestValidDirectionsWithoutPlannedDirection();
            Collections.reverse(directions);

            int distance = 0;

            LinkedList<Direction> reversalPath = new LinkedList<>();

            for (Direction direction : directions) {
                reversalPath.clear();
                if (distance + direction.rail().length() >= length) {
                    reversalPath.add(direction);
                    return reversalPath;
                }

                reversalPath.add(direction);
                LinkedList<Direction> newPath = findReversalPath(new LinkedList<>(reversalPath), direction, distance + direction.rail().length());
                if (newPath != null) {
                    return newPath;
                }
            }
            return null;
        }

        private LinkedList<Direction> findReversalPath(LinkedList<Direction> reversalPath, Direction lastDirection, int distance) {
            List<Direction> directions = findShortestValidDirections(lastDirection);
            Collections.reverse(directions);

            for (Direction direction : directions) {
                if (distance + direction.rail().length() >= length) {
                    reversalPath.add(direction);
                    return reversalPath;
                }
                LinkedList<Direction> reversalPathLast = new LinkedList<>(reversalPath);
                reversalPathLast.add(direction);
                LinkedList<Direction> newPath = findReversalPath(reversalPathLast, direction, distance + direction.rail().length());
                if (newPath != null) {
                    return newPath;
                }
            }
            return null;
        }


        private void backtrackToValid() throws NoPossiblePathException {

            searchingNewPath = true;

            Direction lastPlanned;
            while (true) {

                lastPlanned = planned;
                backtrackingStep();
                if (anyValidPathsExceptLast(lastPlanned.rail())) {
                    bannedDirections.put(lastPlanned.rail(), lastPlanned);
                    return;
                }
            }
        }

        private boolean anyValidPathsExceptLast(Rail invalidRail) {
            for (var rail : graph.getEdgeValuesOfVertex(position.point().getKey())) {
                if (rail == invalidRail || rail == getLastCrossedEdgeOrNull()) {
                    continue;
                }

                if (bannedDirections.get(rail) == null || !rail.equals(bannedDirections.get(rail).rail())) {
                    return true;
                }
            }
            return false;
        }

        private void backtrackingStep() throws NoPossiblePathException {
            if (traversedPath.size() < 2) {
                throw new NoPossiblePathException();
            }

            traversedPath.pop();
            planned = traversedPath.pop();
            step();
        }

        private void step() {
            traversedPath.push(planned);
            position = planned;
        }

        private Direction findValidDirectionByDijkstra() {
            return position.point().getShortestPath().getLast();
        }

        private Direction findShortestValidDirection() {
            var connections = graph.getEdgeValuesOfVertex(position.point().getKey());

            for (Rail rail : connections) {
                if (rail == getLastCrossedEdgeOrNull()) {
                    continue;
                }
                if (bannedDirections.get(rail) != null && bannedDirections.get(rail).point().getKey().equals(rail.getNext(position.point()))) {
                    continue;
                }

                var path = graph.getVertexValue(rail.getNext(position.point())).getShortestPath();
                var potentiallyBanned = path.get(path.size() - 2).rail();
                if (bannedDirections.get(potentiallyBanned) != null) {
                    continue;
                }

                return new Direction(rail, graph.getVertexValue(rail.getNext(position.point())));
            }
            return null;
        }

        private LinkedList<Direction> findShortestValidDirectionsWithoutPlannedDirection() {
            LinkedList<Direction> directions = new LinkedList<>();
            var connections = graph.getEdgeValuesOfVertex(position.point().getKey());

            for (var rail : connections) {
                if (rail != getLastCrossedEdgeOrNull() && rail != planned.rail()) {
                    directions.add(new Direction(rail, graph.getVertexValue(rail.getNext(position.point()))));
                }
            }
            return directions;
        }

        private Rail getLastCrossedEdgeOrNull() {
            return traversedPath.isEmpty() ? null : traversedPath.peek().rail();
        }

        private LinkedList<Direction> findShortestValidDirections(Direction current) {
            LinkedList<Direction> directions = new LinkedList<>();

            for (var rail : graph.getEdgeValuesOfVertex(current.point().getKey())) {
                if (rail != current.rail() && checkIfDirectionValid(current.rail(), current.point(), rail)) {
                    directions.add(new Direction(rail, graph.getVertexValue(rail.getNext(current.point()))));
                }
            }
            return directions;
        }

        private boolean checkIfDirectionValid(Rail last, Switch current, Rail next) {
            for (Transition transition : current.getIllegalTransitions()) {
                if ((transition.left() == last && transition.right() == next) ||
                        (transition.right() == last && transition.left() == next)) {
                    return false;
                }
            }
            return true;
        }
    }
}
