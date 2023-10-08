package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DetailsController {
    private final Plane plane = new Plane();
    @FXML
    StackPane rootStackPane;
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
    FlowPane prioritiesBox;
    @FXML
    MenuButton addButton;
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
    private Parent root;
    private AirportController airportController;

    public void initialize() {

        System.out.println(rootStackPane.getWidth());

        background.fitWidthProperty().bind(rootStackPane.widthProperty());
        background.fitHeightProperty().bind(rootStackPane.heightProperty());

        homeButton.setOnAction(event -> {
            Scene scene = homeButton.getScene();
            scene.setRoot(root);
        });

        colourButton.setOnAction(event -> colorPicker.show());

        colorPicker.setOnAction(event -> plane.setTheme(colorPicker.getValue()));

        colorPicker.setVisible(false);

        doneButton.setOnAction(event -> {
            String airlineCode = codeField.getText();
            String flightNumberString = numberField.getText();

            if (airlineCode.length() == 2 && flightNumberString.length() == 4 && flightNumberString.matches("[0-9]+")) {
                int flightNumber = Integer.parseInt(flightNumberString);
                Flight flight = new Flight(airlineCode.toUpperCase(), flightNumber, this.flight.isArrival());
                flight.setPriorities(new ArrayList<>(this.flight.getPriorities()));
                flight.setBodyColor(plane.getBodyFill());
                flight.setWingColor(plane.getWingFill());
                flight.setWindowColor(plane.getWindowFill());
                if (this.flight.isArrival()) {
                    airportController.arrivalEnqueue(flight);
                } else {
                    airportController.departureEnqueue(flight);
                }
                Scene scene = doneButton.getScene();
                scene.setRoot(root);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
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
        if (newFlight) {
            flight.removeAllPriorities();
        }
        displayPlane(newFlight);
    }

    public void setArrival() {
        background.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("arrival.png"))));
        title.setText("Landing...");
    }

    public void setRootController(AirportController airportController) {
        this.airportController = airportController;
    }

    private void displayPlane(boolean newFlight) {
        plane.setBodyFill(this.flight.getBodyColor());
        plane.setWingFill(this.flight.getWingColor());
        plane.setWindowFill(this.flight.getWindowColor());

        if (!newFlight) {
            codeField.setText(this.flight.getAirlineCode());
            codeField.setEditable(false);
            numberField.setText(String.valueOf(this.flight.getFlightNumber()));
            numberField.setEditable(false);

            flight.getPriorities().forEach(priority -> prioritiesBox.getChildren().add(0, new PriorityNode(priority, true)));

            addButton.setDisable(true);
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
        } else {
            HashMap<String, Priority> choices = new HashMap<>();
            choices.putAll(Priority.getTier());
            if (this.flight.isArrival()) {
                choices.putAll(Priority.getFuel());
                choices.putAll(Priority.getArrivalMiscellaneous());
            } else {
                choices.putAll(Priority.getDepartureMiscellaneous());
            }
            setChoices(choices);
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

    private void setChoices(HashMap<String, Priority> choices) {
        addButton.getItems().clear();

        for (String name : choices.keySet()) {
            MenuItem menuItem = new MenuItem(name);
            addButton.getItems().add(menuItem);

            menuItem.setOnAction(event -> {
                Priority priority = choices.get(menuItem.getText());
                prioritiesBox.getChildren().add(0, new PriorityNode(priority, false));
                flight.addPriority(priority);
                prioritiesBox.getChildren().clear();
                prioritiesBox.getChildren().add(addButton);
                if (Priority.getTier().containsValue(priority)) {
                    choices.keySet().removeIf(key -> Priority.getTier().containsKey(key));
                    setChoices(choices);
                } else if (Priority.getFuel().containsValue(priority)) {
                    choices.keySet().removeIf(key -> Priority.getFuel().containsKey(key));
                    setChoices(choices);
                } else {
                    choices.remove(priority.name());
                    setChoices(choices);
                }

                for (Priority p : flight.getPriorities()) {
                    PriorityNode priorityNode = new PriorityNode(p, false);
                    Button removeButton = priorityNode.getButton();
                    removeButton.setOnAction(e -> {
                        flight.removePriority(priorityNode.getPriority());
                        prioritiesBox.getChildren().remove(priorityNode);
                        if (Priority.getTier().containsValue(p)) {
                            choices.putAll(Priority.getTier());
                            setChoices(choices);
                        } else if (Priority.getFuel().containsValue(p)) {
                            choices.putAll(Priority.getFuel());
                            setChoices(choices);
                        } else {
                            choices.put(p.name(), p);
                            setChoices(choices);
                        }
                    });
                    prioritiesBox.getChildren().add(prioritiesBox.getChildren().size() - 1, priorityNode);
                }
            });
        }
    }

}