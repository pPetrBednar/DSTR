package io.github.ppetrbednar.dstr.ui.module.root;


import io.github.ppetrbednar.dstr.logic.DSTRController;
import io.github.ppetrbednar.dstr.logic.RailwayVisualizer;
import io.github.ppetrbednar.dstr.logic.railway.ui.ActionType;
import io.github.ppetrbednar.dstr.ui.Root;
import io.github.ppetrbednar.dstr.ui.handler.ICompositor;
import io.github.ppetrbednar.dstr.ui.handler.Module;
import io.github.ppetrbednar.dstr.ui.handler.ViewType;
import io.github.ppetrbednar.dstr.ui.module.panel.RootBar;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 */
public class Main extends Module<Main, Root> {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    private final Compositor compositor = new Compositor();
    @FXML
    private AnchorPane container;
    private final RailwayVisualizer visualizer = new RailwayVisualizer();

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void modeAddSwitch(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.ADD_SWITCH);
    }

    public void modeAddRail(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.ADD_RAIL);
    }

    public void modeAddIllegalTransition(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.ADD_ILLEGAL_TRANSITION);
    }

    public void modeRemoveSwitch(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.REMOVE_SWITCH);
    }

    public void modeRemoveRail(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.REMOVE_RAIL);
    }

    public void load(ActionEvent actionEvent) {
        visualizer.load();
    }

    public void save(ActionEvent actionEvent) {
        visualizer.save();
    }

    public void simulate(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.SIMULATE);
    }

    public void clearSimulation(ActionEvent actionEvent) {
        visualizer.clearSimulation();
    }

    public void modeRemoveIllegalTransition(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.REMOVE_ILLEGAL_TRANSITION);
    }

    private class Compositor implements ICompositor {

        @Override
        public void compose(ViewType type) {
            decompose();

            Platform.runLater(() -> {
                visualizer.setPane(container);
                visualizer.recalculate();
                visualizer.printGrid();
            });
        }

        @Override
        public void decompose() {
            container.getChildren().clear();
        }
    }

    public void compose(ViewType type) {
        compositor.compose(type);
    }
}
