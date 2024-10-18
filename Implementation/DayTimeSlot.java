package Implementation;

// DayTimeSlot class
class DayTimeSlot {
    private String day;
    private String timeSlot;

    public DayTimeSlot(String day, String timeSlot) {
        this.day = day;
        this.timeSlot = timeSlot;
    }

    public String getDay() {
        return day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        return day + " " + timeSlot;
    }
}
