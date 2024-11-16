package Implementation;

import java.util.Objects;

public class DayTimeSlot {
    private String day;
    private String timeSlot;

    public DayTimeSlot(String day, String timeSlot) {
        this.day = day;
        this.timeSlot = timeSlot;
    }

    public static DayTimeSlot parse(String dayTimeSlotStr) {
    // Assuming dayTimeSlotStr is formatted as "Day - Time"
    String[] parts = dayTimeSlotStr.split(" - ");
    return new DayTimeSlot(parts[0], parts[1]);
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

    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    DayTimeSlot that = (DayTimeSlot) obj;
    return day.equals(that.day) && timeSlot.equals(that.timeSlot);
}

@Override
public int hashCode() {
    return Objects.hash(day, timeSlot);
}

}
