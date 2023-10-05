package dev.monospace.plane_ahead;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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

    private static QueueController departureQueue = new QueueController();
    private static QueueController arrivalQueue = new QueueController();

    public AirportController() throws IOException {
        departureView = QueueController.load();
        departureQueue = departureQueue.getController();
        departureQueue.randomise(false);
        departureFlight = departureQueue.getNextFlight();

        arrivalView = QueueController.load();
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
                controller.setRoot(departureButton.getScene().getRoot());
                controller.setDepartureQueue(departureQueue);
                controller.setArrivalQueue(arrivalQueue);
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
                controller.setRoot(arrivalButton.getScene().getRoot());
                controller.setDepartureQueue(departureQueue);
                controller.setArrivalQueue(arrivalQueue);
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
        arrivalButton.setOnAction(e -> displayArrival(arrivalFlight.toPlane()));
    }

    private void displayArrival(Plane plane) {
        Plane arrivalPlane;

        if (plane == null) {
            arrivalPlane = Flight.random(true).toPlane();
            arrivalPlane.setVisible(false);
        } else {
            arrivalPlane = arrivalFlight.toPlane();

            arrivalPlane.setTranslateY(-1 * Math.pow(Math.E, (double) 5 / 2));

            DragInfo drag = new DragInfo();

            arrivalPlane.setOnMouseEntered(e -> {
                arrivalPlane.getScene().setCursor(Cursor.HAND);
            });

            arrivalPlane.setOnMouseExited(e -> {
                arrivalPlane.getScene().setCursor(Cursor.DEFAULT);
            });

            arrivalPlane.setOnMousePressed(e -> {
                drag.mouseX = e.getSceneX();
                drag.mouseY = e.getSceneY();
                drag.planeX = arrivalPlane.getTranslateX();
                drag.planeY = arrivalPlane.getTranslateY();
            });

            arrivalPlane.setOnMouseDragged(e -> {
                double x = e.getSceneX() - drag.mouseX + drag.planeX;
                double y = -1 * Math.pow(Math.E, (x + 500) / 200);
                arrivalPlane.setTranslateX(x);
                arrivalPlane.setTranslateY(y);
            });

            arrivalPlane.setOnMouseReleased(e -> {
                if (arrivalPlane.getTranslateX() > -500) {
                    arrivalPlane.setDisable(true);
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1), arrivalPlane);
                    tt.setToX(0);
                    tt.setToY(-1 * Math.pow(Math.E, (double) 5 / 2));
                    tt.play();
                    tt.setOnFinished(event -> arrivalPlane.setDisable(false));
                } else {
                    arrivalPlane.setDisable(true);
                    TranslateTransition tt = new TranslateTransition(Duration.millis(500), arrivalPlane);
                    tt.setToX(-1000);
                    tt.setToY(0);
                    tt.play();
                    tt.setOnFinished(event -> {
                        arrivalFlight = arrivalQueue.dequeue();
                        new Thread(() -> {
                            try {
                                Thread.sleep((long) (Math.random() * 5000 + 5000));
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            Platform.runLater(this::readyArrival);
                        }).start();
                    });
                }
            });

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
        departureButton.setOnAction(e -> displayDeparture(departureFlight.toPlane()));
    }

    private void displayDeparture(Plane plane) {
        Plane departurePlane;

        if (plane == null) {
            departurePlane = Flight.random(false).toPlane();
            departurePlane.setVisible(false);
        } else {
            departurePlane = departureFlight.toPlane();

            DragInfo drag = new DragInfo();

            departurePlane.setOnMouseEntered(e -> {
                departurePlane.getScene().setCursor(Cursor.HAND);
            });

            departurePlane.setOnMouseExited(e -> {
                departurePlane.getScene().setCursor(Cursor.DEFAULT);
            });

            departurePlane.setOnMousePressed(e -> {
                drag.mouseX = e.getSceneX();
                drag.mouseY = e.getSceneY();
                drag.planeX = departurePlane.getTranslateX();
                drag.planeY = departurePlane.getTranslateY();
            });

            departurePlane.setOnMouseDragged(e -> {
                double x = e.getSceneX() - drag.mouseX + drag.planeX;
                double y = -1 * Math.pow(Math.E, x / 200);
                departurePlane.setTranslateX(x);
                departurePlane.setTranslateY(y);
            });

            departurePlane.setOnMouseReleased(e -> {
                if (departurePlane.getTranslateX() < 500) {
                    departurePlane.setDisable(true);
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1), departurePlane);
                    tt.setToX(0);
                    tt.setToY(0);
                    tt.play();
                    tt.setOnFinished(event -> departurePlane.setDisable(false));
                } else {
                    departurePlane.setDisable(true);
                    TranslateTransition tt = new TranslateTransition(Duration.millis(500), departurePlane);
                    tt.setToX(1000);
                    tt.setToY(-1 * Math.pow(Math.E, 5));
                    tt.play();
                    tt.setOnFinished(event -> {
                        departureFlight = departureQueue.dequeue();
                        new Thread(() -> {
                            try {
                                Thread.sleep((long) (Math.random() * 5000 + 5000));
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            Platform.runLater(this::readyDeparture);
                        }).start();
                    });
                }
            });

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

    public void addFlight(Flight flight) {
        if (flight.isArrival()) {
            arrivalQueue.enqueue(flight);
        } else {
            departureQueue.enqueue(flight);
        }
    }

    static class DragInfo {
        double mouseX, mouseY, planeX, planeY;

        @Override
        public String toString() {
            return "DragInfo{" +
                    "mouseX=" + mouseX +
                    ", mouseY=" + mouseY +
                    ", planeX=" + planeX +
                    ", planeY=" + planeY +
                    '}';
        }
    }
}