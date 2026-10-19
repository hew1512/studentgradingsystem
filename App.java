import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import student.Student;


public class App {

    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE_PATH = "data/student.txt";


    private final Map<String, Student> students = new LinkedHashMap<>();

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        loadStudents();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    listStudents();
                    break;
                case "3":
                    saveStudents();
                    break;
                case "99":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println();
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("Student Grading System v1.0");
        System.out.println("===== main menu =====");
        System.out.println("1 - Add student");
        System.out.println("2 - List student");
        System.out.println("3 - Save student");
        System.out.println("99 - Quit");
        System.out.println("=====================");
        System.out.print("[Select menu] ");
    }

 
    private void addStudent() {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Student ID cannot be empty. Add cancelled.");
            return;
        }

        if (students.containsKey(id)) {
            System.out.println("A student with ID " + id + " already exists. Add cancelled.");
            return;
        }

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter surname: ");
        String surname = scanner.nextLine().trim();

        double score;
        while (true) {
            System.out.print("Enter score (0-100): ");
            String scoreInput = scanner.nextLine().trim();
            try {
                score = Double.parseDouble(scoreInput);
                if (score < 0 || score > 100) {
                    System.out.println("Score must be between 0 and 100.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a numeric score.");
            }
        }

        Student student = new Student(id, firstName, surname, score);
        students.put(id, student);

        System.out.println("Student added successfully: " + student);
    }

    private void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.printf("%-10s %-12s %-12s %-8s %-5s%n",
                "ID", "First Name", "Surname", "Score", "Grade");
        System.out.println("---------------------------------------------------");

        for (Student s : students.values()) {
            System.out.printf("%-10s %-12s %-12s %-8.1f %-5s%n",
                    s.getStudentId(), s.getFirstName(), s.getSurname(),
                    s.getScore(), s.getGrade());
        }
    }

    private void saveStudents() {
        File dir = new File(DATA_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new File(DATA_FILE_PATH))) {
            for (Student s : students.values()) {
                writer.println(s.toCsvLine());
            }
            System.out.println("Saved " + students.size() + " student record(s) to " + DATA_FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    

    private void loadStudents() {
        File file = new File(DATA_FILE_PATH);

        if (!file.exists()) {
            System.out.println("No existing data file found. Starting with an empty student list.");
            return;
        }

        int loadedCount = 0;
        int skippedCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 4) {
                    skippedCount++;
                    continue;
                }

                try {
                    String id = parts[0].trim();
                    String firstName = parts[1].trim();
                    String surname = parts[2].trim();
                    double score = Double.parseDouble(parts[3].trim());

                   
                    Student student = new Student(id, firstName, surname, score);
                    students.put(id, student);
                    loadedCount++;
                } catch (NumberFormatException e) {
                    skippedCount++;
                }
            }
            System.out.println("Loaded " + loadedCount + " student record(s) from " + DATA_FILE_PATH
                    + (skippedCount > 0 ? " (" + skippedCount + " invalid line(s) skipped)" : ""));
        } catch (IOException e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
    }
}
