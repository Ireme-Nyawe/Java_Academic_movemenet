package casestudy3;

import java.util.Scanner;

public class TestMultipleRectangle {
    public static void main(String[] args) {
        Scanner scanner =new Scanner(System.in);
//        loop to input values for 3 rect
        double[] rectanglesArea = new double[3];

        for (int i = 0; i < 3; i++) {
            System.out.print("Enter "+(i+1)+" Rectangle's Length: ");
            double len=scanner.nextDouble();
            System.out.print("Enter "+(i+1)+" Rectangle's Width: ");
            double wid=scanner.nextDouble();
            MultipleRectangle r = new MultipleRectangle(len,wid);
            rectanglesArea[i]=r.calculateArea();

        }

        System.out.println("\n--------------------------------------------");
        System.out.println("Here are the calculated Areas for Three rectangles: ");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < 3; i++) {
            System.out.println("The area of rectangle "+(i+1)+" is "+rectanglesArea[i]);
        }
        scanner.close();
    }

}
