package dev.monospace.plane_ahead;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class AirportController {
    private static QueueController departureQueue = new QueueController();
    private static QueueController arrivalQueue = new QueueController();
    private final Node departureView;
    private final Node arrivalView;
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
    private Flight arrivalFlight;
    private Flight departureFlight;

    public AirportController() throws IOException {
        departureView = QueueController.load();
        departureQueue = departureQueue.getController();
        departureQueue.setAirportController(this);
        departureQueue.randomise(false, 5, 16);
        departureFlight = departureQueue.getNextFlight();

        arrivalView = QueueController.load();
        arrivalQueue = arrivalQueue.getController();
        arrivalQueue.setAirportController(this);
        arrivalQueue.randomise(true, 5, 16);
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

        setupDepartureButton();
        setupArrivalButton();


    }

    public void setupDepartureButton() {
        Button departureButton = (Button) departureView.lookup("#button");
        departureButton.setText("+ New");
        departureButton.setOnAction(e -> {
            try {
                Scene scene = departureButton.getScene();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("details-view.fxml"));
                Parent parent = fxmlLoader.load();
                DetailsController controller = fxmlLoader.getController();
                controller.setFlight(Flight.random(false), true);
                controller.setRoot(departureButton.getScene().getRoot());
                controller.setRootController(this);
                scene.setRoot(parent);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void setupArrivalButton() {
        Button arrivalButton = (Button) arrivalView.lookup("#button");
        arrivalButton.setText("+ New");
        arrivalButton.setOnAction(e -> {
            try {
                Scene scene = arrivalButton.getScene();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("details-view.fxml"));
                Parent parent = fxmlLoader.load();
                DetailsController controller = fxmlLoader.getController();
                controller.setFlight(Flight.random(true), true);
                controller.setArrival();
                controller.setRoot(arrivalButton.getScene().getRoot());
                controller.setRootController(this);
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
            arrivalPlane.setEffect(new DropShadow(100, 0, 50, Color.rgb(0, 0, 0, 0.2)));

            arrivalPlane.setTranslateY(-1 * Math.pow(Math.E, (double) 5 / 2));

            DragInfo drag = new DragInfo();

            arrivalPlane.setOnMouseEntered(e -> arrivalPlane.getScene().setCursor(Cursor.HAND));

            arrivalPlane.setOnMouseExited(e -> arrivalPlane.getScene().setCursor(Cursor.DEFAULT));

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
                    tt.setOnFinished(event -> arrivalDequeue());
                    playAirplaneSFX();
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

    public void arrivalEnqueue(Flight flight) {
        arrivalQueue.enqueue(flight);
        if (arrivalFlight == null) {
            arrivalDequeue();
        }
    }

    public void arrivalDequeue() {
        arrivalFlight = arrivalQueue.dequeue();
        if (arrivalFlight != null) {
            new Thread(() -> {
                try {
//                    Thread.sleep((long) (Math.random() * 5000 + 5000));
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(this::readyArrival);
            }).start();
        }
        setupArrivalButton();
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
            departurePlane.setEffect(new DropShadow(100, 0, 50, Color.rgb(0, 0, 0, 0.2)));

            DragInfo drag = new DragInfo();

            departurePlane.setOnMouseEntered(e -> departurePlane.getScene().setCursor(Cursor.HAND));

            departurePlane.setOnMouseExited(e -> departurePlane.getScene().setCursor(Cursor.DEFAULT));

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
                    tt.setOnFinished(event -> departureDequeue());
                    playAirplaneSFX();
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

    public void departureEnqueue(Flight flight) {
        departureQueue.enqueue(flight);
        if (departureFlight == null) {
            departureDequeue();
        }
    }

    public void departureDequeue() {
        departureFlight = departureQueue.dequeue();
        if (departureFlight != null) {
            new Thread(() -> {
                try {
//                    Thread.sleep((long) (Math.random() * 5000 + 5000));
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(this::readyDeparture);
            }).start();
        }
        setupDepartureButton();
    }

    public void playAirplaneSFX(){
        try {
            Audio audio = new Audio("./sounds/airplane_sfx.wav");
            audio.play();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e2) {
            throw new RuntimeException(e2);
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