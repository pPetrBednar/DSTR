package io.github.ppetrbednar.dstr.logic.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph<VK, VV extends Comparable<VV>, EK, EV extends Comparable<EV>> implements IGraph<VK, VV, EK, EV> {
    public class Edge<VK, VV extends Comparable<VV>, EK, EV extends Comparable<EV>> implements Comparable<Edge<VK, VV, EK, EV>> {
        private final EK key;
        private final Vertex<VK, VV, EK, EV> left;
        private final Vertex<VK, VV, EK, EV> right;
        private final EV value;

        public Edge(EK key, Vertex<VK, VV, EK, EV> left, Vertex<VK, VV, EK, EV> right, EV value) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.value = value;
            left.getConnections().add(this);
            right.getConnections().add(this);
        }

        public void clear(Vertex<VK, VV, EK, EV> vertex) {
            (vertex == left ? right : left).getConnections().remove(this);
        }

        public void clear() {
            left.getConnections().remove(this);
            right.getConnections().remove(this);
        }

        public EK getKey() {
            return key;
        }

        public Vertex<VK, VV, EK, EV> getLeft() {
            return left;
        }

        public Vertex<VK, VV, EK, EV> getRight() {
            return right;
        }

        public EV getValue() {
            return value;
        }

        @Override
        public int compareTo(Edge<VK, VV, EK, EV> o) {
            return value.compareTo(o.getValue());
        }
    }

    public class Vertex<VK, VV extends Comparable<VV>, EK, EV extends Comparable<EV>> implements Comparable<Vertex<VK, VV, EK, EV>> {
        private final VK key;
        private final PriorityQueue<Edge<VK, VV, EK, EV>> connections;
        private final VV value;

        public Vertex(VK key, VV value) {
            this.key = key;
            this.value = value;
            connections = new PriorityQueue<>();
        }

        public void clear() {
            connections.clear();
        }

        public VK getKey() {
            return key;
        }

        public PriorityQueue<Edge<VK, VV, EK, EV>> getConnections() {
            return connections;
        }

        public VV getValue() {
            return value;
        }

        @Override
        public int compareTo(Vertex<VK, VV, EK, EV> o) {
            return value.compareTo(o.getValue());
        }
    }

    private final HashMap<VK, Vertex<VK, VV, EK, EV>> vertices;
    private final HashMap<EK, Edge<VK, VV, EK, EV>> edges;

    public Graph() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    @Override
    public void clear() {
        vertices.clear();
        edges.clear();
    }

    @Override
    public boolean isEmpty() {
        return vertices.isEmpty() & edges.isEmpty();
    }

    @Override
    public int size() {
        return vertices.size() + edges.size();
    }

    @Override
    public void addVertex(VK key, VV value) {
        vertices.put(key, new Vertex<>(key, value));
    }

    @Override
    public void addEdge(EK key, VK left, VK right, EV value) {
        edges.put(key, new Edge<>(key, vertices.get(left), vertices.get(right), value));
    }

    @Override
    public void removeVertex(VK key) {
        var vertex = vertices.remove(key);
        vertex.getConnections().forEach(edge -> {
            edges.remove(edge.getKey()).clear(vertex);
        });
    }

    @Override
    public void removeEdge(EK key) {
        var edge = edges.remove(key);
        edge.clear();
    }

    @Override
    public VV getVertexValue(VK key) {
        return vertices.get(key).getValue();
    }

    @Override
    public EV getEdgeValue(EK key) {
        return edges.get(key).getValue();
    }

    @Override
    public Map<VK, VV> getVertexValues() {
        HashMap<VK, VV> output = new HashMap<>();
        vertices.forEach((key, vertex) -> {
            output.put(key, vertex.getValue());
        });
        return output;
    }

    @Override
    public Map<EK, EV> getEdgeValues() {
        HashMap<EK, EV> output = new HashMap<>();
        edges.forEach((key, edge) -> {
            output.put(key, edge.getValue());
        });
        return output;
    }
}
