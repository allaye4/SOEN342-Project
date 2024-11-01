# System Operations for the Cancel Booking Use Case

## Contract CO1: Cancel a Booking

**Operation:** `cancelBooking(booking: Booking)`

**Cross References:** Use Case: Cancel Booking

**Preconditions:**
- Client is logged in
- The booking exists in the system
- The booking belongs to the client
- The booking status is "booked"

**Postconditions:**
- The booking status is changed to "cancelled"
- The offering's capacity is increased by 1
- The offering's status is updated to reflect the new capacity
- The method returns true if the cancellation was successful, false otherwise

---