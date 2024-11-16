package Implementation;

import java.util.*;
import java.sql.*;

public class Driver {
    private static final String ADMIN_PASSWORD = "admin123";
    private static Administrator admin;
    private static List<Instructor> instructors;
    private static List<Client> clients;
    private static Scanner scanner;
    private static Connection conn;

    public static void main(String[] args) {
        admin = new Administrator();
        instructors = new ArrayList<>();
        clients = new ArrayList<>();
        scanner = new Scanner(System.in);


          // Initialize the database connection
        initDatabase();

        int choice;
        do {
            clearScreen();
            showRoleSelectionMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    if (adminLogin()) {
                        adminActions();
                    }
                    break;
                case 2:
                    instructorActions();
                    break;
                case 3:
                    clientActions();
                    break;
                case 4:
                    publicViewing();
                    break;
                case 0:
                    System.out.println("Exiting the system.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);

         closeDatabase();
    }

    private static void initDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:offerings.db");
            System.out.println("Connected to the database.");
            createTablesIfNotExist();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void createTablesIfNotExist() {
        try (Statement stmt = conn.createStatement()) {
            // Create tables if they do not exist
            String createOfferingsTable = "CREATE TABLE IF NOT EXISTS offerings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "location_name TEXT, " +
                    "city TEXT, " +
                    "lesson_type TEXT, " +
                    "mode TEXT, " +
                    "day TEXT, " +
                    "time_slot TEXT, " +
                    "capacity INTEGER, " +
                    "available BOOLEAN)";
            stmt.execute(createOfferingsTable);

            String createInstructorsTable = "CREATE TABLE IF NOT EXISTS instructors (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "specializations TEXT, " +
                    "cities TEXT)";
            stmt.execute(createInstructorsTable);

            String createClientsTable = "CREATE TABLE IF NOT EXISTS clients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "age INTEGER)";
            stmt.execute(createClientsTable);

        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    private static void closeDatabase() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database: " + e.getMessage());
        }
    }

    // SQLite operations for Offerings, Instructors, Clients

    private static void addOffering() {
        System.out.print("Enter location name: ");
        String locationName = scanner.nextLine();
        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        Location location = new Location(locationName, city);

        System.out.print("Enter lesson type: ");
        String lessonType = scanner.nextLine();
        System.out.print("Enter mode (group/private): ");
        String mode = scanner.nextLine();
        System.out.print("Enter day (format: dd/mm/yyyy): ");
        String day = scanner.nextLine();
        System.out.print("Enter time slot (format: ##:##-##:## in 24h format): ");
        String timeSlot = scanner.nextLine();
        DayTimeSlot dayTimeSlot = new DayTimeSlot(day, timeSlot);

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (!admin.isOfferingUnique(location, dayTimeSlot)) {
            System.out.println("An offering already exists at this location and time.");
        } else {
            admin.addOffering(location, lessonType, mode, dayTimeSlot, capacity);
            saveOfferingToDatabase(locationName, city, lessonType, mode, day, timeSlot, capacity);
            System.out.println("Offering added successfully.");
        }
        pause();
    }

    private static void saveOfferingToDatabase(String locationName, String city, String lessonType, 
                                               String mode, String day, String timeSlot, int capacity) {
        String sql = "INSERT INTO offerings (location_name, city, lesson_type, mode, day, time_slot, capacity, available) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, locationName);
            pstmt.setString(2, city);
            pstmt.setString(3, lessonType);
            pstmt.setString(4, mode);
            pstmt.setString(5, day);
            pstmt.setString(6, timeSlot);
            pstmt.setInt(7, capacity);
            pstmt.setBoolean(8, true); // Assuming the offering is available by default
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving offering to database: " + e.getMessage());
        }
    }

    private static void registerInstructor() {
        System.out.print("Enter instructor name: ");
        String instructorName = scanner.nextLine();

        System.out.print("Enter specializations (comma-separated): ");
        String[] specializations = scanner.nextLine().split(", ");

        System.out.print("Enter availability cities (comma-separated): ");
        String[] cities = scanner.nextLine().split(", ");

        Instructor instructor = new Instructor(instructorName, specializations, cities);
        admin.registerInstructor(instructor);
        saveInstructorToDatabase(instructorName, specializations, cities);
        System.out.println("Instructor registered successfully.");
        pause();
    }

    private static void saveInstructorToDatabase(String name, String[] specializations, String[] cities) {
        String sql = "INSERT INTO instructors (name, specializations, cities) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, String.join(", ", specializations));
            pstmt.setString(3, String.join(", ", cities));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving instructor to database: " + e.getMessage());
        }
    }

    private static void registerClient() {
        System.out.print("Enter client name: ");
        String clientName = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Client client = new Client(clientName, age);
        clients.add(client);
        saveClientToDatabase(clientName, age);
        System.out.println("Client registered successfully.");
        pause();
    }

    private static void saveClientToDatabase(String name, int age) {
        String sql = "INSERT INTO clients (name, age) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving client to database: " + e.getMessage());
        }
    }

    private static boolean adminLogin() {
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        if (password.equals(ADMIN_PASSWORD)) {
            return true;
        } else {
            System.out.println("Incorrect password.");
            pause();
            return false;
        }
    }

    private static void showRoleSelectionMenu() {
        System.out.println("=== Welcome to the Offerings Management System ===");
        System.out.println("1. Admin");
        System.out.println("2. Instructor");
        System.out.println("3. Client");
        System.out.println("4. Public Viewing");
        System.out.println("0. Exit");
        System.out.print("Select your role: ");
    }

    private static void adminActions() {
    int choice;
    do {
        clearScreen();
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Add Offering");
        System.out.println("2. View Offerings");
        System.out.println("3. Delete Offering");
        System.out.println("4. View Instructors");
        System.out.println("5. Delete Instructor");
        System.out.println("6. View Clients");
        System.out.println("7. Delete Client");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addOffering();
                break;
            case 2:
                admin.viewOfferings();
                pause();
                break;
            case 3:
                deleteOffering();
                break;
            case 4:
                viewInstructors();
                pause();
                break;
            case 5:
                deleteInstructor();
                break;
            case 6:
                viewClients();
                pause();
                break;
            case 7:
                deleteClient();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    } while (choice != 0);
}




private static void deleteOffering() {
    List<Offering> offerings = admin.getOfferings();
    if (offerings.isEmpty()) {
        System.out.println("No offerings available to delete.");
        pause();
        return;
    }

    System.out.println("=== Offerings List ===");
    for (int i = 0; i < offerings.size(); i++) {
        System.out.println((i + 1) + ". " + offerings.get(i));
    }

    System.out.print("Enter the number of the offering to delete: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice > 0 && choice <= offerings.size()) {
        Offering selectedOffering = offerings.get(choice - 1);
        
        
        
        // Removing the offering from the list
        admin.deleteOffering(selectedOffering);
        System.out.println("Offering deleted successfully.");
    } else {
        System.out.println("Invalid selection.");
    }
    pause();
}
private static void deleteInstructor() {
    List<Instructor> instructors = admin.getInstructors();
    if (instructors.isEmpty()) {
        System.out.println("No instructors available to delete.");
        pause();
        return;
    }

    System.out.println("=== Instructors List ===");
    for (int i = 0; i < instructors.size(); i++) {
        System.out.println((i + 1) + ". " + instructors.get(i));
    }

    System.out.print("Enter the number of the instructor to delete: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice > 0 && choice <= instructors.size()) {
        Instructor selectedInstructor = instructors.get(choice - 1);
        
        
        
        // Removing the instructor from the list
        admin.removeInstructor(selectedInstructor.getName());
        System.out.println("Instructor deleted successfully.");
    } else {
        System.out.println("Invalid selection.");
    }
    pause();
}
private static void deleteClient() {
    if (clients.isEmpty()) {
        System.out.println("No clients available to delete.");
        pause();
        return;
    }

    System.out.println("=== Clients List ===");
    for (int i = 0; i < clients.size(); i++) {
        System.out.println((i + 1) + ". " + clients.get(i));
    }

    System.out.print("Enter the number of the client to delete: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice > 0 && choice <= clients.size()) {
        Client selectedClient = clients.get(choice - 1);
        
        
        
        // Removing the client from the list
        clients.remove(choice - 1);
        System.out.println("Client deleted successfully.");
    } else {
        System.out.println("Invalid selection.");
    }
    pause();
}
private static void viewInstructors() {
    System.out.println("=== Registered Instructors ===");
    List<Instructor> instructors = admin.getInstructors();
    if (instructors.isEmpty()) {
        System.out.println("No instructors registered.");
    } else {
        for (Instructor instructor : instructors) {
            System.out.println(instructor);
        }
    }
}


private static void viewClients() {
    System.out.println("=== Registered Clients ===");
    if (clients.isEmpty()) {
        System.out.println("No clients registered.");
    } else {
        for (Client client : clients) {
            System.out.println(client);
        }
    }
}


    private static void instructorActions() {
        int choice;
        do {
            clearScreen();
            System.out.println("=== Instructor Menu ===");
            System.out.println("1. Register Instructor");
            System.out.println("2. View Available Offerings");
            System.out.println("3. Register for Offering");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerInstructor();
                    break;
                case 2:
                    admin.viewOfferings();
                    pause();
                    break;
                case 3:
                    registerForOffering();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void clientActions() {
        int choice;
        do {
            clearScreen();
            System.out.println("=== Client Menu ===");
            System.out.println("1. Register Client");
            System.out.println("2. View Available Offerings");
            System.out.println("3. Book an Offering");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerClient();
                    break;
                case 2:
                    publicViewing();
                    break;
                case 3:
                    bookOffering();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void publicViewing() {
    clearScreen();
    System.out.println("=== List of Offerings ===");

    // Iterate through the offerings and display only those that are available and not full
    for (Offering offering : admin.getOfferings()) {
        if (!offering.isAvailable() && offering.getCapacity() > 0) {
            System.out.println(offering);
        }
    }

    pause();
}



    private static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private static void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void initializeData() {
        Location location1 = new Location("Gym A", "Montreal");
        DayTimeSlot slot1 = new DayTimeSlot("01/01/2024", "12:00-13:00");
        admin.addOffering(location1, "Yoga", "Group", slot1, 2);

        Instructor instructor1 = new Instructor("Alice", new String[]{"Yoga"}, new String[]{"Montreal"});
        admin.registerInstructor(instructor1);
    }

  private static void registerForOffering() {
    System.out.print("Enter your name: ");
    String instructorName = scanner.nextLine();

    Instructor instructor = admin.getInstructorByName(instructorName);
    if (instructor == null) {
        System.out.println("Instructor not found.");
        pause();
        return;
    }

    List<Offering> matchingOfferings = admin.findMatchingOfferings(instructor, admin.getOfferings());
    if (matchingOfferings.isEmpty()) {
        System.out.println("No matching offerings found for your specialization and location.");
        pause();
        return;
    }

    System.out.println("=== Matching Offerings ===");
    for (int i = 0; i < matchingOfferings.size(); i++) {
        System.out.println((i + 1) + ". " + matchingOfferings.get(i));
    }

    System.out.print("Enter the number of the offering to register for: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice > 0 && choice <= matchingOfferings.size()) {
        Offering selectedOffering = matchingOfferings.get(choice - 1);
        selectedOffering.assignInstructor(instructor);
        System.out.println("Successfully registered for the offering.");
    } else {
        System.out.println("Invalid selection.");
    }
    pause();
}


private static Client getClientByName(String name) {
    for (Client client : clients) {
        if (client.getName().equalsIgnoreCase(name)) {
            return client;
        }
    }
    return null; // Client not found
}


private static void bookOffering() {
    System.out.print("Enter your name: ");
    String clientName = scanner.nextLine();

    Client client = getClientByName(clientName);
    if (client == null) {
        System.out.println("Client not found.");
        pause();
        return;
    }

    // Filter the offerings to show only those that are available and not full (capacity > 0)
    List<Offering> availableOfferings = admin.getOfferings().stream()
            .filter(offering -> !offering.isAvailable() && offering.getCapacity() > 0)
            .toList();

    if (availableOfferings.isEmpty()) {
        System.out.println("No available offerings to book.");
        pause();
        return;
    }

    System.out.println("=== Available Offerings ===");
    for (int i = 0; i < availableOfferings.size(); i++) {
        System.out.println((i + 1) + ". " + availableOfferings.get(i));
    }

    System.out.print("Enter the number of the offering to book: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice > 0 && choice <= availableOfferings.size()) {
        if(client.getAge()<18){
            System.out.print("Enter your Guardian's name: ");
            String GuardianName = scanner.nextLine();
        }
        client.bookOffering(availableOfferings.get(choice - 1), client);
        System.out.println("Offering booked successfully.");

    } else {
        System.out.println("Invalid selection.");
    }
    pause();
}



}
