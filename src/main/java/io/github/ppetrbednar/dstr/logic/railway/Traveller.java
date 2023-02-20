package io.github.ppetrbednar.dstr.logic.railway;

import io.github.ppetrbednar.dstr.logic.graph.Edge;
import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.graph.Vertex;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPossiblePathException;
import io.github.ppetrbednar.dstr.logic.railway.structures.Direction;
import io.github.ppetrbednar.dstr.logic.railway.structures.IllegalTransition;
import io.github.ppetrbednar.dstr.logic.railway.structures.Rail;
import io.github.ppetrbednar.dstr.logic.railway.structures.Switch;

import java.util.*;

public class Traveller {
    private Graph<String, Switch, Rail> graph;
    private Vertex<String, Switch, Rail> source;
    private Vertex<String, Switch, Rail> target;
    private Stack<Direction> traversedPath;
    private HashMap<Edge<String, Switch, Rail>, Direction> bannedDirections;
    private HashMap<Vertex<String, Switch, Rail>, LinkedList<Direction>> reversalPaths;
    private Vertex<String, Switch, Rail> position;
    private Direction planned;
    private boolean searchingNewPath;
    private final int length;

    public Traveller(Graph<String, Switch, Rail> graph, Vertex<String, Switch, Rail> source, Vertex<String, Switch, Rail> target, int length) {
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

                if (!checkIfDirectionValid(traversedPath.isEmpty() ? null : traversedPath.peek().edge(), position, planned.edge())
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

    public HashMap<Vertex<String, Switch, Rail>, LinkedList<Direction>> getReversalPaths() {
        return reversalPaths;
    }

    private boolean reversalPathFound() throws NoPossiblePathException {
        LinkedList<Direction> reversalPath = findReversalPath();
        if (reversalPath == null) {
            if (anyValidPathsExceptLast(planned.edge())) {
                bannedDirections.put(planned.edge(), planned);
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
            if (distance + direction.edge().getValue().length() >= length) {
                reversalPath.add(direction);
                return reversalPath;
            }

            reversalPath.add(direction);
            LinkedList<Direction> newPath = findReversalPath(new LinkedList<>(reversalPath), direction, distance + direction.edge().getValue().length());
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
            if (distance + direction.edge().getValue().length() >= length) {
                reversalPath.add(direction);
                return reversalPath;
            }
            LinkedList<Direction> reversalPathLast = new LinkedList<>(reversalPath);
            reversalPathLast.add(direction);
            LinkedList<Direction> newPath = findReversalPath(reversalPathLast, direction, distance + direction.edge().getValue().length());
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
            if (anyValidPathsExceptLast(lastPlanned.edge())) {
                bannedDirections.put(lastPlanned.edge(), lastPlanned);
                return;
            }
        }
    }

    private boolean anyValidPathsExceptLast(Edge<String, Switch, Rail> invalidEdge) {
        for (var edge : position.getConnections()) {
            if (edge == invalidEdge || edge == getLastCrossedEdgeOrNull()) {
                continue;
            }

            if (bannedDirections.get(edge) == null || !edge.equals(bannedDirections.get(edge).edge())) {
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
        position = planned.vertex();
    }

    private Direction findValidDirectionByDijkstra() {
        return position.getValue().getShortestPath().getLast();
    }

    private Direction findShortestValidDirection() {
        var connections = position.getConnections();

        for (Edge<String, Switch, Rail> edge : connections) {
            if (edge == getLastCrossedEdgeOrNull()) {
                continue;
            }
            if (bannedDirections.get(edge) != null && bannedDirections.get(edge).vertex() == edge.getNext(position)) {
                continue;
            }

            var path = edge.getNext(position).getValue().getShortestPath();
            var potentiallyBanned = path.get(path.size() - 2).edge();
            if (bannedDirections.get(potentiallyBanned) != null) {
                continue;
            }

            return new Direction(edge, edge.getNext(position));
        }
        return null;
    }

    private LinkedList<Direction> findShortestValidDirectionsWithoutPlannedDirection() {
        LinkedList<Direction> directions = new LinkedList<>();
        var connections = position.getConnections();

        for (var edge : connections) {
            if (edge != getLastCrossedEdgeOrNull() && edge != planned.edge()) {
                directions.add(new Direction(edge, edge.getNext(position)));
            }
        }
        return directions;
    }

    private Edge<String, Switch, Rail> getLastCrossedEdgeOrNull() {
        return traversedPath.isEmpty() ? null : traversedPath.peek().edge();
    }

    private LinkedList<Direction> findShortestValidDirections(Direction current) {
        LinkedList<Direction> directions = new LinkedList<>();

        for (var edge : current.vertex().getConnections()) {
            if (edge != current.edge() && checkIfDirectionValid(current.edge(), current.vertex(), edge)) {
                directions.add(new Direction(edge, edge.getNext(current.vertex())));
            }
        }
        return directions;
    }

    private boolean checkIfDirectionValid(Edge<String, Switch, Rail> last, Vertex<String, Switch, Rail> current, Edge<String, Switch, Rail> next) {
        for (IllegalTransition transition : current.getValue().getIllegalTransitions()) {
            if ((transition.getLeft() == last && transition.getRight() == next) ||
                    (transition.getRight() == last && transition.getLeft() == next)) {
                return false;
            }
        }
        return true;
    }
}
