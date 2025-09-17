package question2;
import java.util.Scanner;

public class StudentGrading {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int totalStudents = 0;
        int passes = 0;
        int fails = 0;

        while (true) {
            System.out.print("Enter student's mark (0–100) or -1 to finish: ");
            int mark = scanner.nextInt();

            if (mark == -1) {
                break;
            }

            if (mark < 0 || mark > 100) {
                System.out.println("Invalid mark. Please enter a value between 0 and 100.");
                continue;
            }

            totalStudents++;

            // Assign grade
            String grade;
            if (mark >= 80) {
                grade = "A";
            } else if (mark >= 70) {
                grade = "B";
            } else if (mark >= 60) {
                grade = "C";
            } else if (mark >= 50) {
                grade = "D";
            } else {
                grade = "F";
            }

            // Count passes and fails
            if (mark >= 50) {
                passes++;
            } else {
                fails++;
            }

            System.out.println("Grade: " + grade);
        }

        // Summary report
        System.out.println("\nSummary Report:");
        System.out.println("Total students: " + totalStudents);
        System.out.println("Passed: " + passes);
        System.out.println("Failed: " + fails);

        if (totalStudents > 0) {
            double passRate = (double) passes / totalStudents * 100;
            System.out.println("Class Pass Rate: " + passRate + "%");
        } else {
            System.out.println("No student data entered.");
        }

    }
}
