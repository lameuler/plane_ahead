package dev.monospace.plane_ahead;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class FlightNode extends BorderPane {

    private Flight flight;

    public FlightNode(Flight flight) {
        this.flight = flight;
        Label label = new Label();
        label.setText(flight.getAirlineCode() + " " + flight.getFlightNumber());
        label.setMinHeight(100);
        label.setMinWidth(150);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        Plane plane = new Plane();
        plane.setBodyFill(flight.getBodyColor());
        plane.setWingFill(flight.getWingColor());
        plane.setWindowFill(flight.getWindowColor());
        plane.setMaxSize(133, 100);
        plane.setScaleX(0.08);
        plane.setScaleY(0.08);
        plane.setTranslateX(-40);
        plane.setTranslateY(-35);
        plane.setEffect(new DropShadow(100,0,0,Color.rgb(0,0,0,0.2)));
//        BorderPane.setAlignment(plane, Pos.CENTER);
        this.setLeft(label);
        this.setRight(plane);
        this.setPrefHeight(100);

        this.getStyleClass().add("flight-node");
        // when selected, add a drop shadow

        this.setOnMouseClicked(event -> {
            for (Node node : this.getParent().getChildrenUnmodifiable()) {
                if (node == this) {
                    if (this.getStyleClass().contains("flight-node-selected")) {
                        this.getStyleClass().remove("flight-node-selected");
                    } else {
                        this.getStyleClass().add("flight-node-selected");
                    }
                } else {
                    node.getStyleClass().remove("flight-node-selected");
                }
            }
        });
    }

    public Flight getFlight() {
        return flight;
    }
}
