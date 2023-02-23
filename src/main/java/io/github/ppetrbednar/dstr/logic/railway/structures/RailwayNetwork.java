package io.github.ppetrbednar.dstr.logic.railway.structures;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.RailwayNetworkLoadException;
import io.github.ppetrbednar.dstr.logic.railway.pathfinding.EnhancedDijkstraAlgorithm;
import io.github.ppetrbednar.dstr.logic.railway.pathfinding.DijkstraAlgorithmWithTraveller;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RailwayNetwork {
    private String uid;
    private Graph<String, Switch, String, Rail> network;

    public RailwayNetwork(String uid, Graph<String, Switch, String, Rail> network) {
        this.uid = uid;
        this.network = network;
    }

    public RailwayPath getShortestValidPath(String source, String target, int length) {
        try {
            return EnhancedDijkstraAlgorithm.getShortestValidPath(network, source, target, length);
        } catch (NoPathFoundException e) {
            System.out.println("No path found");
        }
        return null;
    }

    public void printShortestValidPath(String source, String target, int length) {
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println("L = " + length);

        try {
            RailwayPath path = EnhancedDijkstraAlgorithm.getShortestValidPath(network, source, target, length);
            path.print(network.getVertexValue(source), network.getVertexValue(target));
        } catch (NoPathFoundException e) {
            System.out.println("No path found");
        }
    }

    public void printShortestValidPathLegacy(String source, String target, int length) {
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println("L = " + length);


        try {
            RailwayPath path = DijkstraAlgorithmWithTraveller.getShortestValidPath(network, source, target, length);
            path.print(network.getVertexValue(source), network.getVertexValue(target));
        } catch (NoPathFoundException e) {
            System.out.println("No path found");
        }
    }

    public void removeSwitch(String key) {
        network.removeVertex(key);
    }

    public void removeRail(String key) {
        Rail rail = network.getEdgeValue(key);
        network.removeEdge(key);
    }

    public void addSwitch(String key, Position position) {
        network.addVertex(key, new Switch(key, position));
    }

    public void addRail(String key, String left, String right, int length) {
        Rail rail = new Rail(key, left, right, length);
        network.addEdge(key, left, right, rail);
    }

    public static RailwayNetwork loadRailwayNetwork(File file) throws RailwayNetworkLoadException {
        try {
            String jsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(jsonString);
            return JsonPackager.jsonObjectToRailwayNetwork(jsonObject);
        } catch (IOException e) {
            throw new RailwayNetworkLoadException("Loading failed");
        } catch (JsonException e) {
            throw new RailwayNetworkLoadException("Loaded json file corrupted");
        }
    }

    public static void saveRailwayNetwork(RailwayNetwork railwayNetwork, File file) {
        try {
            JsonObject json = JsonPackager.networkToJsonObject(railwayNetwork);
            FileUtils.writeStringToFile(file, Jsoner.prettyPrint(json.toJson()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Saving failed");
        }
    }

    public String getUid() {
        return uid;
    }

    public Graph<String, Switch, String, Rail> getNetwork() {
        return network;
    }
}
