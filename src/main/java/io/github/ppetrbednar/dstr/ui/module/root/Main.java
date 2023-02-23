package io.github.ppetrbednar.dstr.ui.module.root;


import com.jfoenix.controls.JFXButton;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashSet;
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
    private final RailwayVisualizer visualizer = new RailwayVisualizer(this);
    private HashSet<JFXButton> buttons;
    @FXML
    private JFXButton addSwitchBtn;
    @FXML
    private JFXButton addRailBtn;
    @FXML
    private JFXButton addIllegalTransitionBtn;
    @FXML
    private JFXButton removeSwitchBtn;
    @FXML
    private JFXButton removeRailBtn;
    @FXML
    private JFXButton removeIllegalTransition;
    @FXML
    private JFXButton simulateBtn;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        buttons = new HashSet<>();
        buttons.add(addSwitchBtn);
        buttons.add(addRailBtn);
        buttons.add(addIllegalTransitionBtn);
        buttons.add(removeSwitchBtn);
        buttons.add(removeRailBtn);
        buttons.add(removeIllegalTransition);
        buttons.add(simulateBtn);
    }

    private void selectBtn(JFXButton btn, String style) {
        Platform.runLater(() -> {
            buttons.forEach(jfxButton -> {
                if (jfxButton == btn) {
                    jfxButton.getStyleClass().add(style);
                } else {
                    jfxButton.getStyleClass().remove("btn-style-2");
                    jfxButton.getStyleClass().remove("btn-style-3");
                }
            });
        });
    }

    public void modeAddSwitch(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.ADD_SWITCH);
        selectBtn(addSwitchBtn, "btn-style-3");
    }

    public void modeAddRail(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.ADD_RAIL);
        selectBtn(addRailBtn, "btn-style-3");
    }

    public void modeAddIllegalTransition(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.ADD_ILLEGAL_TRANSITION);
        selectBtn(addIllegalTransitionBtn, "btn-style-3");
    }

    public void modeRemoveSwitch(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.REMOVE_SWITCH);
        selectBtn(removeSwitchBtn, "btn-style-2");
    }

    public void modeRemoveRail(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.REMOVE_RAIL);
        selectBtn(removeRailBtn, "btn-style-2");
    }

    public void load(ActionEvent actionEvent) {
        visualizer.load();
    }

    public void save(ActionEvent actionEvent) {
        visualizer.save();
    }

    public void simulate(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.SIMULATE);
        selectBtn(simulateBtn, "btn-style-3");
    }

    public void clearSimulation(ActionEvent actionEvent) {
        visualizer.clearSimulation();
    }

    public void modeRemoveIllegalTransition(ActionEvent actionEvent) {
        visualizer.setActionType(ActionType.REMOVE_ILLEGAL_TRANSITION);
        selectBtn(removeIllegalTransition, "btn-style-2");
    }

    public void showSwitchTooltips(MouseEvent mouseEvent) {
        visualizer.showSwitchTooltips();
    }

    public void hideSwitchTooltips(MouseEvent mouseEvent) {
        visualizer.hideSwitchTooltips();
    }

    public void showRailTooltips(MouseEvent mouseEvent) {
        visualizer.showRailTooltips();
    }

    public void hideRailTooltips(MouseEvent mouseEvent) {
        visualizer.hideRailTooltips();
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
