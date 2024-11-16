package Implementation;

import java.sql.*;

public class Offering {
    private int id;
    private Location location;
    private String lessonType;
    private String mode; // group or private
    private DayTimeSlot dayTimeSlot;
    private int capacity;
    private Instructor instructor;
    boolean isAvailable;

    public Offering(Location location, String lessonType, String mode, DayTimeSlot dayTimeSlot, int capacity) {
        this.location = location;
        this.lessonType = lessonType;
        this.mode = mode;
        this.dayTimeSlot = dayTimeSlot;
        this.capacity = capacity;
        this.isAvailable = true;
        
    }

    public void assignInstructor(Instructor instructor) {
        this.instructor = instructor;
        this.isAvailable = false;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
    this.isAvailable = isAvailable;
}


    public String getLessonType() {
        return lessonType;
    }

    public String getMode() {
        return mode;
    }

    public Location getLocation() {
        return location;
    }

    public DayTimeSlot getDayTimeSlot() {
        return dayTimeSlot;
    }

    public Instructor getInstructor() {
        return instructor;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(){
        if(capacity == 0){
            System.out.println("Maximum capacity has been reached");
        }
        else{
        this.capacity = capacity - 1;
    }

    }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Assigned to " + instructor.getName();
        return lessonType + " (" + mode + "), " + location + ", " + dayTimeSlot + " - " + status + ", Current Capacity: " + capacity;
    }

    public String getCity() {
        return location.getCity();
    }

     public static Offering getOfferingById(int id) throws SQLException {
        String query = "SELECT * FROM Offering WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Extract values and create Offering object
            }
            return null;
        }
    }

    public void save() throws SQLException {
        String query = "INSERT INTO Offering (location, lessonType, mode, dayTimeSlot, capacity, instructorId, isAvailable) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location.toString());
            stmt.setString(2, lessonType);
            stmt.setString(3, mode);
            stmt.setString(4, dayTimeSlot.toString());
            stmt.setInt(5, capacity);
            stmt.setInt(6, instructor != null ? instructor.getId() : -1);
            stmt.setBoolean(7, isAvailable);
            stmt.executeUpdate();
        }
    }
}
