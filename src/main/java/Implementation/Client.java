package Implementation;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Client {
    private int id;
    private String name;
    private int age;
    private List<Offering> bookedOfferings;

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
        this.bookedOfferings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public List<Offering> getBookedOfferings() {
        return bookedOfferings;
    }

    // Method to book an offering
    public void bookOffering(Offering offering, Client client) {
        if (offering.getCapacity() > 0) {
            bookedOfferings.add(offering);
            offering.setCapacity(); 
            if(client.getAge()<18){ 
                offering.setCapacity(); 
            }
            System.out.println("Successfully booked: " + offering);
        } else {
            System.out.println("The offering is full already.");
        }
    }

    @Override
    public String toString() {
        return name + " (Age: " + age + ")";
    }

    public void save() throws SQLException {
        String query = "INSERT INTO Client (name, age) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.executeUpdate();
        }
    }
}
