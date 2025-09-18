package casestudy4;

import java.util.Scanner;

public class ShapeChooser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a shape to calculate area:");
        System.out.println("1. Rectangle");
        System.out.println("2. Circle");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter length: ");
                double length = scanner.nextDouble();
                System.out.print("Enter width: ");
                double width = scanner.nextDouble();
                Rectangle rectangle = new Rectangle(length, width);
                System.out.println("Area of Rectangle = " + rectangle.calculateArea());
                break;

            case 2:
                System.out.print("Enter radius: ");
                double radius = scanner.nextDouble();
                Circle circle = new Circle(radius);
                System.out.println("Area of Circle = " + circle.calculateArea());
                break;

            default:
                System.out.println("Invalid choice!");
        }

        scanner.close();
    }
}