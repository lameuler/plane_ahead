package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Random;

public class QueueController {
    @FXML
    private VBox box;
    @FXML
    private Button button;

    @FXML
    public void initialize() {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(6, 15); i++) {
            box.getChildren().add(new FlightNode(Flight.random()));
        }
        box.getChildren().add(new FlightNode(new Flight("PC", 5132)));
    }

    public static Node load() throws IOException {
        FXMLLoader loader = new FXMLLoader(QueueController.class.getResource("queue-view.fxml"));
        Node node = loader.load();
        loader.<QueueController>getController(); // TODO: bind to PriorityQueue
        return node;
    }

    public Button getButton() {
        return button;
    }
}
