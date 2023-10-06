module dev.monospace.plane_ahead {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens dev.monospace.plane_ahead to javafx.fxml;
    exports dev.monospace.plane_ahead;
}