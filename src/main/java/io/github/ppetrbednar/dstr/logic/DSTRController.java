package io.github.ppetrbednar.dstr.logic;

import io.github.ppetrbednar.dstr.logic.railway.*;
import io.github.ppetrbednar.dstr.logic.graph.*;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.NoPathFoundException;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.RailwayNetworkLoadException;
import io.github.ppetrbednar.dstr.logic.railway.structures.*;

import java.io.File;

public class DSTRController {
    public static void test() {

        /*
        RailwayNetwork railway = new RailwayNetwork("Example railway", network);
        RailwayNetwork.saveRailwayNetwork(railway, file);
*/
        example2();
    }

    private static void example1() {
        File file = new File("./railway_network_example.json");
        try {
            RailwayNetwork railwayNetwork = RailwayNetwork.loadRailwayNetwork(file);

            String source = "V23";
            String target = "V11";
            int length = 150;

            railwayNetwork.removeRail("E05");
            railwayNetwork.removeRail("E18");

            railwayNetwork.printShortestValidPath(source, target, length);
        } catch (RailwayNetworkLoadException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void example2() {
        File file = new File("./railway_network_example.json");
        try {
            RailwayNetwork railwayNetwork = RailwayNetwork.loadRailwayNetwork(file);

            railwayNetwork.addSwitch("SV101");
            railwayNetwork.addRail("SE101", "V13", "SV101", 200);

            String source = "V23";
            String target = "V11";
            int length = 150;

            railwayNetwork.removeRail("E05");
            railwayNetwork.removeRail("E18");

            railwayNetwork.printShortestValidPath(source, target, length);
        } catch (RailwayNetworkLoadException e) {
            System.out.println(e.getMessage());
        }
    }
}
