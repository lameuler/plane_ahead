package dev.monospace.plane_ahead;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class NewDepartureController {
    public static Node load() throws IOException {
        FXMLLoader loader = new FXMLLoader(NewDepartureController.class.getResource("new-departure-view.fxml"));
        Node node = loader.load();
        loader.<QueueController>getController();
        return node;
    }
}
