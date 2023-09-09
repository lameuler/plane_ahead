package dev.monospace.plane_ahead;

import javafx.scene.paint.Color;

import java.util.Random;

public class Flight {
    /**
     * Two character airline designator
     */
    private String airlineCode;

    /**
     * Four digit flight number
     */
    private int flightNumber;

    private int tier;
    private int emergency;
    private double fuel;

    private Color bodyColor;
    private Color wingColor;
    private Color windowColor;

    public Flight(String airlineCode, int flightNumber) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.tier = 0;
        this.emergency = 0;
        this.fuel = 0;
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

    public static Flight random() {
        Random random = new Random();
        String code = "" + (char) random.nextInt('A','Z'+1) + (char) random.nextInt('A','Z'+1);
        int number = random.nextInt(1000, 9999);
        Flight flight = new Flight(code, number);
        double h = random.nextDouble(360);
        double s = random.nextDouble(0.5);
        double b = random.nextDouble(0.9, 0.98);
        flight.setBodyColor(Color.hsb(h, s, b));
        flight.setWingColor(Color.hsb(h, s * random.nextDouble(1.5), 1 - (1 - b)*random.nextDouble(1, 1.5)));
        return flight;
    }
}
