package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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

        colourButton.setOnAction(event -> {
            colorPicker.show();
        });

        colorPicker.setOnAction(event -> {
            plane.setTheme(colorPicker.getValue());
        });

        colorPicker.setVisible(false);
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        displayPlane();
    }

    public void setArrival() {
        background.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("arrival.png"))));
        title.setText("Landing...");
    }

    private void displayPlane() {
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
