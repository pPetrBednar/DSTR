package io.github.ppetrbednar.dstr.logic.railway.structures;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.github.ppetrbednar.dstr.logic.graph.Graph;

import java.math.BigDecimal;
import java.util.LinkedList;

public class JsonPackager {
    private static JsonObject vertexToJsonObject(Switch vertex) {
        JsonObject obj = new JsonObject();
        obj.put("key", vertex.getKey());
        obj.put("illegalTransitions", illegalTransitionsToJsonArray(vertex));
        obj.put("x", vertex.getPosition().x());
        obj.put("y", vertex.getPosition().y());
        return obj;
    }

    private static void jsonObjectOfVertexToNetwork(Graph<String, Switch, String, Rail> network, JsonObject json) {
        String key = (String) json.get("key");
        int x = ((BigDecimal) json.get("x")).intValue();
        int y = ((BigDecimal) json.get("y")).intValue();
        network.addVertex((String) json.get("key"), new Switch(key, new Position(x, y)));
    }

    private static JsonObject edgeToJsonObject(Rail edge) {
        JsonObject obj = new JsonObject();
        obj.put("key", edge.key());
        obj.put("left", edge.left());
        obj.put("right", edge.right());
        obj.put("length", edge.length());
        return obj;
    }

    private static void jsonObjectOfEdgeToNetwork(Graph<String, Switch, String, Rail> network, JsonObject json) {
        String key = (String) json.get("key");
        String left = (String) json.get("left");
        String right = (String) json.get("right");
        int length = ((BigDecimal) json.get("length")).intValue();

        network.addEdge(
                key,
                left,
                right,
                new Rail(key, left, right, length)
        );
    }

    private static JsonObject illegalTransitionToJsonObject(Transition transition) {
        JsonObject obj = new JsonObject();
        obj.put("left", transition.left().key());
        obj.put("right", transition.right().key());
        return obj;
    }

    private static Transition jsonObjectToIllegalTransition(Graph<String, Switch, String, Rail> network, Switch vertex, JsonObject json) {
        return new Transition(network.getEdgeValue((String) json.get("left")), vertex, network.getEdgeValue((String) json.get("right")));
    }

    private static JsonArray verticiesToJsonArray(LinkedList<Switch> vertices) {
        JsonArray arr = new JsonArray();
        vertices.forEach(vertex -> {
            arr.add(vertexToJsonObject(vertex));
        });
        return arr;
    }

    private static void jsonArrayOfVerticesToNetwork(Graph<String, Switch, String, Rail> network, JsonArray array) {
        array.forEach(jsonObject -> {
            jsonObjectOfVertexToNetwork(network, (JsonObject) jsonObject);
        });
    }

    private static JsonArray edgesToJsonArray(LinkedList<Rail> edges) {
        JsonArray arr = new JsonArray();
        edges.forEach(edge -> {
            arr.add(edgeToJsonObject(edge));
        });
        return arr;
    }

    private static void jsonArrayOfEdgesToNetwork(Graph<String, Switch, String, Rail> network, JsonArray array) {
        array.forEach(jsonObject -> {
            jsonObjectOfEdgeToNetwork(network, (JsonObject) jsonObject);
        });
    }


    private static JsonArray illegalTransitionsToJsonArray(Switch vertex) {
        JsonArray arr = new JsonArray();
        vertex.getIllegalTransitions().forEach(transition -> {
            arr.add(illegalTransitionToJsonObject(transition));
        });
        return arr;
    }

    private static void jsonArrayOfIllegalTransitionsToNetwork(Graph<String, Switch, String, Rail> network, Switch vertex, JsonArray array) {
        array.forEach(jsonObject -> {
            Transition illegalTransition = jsonObjectToIllegalTransition(network, vertex, (JsonObject) jsonObject);
            vertex.getIllegalTransitions().add(illegalTransition);
        });
    }

    private static void jsonArrayOfIllegalTransitionsFromVerticesToNetwork(Graph<String, Switch, String, Rail> network, JsonArray array) {
        array.forEach(jsonObject -> {
            JsonObject jsonVertex = (JsonObject) jsonObject;
            jsonArrayOfIllegalTransitionsToNetwork(
                    network,
                    network.getVertexValue((String) jsonVertex.get("key")),
                    (JsonArray) jsonVertex.get("illegalTransitions")
            );
        });
    }

    public static JsonObject networkToJsonObject(RailwayNetwork railway) {
        JsonObject obj = new JsonObject();
        obj.put("uid", railway.getUid());
        obj.put("vertices", verticiesToJsonArray(new LinkedList<>(railway.getNetwork().getVertexValues().values())));
        obj.put("edges", edgesToJsonArray(new LinkedList<>(railway.getNetwork().getEdgeValues().values())));
        return obj;
    }

    public static RailwayNetwork jsonObjectToRailwayNetwork(JsonObject jsonObject) {
        String uid = (String) jsonObject.get("uid");
        Graph<String, Switch, String, Rail> network = new Graph<>();
        jsonArrayOfVerticesToNetwork(network, (JsonArray) jsonObject.get("vertices"));
        jsonArrayOfEdgesToNetwork(network, (JsonArray) jsonObject.get("edges"));
        jsonArrayOfIllegalTransitionsFromVerticesToNetwork(network, (JsonArray) jsonObject.get("vertices"));
        return new RailwayNetwork(uid, network);
    }
}
