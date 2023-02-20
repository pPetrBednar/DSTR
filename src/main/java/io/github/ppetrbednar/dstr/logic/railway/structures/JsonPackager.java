package io.github.ppetrbednar.dstr.logic.railway.structures;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.github.ppetrbednar.dstr.logic.graph.Edge;
import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.graph.Vertex;

import java.math.BigDecimal;
import java.util.LinkedList;

public class JsonPackager {
    private static JsonObject vertexToJsonObject(Vertex<String, Switch, Rail> vertex) {
        JsonObject obj = new JsonObject();
        obj.put("key", vertex.getKey());
        obj.put("illegalTransitions", illegalTransitionsToJsonArray(vertex));
        return obj;
    }

    private static void jsonObjectOfVertexToNetwork(Graph<String, Switch, Rail> network, JsonObject json) {
        network.addVertex(new Vertex<>((String) json.get("key"), new Switch()));
    }

    private static JsonObject edgeToJsonObject(Edge<String, Switch, Rail> edge) {
        JsonObject obj = new JsonObject();
        obj.put("key", edge.getKey());
        obj.put("left", edge.getLeft().getKey());
        obj.put("right", edge.getRight().getKey());
        obj.put("length", edge.getValue().length());
        return obj;
    }

    private static void jsonObjectOfEdgeToNetwork(Graph<String, Switch, Rail> network, JsonObject json) {
        network.addEdge(
                (String) json.get("key"),
                (String) json.get("left"),
                (String) json.get("right"),
                new Rail(((BigDecimal) json.get("length")).intValue())
        );
    }

    private static JsonObject illegalTransitionToJsonObject(IllegalTransition transition) {
        JsonObject obj = new JsonObject();
        obj.put("left", transition.getLeft().getKey());
        obj.put("right", transition.getRight().getKey());
        return obj;
    }

    private static IllegalTransition jsonObjectToIllegalTransition(Graph<String, Switch, Rail> network, JsonObject json) {
        return new IllegalTransition(network.getEdge((String) json.get("left")), network.getEdge((String) json.get("right")));
    }

    private static JsonArray verticiesToJsonArray(LinkedList<Vertex<String, Switch, Rail>> verticies) {
        JsonArray arr = new JsonArray();
        verticies.forEach(vertex -> {
            arr.add(vertexToJsonObject(vertex));
        });
        return arr;
    }

    private static void jsonArrayOfVerticesToNetwork(Graph<String, Switch, Rail> network, JsonArray array) {
        array.forEach(jsonObject -> {
            jsonObjectOfVertexToNetwork(network, (JsonObject) jsonObject);
        });
    }

    private static JsonArray edgesToJsonArray(LinkedList<Edge<String, Switch, Rail>> edges) {
        JsonArray arr = new JsonArray();
        edges.forEach(edge -> {
            arr.add(edgeToJsonObject(edge));
        });
        return arr;
    }

    private static void jsonArrayOfEdgesToNetwork(Graph<String, Switch, Rail> network, JsonArray array) {
        array.forEach(jsonObject -> {
            jsonObjectOfEdgeToNetwork(network, (JsonObject) jsonObject);
        });
    }


    private static JsonArray illegalTransitionsToJsonArray(Vertex<String, Switch, Rail> vertex) {
        JsonArray arr = new JsonArray();
        vertex.getValue().getIllegalTransitions().forEach(transition -> {
            arr.add(illegalTransitionToJsonObject(transition));
        });
        return arr;
    }

    private static void jsonArrayOfIllegalTransitionsToNetwork(Graph<String, Switch, Rail> network, Vertex<String, Switch, Rail> vertex, JsonArray array) {
        array.forEach(jsonObject -> {
            IllegalTransition illegalTransition = jsonObjectToIllegalTransition(network, (JsonObject) jsonObject);
            vertex.getValue().getIllegalTransitions().add(illegalTransition);
        });
    }

    private static void jsonArrayOfIllegalTransitionsFromVerticesToNetwork(Graph<String, Switch, Rail> network, JsonArray array) {
        array.forEach(jsonObject -> {
            JsonObject jsonVertex = (JsonObject) jsonObject;
            jsonArrayOfIllegalTransitionsToNetwork(
                    network,
                    network.getVertex((String) jsonVertex.get("key")),
                    (JsonArray) jsonVertex.get("illegalTransitions")
            );
        });
    }

    public static JsonObject networkToJsonObject(RailwayNetwork railway) {
        JsonObject obj = new JsonObject();
        obj.put("uid", railway.getUid());
        obj.put("vertices", verticiesToJsonArray(new LinkedList<>(railway.getNetwork().getVertices().values())));
        obj.put("edges", edgesToJsonArray(new LinkedList<>(railway.getNetwork().getEdges().values())));
        return obj;
    }

    public static RailwayNetwork jsonObjectToRailwayNetwork(JsonObject jsonObject) {
        String uid = (String) jsonObject.get("uid");
        Graph<String, Switch, Rail> network = new Graph<>();
        jsonArrayOfVerticesToNetwork(network, (JsonArray) jsonObject.get("vertices"));
        jsonArrayOfEdgesToNetwork(network, (JsonArray) jsonObject.get("edges"));
        jsonArrayOfIllegalTransitionsFromVerticesToNetwork(network, (JsonArray) jsonObject.get("vertices"));
        return new RailwayNetwork(uid, network);
    }
}
