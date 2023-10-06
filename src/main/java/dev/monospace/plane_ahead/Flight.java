package dev.monospace.plane_ahead;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Flight {
    private final String airlineCode;
    private final int flightNumber;

    private ArrayList<Priority> priorities = new ArrayList<>();
    private final boolean arrival;

    private Color bodyColor;
    private Color wingColor;
    private Color windowColor;

    public Flight(String airlineCode, int flightNumber, boolean arrival) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.arrival = arrival;
        this.bodyColor = Color.rgb(226, 239, 246);
        this.wingColor = Color.rgb(220, 233, 240);
        this.windowColor = Color.rgb(49, 86, 140);
    }

    public static Flight random(boolean arrival) {
        Random random = new Random();
        String code = String.valueOf((char) random.nextInt('A', 'Z' + 1)) + (char) random.nextInt('A', 'Z' + 1);
        int number = random.nextInt(1000, 9999);
        Flight flight = new Flight(code, number, arrival);
        double h = random.nextDouble(360);
        double s = random.nextDouble(0.5);
        double b = random.nextDouble(0.9, 0.98);
        flight.setBodyColor(Color.hsb(h, s, b));
        flight.setWingColor(Color.hsb(h, s * random.nextDouble(1.5), 1 - (1 - b) * random.nextDouble(1, 1.5)));
        flight.addPriority(Priority.randomTier());
        if (arrival) {
            flight.addPriority(Priority.randomFuel());
            Priority randomPriority = Priority.randomArrivalMiscellaneous();
            flight.addPriority(randomPriority);
            Priority randomPriority2 = Priority.randomArrivalMiscellaneous();
            while (randomPriority2.name().equals(randomPriority.name())) {
                randomPriority2 = Priority.randomArrivalMiscellaneous();
            }
            flight.addPriority(randomPriority2);
        } else {
            Priority randomPriority = Priority.randomDepartureMiscellaneous();
            flight.addPriority(randomPriority);
            Priority randomPriority2 = Priority.randomDepartureMiscellaneous();
            while (randomPriority2.name().equals(randomPriority.name())) {
                randomPriority2 = Priority.randomDepartureMiscellaneous();
            }
            flight.addPriority(randomPriority2);
        }
        return flight;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public Color getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(Color bodyColor) {
        this.bodyColor = bodyColor;
    }

    public Color getWingColor() {
        return wingColor;
    }

    public void setWingColor(Color wingColor) {
        this.wingColor = wingColor;
    }

    public Color getWindowColor() {
        return windowColor;
    }

    public void setWindowColor(Color windowColor) {
        this.windowColor = windowColor;
    }

    public ArrayList<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(ArrayList<Priority> priorities) {
        this.priorities = priorities;
    }

    public void addPriority(Priority priority) {
        priorities.add(priority);
    }

    public void removePriority(Priority priority) {
        priorities.remove(priority);
    }

    public void removeAllPriorities() {
        priorities.clear();
    }

    public boolean isArrival() {
        return arrival;
    }

    public Plane toPlane() {
        Plane plane = new Plane();
        plane.setBodyFill(this.getBodyColor());
        plane.setWingFill(this.getWingColor());
        plane.setWindowFill(this.getWindowColor());
        return plane;
    }

    public Integer getUrgency() {
        return priorities.stream().map(Priority::urgency).reduce(0, Integer::sum);
    }
}