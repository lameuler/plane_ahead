package dev.monospace.plane_ahead;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class PlaneAheadApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlaneAheadApplication.class.getResource("airport-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1280, 720);
        AirportController controller = fxmlLoader.getController();
        scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        stage.setTitle("Plane Ahead!");
        stage.setScene(scene);
        stage.show();
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
    }

    public static void main(String[] args) {
        launch();
    }
}