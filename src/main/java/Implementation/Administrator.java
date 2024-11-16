package Implementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Administrator {
    private List<Offering> offerings;
    private List<Instructor> instructors;

    public Administrator() {
        offerings = new ArrayList<>();
        instructors = new ArrayList<>();
    }

    public void addOffering(Location location, String lessonType, String mode, DayTimeSlot dayTimeSlot, int capacity) {
        Offering offering = new Offering(location, lessonType, mode, dayTimeSlot, capacity);
        offerings.add(offering);
        System.out.println("Offering added: " + offering);
    }

    public void viewOfferings() {
        System.out.println("=== Offerings List ===");
        for (Offering offering : offerings) {
            System.out.println(offering);
        }
    }


    public List<Offering> fetchOfferingsFromDatabase() {
    List<Offering> offerings = new ArrayList<>();
    String query = "SELECT id, location_name, lesson_type, mode, day, time_slot, capacity, available FROM offerings";
    
    // Check connection and debug message if connection fails
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        // Check if result set has data
        if (!rs.next()) {
            System.out.println("No offerings found in the database.");
            return offerings; // Return empty list if no data found
        }

        // Reset ResultSet cursor to the first row
        do {
            int id = rs.getInt("id");
            String locationStr = rs.getString("location_name");
            String lessonType = rs.getString("lesson_type");
            String mode = rs.getString("mode");
            String day = rs.getString("day");
            String timeSlot = rs.getString("time_slot");
            int capacity = rs.getInt("capacity");
            boolean isAvailable = rs.getBoolean("available");

            // Debugging output to check what values are being fetched
            System.out.println("Fetched Offering: " + id + ", " + locationStr + ", " + lessonType);

            // Parse complex objects
            Location location = Location.parse(locationStr);
            DayTimeSlot dayTimeSlot = new DayTimeSlot(day, timeSlot);

            // Create Offering without an instructor (since there's no instructorId column)
            Offering offering = new Offering(location, lessonType, mode, dayTimeSlot, capacity);
            offering.setId(id);
            offering.setAvailable(isAvailable);

            offerings.add(offering);
        } while (rs.next()); // Iterate through all rows in the result set
        
    } catch (SQLException e) {
        System.out.println("Error fetching offerings from the database: " + e.getMessage());
        e.printStackTrace(); // Print detailed stack trace for debugging
    }

    return offerings;
}



    public List<Offering> getOfferings() {
        return offerings;
    }


    public List<Instructor> getInstructors() {
        return instructors;
    }

    public List<Offering> findMatchingOfferings(Instructor instructor, List<Offering> offerings) {
    List<Offering> matchingOfferings = new ArrayList<>();

    for (Offering offering : offerings) {
        // Check if the lesson type matches any of the instructor's specializations
        boolean isSpecialized = false;
        for (String specialization : instructor.getSpecializations()) {
            if (offering.getLessonType().equalsIgnoreCase(specialization)) {
                isSpecialized = true;
                break;
            }
        }

        // Check if the offering's city is in the instructor's available cities
        boolean isAvailableInCity = false;
        for (String city : instructor.getCities()) {
            if (city.equalsIgnoreCase(offering.getLocation().getCity())) {
                isAvailableInCity = true;
                break;
            }
        }

        // If both checks pass and the offering is available, add it to the matching list
        if (isSpecialized && isAvailableInCity && offering.isAvailable()) {
            matchingOfferings.add(offering);
        }
    }

    return matchingOfferings;
}



    public void registerInstructor(Instructor instructor) {
        instructors.add(instructor);
        System.out.println("Instructor registered: " + instructor);
    }
    public Instructor getInstructorByName(String name) {
    for (Instructor instructor : instructors) {
        if (instructor.getName().equalsIgnoreCase(name)) {
            return instructor;
        }
    }
    return null; // Instructor not found
}
public Offering findMatchingOffering(String lessonType, String city) {
    for (Offering offering : offerings) {
        if (offering.getLessonType().equalsIgnoreCase(lessonType) &&
            offering.getLocation().getCity().equalsIgnoreCase(city) &&
            offering.isAvailable()) {
            return offering;
        }
    }
    return null; // No matching offering found
}
public Offering getOfferingByLocationAndTime(String locationName, DayTimeSlot dayTimeSlot) {
    for (Offering offering : offerings) {
        if (offering.getLocation().getName().equalsIgnoreCase(locationName) &&
            offering.getDayTimeSlot().equals(dayTimeSlot)) {
            return offering;
        }
    }
    return null; // Offering not found
}


public void deleteOffering(Offering offering) {
    offerings.remove(offering);
}


    public boolean removeInstructor(String instructorName) {
    Iterator<Instructor> iterator = instructors.iterator();
    while (iterator.hasNext()) {
        Instructor instructor = iterator.next();
        if (instructor.getName().equalsIgnoreCase(instructorName)) {
            iterator.remove();
            return true;
        }
    }
    return false;
}


    public boolean isOfferingUnique(Location location, DayTimeSlot dayTimeSlot) {
    for (Offering offering : offerings) {
        if (offering.getLocation().equals(location) && offering.getDayTimeSlot().equals(dayTimeSlot)) {
            return false; // Offering already exists
        }
    }
    return true; // Offering is unique
}


}
