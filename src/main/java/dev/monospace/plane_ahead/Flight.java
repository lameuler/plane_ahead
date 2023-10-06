package dev.monospace.plane_ahead;

import javafx.scene.paint.Color;

import java.util.Random;

public class Flight {
    /**
     * Two character airline designator
     */
    private final String airlineCode;

    /**
     * Four digit flight number
     */
    private final int flightNumber;

    private int tier;
    private int emergency;
    private boolean arrival;

    private Color bodyColor;
    private Color wingColor;
    private Color windowColor;

    public Flight(String airlineCode, int flightNumber, boolean arrival) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.tier = 0;
        this.emergency = 0;
        this.arrival = arrival;
        this.bodyColor = Color.rgb(226, 239, 246);
        this.wingColor = Color.rgb(220, 233, 240);
        this.windowColor = Color.rgb(49, 86, 140);
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

    public Color getWingColor() {
        return wingColor;
    }

    public Color getWindowColor() {
        return windowColor;
    }

    public void setBodyColor(Color bodyColor) {
        this.bodyColor = bodyColor;
    }

    public void setWingColor(Color wingColor) {
        this.wingColor = wingColor;
    }

    public void setWindowColor(Color windowColor) {
         this.windowColor = windowColor;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public boolean isArrival() {
        return arrival;
    }

    public void setArrival(boolean arrival) {
        this.arrival = arrival;
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
        flight.setTier(random.nextInt(0, 5));
        flight.setEmergency(random.nextInt(0, 5));
        return flight;
    }

    public Plane toPlane() {
        Plane plane = new Plane();
        plane.setBodyFill(this.getBodyColor());
        plane.setWingFill(this.getWingColor());
        plane.setWindowFill(this.getWindowColor());
        return plane;
    }

    public Integer getUrgency() {
        return this.getTier() + this.getEmergency();
    }
}