package dev.monospace.plane_ahead;

import java.util.ArrayList;
import java.util.HashMap;
public record Priority(String name, int urgency) {

    private static final HashMap<Integer, String> tier = new HashMap<>() {{
        put(0, "I");
        put(1, "II");
        put(3, "III");
    }};

    private static final HashMap<Integer, String> fuel = new HashMap<>() {{
        put(0, "Max");
        put(2, "Moderate");
        put(5, "Low");
    }};

    private static final HashMap<String, Integer> arrivalMiscellaneous = new HashMap<>() {{
        put("VIP", 3);
        put("Fire", 5);
        put("Close to airport", 2);
        put("Emergency", 10);
        put("Medical emergency", 8);
        put("Aircraft malfunction", 6);
        put("Severe weather", 4);
        put("Large aircraft", 2);
        put("Small aircraft", 1);
    }};

    private static final HashMap<String, Integer> departureMiscellaneous = new HashMap<>() {{
        put("VIP", 2);
        put("Cargo flight", 3);
        put("Military flight", 4);
        put("Delayed flight", 2);
        put("Aircraft maintenance", -2);
        put("Aircraft refueling", -1);
    }};

    public static Priority tierPriority(int urgency) {
        return new Priority("Tier " + tier.get(urgency), urgency);
    }

    public static Priority randomTier() {
        ArrayList<Integer> keys = new ArrayList<>(tier.keySet());
        int key = keys.get((int) (Math.random() * keys.size()));
        return tierPriority(key);
    }

    public static HashMap<String, Priority> getTier() {
        HashMap<String, Priority> priorities = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            priorities.put("Tier " + tier.get(i), tierPriority(i));
        }
        return priorities;
    }

    public static Priority fuelPriority(int urgency) {
        return new Priority(fuel.get(urgency) + " Fuel", urgency);
    }

    public static Priority randomFuel() {
        ArrayList<Integer> keys = new ArrayList<>(fuel.keySet());
        int key = keys.get((int) (Math.random() * keys.size()));
        return fuelPriority(key);
    }

    public static HashMap<String, Priority> getFuel() {
        HashMap<String, Priority> priorities = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            priorities.put(fuel.get(i) + " Fuel", fuelPriority(i));
        }
        return priorities;
    }

    public static Priority randomArrivalMiscellaneous() {
        ArrayList<String> keys = new ArrayList<>(arrivalMiscellaneous.keySet());
        String key = keys.get((int) (Math.random() * keys.size()));
        return new Priority(key, arrivalMiscellaneous.get(key));
    }

    public static HashMap<String, Priority> getArrivalMiscellaneous() {
        HashMap<String, Priority> priorities = new HashMap<>();
        for (String key : arrivalMiscellaneous.keySet()) {
            priorities.put(key, new Priority(key, arrivalMiscellaneous.get(key)));
        }
        return priorities;
    }

    public static Priority randomDepartureMiscellaneous() {
        ArrayList<String> keys = new ArrayList<>(departureMiscellaneous.keySet());
        String key = keys.get((int) (Math.random() * keys.size()));
        return new Priority(key, departureMiscellaneous.get(key));
    }

    public static HashMap<String, Priority> getDepartureMiscellaneous() {
        HashMap<String, Priority> priorities = new HashMap<>();
        for (String key : departureMiscellaneous.keySet()) {
            priorities.put(key, new Priority(key, departureMiscellaneous.get(key)));
        }
        return priorities;
    }
}