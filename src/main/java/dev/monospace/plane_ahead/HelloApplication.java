package dev.monospace.plane_ahead;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("airport-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Plane Ahead!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        ColorPicker bodyPicker = new ColorPicker(Color.BEIGE);
        ColorPicker wingPicker = new ColorPicker(Color.LIGHTGRAY);
        ColorPicker windowPicker = new ColorPicker(Color.BLACK);

        Plane plane = new Plane();

        plane.setBodyFill(bodyPicker.getValue());
        bodyPicker.setOnAction(e -> plane.setBodyFill(bodyPicker.getValue()));

        plane.setWingFill(wingPicker.getValue());
        wingPicker.setOnAction(e -> plane.setWingFill(wingPicker.getValue()));

        plane.setWindowFill(windowPicker.getValue());
        windowPicker.setOnAction(e -> plane.setWindowFill(windowPicker.getValue()));

//        VBox root = (VBox) scene.getRoot();
//        root.getChildren().addAll(plane, bodyPicker, wingPicker, windowPicker);

        plane.setScaleX(0.2);
        plane.setScaleY(0.2);
    }

    public static void main(String[] args) {
        launch();
    }
}