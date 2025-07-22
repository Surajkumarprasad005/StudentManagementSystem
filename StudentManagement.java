import java.util.*;
import java.io.*;

public class StudentManagement {

    // --- Student Class ---
    static class Student {
        private String name;
        private int rollNo;
        private String grade;

        public Student(String name, int rollNo, String grade) {
            this.name = name;
            this.rollNo = rollNo;
            this.grade = grade;
        }

        public String getName() { return name; }
        public int getRollNo() { return rollNo; }
        public String getGrade() { return grade; }

        public void setName(String name) { this.name = name; }
        public void setGrade(String grade) { this.grade = grade; }

        @Override
        public String toString() {
            return "Name: " + name + "\nRoll No: " + rollNo + "\nGrade: " + grade;
        }

        public String toFileString() {
            return rollNo + "," + name + "," + grade;
        }

        public static Student fromFileString(String line) {
            String[] parts = line.split(",");
            if (parts.length != 3) return null;
            int roll = Integer.parseInt(parts[0]);
            return new Student(parts[1], roll, parts[2]);
        }
    }

    // --- Management System ---
    static class StudentSystem {
        private ArrayList<Student> students = new ArrayList<>();
        private final String filename = "students.txt";

        public StudentSystem() {
            loadFromFile();
        }

        public void addStudent(Student s) {
            for (Student stu : students) {
                if (stu.getRollNo() == s.getRollNo()) {
                    System.out.println("Student already exists.");
                    return;
                }
            }
            students.add(s);
            saveToFile();
            System.out.println("Student added successfully.");
        }

        public boolean removeStudent(int rollNo) {
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getRollNo() == rollNo) {
                    students.remove(i);
                    saveToFile();
                    return true;
                }
            }
            return false;
        }

        public Student searchStudent(int rollNo) {
            for (Student s : students) {
                if (s.getRollNo() == rollNo) {
                    return s;
                }
            }
            return null;
        }

        public void displayAll() {
            if (students.isEmpty()) {
                System.out.println("No student records.");
            } else {
                for (Student s : students) {
                    System.out.println("\n" + s);
                }
            }
        }

        private void saveToFile() {
            try (FileWriter fw = new FileWriter(filename)) {
                for (Student s : students) {
                    fw.write(s.toFileString() + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error saving file.");
            }
        }

        private void loadFromFile() {
            try {
                File file = new File(filename);
                Scanner sc = new Scanner(file);
                students.clear();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    Student s = Student.fromFileString(line);
                    if (s != null) students.add(s);
                }
                sc.close();
            } catch (FileNotFoundException e) {
                // File not found initially – ok to ignore
            }
        }
    }

    // --- Main Method ---
    public static void main(String[] args) {
        StudentSystem sm = new StudentSystem();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Student Management Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Enter a number.");
                sc.next(); // consume invalid input
            }

            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Roll No: ");
                    int roll = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Grade: ");
                    String grade = sc.nextLine();
                    sm.addStudent(new Student(name, roll, grade));
                    break;

                case 2:
                    System.out.print("Enter Roll No to remove: ");
                    int r = sc.nextInt();
                    if (sm.removeStudent(r)) {
                        System.out.println("Student removed.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Roll No to search: ");
                    int sr = sc.nextInt();
                    Student s = sm.searchStudent(sr);
                    if (s != null) {
                        System.out.println("Found:\n" + s);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    sm.displayAll();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}

