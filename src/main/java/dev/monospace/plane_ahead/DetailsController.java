package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
    TextField codeField;
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
    private final Plane plane = new Plane();

    private Parent root;
    private QueueController departureQueue;
    private QueueController arrivalQueue;

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

        doneButton.setOnAction(event -> {
            String airlineCode = codeField.getText();
            String flightNumberString = numberField.getText();

            if (airlineCode.length() == 2 && flightNumberString.length() == 4 && flightNumberString.matches("[0-9]+")) {
                int flightNumber = Integer.parseInt(flightNumberString);
                Flight flight = new Flight(airlineCode.toUpperCase(), flightNumber, this.flight.isArrival());
                flight.setBodyColor(plane.getBodyFill());
                flight.setWingColor(plane.getWingFill());
                flight.setWindowColor(plane.getWindowFill());
                if (this.flight.isArrival()) {
                    arrivalQueue.enqueue(flight);
                } else {
                    departureQueue.enqueue(flight);
                }
                Scene scene = doneButton.getScene();
                scene.setRoot(root);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.initStyle(StageStyle.UNDECORATED);
                ImageView alertIcon = new ImageView(Objects.requireNonNull(getClass().getResource("error.png")).toExternalForm());
                alertIcon.setFitHeight(100);
                alertIcon.setFitWidth(100);
                alert.setGraphic(alertIcon);
                alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("details-style.css")).toExternalForm());
                alert.initOwner(doneButton.getScene().getWindow());
                alert.setTitle("Invalid flight details");
                alert.setHeaderText("Invalid flight details");
                alert.setContentText("Please enter a valid airline code and flight number.");
                alert.showAndWait();
            }
        });
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

    public void setDepartureQueue(QueueController departureQueue) {
        this.departureQueue = departureQueue;
    }

    public void setArrivalQueue(QueueController arrivalQueue) {
        this.arrivalQueue = arrivalQueue;
    }

    private void displayPlane(boolean newFlight) {
        plane.setBodyFill(this.flight.getBodyColor());
        plane.setWingFill(this.flight.getWingColor());
        plane.setWindowFill(this.flight.getWindowColor());

        if (!newFlight) {
            codeField.setText(this.flight.getAirlineCode());
            codeField.setDisable(true);
            numberField.setText(String.valueOf(this.flight.getFlightNumber()));
            numberField.setDisable(true);

            colourButton.setDisable(true);

            doneButton.setDisable(true);

            editButton.setVisible(true);
            deleteButton.setVisible(true);

            // TODO: Implement edit and delete functionality
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Coming soon!");
            tooltip.setShowDelay(Duration.millis(400));
            editButton.setTooltip(tooltip);
            deleteButton.setTooltip(tooltip);
        }
        if (flight.isArrival()) {
            plane.setScaleX(-0.31);
            plane.setTranslateX(-8);
        } else {
            plane.setScaleX(0.31);
            plane.setTranslateX(8);
        }
        plane.setScaleY(0.31);
        plane.setTranslateY(-100);
        planeLayer.getChildren().add(plane);
    }
}