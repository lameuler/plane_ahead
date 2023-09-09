package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Random;

public class QueueController {
    @FXML
    private VBox box;

    @FXML
    public void initialize() {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(6, 15); i++) {
            Label label = new Label();
            label.setMinHeight(50);
            label.setText("Flight "+random.nextInt(1000, 9999));
            box.getChildren().add(label);
        }
    }
}
