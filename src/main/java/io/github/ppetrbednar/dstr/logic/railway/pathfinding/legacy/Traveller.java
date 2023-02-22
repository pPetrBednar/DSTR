package io.github.ppetrbednar.dstr.logic.railway.pathfinding.legacy;

import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPossiblePathException;
import io.github.ppetrbednar.dstr.logic.railway.structures.Direction;
import io.github.ppetrbednar.dstr.logic.railway.structures.Transition;
import io.github.ppetrbednar.dstr.logic.railway.structures.Rail;
import io.github.ppetrbednar.dstr.logic.railway.structures.Switch;

import java.util.*;

public class Traveller {
    private Graph<String, Switch, String, Rail> graph;
    private Switch source;
    private Switch target;
    private Stack<Direction> traversedPath;
    private HashMap<Rail, Direction> bannedDirections;
    private HashMap<Switch, LinkedList<Direction>> reversalPaths;
    private Switch position;
    private Direction planned;
    private boolean searchingNewPath;
    private final int length;

    public Traveller(Graph<String, Switch, String, Rail> graph, Switch source, Switch target, int length) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        position = target;
        bannedDirections = new HashMap<>();
        traversedPath = new Stack<>();
        reversalPaths = new HashMap<>();
        searchingNewPath = false;
        this.length = length;
    }

    public boolean findTraversablePath() {
        try {
            while (position != source) {
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

                if (!checkIfDirectionValid(traversedPath.isEmpty() ? null : traversedPath.peek().rail(), position, planned.rail())
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

    public Stack<Direction> getTraversedPath() {
        return traversedPath;
    }

    public HashMap<Switch, LinkedList<Direction>> getReversalPaths() {
        return reversalPaths;
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

        reversalPaths.put(position, reversalPath);
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
        for (var rail : graph.getEdgeValuesOfVertex(position.getKey())) {
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
        position = planned.point();
    }

    private Direction findValidDirectionByDijkstra() {
        return position.getShortestPath().getLast();
    }

    private Direction findShortestValidDirection() {
        var connections = graph.getEdgeValuesOfVertex(position.getKey());

        for (Rail rail : connections) {
            if (rail == getLastCrossedEdgeOrNull()) {
                continue;
            }
            if (bannedDirections.get(rail) != null && bannedDirections.get(rail).point().getKey().equals(rail.getNext(position))) {
                continue;
            }

            var path = graph.getVertexValue(rail.getNext(position)).getShortestPath();
            var potentiallyBanned = path.get(path.size() - 2).rail();
            if (bannedDirections.get(potentiallyBanned) != null) {
                continue;
            }

            return new Direction(rail, graph.getVertexValue(rail.getNext(position)));
        }
        return null;
    }

    private LinkedList<Direction> findShortestValidDirectionsWithoutPlannedDirection() {
        LinkedList<Direction> directions = new LinkedList<>();
        var connections = graph.getEdgeValuesOfVertex(position.getKey());

        for (var rail : connections) {
            if (rail != getLastCrossedEdgeOrNull() && rail != planned.rail()) {
                directions.add(new Direction(rail, graph.getVertexValue(rail.getNext(position))));
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
