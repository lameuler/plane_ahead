package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    @FXML
    private Button exitButton;

    private final Node departureView;
    private final Node arrivalView;

    private Flight arrivalFlight;
    private Flight departureFlight;

    public AirportController() throws IOException {
        QueueController departureQueue = new QueueController();
        departureView = departureQueue.load();
        departureQueue = departureQueue.getController();
        departureQueue.randomise(false);
        departureFlight = departureQueue.getNextFlight();

        QueueController arrivalQueue = new QueueController();
        arrivalView = arrivalQueue.load();
        arrivalQueue = arrivalQueue.getController();
        arrivalQueue.randomise(true);
        arrivalFlight = arrivalQueue.getNextFlight();
    }

    public void initialize() {
        departureToggle.getStyleClass().remove("radio-button");
        arrivalToggle.getStyleClass().remove("radio-button");
        displayTab(tabToggle.getSelectedToggle());
        tabToggle.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> displayTab(oldValue));

        exitButton.setOnAction(e -> System.exit(0));

        displayPlanes();

        Button departureButton = (Button) departureView.lookup("#button");
        departureButton.setOnAction(e -> {
            try {
                Scene scene = departureButton.getScene();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("details-view.fxml"));
                Parent parent = fxmlLoader.load();
                DetailsController controller = fxmlLoader.getController();
                controller.setFlight(Flight.random(false), true);
                scene.setRoot(parent);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button arrivalButton = (Button) arrivalView.lookup("#button");
        arrivalButton.setOnAction(e -> {
            try {
                Scene scene = arrivalButton.getScene();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("details-view.fxml"));
                Parent parent = fxmlLoader.load();
                DetailsController controller = fxmlLoader.getController();
                controller.setFlight(Flight.random(true), true);
                controller.setArrival();
                scene.setRoot(parent);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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

        Plane arrivalPlane = arrivalFlight.toPlane(true, true);

        arrivalPlane.setScaleX(-0.12);
        arrivalPlane.setScaleY(0.12);

        arrivalPlane.setLayoutX(850);
        arrivalPlane.setLayoutY(350);

        planeLayer.getChildren().add(arrivalPlane);


        Plane departurePlane = departureFlight.toPlane(true, false);

        departurePlane.setScaleX(0.12);
        departurePlane.setScaleY(0.12);

        departurePlane.setLayoutX(125);
        departurePlane.setLayoutY(75);

        planeLayer.getChildren().add(departurePlane);
    }

    public Flight getArrivalFlight() {
        return arrivalFlight;
    }

    public void setArrivalFlight(Flight arrivalFlight) {
        this.arrivalFlight = arrivalFlight;
        displayPlanes();
    }

    public Flight getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(Flight departureFlight) {
        this.departureFlight = departureFlight;
        displayPlanes();
    }
}
