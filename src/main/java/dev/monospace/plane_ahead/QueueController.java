package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Random;

public class QueueController {
    @FXML
    private VBox box;
    @FXML
    private Button button;

    public void randomise(boolean arrival) {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(6, 15); i++) {
            box.getChildren().add(new FlightNode(Flight.random(arrival)));
        }
    }

    public static Node load(boolean arrival) throws IOException {
        FXMLLoader loader = new FXMLLoader(QueueController.class.getResource("queue-view.fxml"));
        Node node = loader.load();
        QueueController controller = loader.getController(); // TODO: bind to PriorityQueue
        controller.randomise(arrival);
        return node;
    }
}
