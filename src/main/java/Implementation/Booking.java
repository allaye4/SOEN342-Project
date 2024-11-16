package Implementation;

import java.sql.*;
public class Booking {
    private Offering offering;
    private Client client;

    public Booking(Offering offering, Client client) {
        this.offering = offering;
        this.client = client;
        this.offering.isAvailable = false; // Mark the offering as unavailable
    }

    @Override
    public String toString() {
        return "Booking: " + client + " for " + offering;
    }

    public void save() throws SQLException {
        String query = "INSERT INTO Booking (clientId, offeringId) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, client.getId());
            stmt.setInt(2, offering.getId());
            stmt.executeUpdate();
        }
    }
}
