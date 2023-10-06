package dev.monospace.plane_ahead;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

import java.io.IOException;
import java.util.Objects;

public class PlaneAheadApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlaneAheadApplication.class.getResource("airport-view.fxml"));
        Parent root = fxmlLoader.load();
        Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(root, ScreenSize.getWidth(), ScreenSize.getHeight());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("app.css")).toExternalForm());
        stage.setTitle("Plane Ahead!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}