package dev.monospace.plane_ahead;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
    @FXML
    private Button arrivalButton;
    @FXML
    private Button departureButton;

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
        arrivalButton.setLayoutX(1380);
        arrivalButton.setLayoutY(780);
        departureButton.setLayoutX(800);
        departureButton.setLayoutY(505);

        arrivalButton.setDisable(true);
        departureButton.setDisable(true);
        arrivalButton.setVisible(false);
        departureButton.setVisible(false);

        arrivalButton.setOnAction(e -> displayArrival(arrivalFlight.toPlane(true, true)));
        departureButton.setOnAction(e -> displayDeparture(departureFlight.toPlane(true, false)));

        displayDeparture(null);
        displayArrival(null);

        departureToggle.getStyleClass().remove("radio-button");
        arrivalToggle.getStyleClass().remove("radio-button");
        displayTab(tabToggle.getSelectedToggle());
        tabToggle.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> displayTab(oldValue));

        exitButton.setOnAction(e -> System.exit(0));

        readyArrival();
        readyDeparture();

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

    private void readyArrival() {
        arrivalButton.setDisable(false);
        arrivalButton.setVisible(true);
    }

    private void displayArrival(Plane plane) {
        Plane arrivalPlane;

        if (plane == null) {
            arrivalPlane = Flight.random(true).toPlane(true, true);
            arrivalPlane.setVisible(false);
        }
        else {
            arrivalPlane = arrivalFlight.toPlane(true, true);
            planeLayer.getChildren().remove(3);
            arrivalButton.setDisable(true);
            arrivalButton.setVisible(false);
        }

        arrivalPlane.setScaleX(-0.12);
        arrivalPlane.setScaleY(0.12);

        arrivalPlane.setLayoutX(850);
        arrivalPlane.setDisable(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), arrivalPlane);
        transition.setFromX(50);
        transition.setToX(0);
        transition.play();
        transition.setOnFinished(e -> arrivalPlane.setDisable(false));
        arrivalPlane.setLayoutY(350);

        planeLayer.getChildren().add(3, arrivalPlane);
    }

    private void readyDeparture() {
        departureButton.setDisable(false);
        departureButton.setVisible(true);
    }

    private void displayDeparture(Plane plane) {
        Plane departurePlane;

        if (plane == null) {
            departurePlane = Flight.random(false).toPlane(true, false);
            departurePlane.setVisible(false);
        }
        else {
            departurePlane = departureFlight.toPlane(true, false);
            planeLayer.getChildren().remove(2);
            departureButton.setDisable(true);
            departureButton.setVisible(false);
        }

        departurePlane.setScaleX(0.12);
        departurePlane.setScaleY(0.12);

        departurePlane.setLayoutX(150);
        departurePlane.setDisable(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), departurePlane);
        transition.setFromX(-50);
        transition.setToX(0);
        transition.play();
        transition.setOnFinished(e -> departurePlane.setDisable(false));
        departurePlane.setLayoutY(75);

        planeLayer.getChildren().add(2, departurePlane);
    }

    public Flight getArrivalFlight() {
        return arrivalFlight;
    }

    public void setArrivalFlight(Flight arrivalFlight) {
        this.arrivalFlight = arrivalFlight;
        readyArrival();
    }

    public Flight getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(Flight departureFlight) {
        this.departureFlight = departureFlight;
        readyDeparture();
    }
}
