package io.github.ppetrbednar.dstr.logic;

import io.github.ppetrbednar.dstr.logic.structures.Edge;
import io.github.ppetrbednar.dstr.logic.structures.Graph;
import io.github.ppetrbednar.dstr.logic.structures.IGraph;
import io.github.ppetrbednar.dstr.logic.structures.Vertex;

public class DSTRController {
    public static void test() {
        System.out.println("start");
        Graph network = new Graph();

        Vertex A = new Vertex("A");
        Vertex B = new Vertex("B");
        Vertex C = new Vertex("C");
        Vertex D = new Vertex("D");
        Vertex E = new Vertex("E");
        Vertex F = new Vertex("F");

        network.addVertex(A);
        network.addVertex(B);
        network.addVertex(C);
        network.addVertex(D);
        network.addVertex(E);
        network.addVertex(F);

        network.addEdge(new Edge("ab", A, B, 10));
        network.addEdge(new Edge("ac", A, C, 15));

        network.addEdge(new Edge("bd", B, D, 12));
        network.addEdge(new Edge("bf", B, F, 15));

        network.addEdge(new Edge("ce", C, E, 10));

        network.addEdge(new Edge("de", D, E, 2));
        network.addEdge(new Edge("df", D, F, 1));

        network.addEdge(new Edge("fe", F, E, 5));

        Graph net = IGraph.calculateShortestPathFromSource(network, A);
        System.out.println("done");
        String out = "";
        for (Vertex v : IGraph.getShortestPath(net, A, F)) {
            out += v.getKey() + " -> ";
        }
        out = out.substring(0, out.length() - 3);
        System.out.println(out);
    }
}
