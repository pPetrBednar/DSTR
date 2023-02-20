package io.github.ppetrbednar.dstr.logic.railway.structures;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.graph.Vertex;
import io.github.ppetrbednar.dstr.logic.railway.PathFinder;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.RailwayNetworkLoadException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RailwayNetwork {
    private String uid;
    private Graph<String, Switch, Rail> network;

    public RailwayNetwork(String uid, Graph<String, Switch, Rail> network) {
        this.uid = uid;
        this.network = network;
    }

    public void printShortestValidPath(String source, String target, int length) {
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println("L = " + length);

        RailwayPath path = null;
        try {
            path = PathFinder.getShortestValidPath(network, source, target, length);
            path.print(network.getVertex(source), network.getVertex(target));
        } catch (NoPathFoundException e) {
            System.out.println("No path found");
        }
    }

    public void removeSwitch(String key) {
        network.removeVertex(key);
    }

    public void removeRail(String key) {
        network.removeEdge(key);
    }

    public void addSwitch(String key) {
        network.addVertex(new Vertex<>(key, new Switch()));
    }

    public void addRail(String key, String leftSwitchKey, String rightSwitchKey, int length) {
        network.addEdge(key, leftSwitchKey, rightSwitchKey, new Rail(length));
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

    public Graph<String, Switch, Rail> getNetwork() {
        return network;
    }
}
