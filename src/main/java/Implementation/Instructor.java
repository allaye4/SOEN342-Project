package Implementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Instructor {
    private int id;
    private String name;
    private String[] specializations;
    private String[] availabilityCities;

    public Instructor(String name, String[] specializations, String[] availabilityCities) {
        this.name = name;
        this.specializations = specializations;
        this.availabilityCities = availabilityCities;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Return the list of cities where the instructor is available
    public List<String> getCities() {
        return new ArrayList<>(List.of(availabilityCities));
    }

    // Return the list of specializations the instructor has
    public List<String> getSpecializations() {
        return new ArrayList<>(List.of(specializations));
    }

    // Check if the instructor specializes in a certain lesson type
    public boolean isSpecialized(String specialization) {
        for (String s : specializations) {
            if (s.equalsIgnoreCase(specialization)) {
                return true;
            }
        }
        return false;
    }

    // Check if the instructor is available in a certain city
    public boolean isAvailableInCity(String city) {
        for (String c : availabilityCities) {
            if (c.equalsIgnoreCase(city)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (Specializations: " + String.join(", ", specializations) + ")";
    }

    public static Instructor getInstructorById(int id) throws SQLException {
        String query = "SELECT * FROM Instructor WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String specializations = rs.getString("specializations");
                String cities = rs.getString("cities");
                return new Instructor(name, specializations.split(","), cities.split(","));
            }
            return null;
        }
    }

    public void save() throws SQLException {
        String query = "INSERT INTO Instructor (name, specializations, cities) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, String.join(",", specializations));
            stmt.setString(3, String.join(",", availabilityCities));
            stmt.executeUpdate();
        }
    }
}
