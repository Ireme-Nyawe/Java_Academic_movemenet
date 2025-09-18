package casestudy5;

import java.util.Scanner;

public class ShapeComparison {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter length of rectangle: ");
        double length = scanner.nextDouble();
        System.out.print("Enter width of rectangle: ");
        double width = scanner.nextDouble();
        Rectangle rectangle = new Rectangle(length, width);

        System.out.print("Enter radius of circle: ");
        double radius = scanner.nextDouble();
        Circle circle = new Circle(radius);

        double rectangleArea = rectangle.calculateArea();
        double circleArea = circle.calculateArea();

        System.out.println("Area of Rectangle = " + rectangleArea);
        System.out.println("Area of Circle = " + circleArea);

        if (rectangleArea > circleArea) {
            System.out.println("Rectangle has a larger area.");
        } else if (circleArea > rectangleArea) {
            System.out.println("Circle has a larger area.");
        } else {
            System.out.println("Rectangle and Circle have same area.");
        }

        scanner.close();
    }
}