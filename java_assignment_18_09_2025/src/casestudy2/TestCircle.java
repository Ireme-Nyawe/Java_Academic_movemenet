package casestudy2;

import java.util.Scanner;

public class TestCircle {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Circle radius: ");
        double rad = input.nextDouble();
        Circle circle1 = new Circle(rad);
        System.out.println("--------------------------------");
        System.out.println("Select an operation to move with: ");
        System.out.println("1. display calculated circle Area.");
        System.out.println("2. display calculated circle circumference.");

        int op = input.nextInt();
        switch (op){
            case 1:
                System.out.println("calculated circle Area is: "+circle1.calculateArea());
                break;
            case 2:
                System.out.println("calculated circle circumference is: "+circle1.calculateCircumference());
                break;
            default:
                System.out.println("Invalid input");

        }
        input.close();

    }
}
