package io.github.ppetrbednar.dstr.logic;

import io.github.ppetrbednar.dstr.logic.graph.Graph;
import io.github.ppetrbednar.dstr.logic.railway.exceptions.RailwayNetworkLoadException;
import io.github.ppetrbednar.dstr.logic.railway.structures.*;
import io.github.ppetrbednar.dstr.logic.railway.ui.ActionType;
import io.github.ppetrbednar.dstr.ui.module.root.Main;
import io.github.ppetrbednar.dstr.ui.window.alert.AlertManager;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.util.HashMap;

public class RailwayVisualizer {

    private final HashMap<String, Node> nodes;
    private File save;
    private final Main window;
    private AnchorPane pane;
    private final int cellCountWidth = 60;
    private final int cellCountHeight = 30;
    private final double tooltipRailWidth = 40;
    private final double tooltipRailHeight = 40;
    private final double tooltipSwitchWidth = 40;
    private final double tooltipSwitchHeight = 40;
    private double cellSizeWidth;
    private double cellSizeHeight;
    private RailwayNetwork railwayNetwork;
    private ActionType actionType = ActionType.NONE;
    private int actionCounter;
    private String tempValue1;
    private String tempValue2;
    private RailwayPath path;
    private final HashMap<String, Tooltip> switchTooltips;
    private final HashMap<String, Tooltip> railTooltips;

    public RailwayVisualizer(Main main) {
        this.railwayNetwork = new RailwayNetwork("", new Graph<>());
        nodes = new HashMap<>();
        this.window = main;
        switchTooltips = new HashMap<>();
        railTooltips = new HashMap<>();
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
        actionCounter = 0;
        tempValue1 = null;
        tempValue2 = null;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    public void recalculate() {
        cellSizeWidth = pane.getWidth() / cellCountWidth;
        cellSizeHeight = pane.getHeight() / cellCountHeight;
    }

    public void printGrid() {
        for (int y = 0; y < cellCountHeight; y++) {
            for (int x = 0; x < cellCountWidth; x++) {
                Circle c = new Circle();
                c.setViewOrder(100);
                c.setFill(Color.rgb(0, 0, 0, 0.2));
                c.setRadius(3);
                c.setLayoutX((x + 0.5) * cellSizeWidth);
                c.setLayoutY((y + 0.5) * cellSizeHeight);

                int finalY = y;
                int finalX = x;
                c.setOnMousePressed(mouseEvent -> {
                    action(finalX, finalY, null);
                });
                c.setCursor(Cursor.HAND);
                pane.getChildren().add(c);
            }
        }
    }

    private void action(int x, int y, String key) {
        switch (actionType) {
            case NONE -> System.out.println(x + " - " + y);
            case ADD_SWITCH -> addSwitch(x, y, key);
            case ADD_RAIL -> addRail(x, y, key);
            case ADD_ILLEGAL_TRANSITION -> addIllegalTransition(x, y, key);
            case REMOVE_SWITCH -> removeSwitch(key);
            case REMOVE_RAIL -> removeRail(key);
            case SIMULATE -> simulate(key);
        }
    }

    private void removeRail(String key) {
        if (key == null || railwayNetwork.getNetwork().getEdgeValue(key) == null) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select rail to remove.", false);
            am.show();
            return;
        }

        railwayNetwork.removeRail(key);
        pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(key));
        nodes.remove(key);
        railTooltips.remove(key);
    }

    private void removeSwitch(String key) {
        if (key == null || railwayNetwork.getNetwork().getVertexValue(key) == null) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select switch to remove.", false);
            am.show();
            return;
        }
        Switch sw = railwayNetwork.getNetwork().getVertexValue(key);
        var rails = railwayNetwork.getNetwork().getEdgeValuesOfVertex(key);
        var illegalTransitions = sw.getIllegalTransitions();

        rails.forEach(rail -> {
            pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(rail.key()));
            nodes.remove(rail.key());
            railTooltips.remove(rail.key());
        });
        illegalTransitions.forEach(transition -> pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(transition.left().key() + transition.point().getKey() + transition.right().key())));

        railwayNetwork.removeSwitch(key);
        pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(key));
        nodes.remove(sw.getKey());
        switchTooltips.remove(key);
    }

    @SuppressWarnings("unchecked")
    private void addSwitch(int x, int y, String key) {

        if (key != null) {
            return;
        }

        AlertManager am = new AlertManager(AlertManager.AlertType.SINGLE_INPUT, "Enter switch id.", true);
        am.clear();
        am.showAndWait(() -> {
            String k = (String) am.getResult();
            if (k != null && !k.isEmpty()) {
                railwayNetwork.addSwitch(k, new Position(x, y));
                printSwitch(x, y, k);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void addRail(int x, int y, String key) {

        switch (actionCounter) {
            case 0 -> {
                if (key == null || railwayNetwork.getNetwork().getVertexValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select first switch to connect rail.", false);
                    am.show();
                    return;
                }
                tempValue1 = key;
                actionCounter++;
            }
            case 1 -> {
                if (key == null || railwayNetwork.getNetwork().getVertexValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select second switch to connect rail.", false);
                    am.show();
                    return;
                }
                AlertManager am = new AlertManager(AlertManager.AlertType.DOUBLE_INPUT, "Enter rail id and rail length.", true);
                am.clear();
                am.showAndWait(() -> {
                    Pair<String, String> keyPair = (Pair<String, String>) am.getResult();
                    if (keyPair != null && !keyPair.getKey().isEmpty() && !keyPair.getValue().isEmpty()) {
                        railwayNetwork.addRail(keyPair.getKey(), tempValue1, key, Integer.parseInt(keyPair.getValue()));
                        printRail(railwayNetwork.getNetwork().getEdgeValue(keyPair.getKey()));

                        actionCounter = 0;
                        tempValue1 = null;
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addIllegalTransition(int x, int y, String key) {

        switch (actionCounter) {
            case 0 -> {
                if (key == null || railwayNetwork.getNetwork().getEdgeValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select first rail of transition.", false);
                    am.show();
                    return;
                }
                tempValue1 = key;
                actionCounter++;
            }
            case 1 -> {
                if (key == null || railwayNetwork.getNetwork().getVertexValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select switch of transition.", false);
                    am.show();
                    return;
                }

                tempValue2 = key;
                actionCounter++;
            }
            case 2 -> {
                if (key == null || railwayNetwork.getNetwork().getEdgeValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select second rail of transition.", false);
                    am.show();
                    return;
                }

                Rail left = railwayNetwork.getNetwork().getEdgeValue(tempValue1);
                Switch sw = railwayNetwork.getNetwork().getVertexValue(tempValue2);
                Rail right = railwayNetwork.getNetwork().getEdgeValue(key);
                Transition transition = new Transition(left, sw, right);
                sw.getIllegalTransitions().add(transition);

                printIllegalTransition(transition);

                actionCounter = 0;
                tempValue1 = null;
                tempValue2 = null;
            }
        }
    }

    private void printSwitch(int x, int y, String key) {
        Circle c = new Circle();
        c.getStyleClass().add("hover-effect-fill");
        c.setId(key);
        c.setViewOrder(30);
        c.setFill(Color.rgb(0, 0, 0, 1));
        c.setRadius(8);
        c.setLayoutX((x + 0.5) * cellSizeWidth);
        c.setLayoutY((y + 0.5) * cellSizeHeight);
        c.setOnMousePressed(mouseEvent -> {
            action(-1, -1, key);
        });

        Tooltip t = new Tooltip(key);
        t.setPrefSize(tooltipSwitchWidth, tooltipSwitchHeight);
        t.setStyle("-fx-font-size: 9px;");
        t.setTextAlignment(TextAlignment.CENTER);
        t.setShowDelay(new Duration(0));
        Tooltip.install(c, t);
        pane.getChildren().add(c);
        nodes.put(key, c);
        switchTooltips.put(key, t);
    }

    private void printRail(Rail rail) {
        Line line = new Line();
        line.getStyleClass().add("hover-effect-stroke");
        line.setId(rail.key());
        line.setStroke(Color.rgb(0, 0, 0, 1));
        line.setStrokeWidth(4);
        line.setViewOrder(50);

        Switch left = railwayNetwork.getNetwork().getVertexValue(rail.left());
        Switch right = railwayNetwork.getNetwork().getVertexValue(rail.right());

        line.setStartX((left.getPosition().x() + 0.5) * cellSizeWidth);
        line.setStartY((left.getPosition().y() + 0.5) * cellSizeHeight);
        line.setEndX((right.getPosition().x() + 0.5) * cellSizeWidth);
        line.setEndY((right.getPosition().y() + 0.5) * cellSizeHeight);

        line.setOnMousePressed(mouseEvent -> {
            action(-1, -1, rail.key());
        });

        Tooltip t = new Tooltip(rail.key() + "\n(" + rail.length() + ")");
        t.setPrefSize(tooltipRailWidth, tooltipRailHeight);
        t.setStyle("-fx-font-size: 9px;");
        t.setTextAlignment(TextAlignment.CENTER);
        t.setShowDelay(new Duration(0));
        Tooltip.install(line, t);
        pane.getChildren().add(line);
        nodes.put(rail.key(), line);
        railTooltips.put(rail.key(), t);
    }

    public void showRailTooltips() {
        Bounds screenBounds = pane.localToScreen(pane.getBoundsInLocal());
        int screenBoundsMinX = (int) screenBounds.getMinX();
        int screenBoundsMinY = (int) screenBounds.getMinY();

        railTooltips.forEach((key, tooltip) -> {
            Rail rail = railwayNetwork.getNetwork().getEdgeValue(key);
            Switch left = railwayNetwork.getNetwork().getVertexValue(rail.left());
            Switch right = railwayNetwork.getNetwork().getVertexValue(rail.right());
            double midPointX = screenBoundsMinX - (tooltipRailWidth / 2) + ((left.getPosition().x() + 0.5) * cellSizeWidth + (right.getPosition().x() + 0.5) * cellSizeWidth) / 2;
            double midPointY = screenBoundsMinY - (tooltipRailHeight / 2) + ((left.getPosition().y() + 0.5) * cellSizeHeight + (right.getPosition().y() + 0.5) * cellSizeHeight) / 2;
            tooltip.show(pane, midPointX, midPointY);
        });
    }

    public void hideRailTooltips() {
        railTooltips.forEach((rail, tooltip) -> tooltip.hide());
    }

    public void showSwitchTooltips() {
        Bounds screenBounds = pane.localToScreen(pane.getBoundsInLocal());
        int screenBoundsMinX = (int) screenBounds.getMinX();
        int screenBoundsMinY = (int) screenBounds.getMinY();

        switchTooltips.forEach((key, tooltip) -> {
            Switch sw = railwayNetwork.getNetwork().getVertexValue(key);
            double pointX = screenBoundsMinX - (tooltipSwitchWidth / 2) + (sw.getPosition().x() + 0.5) * cellSizeWidth;
            double pointY = screenBoundsMinY - (tooltipSwitchHeight / 2) + (sw.getPosition().y() + 0.5) * cellSizeHeight;
            tooltip.show(pane, pointX, pointY);
        });
    }

    public void hideSwitchTooltips() {
        switchTooltips.forEach((sw, tooltip) -> tooltip.hide());
    }

    private void printIllegalTransition(Transition transition) {
        Switch sw = transition.point();
        Switch left = railwayNetwork.getNetwork().getVertexValue(transition.left().getNext(sw));
        Switch right = railwayNetwork.getNetwork().getVertexValue(transition.right().getNext(sw));

        double distanceLeft = Math.sqrt(Math.pow(left.getPosition().x() - sw.getPosition().x(), 2) + Math.pow(left.getPosition().y() - sw.getPosition().y(), 2));
        double distanceRight = Math.sqrt(Math.pow(right.getPosition().x() - sw.getPosition().x(), 2) + Math.pow(right.getPosition().y() - sw.getPosition().y(), 2));

        double distance = 1;
        double pointLeftX = sw.getPosition().x() + (distance * (left.getPosition().x() - sw.getPosition().x()) / distanceLeft);
        double pointLeftY = sw.getPosition().y() + (distance * (left.getPosition().y() - sw.getPosition().y()) / distanceLeft);

        double pointRightX = sw.getPosition().x() + (distance * (right.getPosition().x() - sw.getPosition().x()) / distanceRight);
        double pointRightY = sw.getPosition().y() + (distance * (right.getPosition().y() - sw.getPosition().y()) / distanceRight);

        Polygon polygon = new Polygon();
        polygon.getStyleClass().add("hover-effect-fill");
        polygon.setId(transition.left().key() + transition.point().getKey() + transition.right().key());
        polygon.setViewOrder(200);
        polygon.getPoints().addAll(
                (sw.getPosition().x() + 0.5) * cellSizeWidth,
                (sw.getPosition().y() + 0.5) * cellSizeHeight,
                (pointLeftX + 0.5) * cellSizeWidth,
                (pointLeftY + 0.5) * cellSizeHeight,
                (pointRightX + 0.5) * cellSizeWidth,
                (pointRightY + 0.5) * cellSizeHeight
        );
        polygon.setFill(Color.rgb(192, 192, 192));
        polygon.setOnMousePressed(mouseEvent -> {
            if (actionType == ActionType.REMOVE_ILLEGAL_TRANSITION) {
                Switch swi = railwayNetwork.getNetwork().getVertexValue(transition.point().getKey());
                swi.getIllegalTransitions().remove(transition);
                pane.getChildren().remove(polygon);
            }
        });

        pane.getChildren().add(polygon);
    }

    public void load() {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window.getCallback().getStage());

        if (file == null) {
            return;
        }

        if (file.exists()) {
            try {
                railwayNetwork = RailwayNetwork.loadRailwayNetwork(file);
                clear();
                printGrid();
                printRailwayNetwork();
                save = file;
                window.getModelUID().setText(railwayNetwork.getUid());
            } catch (RailwayNetworkLoadException e) {
                System.out.println("Not loaded");
            }
        }
    }

    private void printRailwayNetwork() {
        railwayNetwork.getNetwork().getVertexValues().forEach((s, sw) -> {
            printSwitch(sw.getPosition().x(), sw.getPosition().y(), sw.getKey());
            sw.getIllegalTransitions().forEach(this::printIllegalTransition);
        });
        railwayNetwork.getNetwork().getEdgeValues().forEach((s, rail) -> {
            printRail(rail);
        });
    }

    private void clear() {
        pane.getChildren().clear();
        path = null;
        nodes.clear();
        railTooltips.clear();
        switchTooltips.clear();
    }

    public void save() {

        if (save == null) {
            DirectoryChooser dirChooser = new DirectoryChooser();
            File file = dirChooser.showDialog(window.getCallback().getStage());

            if (file == null) {
                return;
            }

            if (file.exists() && file.isDirectory()) {
                RailwayNetwork.saveRailwayNetwork(railwayNetwork, new File(file.getPath() + "/" + railwayNetwork.getUid() + ".json"));
                save = new File(file.getPath() + "/" + railwayNetwork.getUid() + ".json");
            }
        } else {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Are you sure you want to save?", true);
            am.showAndWait(() -> {
                if ((boolean) am.getResult()) {
                    RailwayNetwork.saveRailwayNetwork(railwayNetwork, save);
                }
            });
        }
    }

    public void simulate(String key) {
        switch (actionCounter) {
            case 0 -> {
                if (key == null || railwayNetwork.getNetwork().getVertexValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select source switch.", false);
                    am.show();
                    return;
                }
                tempValue1 = key;
                actionCounter++;
            }
            case 1 -> {
                if (key == null || railwayNetwork.getNetwork().getVertexValue(key) == null) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Select target switch.", false);
                    am.show();
                    return;
                }

                AlertManager am = new AlertManager(AlertManager.AlertType.SINGLE_INPUT, "Select railway set length.", true);
                am.clear();
                am.showAndWait(() -> {
                    Integer length = Integer.parseInt((String) am.getResult());
                    simulate(tempValue1, key, length);

                    actionCounter = 0;
                    tempValue1 = null;
                });

            }
        }
    }

    private void simulate(String source, String target, Integer length) {
        Platform.runLater(() -> {
            path = railwayNetwork.getShortestValidPath(source, target, length);

            /*
            if (path == null) {
                path = railwayNetwork.getShortestValidPathLegacy(source, target, length);
            }*/

            if (path == null) {
                AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "No valid path found.", false);
                am.show();
                return;
            }

            path.getPath().forEach(direction -> {
                Line line = (Line) nodes.get(direction.rail().key());
                line.setStroke(Color.web("#ff0000"));
            });
            path.getReversalPaths().forEach((transition, directions) -> {
                directions.forEach(direction -> {
                    Line line = (Line) nodes.get(direction.rail().key());
                    line.setStroke(Color.web("#00ff00"));
                });
            });
        });
    }

    public void clearSimulation() {
        Platform.runLater(() -> {
            clear();
            printGrid();
            printRailwayNetwork();
        });
    }

    public void changeModelUID(String uid) {
        if (railwayNetwork != null) {
            railwayNetwork.setUid(uid);
        }
    }
}
