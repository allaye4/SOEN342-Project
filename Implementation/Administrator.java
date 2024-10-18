package Implementation;

import java.util.ArrayList;
import java.util.List;

// Administrator class
class Administrator {
    private List<Offering> offerings;

    public Administrator() {
        offerings = new ArrayList<>();
    }

    // Method to add new offering
    public void addOffering(Location location, String lessonType, String mode, DayTimeSlot dayTimeSlot, int capacity) {
        Offering offering = new Offering(location, lessonType, mode, dayTimeSlot, capacity);
        offerings.add(offering);
        System.out.println("Offering added: " + offering);
    }

    // Method to view all offerings
    public void viewOfferings() {
        System.out.println("Offerings:");
        for (Offering offering : offerings) {
            System.out.println(offering);
        }
    }

    public List<Offering> getOfferings() {
        return offerings;
    }
}
