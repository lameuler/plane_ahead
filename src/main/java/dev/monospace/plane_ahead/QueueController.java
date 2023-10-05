package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QueueController {
    @FXML
    private VBox box;
    @FXML
    private VBox nextBox;

    private final PriorityQueue<Flight, Integer> flights = new PriorityQueue<>();
    private final ArrayList<Flight> scheduledFlights = new ArrayList<>();
    private Flight nextFlight;
    private static QueueController controller;

    public void randomise(boolean arrival) {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(6, 15); i++) {
            Flight flight = Flight.random(arrival);
            flights.enqueue(flight, flight.getUrgency());
            scheduledFlights.add(flight);
        }
        dequeue();
    }

    public void displayScheduledFlights() {
        box.getChildren().clear();
        for (Flight flight : scheduledFlights) {
            box.getChildren().add(new FlightNode(flight, this));
        }
    }

    public static Node load() throws IOException {
        FXMLLoader loader = new FXMLLoader(QueueController.class.getResource("queue-view.fxml"));
        Node node = loader.load();
        controller = loader.getController();
        return node;
    }

    public void enqueue(Flight flight) {
        flights.enqueue(flight, flight.getUrgency());
        scheduledFlights.add(flight);
        displayScheduledFlights();
    }

    public Flight dequeue() {
        Flight flight = flights.dequeue();
        scheduledFlights.remove(flight);
        displayScheduledFlights();
        setNextFlight(flight);
        return flight;
    }

    public Flight getNextFlight() {
        return nextFlight;
    }

    public void setNextFlight(Flight nextFlight) {
        this.nextFlight = nextFlight;
        nextBox.getChildren().clear();
        nextBox.getChildren().add(new FlightNode(nextFlight, this));
    }

    public QueueController getController() {
        return controller;
    }
}
