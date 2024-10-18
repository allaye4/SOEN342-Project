package Implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




// Main class with terminal interface
public class Driver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Administrator admin = new Administrator();

        // Example data: Locations, instructors, and lessons
        Location location1 = new Location("EV-Building", "Montreal");
        Location location2 = new Location("Gym A", "Laval");

        Instructor instructor1 = new Instructor("Grace", new String[]{"Swimming"}, new String[]{"Montreal", "Laval"});
        Instructor instructor2 = new Instructor("Alex", new String[]{"Judo"}, new String[]{"Montreal"});

        // Terminal interface
        while (true) {
            System.out.println("\nChoose an action: ");
            System.out.println("1. Add new offering");
            System.out.println("2. View available offerings");
            System.out.println("3. Assign offering to instructor");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Add new offering
                    System.out.println("Enter lesson type (e.g., Judo, Swimming): ");
                    String lessonType = scanner.nextLine();

                    System.out.println("Enter mode (group or private): ");
                    String mode = scanner.nextLine();

                    System.out.println("Enter location (e.g., EV-Building, Montreal): ");
                    String locationName = scanner.nextLine();
                    System.out.println("Enter city (e.g., Montreal): ");
                    String city = scanner.nextLine();
                    Location location = new Location(locationName, city);

                    System.out.println("Enter day (e.g., Sunday): ");
                    String day = scanner.nextLine();
                    System.out.println("Enter time slot (e.g., 12:00-13:00): ");
                    String timeSlot = scanner.nextLine();
                    DayTimeSlot dayTimeSlot = new DayTimeSlot(day, timeSlot);

                    System.out.println("Enter capacity: ");
                    int capacity = scanner.nextInt();

                    admin.addOffering(location, lessonType, mode, dayTimeSlot, capacity);
                    break;

                case 2: // View available offerings
                    admin.viewOfferings();
                    break;

                case 3: // Assign offering to instructor
                    admin.viewOfferings();
                    System.out.println("Enter the index of the offering to assign: ");
                    int offeringIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Offering selectedOffering = admin.getOfferings().get(offeringIndex - 1);
                    if (!selectedOffering.isAvailable()) {
                        System.out.println("Offering is not available.");
                        break;
                    }

                    System.out.println("Select an instructor:");
                    System.out.println("1. Grace (Swimming)");
                    System.out.println("2. Alex (Judo)");

                    int instructorChoice = scanner.nextInt();
                    Instructor selectedInstructor = (instructorChoice == 1) ? instructor1 : instructor2;

                    if (selectedInstructor.isAvailableInCity(selectedOffering.getLocation().getCity())
                            && selectedOffering.getLessonType().equals(selectedInstructor.getSpecializations()[0])) {
                        selectedOffering.assignInstructor(selectedInstructor);
                        System.out.println("Instructor assigned successfully.");
                    } else {
                        System.out.println("Instructor is not available for this offering.");
                    }
                    break;

                case 4: // Exit
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
