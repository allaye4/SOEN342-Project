package Implementation;

// Instructor class
class Instructor {
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

    public String[] getSpecializations() {
        return specializations;
    }

    public boolean isSpecialized(String specialization) {
        for (String s : specializations) {
            if (s.equals(specialization)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAvailableInCity(String city) {
        for (String c : availabilityCities) {
            if (c.equals(city)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (Specializations: " + String.join(", ", specializations) + ")";
    }
}