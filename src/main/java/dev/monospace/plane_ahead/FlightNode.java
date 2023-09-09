package dev.monospace.plane_ahead;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class FlightNode extends BorderPane {

    private Flight flight;

    public FlightNode(Flight flight) {
        this.flight = flight;
        Label label = new Label();
        label.setText(flight.getAirlineCode()+"\n"+flight.getFlightNumber());
        label.setMinHeight(60);
        Plane plane = new Plane();
        plane.setBodyFill(flight.getBodyColor());
        plane.setWingFill(flight.getWingColor());
        plane.setWindowFill(flight.getWindowColor());
        plane.setMaxSize(80, 60);
        plane.setScaleX(0.064655);
        plane.setScaleY(0.064655);
        plane.setTranslateX(-37.333);
        plane.setTranslateY(-28);
        plane.setEffect(new DropShadow(100,0,0,Color.rgb(0,0,0,0.2)));
//        BorderPane.setAlignment(plane, Pos.CENTER);
        this.setLeft(label);
        this.setRight(plane);
        this.setPrefHeight(60);

        this.setStyle("-fx-background-color: #EAF1FD; -fx-background-radius: 10; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
        this.setEffect(new DropShadow(12,0,0,Color.rgb(0,0,0,0.1)));
    }

    public Flight getFlight() {
        return flight;
    }
}
