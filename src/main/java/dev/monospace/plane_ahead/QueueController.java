package dev.monospace.plane_ahead;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QueueController {
    private static QueueController controller;
    private final PriorityQueue<Flight, Integer> flights = new PriorityQueue<>();
    private final ArrayList<Flight> scheduledFlights = new ArrayList<>();
    @FXML
    private VBox box;
    @FXML
    private VBox nextBox;
    private Flight nextFlight;
    private AirportController airportController;

    public static Node load() throws IOException {
        FXMLLoader loader = new FXMLLoader(QueueController.class.getResource("queue-view.fxml"));
        Node node = loader.load();
        controller = loader.getController();
        return node;
    }

    public void randomise(boolean arrival, int origin, int bound) {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(origin, bound); i++) {
            Flight flight = Flight.random(arrival);
            flights.enqueue(flight, flight.getUrgency());
            scheduledFlights.add(flight);
        }
        dequeue();
    }

    public void displayScheduledFlights() {
        box.getChildren().clear();
        for (Flight flight : scheduledFlights) {
            box.getChildren().add(new FlightNode(flight, airportController));
        }
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
        if (nextFlight != null) {
            nextBox.getChildren().add(new FlightNode(nextFlight, airportController));
        }
    }

    public QueueController getController() {
        return controller;
    }

    public void setAirportController(AirportController airportController) {
        this.airportController = airportController;
    }
}
