package dev.monospace.plane_ahead;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;

public class FlightNode extends BorderPane {

    private final Flight flight;
    private QueueController queueController;

    public FlightNode(Flight flight, QueueController queueController) {
        this.flight = flight;
        this.queueController = queueController;
        Label label = new Label();
        label.setText(flight.getAirlineCode() + " " + flight.getFlightNumber());
        label.setMinHeight(100);
        label.setMinWidth(150);
        label.setAlignment(Pos.CENTER);
        Plane plane = new Plane();
        plane.setBodyFill(flight.getBodyColor());
        plane.setWingFill(flight.getWingColor());
        plane.setWindowFill(flight.getWindowColor());
        plane.setMaxSize(133, 100);
        if (flight.isArrival()) {
            plane.setScaleX(-0.08);
            plane.setTranslateX(40);
        } else {
            plane.setScaleX(0.08);
            plane.setTranslateX(-40);
        }
        plane.setScaleY(0.08);
        plane.setTranslateY(-35);
        plane.setEffect(new DropShadow(100, 0, 0, Color.rgb(0, 0, 0, 0.2)));
//        BorderPane.setAlignment(plane, Pos.CENTER);
        this.setLeft(label);
        this.setRight(plane);
        this.setPrefHeight(100);

        this.getStyleClass().add("flight-node");
        // when selected, add a drop shadow

        this.setOnMouseClicked(event -> {
            Scene scene = this.getScene();
            Button button = (Button) scene.lookup("#button");
            VBox vBox = (VBox) scene.lookup("#nextBox");
            if (vBox.getChildrenUnmodifiable().get(0) == this) {
                for (Node node : ((VBox) scene.lookup("#box")).getChildrenUnmodifiable()) {
                    node.getStyleClass().remove("flight-node-selected");
                }
                if (this.getStyleClass().contains("flight-node-selected")) {
                    this.getStyleClass().remove("flight-node-selected");
                    setNew(scene, button);
                } else {
                    this.getStyleClass().add("flight-node-selected");
                    setView(scene, button);
                }
            } else {
                ((VBox) scene.lookup("#nextBox")).getChildrenUnmodifiable().get(0).getStyleClass().remove("flight-node-selected");
                for (Node node : this.getParent().getChildrenUnmodifiable()) {
                    if (node == this) {
                        if (this.getStyleClass().contains("flight-node-selected")) {
                            this.getStyleClass().remove("flight-node-selected");
                            setNew(scene, button);
                        } else {
                            this.getStyleClass().add("flight-node-selected");
                            setView(scene, button);
                        }
                    } else {
                        node.getStyleClass().remove("flight-node-selected");
                    }
                }
            }
        });

        Tooltip tooltip = new Tooltip();
        tooltip.setText("Urgency: " + flight.getUrgency());
        tooltip.setShowDelay(Duration.millis(400));
        Tooltip.install(this, tooltip);
    }

    public Flight getFlight() {
        return flight;
    }

    private void setNew(Scene scene, Button button) {
        button.setText("+ New");
        button.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("details-view.fxml"));
                Parent parent = fxmlLoader.load();
                DetailsController controller = fxmlLoader.getController();
                controller.setFlight(Flight.random(this.getFlight().isArrival()), true);
                if (this.getFlight().isArrival()) {
                    controller.setArrival();
                    controller.setArrivalQueue(queueController);
                }
                else {
                    controller.setDepartureQueue(queueController);
                }
                controller.setRoot(scene.getRoot());
                scene.setRoot(parent);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void setView(Scene scene, Button button) {
        button.setText("🕶 View");
        button.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("details-view.fxml"));
                Parent parent = fxmlLoader.load();
                DetailsController controller = fxmlLoader.getController();
                controller.setFlight(this.getFlight(), false);
                if (this.getFlight().isArrival()) {
                    controller.setArrival();
                    controller.setArrivalQueue(queueController);
                }
                else {
                    controller.setDepartureQueue(queueController);
                }
                controller.setRoot(scene.getRoot());
                scene.setRoot(parent);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}