package io.github.ppetrbednar.dstr.logic;

import io.github.ppetrbednar.dstr.logic.structures.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;

public class DSTRController {
    public static void test() {
        System.out.println("start");
        Graph network = new Graph();

       /* Vertex A = new Vertex("A");
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

        Graph net = Pathfinder.calculateShortestPathFromSource(network, A);
        System.out.println("done");
        String out = "";
        for (Vertex v : Pathfinder.getShortestPath(net, A, F)) {
            out += v.getKey() + " -> ";
        }
        out = out.substring(0, out.length() - 3);
        System.out.println(out);*/


        Vertex v01 = new Vertex("V01");
        Vertex v02 = new Vertex("V02");
        Vertex v03 = new Vertex("V03");
        Vertex v04 = new Vertex("V04");
        Vertex v06 = new Vertex("V06");
        Vertex v07 = new Vertex("V07");
        Vertex v08 = new Vertex("V08");
        Vertex v09 = new Vertex("V09");
        Vertex v10 = new Vertex("V10");
        Vertex v11 = new Vertex("V11");
        Vertex v12 = new Vertex("V12");
        Vertex v13 = new Vertex("V13");
        Vertex v14 = new Vertex("V14");
        Vertex v15 = new Vertex("V15");
        Vertex v16 = new Vertex("V16");
        Vertex v17 = new Vertex("V17");
        Vertex v18 = new Vertex("V18");
        Vertex v19 = new Vertex("V19");
        Vertex v20 = new Vertex("V20");
        Vertex v21 = new Vertex("V21");
        Vertex v22 = new Vertex("V22");
        Vertex v23 = new Vertex("V23");

        network.addVertex(v01);
        network.addVertex(v02);
        network.addVertex(v03);
        network.addVertex(v04);
        network.addVertex(v06);
        network.addVertex(v07);
        network.addVertex(v08);
        network.addVertex(v09);
        network.addVertex(v10);
        network.addVertex(v11);
        network.addVertex(v12);
        network.addVertex(v13);
        network.addVertex(v14);
        network.addVertex(v15);
        network.addVertex(v16);
        network.addVertex(v17);
        network.addVertex(v18);
        network.addVertex(v19);
        network.addVertex(v20);
        network.addVertex(v21);
        network.addVertex(v22);
        network.addVertex(v23);

        network.addEdge(new Edge("E01", 157), "V01", "V03");
        network.addEdge(new Edge("E02", 100), "V02", "V04");
        network.addEdge(new Edge("E03", 20), "V06", "V08");
        network.addEdge(new Edge("E04", 280), "V09", "V11");
        // network.addEdge(new Edge("E05", 259), "V10", "V12");
        network.addEdge(new Edge("E06", 727), "V07", "V18");
        network.addEdge(new Edge("E07", 40), "V13", "V14");
        network.addEdge(new Edge("E08", 139), "V15", "V20");
        network.addEdge(new Edge("E09", 302), "V16", "V17");
        network.addEdge(new Edge("E10", 236), "V21", "V23");
        network.addEdge(new Edge("E11", 523), "V22", "V19");
        network.addEdge(new Edge("E12", 73), "V03", "V07");
        network.addEdge(new Edge("E13", 85), "V04", "V07");
        network.addEdge(new Edge("E14", 80), "V04", "V06");
        network.addEdge(new Edge("E16", 34), "V08", "V09");
        network.addEdge(new Edge("E17", 41), "V08", "V10");
        // network.addEdge(new Edge("E18", 34), "V11", "V13");
        network.addEdge(new Edge("E19", 40), "V12", "V13");
        network.addEdge(new Edge("E20", 38), "V14", "V15");
        network.addEdge(new Edge("E21", 41), "V14", "V16");
        network.addEdge(new Edge("E22", 36), "V17", "V19");
        network.addEdge(new Edge("E23", 40), "V18", "V19");
        network.addEdge(new Edge("E24", 33), "V20", "V21");
        network.addEdge(new Edge("E25", 60), "V20", "V22");


        /*   network.addEdge(new Edge("S01", 165), "V06", "V07");
        network.addEdge(new Edge("S02", 158), "V03", "V04");
        network.addEdge(new Edge("S03", 75), "V09", "V10");
       // network.addEdge(new Edge("S04", 74), "V11", "V12");
        network.addEdge(new Edge("S05", 79), "V15", "V16");
        network.addEdge(new Edge("S06", 76), "V17", "V18");
        network.addEdge(new Edge("S07", 93), "V21", "V22");*/

        /*
        SP 01
         */
        Vertex sv01 = new Vertex("SV01");
        Vertex sv02 = new Vertex("SV02");
        Vertex sv03 = new Vertex("SV03");

        network.addVertex(sv01);
        network.addVertex(sv02);
        network.addVertex(sv03);

        network.addEdge(new Edge("SE01", 25), "SV01", "V02");
        network.addEdge(new Edge("SE02", 100), "SV02", "SV01");
        network.addEdge(new Edge("SE03", 60), "SV03", "SV01");

        sv01.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("SE02"), network.getEdge("SE03")));
        /*
            /SP01
        */

        v04.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E14"), network.getEdge("E13")));
        v07.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E12"), network.getEdge("E13")));
        v08.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E16"), network.getEdge("E17")));
        v13.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E18"), network.getEdge("E19")));
        v14.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E20"), network.getEdge("E21")));
        v19.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E22"), network.getEdge("E23")));
        v20.getValues().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E24"), network.getEdge("E25")));

        String source = "V23";
        String target = "V11";
        int l = 150;

        RailwayPath path = Pathfinder.getShortestPathWithTraveller(network, source, target, l);
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println("L = " + l);
        path.print(network.getVertex(source), network.getVertex(target));

      /*  System.out.println("done");
        String out = "";
        for (Direction v : Pathfinder.getShortestPath(network, network.getVertex("V23"), network.getVertex("V11"))) {
            out += v.getVertex().getKey() + " -> " + (v.getEdge() == null ? "" : (v.getEdge().getKey() + " -> "));
        }
        out = out.substring(0, out.length() - 3);
        System.out.println(out);
        for (Vertex v : network.getVertices().values()) {
            String pom = "";
            for (Direction j : v.getValues().getShortestPath()) {
                pom += j.getVertex().getKey() + " -> " + (j.getEdge() == null ? "" : (j.getEdge().getKey() + " -> "));
            }
            System.out.println(v.getKey() + ": " + v.getValues().getDistance() + ": " + pom);
        }*/
    }
}
