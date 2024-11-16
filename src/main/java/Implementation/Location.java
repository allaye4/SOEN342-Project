package Implementation;

import java.util.Objects;

public class Location {
    private String name;
    private String city;

    public Location(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public static Location parse(String locationStr) {
    // Assuming locationStr is formatted as "City, Address"
    String[] parts = locationStr.split(", ");
    return new Location(parts[0], parts[1]);
}
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return name + ", " + city;
    }

    public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Location location = (Location) obj;
    return name.equals(location.name) && city.equals(location.city);
}

@Override
public int hashCode() {
    return Objects.hash(name, city);
}
}
