package io.github.ppetrbednar.dstr.logic.railway.testing;

public class RailwayExampleObject {
    public static void test() {
      /*  Graph<String, Switch, Rail> network = new Graph<>();

        Vertex<String, Switch, Rail> v01 = new Vertex<>("V01", new Switch());
        Vertex<String, Switch, Rail> v02 = new Vertex<>("V02", new Switch());
        Vertex<String, Switch, Rail> v03 = new Vertex<>("V03", new Switch());
        Vertex<String, Switch, Rail> v04 = new Vertex<>("V04", new Switch());
        Vertex<String, Switch, Rail> v06 = new Vertex<>("V06", new Switch());
        Vertex<String, Switch, Rail> v07 = new Vertex<>("V07", new Switch());
        Vertex<String, Switch, Rail> v08 = new Vertex<>("V08", new Switch());
        Vertex<String, Switch, Rail> v09 = new Vertex<>("V09", new Switch());
        Vertex<String, Switch, Rail> v10 = new Vertex<>("V10", new Switch());
        Vertex<String, Switch, Rail> v11 = new Vertex<>("V11", new Switch());
        Vertex<String, Switch, Rail> v12 = new Vertex<>("V12", new Switch());
        Vertex<String, Switch, Rail> v13 = new Vertex<>("V13", new Switch());
        Vertex<String, Switch, Rail> v14 = new Vertex<>("V14", new Switch());
        Vertex<String, Switch, Rail> v15 = new Vertex<>("V15", new Switch());
        Vertex<String, Switch, Rail> v16 = new Vertex<>("V16", new Switch());
        Vertex<String, Switch, Rail> v17 = new Vertex<>("V17", new Switch());
        Vertex<String, Switch, Rail> v18 = new Vertex<>("V18", new Switch());
        Vertex<String, Switch, Rail> v19 = new Vertex<>("V19", new Switch());
        Vertex<String, Switch, Rail> v20 = new Vertex<>("V20", new Switch());
        Vertex<String, Switch, Rail> v21 = new Vertex<>("V21", new Switch());
        Vertex<String, Switch, Rail> v22 = new Vertex<>("V22", new Switch());
        Vertex<String, Switch, Rail> v23 = new Vertex<>("V23", new Switch());

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

        network.addEdge("E01", "V01", "V03", new Rail(157));
        network.addEdge("E02", "V02", "V04", new Rail(100));
        network.addEdge("E03", "V06", "V08", new Rail(20));
        network.addEdge("E04", "V09", "V11", new Rail(280));
        network.addEdge("E05", "V10", "V12", new Rail(259));
        network.addEdge("E06", "V07", "V18", new Rail(727));
        network.addEdge("E07", "V13", "V14", new Rail(40));
        network.addEdge("E08", "V15", "V20", new Rail(139));
        network.addEdge("E09", "V16", "V17", new Rail(302));
        network.addEdge("E10", "V21", "V23", new Rail(236));
        network.addEdge("E11", "V22", "V19", new Rail(523));
        network.addEdge("E12", "V03", "V07", new Rail(73));
        network.addEdge("E13", "V04", "V07", new Rail(85));
        network.addEdge("E14", "V04", "V06", new Rail(80));
        network.addEdge("E16", "V08", "V09", new Rail(34));
        network.addEdge("E17", "V08", "V10", new Rail(41));
        network.addEdge("E18", "V11", "V13", new Rail(34));
        network.addEdge("E19", "V12", "V13", new Rail(40));
        network.addEdge("E20", "V14", "V15", new Rail(38));
        network.addEdge("E21", "V14", "V16", new Rail(41));
        network.addEdge("E22", "V17", "V19", new Rail(36));
        network.addEdge("E23", "V18", "V19", new Rail(40));
        network.addEdge("E24", "V20", "V21", new Rail(33));
        network.addEdge("E25", "V20", "V22", new Rail(60));


        Vertex<String, Switch, Rail> sv01 = new Vertex<>("SV01", new Switch());
        Vertex<String, Switch, Rail> sv02 = new Vertex<>("SV02", new Switch());
        Vertex<String, Switch, Rail> sv03 = new Vertex<>("SV03", new Switch());

        network.addVertex(sv01);
        network.addVertex(sv02);
        network.addVertex(sv03);

        network.addEdge("SE01", "SV01", "V02", new Rail(25));
        network.addEdge("SE02", "SV02", "SV01", new Rail(100));
        network.addEdge("SE03", "SV03", "SV01", new Rail(60));

        sv01.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("SE02"), network.getEdge("SE03")));

        v04.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E14"), network.getEdge("E13")));
        v07.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E12"), network.getEdge("E13")));
        v08.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E16"), network.getEdge("E17")));
        v13.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E18"), network.getEdge("E19")));
        v14.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E20"), network.getEdge("E21")));
        v19.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E22"), network.getEdge("E23")));
        v20.getValue().getIllegalTransitions().add(new IllegalTransition(network.getEdge("E24"), network.getEdge("E25")));

        RailwayNetwork railwayNetwork = new RailwayNetwork("Example", network);
        String source = "V23";
        String target = "V11";
        int length = 150;

        railwayNetwork.removeRail("E05");
        railwayNetwork.removeRail("E18");

        railwayNetwork.printShortestValidPath(source, target, length);*/
    }
}
