package io.github.ppetrbednar.dstr.logic.railway;

import io.github.ppetrbednar.dstr.logic.structures.Direction;
import io.github.ppetrbednar.dstr.logic.structures.Edge;
import io.github.ppetrbednar.dstr.logic.structures.Graph;
import io.github.ppetrbednar.dstr.logic.structures.Vertex;

import java.util.*;

public class Traveller {
    private Graph graph;
    private Vertex source;
    private Vertex target;
    private Stack<Direction> traversedPath;
    private HashMap<Edge, Direction> bannedDirections;
    private Vertex position;
    private boolean searchingNewPath;
    private Direction planned;
    private int length;
    private HashMap<Vertex, LinkedList<Direction>> reversalPaths;

    public Traveller(Graph graph, Vertex source, Vertex target, int length) {
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
        while (position != source) {
            if (!anyValidPathsExceptLast(null)) {
                backtrackToValid();
                continue;
            }

            if (searchingNewPath) {
                planned = findShortestValidDirectionNotUsingBannedPaths();
                if (planned == null) {
                    backtrackingStep();
                    step();
                    backtrackToValid();
                    continue;
                }
            } else {
                planned = findValidDirectionByDijkstra();
            }

            if (plannedStepLegal()) {
                step();
                searchingNewPath = false;
            } else {

                LinkedList<Direction> reversalPath = findReversalPath();
                if (reversalPath == null) {
                    if (anyValidPathsExceptLast(planned.getEdge())) {
                        bannedDirections.put(planned.getEdge(), planned);
                        searchingNewPath = true;
                    } else {
                        backtrackingStep();
                        step();
                        backtrackToValid();
                    }
                    continue;
                }

                reversalPaths.put(position, reversalPath);
                step();

                /*
                Possible skip of possible route when immediately backtracking
                 */

            }
        }
        return true;
    }

    public Stack<Direction> getTraversedPath() {
        return traversedPath;
    }

    public HashMap<Vertex, LinkedList<Direction>> getReversalPaths() {
        return reversalPaths;
    }

    private LinkedList<Direction> findReversalPath() {
        List<Direction> directions = findShortestValidDirectionsWithoutPlannedDirection();
        directions.sort((o1, o2) -> {
            Integer o1Distance = o1.getEdge().getWeight();
            Integer o2Distance = o2.getEdge().getWeight();

            return o2Distance.compareTo(o1Distance);
        });

        int distance = 0;

        LinkedList<Direction> reversalPath = new LinkedList<>();

        for (Direction direction : directions) {
            reversalPath.clear();
            if (distance + direction.getEdge().getWeight() >= length) {
                reversalPath.add(direction);
                return reversalPath;
            }

            reversalPath.add(direction);
            LinkedList<Direction> newPath = findReversalPath(new LinkedList<>(reversalPath), direction, distance + direction.getEdge().getWeight());
            if (newPath != null) {
                return newPath;
            }
        }
        return null;
    }

    private LinkedList<Direction> findReversalPath(LinkedList<Direction> reversalPath, Direction lastDirection, int distance) {
        List<Direction> directions = findShortestValidDirectionsWithoutLastDirection(lastDirection);
        directions.sort((o1, o2) -> {
            Integer o1Distance = o1.getEdge().getWeight();
            Integer o2Distance = o2.getEdge().getWeight();

            return o2Distance.compareTo(o1Distance);
        });

        for (Direction direction : directions) {
            if (distance + direction.getEdge().getWeight() >= length) {
                reversalPath.add(direction);
                return reversalPath;
            }
            LinkedList<Direction> reversalPathLast = new LinkedList<>(reversalPath);
            reversalPathLast.add(direction);
            LinkedList<Direction> newPath = findReversalPath(reversalPathLast, direction, distance + direction.getEdge().getWeight());
            if (newPath != null) {
                return newPath;
            }
        }
        return null;
    }


    private void backtrackToValid() {

        searchingNewPath = true;

        Direction lastPlanned;
        while (true) {

            /*
            If at start and nothing found, throw NoPossiblePathFoundException
            */

            lastPlanned = planned;
            backtrackingStep();
            step();
            if (anyValidPathsExceptLast(lastPlanned.getEdge())) {
                bannedDirections.put(lastPlanned.getEdge(), lastPlanned);
                return;
            }
        }
    }

    /**
     * Checks for any valid path except banned paths and last visited path.
     *
     * @return True if found any valid path. False when no valid path found.
     */
    private boolean anyValidPathsExceptLast(Edge invalidEdge) {
        for (Edge edge : position.getConnections()) {
            if (edge == invalidEdge || (!traversedPath.empty() && edge == traversedPath.peek().getEdge())) {
                continue;
            }

            if (bannedDirections.get(edge) == null || !edge.equals(bannedDirections.get(edge).getEdge())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if planned step is legal in terms of train turning. If step is not legal, there needs to be Reversal path search done.
     *
     * @return True if step legal. False when not legal.
     */
    private boolean plannedStepLegal() {
        for (IllegalTransition transition : position.getValues().getIllegalTransitions()) {
            if ((transition.getLeft() == traversedPath.peek().getEdge() && transition.getRight() == planned.getEdge()) ||
                    (transition.getRight() == traversedPath.peek().getEdge() && transition.getLeft() == planned.getEdge())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for any valid path except banned paths.
     *
     * @return True if found any valid path. False when no valid path found.
     */
    private boolean anyValidPaths() {
        for (Edge edge : position.getConnections()) {
            if ((bannedDirections.get(edge) == null || !edge.equals(bannedDirections.get(edge).getEdge()))) {
                return true;
            }
        }
        return false;
    }

    private void backtrackingStep() {
        traversedPath.pop();
        planned = traversedPath.pop();
    }

    private void step() {
        traversedPath.push(planned);
        position = planned.getVertex();
    }

    private Direction findValidDirectionByDijkstra() {
        return position.getValues().getShortestPath().getLast();
    }

    private Direction findShortestValidDirection() {
        List<Edge> connections = position.getConnections();
        connections.sort((o1, o2) -> {
            Integer o1Distance = o1.getNext(position).getValues().getDistance();
            Integer o2Distance = o2.getNext(position).getValues().getDistance();

            return o1Distance.compareTo(o2Distance);
        });

        for (Edge edge : connections) {
            if (edge == traversedPath.peek().getEdge()) {
                continue;
            }
            if (bannedDirections.get(edge) != null && bannedDirections.get(edge).getVertex() == edge.getNext(position)) {
                continue;
            }
            return new Direction(edge, edge.getNext(position));
        }
        return null;
    }

    private Direction findShortestValidDirectionNotUsingBannedPaths() {
        List<Edge> connections = position.getConnections();
        connections.sort((o1, o2) -> {
            Integer o1Distance = o1.getNext(position).getValues().getDistance();
            Integer o2Distance = o2.getNext(position).getValues().getDistance();

            return o1Distance.compareTo(o2Distance);
        });

        for (Edge edge : connections) {
            if (edge == traversedPath.peek().getEdge()) {
                continue;
            }
            if (bannedDirections.get(edge) != null && bannedDirections.get(edge).getVertex() == edge.getNext(position)) {
                continue;
            }

            var path = edge.getNext(position).getValues().getShortestPath();
            Edge potentiallyBanned = path.get(path.size() - 2).getEdge();
            if (bannedDirections.get(potentiallyBanned) != null) {
                continue;
            }

            return new Direction(edge, edge.getNext(position));
        }
        return null;
    }

    private LinkedList<Direction> findShortestValidDirectionsWithoutPlannedDirection() {

        LinkedList<Direction> directions = new LinkedList<>();
        List<Edge> connections = position.getConnections();
        connections.sort((o1, o2) -> {
            Integer o1Distance = o1.getNext(position).getValues().getDistance();
            Integer o2Distance = o2.getNext(position).getValues().getDistance();

            return o1Distance.compareTo(o2Distance);
        });

        for (Edge edge : connections) {
            if (edge == traversedPath.peek().getEdge()) {
                continue;
            }

            if (edge == planned.getEdge()) {
                continue;
            }

           /* if (bannedDirections.get(edge) != null && bannedDirections.get(edge).getVertex() == edge.getNext(position)) {
                continue;
            }*/
            directions.add(new Direction(edge, edge.getNext(position)));
        }
        return directions;
    }

    private LinkedList<Direction> findShortestValidDirectionsWithoutLastDirection(Direction current) {

        LinkedList<Direction> directions = new LinkedList<>();
        List<Edge> connections = current.getVertex().getConnections();
        connections.sort((o1, o2) -> {
            Integer o1Distance = o1.getNext(current.getVertex()).getValues().getDistance();
            Integer o2Distance = o2.getNext(current.getVertex()).getValues().getDistance();

            return o1Distance.compareTo(o2Distance);
        });

        for (Edge edge : connections) {

            if (edge == current.getEdge()) {
                continue;
            }

           /* if (bannedDirections.get(edge) != null && bannedDirections.get(edge).getVertex() == edge.getNext(current.getVertex())) {
                continue;
            }*/

            if (!checkIfDirectionValid(current.getEdge(), current.getVertex(), edge)) {
                continue;
            }

            directions.add(new Direction(edge, edge.getNext(current.getVertex())));
        }
        return directions;
    }

    private boolean checkIfDirectionValid(Edge last, Vertex current, Edge next) {
        for (IllegalTransition transition : current.getValues().getIllegalTransitions()) {
            if ((transition.getLeft() == last && transition.getRight() == next) ||
                    (transition.getRight() == last && transition.getLeft() == next)) {
                return false;
            }
        }
        return true;
    }
}
