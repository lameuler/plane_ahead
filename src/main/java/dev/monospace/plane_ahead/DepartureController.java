package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DepartureController {
    @FXML
    Button homeButton;
    @FXML
    Button colourButton;
    @FXML
    StackPane planeLayer;
    @FXML
    TextField numberField;
    @FXML
    Button doneButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;

    private Flight flight;

    public void initialize() {
        homeButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("airport-view.fxml"));
                Scene scene = homeButton.getScene();
                scene.setRoot(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        displayPlane();
    }

    private void displayPlane() {
        Plane plane = new Plane();
        if (this.flight == null) {
            plane.randomise();
        }
        else {
            plane.setBodyFill(this.flight.getBodyColor());
            plane.setWingFill(this.flight.getWingColor());
            plane.setWindowFill(this.flight.getWindowColor());

            numberField.setText(this.flight.getAirlineCode() + " " + this.flight.getFlightNumber());
            numberField.setDisable(true);

            colourButton.setDisable(true);

            doneButton.setDisable(true);

            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }
        plane.setScaleX(0.31);
        plane.setScaleY(0.31);
        plane.setTranslateX(8);
        plane.setTranslateY(-100);
        planeLayer.getChildren().add(plane);
    }
}
