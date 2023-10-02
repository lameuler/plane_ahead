package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QueueController {
    @FXML
    private VBox box;
    @FXML
    private VBox nextBox;

    private PriorityQueue<Flight, Integer> flights = new PriorityQueue<>();
    private ArrayList<Flight> scheduledFlights = new ArrayList<>();
    private Flight nextFlight;
    private static QueueController controller;

    public void randomise(boolean arrival) {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(6, 15); i++) {
            Flight flight = Flight.random(arrival);
            flights.enqueue(flight, flight.getUrgency());
            scheduledFlights.add(flight);
        }
        nextFlight = flights.dequeue();
        nextBox.getChildren().add(new FlightNode(nextFlight));
        scheduledFlights.remove(nextFlight);
        displayScheduledFlights();
    }

    public void displayScheduledFlights() {
        box.getChildren().clear();
        for (Flight flight : scheduledFlights) {
            box.getChildren().add(new FlightNode(flight));
        }
    }

    public static Node load() throws IOException {
        FXMLLoader loader = new FXMLLoader(QueueController.class.getResource("queue-view.fxml"));
        Node node = loader.load();
        controller = loader.getController();
        return node;
    }

    public PriorityQueue<Flight, Integer> getFlights() {
        return flights;
    }

    public void setFlights(PriorityQueue<Flight, Integer> flights) {
        this.flights = flights;
        displayScheduledFlights();
    }

    public Flight getNextFlight() {
        return nextFlight;
    }

    public void setNextFlight(Flight nextFlight) {
        this.nextFlight = nextFlight;
        nextBox.getChildren().clear();
        nextBox.getChildren().add(new FlightNode(nextFlight));
    }

    public QueueController getController() {
        return controller;
    }
}
