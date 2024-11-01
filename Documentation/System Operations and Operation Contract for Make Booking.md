# System Operations for the Make Booking Use Case

- makeBooking(offering: Offering)
- updateOffering(offering: Offering)


# Contracts for Make Booking Use Case

## Contract CO1: Client Books an Offering

**Operation:** `makeBookings(offering: Offering)`

**Cross References:** Use Case: Make Booking

**Preconditions:**
- Client is logged in
- The offering is a valid offering
- The offering is available (i.e has availability)


**Postconditions:**
- The booking is created.
- The organization's offering information is updated.
- The booking status is set to "booked".
- The method returns true if the booking was successful, false otherwise.

---

## Contract CO2: Update Organization Offerings

**Operation:** `updateOffering(offering: Offering)`

**Cross References:** Use Case: Make Booking

**Preconditions:**
- Client is logged in
- The offering is a valid offering
- The offering exists in the organization's offerings

**Postconditions:**
- Offering capacity is decreased by 1
- The offering's status is updated to reflect the new capacity

---
