package Implementation;

// Offering class
class Offering {
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

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Assigned to " + instructor.getName();
        return lessonType + " (" + mode + "), " + location + ", " + dayTimeSlot + " - " + status;
    }
}

