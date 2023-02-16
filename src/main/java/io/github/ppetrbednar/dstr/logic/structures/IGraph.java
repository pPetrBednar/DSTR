package io.github.ppetrbednar.dstr.logic.structures;

import java.util.*;

public interface IGraph {
    void clear();

    boolean isEmpty();

    int size();

    void addVertex(Vertex vertex);

    void addEdge(Edge edge);

    Vertex removeVertex(String key);

    Edge removeEdge(String key);

    Vertex getVertex(String key);

    Edge getEdge(String key);

    Map<String, Vertex> getVertices();

    Map<String, Edge> getEdges();

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
            adjacent.getValues().getShortestPath().add(source);
        }
    }

    public static List<Vertex> getShortestPath(Graph graph, Vertex source, Vertex target) {
        if (graph.getVertex(source.getKey()).getValues().getDistance() != 0) {
            graph = IGraph.calculateShortestPathFromSource(graph, source);
        }

        List<Vertex> shortestPath = graph.getVertex(target.getKey()).getValues().getShortestPath();
        shortestPath.add(target);
        return shortestPath;
    }

    public static List<Vertex> getShortestPath(Graph graph, String sourceKey, String targetKey) {
        return getShortestPath(graph, graph.getVertex(sourceKey), graph.getVertex(targetKey));
    }
}

   /* Vytvoř
            Zruš
    JePrázdný (­Boolean)
    Mohutnost (­PočetPrvků)
    Prohlídka(ØTyp, ØPočátek, ØAkce) vrcholová/hranová, do hloubky/šířky
    VložVrchol(ØVrchol)
    VložHranu(ØHrana)
    OdeberVrchol(ØKlíč, ­Vrchol)
    OdeberHranu(ØKlíč, ­Hrana)
    NajdiVrchol (ØKlíč, ­Vrchol)
    NajdiHranu(ØKlíč, ­Hrana)
    ZpřístupniNásledníky(ØKoho,­Prvky)
    ZpřístupniPředchůdce(ØKoho,­Prvky)
    ZpřístupniIncidenčníPrvky(ØKoho,­Prvky)
    DefinujBránu(ØPrvek)
    AnulujBránu(ØPrvek)
    ZpřístupniBrány(­Prvky)
B. Sjednocení (ØGrafA, ØGrafB, ­GrafC)+*/

