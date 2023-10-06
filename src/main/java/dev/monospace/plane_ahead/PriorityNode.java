package dev.monospace.plane_ahead;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class PriorityNode extends BorderPane {
    private final Priority priority;

    public PriorityNode(Priority priority, boolean disable) {
        this.priority = priority;

        Label label = new Label();
        label.setText(priority.name());
        label.setMinWidth(100);
        label.setAlignment(Pos.CENTER);

        Button button = new Button();
        button.getStyleClass().add("remove-button");

        this.setLeft(label);
        this.setRight(button);

        setMargin(button, new Insets(0, 0, 0, 10));
        setAlignment(button, Pos.CENTER_RIGHT);

        this.getStyleClass().add("priority-node");

        Tooltip tooltip = new Tooltip();
        tooltip.setText("Urgency: " + priority.urgency());
        tooltip.setShowDelay(Duration.millis(400));
        Tooltip.install(this, tooltip);

        if (disable) {
            button.setDisable(true);
//            this.setDisable(true);
        }
    }

    public Priority getPriority() {
        return priority;
    }
}
