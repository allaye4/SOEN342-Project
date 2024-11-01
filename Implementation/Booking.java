package Implementation;

public class Booking {
    Offering offering;
    Client client;

    public Booking(Offering offering, Client client) {
        this.offering = offering;
        this.client = client;
        this.offering.isAvailable = false;
    }
}
