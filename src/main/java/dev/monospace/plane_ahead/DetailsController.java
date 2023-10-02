package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DetailsController {
    @FXML
    Button homeButton;
    @FXML
    Button colourButton;
    @FXML
    ColorPicker colorPicker;
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
    @FXML
    ImageView background;
    @FXML
    Label title;

    private Flight flight;
    private Plane plane = new Plane();

    private Parent root;

    public void initialize() {
        homeButton.setOnAction(event -> {
            Scene scene = homeButton.getScene();
            scene.setRoot(root);
        });

        colourButton.setOnAction(event -> {
            colorPicker.show();
        });

        colorPicker.setOnAction(event -> {
            plane.setTheme(colorPicker.getValue());
        });

        colorPicker.setVisible(false);
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public void setFlight(Flight flight, Boolean newFlight) {
        this.flight = flight;
        displayPlane(newFlight);
    }

    public void setArrival() {
        background.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("arrival.png"))));
        title.setText("Landing...");
    }

    private void displayPlane(boolean newFlight) {
        plane.setBodyFill(this.flight.getBodyColor());
        plane.setWingFill(this.flight.getWingColor());
        plane.setWindowFill(this.flight.getWindowColor());

        if (!newFlight) {
            numberField.setText(this.flight.getAirlineCode() + " " + this.flight.getFlightNumber());
            numberField.setDisable(true);

            colourButton.setDisable(true);

            doneButton.setDisable(true);

            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }
        if (flight.isArrival()) {
            plane.setScaleX(-0.31);
            plane.setTranslateX(-8);
        }
        else {
            plane.setScaleX(0.31);
            plane.setTranslateX(8);
        }
        plane.setScaleY(0.31);
        plane.setTranslateY(-100);
        planeLayer.getChildren().add(plane);
    }
}
