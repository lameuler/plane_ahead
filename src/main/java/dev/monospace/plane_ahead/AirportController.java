package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class AirportController {
    @FXML
    private AnchorPane planeLayer;
    @FXML
    private RadioButton departureToggle;
    @FXML
    private RadioButton arrivalToggle;
    @FXML
    private ToggleGroup tabToggle;
    @FXML
    private VBox tabContent;

    private final Node departureView;
    private final Node arrivalView;

    public AirportController() throws IOException {
        departureView = QueueController.load();
        arrivalView = QueueController.load();
    }

    public void initialize() {
        departureToggle.getStyleClass().remove("radio-button");
        arrivalToggle.getStyleClass().remove("radio-button");
        displayTab(tabToggle.getSelectedToggle());
        tabToggle.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> displayTab(oldValue));
        displayPlanes();
    }

    private void displayTab(Toggle oldValue) {
        if (departureToggle.isSelected()) {
            tabContent.getChildren().setAll(departureView);
        } else if (arrivalToggle.isSelected()) {
            tabContent.getChildren().setAll(arrivalView);
        } else {
            tabToggle.selectToggle(oldValue);
        }
    }

    private void displayPlanes() {
        ColorPicker bodyPicker = new ColorPicker(Color.BEIGE);
        ColorPicker wingPicker = new ColorPicker(Color.LIGHTGRAY);
        ColorPicker windowPicker = new ColorPicker(Color.BLACK);

        Plane arrivalPlane = new Plane(true);

        arrivalPlane.setBodyFill(bodyPicker.getValue());
        bodyPicker.setOnAction(e -> arrivalPlane.setBodyFill(bodyPicker.getValue()));
        arrivalPlane.setWingFill(wingPicker.getValue());
        wingPicker.setOnAction(e -> arrivalPlane.setWingFill(wingPicker.getValue()));
        arrivalPlane.setWindowFill(windowPicker.getValue());
        windowPicker.setOnAction(e -> arrivalPlane.setWindowFill(windowPicker.getValue()));

        arrivalPlane.setScaleX(-0.15);
        arrivalPlane.setScaleY(0.15);

        arrivalPlane.setLayoutX(670);
        arrivalPlane.setLayoutY(350);

        planeLayer.getChildren().add(arrivalPlane);


        Plane departurePlane = new Plane(true);

        departurePlane.setBodyFill(Color.LIGHTSKYBLUE);
        departurePlane.setWingFill(Color.HOTPINK);
        departurePlane.setWindowFill(Color.WHITE);

        departurePlane.setScaleX(0.15);
        departurePlane.setScaleY(0.15);

        departurePlane.setLayoutX(0);
        departurePlane.setLayoutY(60);

        planeLayer.getChildren().add(departurePlane);
    }
}
