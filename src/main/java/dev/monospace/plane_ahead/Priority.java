package dev.monospace.plane_ahead;

import java.util.HashMap;
public record Priority(String name, int urgency) {

    private static HashMap<Integer, String> tier = new HashMap<>() {{
        put(0, "I");
        put(1, "II");
        put(2, "III");
    }};

    private static HashMap<Integer, String> fuel = new HashMap<>() {{
        put(0, "Max");
        put(1, "Moderate");
        put(2, "Low");
    }};

    public static Priority tierPriority(int urgency) {
        return new Priority("Tier " + tier.get(urgency), urgency);
    }

    public static Priority fuelPriority(int urgency) {
        return new Priority(fuel.get(urgency) + " Fuel", urgency);
    }
}