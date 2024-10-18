# System Operations for the Process Offerings Use Case

- addOffering(LocationType, String, String, DayTimeSlotType, int)
- selectOffering(Offering, Instructor)
- viewOfferings()
- registerInstructor(String, String[], String[])
- viewAvailableOfferings(Instructor)
- deleteInstructor(Instructor)

# Contracts for Process Offerings Use Case

## Contract CO1: Administrator Creates an Offering

**Operation:** `addOffering(location: LocationType, lessonType: String, mode: String, dayTimeSlot: DayTimeSlotType, capacity: int)`

**Cross References:** Use Case: Process Offerings

**Preconditions:**
- Administrator is logged in.
- The location is available for the specified `DayTimeSlot`.
- The lesson type (e.g., Judo, swimming) is valid.

**Postconditions:**
- An `Offering` instance `offering` was created. *(instance creation)*
- `offering.location` was set to `location`. *(attribute modification)*
- `offering.lessonType` was set to `lessonType`. *(attribute modification)*
- `offering.mode` was set to `mode` (group or private). *(attribute modification)*
- `offering.dayTimeSlot` was set to the given time slots. *(attribute modification)*
- `offering` was made available for public viewing. *(association formed)*

---

## Contract CO2: Instructor Selects Offering

**Operation:** `selectOffering(offering: Offering, instructor: Instructor)`

**Cross References:** Use Case: Process Offerings

**Preconditions:**
- The offering is available.
- The instructor is registered and has the correct specialization for the offering.
- The instructor is available in the offering's city.

**Postconditions:**
- `instructor` was associated with the `offering`. *(association formed)*
- The `offering` was marked as no longer available to other instructors. *(attribute modification)*
- The `offering` was updated to show the assigned instructor for public viewing. *(attribute modification)*

---

## Contract CO3: Admin Views Available Offerings

**Operation:** `viewOfferings()`

**Cross References:** Use Case: Process Offerings

**Preconditions:**
- Offerings are available in the system.

**Postconditions:**
- A list of offerings is presented showing location, lesson type, `dayTimeSlot`, and availability status (only for admin). *(no instance creation or modification; only viewing public data)*

---

## Contract CO4: Instructor Registration

**Operation:** `registerInstructor(name: String, specialization: String[], availabilityCities: String[])`

**Cross References:** Use Case: Process Offerings

**Preconditions:**
- The instructor is not already registered in the system.
- Valid specialization(s) and city names are provided.

**Postconditions:**
- An `Instructor` instance `instructor` was created. *(instance creation)*
- `instructor.name` was set to `name`. *(attribute modification)*
- `instructor.specialization` was set to `specialization`. *(attribute modification)*
- `instructor.availabilityCities` was set to `availabilityCities`. *(attribute modification)*
- `instructor` was made available to take on future offerings. *(association formed)*

---

## Contract CO5: Instructor Views Available Offerings

**Operation:** `viewAvailableOfferings(instructor: Instructor)`

**Cross References:** Use Case: Process Offerings

**Preconditions:**
- The instructor is registered in the system.

**Postconditions:**
- A list of offerings that are available is displayed. *(no instance creation or modification; only data viewing)*
- The offerings are displayed with details such as lesson type, location, and time slot.

---

## Contract CO6: Delete Instructor

**Operation:** `deleteInstructor(instructor: Instructor)`

**Cross References:** Use Case: Process Offerings

**Preconditions:**
- The administrator is logged in and has access to the system.
- The instructor exists in the system.

**Postconditions:**
- The `Instructor` instance `instructor` was deleted. *(instance deletion)*
- Any offerings associated with `instructor` were marked as available to other instructors. *(attribute modification/association removed)*
- The instructor is no longer able to log into the system or view offerings. *(association broken)*
