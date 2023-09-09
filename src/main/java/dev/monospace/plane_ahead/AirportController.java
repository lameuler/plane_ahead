package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AirportController {
    @FXML
    private Pane planeLayer;
    @FXML
    private ToggleButton departureToggle;
    @FXML
    private ToggleButton arrivalToggle;
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
        displayTab();
        tabToggle.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> displayTab());
    }

    private void displayTab() {
        if (departureToggle.isSelected()) {
            tabContent.getChildren().setAll(departureView);
        } else {
            tabContent.getChildren().setAll(arrivalView);
        }
    }
}
