package Implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private static Administrator admin;
    private static List<Instructor> instructors;
    private static List<Client> clients;
    private static Scanner scanner;

    public static void main(String[] args) {
        admin = new Administrator();
        instructors = new ArrayList<>();
        clients = new ArrayList<>();
        scanner = new Scanner(System.in);
        int choice;

        do {
            clearScreen();
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addOffering();
                    break;
                case 2:
                    admin.viewOfferings();
                    pause();
                    break;
                case 3:
                    registerInstructor();
                    break;
                case 4:
                    viewAvailableOfferings();
                    pause();
                    break;
                case 5:
                    makeBooking();
                    break;
                case 0:
                    System.out.println("Exiting the system.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }

    private static void showMenu() {
        System.out.println("=== Offerings Management System ===");
        System.out.println("1. Add Offering");
        System.out.println("2. View Offerings");
        System.out.println("3. Register Instructor");
        System.out.println("4. View Available Offerings");
        System.out.println("5. Make Booking");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void clearScreen() {
        // Clear the console screen (works in most terminals)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void addOffering() {
        System.out.println("Enter Offering Details:");
        System.out.print("Location Name: ");
        String locationName = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Lesson Type: ");
        String lessonType = scanner.nextLine();
        System.out.print("Mode (group/private): ");
        String mode = scanner.nextLine();
        System.out.print("Day: ");
        String day = scanner.nextLine();
        System.out.print("Time Slot: ");
        String timeSlot = scanner.nextLine();
        System.out.print("Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Location location = new Location(locationName, city);
        DayTimeSlot dayTimeSlot = new DayTimeSlot(day, timeSlot);
        admin.addOffering(location, lessonType, mode, dayTimeSlot, capacity);
        pause();
    }

    private static void registerInstructor() {
        System.out.print("Instructor Name: ");
        String name = scanner.nextLine();
        System.out.print("Specializations (comma-separated): ");
        String[] specializations = scanner.nextLine().split(",");
        System.out.print("Availability Cities (comma-separated): ");
        String[] availabilityCities = scanner.nextLine().split(",");

        Instructor instructor = new Instructor(name, specializations, availabilityCities);
        instructors.add(instructor);
        System.out.println("Instructor registered: " + instructor);
        pause();
    }

    private static void viewAvailableOfferings() {
        System.out.println("Available Offerings:");
        for (Offering offering : admin.getOfferings()) {
            if (offering.isAvailable()) {
                System.out.println(offering);
            }
        }
        pause();
    }

    private static void makeBooking() {
        System.out.println("Enter Booking Details:");
        System.out.print("Client Name: ");
        String clientName = scanner.nextLine();
        System.out.print("Client Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Client client = new Client(clientName, age);
        clients.add(client);

        System.out.println("Available Offerings:");
        for (Offering offering : admin.getOfferings()) {
            if (offering.isAvailable()) {
                System.out.println(offering);
            }
        }

        System.out.print("Select Offering (lesson type, location): ");
        String offeringInput = scanner.nextLine();
        String[] offeringDetails = offeringInput.split(",");
        String lessonType = offeringDetails[0].trim();
        String locationName = offeringDetails[1].trim();

        Offering selectedOffering = null;
        for (Offering offering : admin.getOfferings()) {
            if (offering.getLessonType().equalsIgnoreCase(lessonType) &&
                offering.getLocation().getName().equalsIgnoreCase(locationName) &&
                offering.isAvailable()) {
                selectedOffering = offering;
                break;
            }
        }

        if (selectedOffering != null) {
            Booking booking = new Booking(selectedOffering, client);
            System.out.println("Booking successful for " + clientName + " to " + selectedOffering);
        } else {
            System.out.println("Selected offering is not available.");
        }
        pause();
    }
}
