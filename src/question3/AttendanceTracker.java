package question3;
import java.util.Scanner;

public class AttendanceTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int MAX_DAYS = 30;
        int[] attendance = new int[MAX_DAYS];
        int dayCount = 0;

        System.out.print("Enter total number of students in the class: ");
        int classSize = scanner.nextInt();

        String moreData = "yes";
        do {
            if (dayCount >= MAX_DAYS) {
                System.out.println("Maximum of 30 days reached.");
                break;
            }

            System.out.print("Enter number of students present on Day " + (dayCount + 1) + ": ");
            int present = scanner.nextInt();

            // ✅ Basic validation
            if (present < 0 || present > classSize) {
                System.out.println("Invalid number. Please enter between 0 and " + classSize + ".");
                continue;
            }

            attendance[dayCount] = present;
            dayCount++;

            System.out.print("Do you want to enter attendance for another day? (yes/no): ");
            moreData = scanner.next();

        } while (moreData.equalsIgnoreCase("yes"));

        int totalAttendance = 0;
        int lowAttendanceDays = 0;

        System.out.println("\nAttendance Summary:");
        System.out.println("Day\tStudents Present");

        for (int i = 0; i < dayCount; i++) {
            System.out.println((i + 1) + "\t" + attendance[i]);
            totalAttendance += attendance[i];

            if (attendance[i] < classSize / 2.0) {
                lowAttendanceDays++;
            }
        }

        if (dayCount > 0) {
            double averageAttendance = (double) totalAttendance / dayCount;
            double lowAttendancePercent = (double) lowAttendanceDays / dayCount * 100;

            System.out.println("\nAverage attendance: " + averageAttendance + " students");
            System.out.println("Days with attendance below 50%: " + lowAttendanceDays + " (" + lowAttendancePercent + "%)");
        } else {
            System.out.println("No attendance data recorded.");
        }
    }
}
