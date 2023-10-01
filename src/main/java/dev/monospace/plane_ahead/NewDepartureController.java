package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class NewDepartureController {
    @FXML
    Button homeButton;
    @FXML
    StackPane planeLayer;

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

        Plane plane = new Plane();
        plane.randomise();

        plane.setScaleX(0.31);
        plane.setScaleY(0.31);
        plane.setTranslateX(8);
        plane.setTranslateY(-100);

        planeLayer.getChildren().add(plane);
    }
}
