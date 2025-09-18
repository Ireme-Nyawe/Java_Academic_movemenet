package casestudy1;
import java.util.Scanner;

public class TestRectangle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter width: ");
        double width=scanner.nextDouble();
        System.out.print("Enter Length: ");
        double length=scanner.nextDouble();

        Rectangle rectangle1= new Rectangle(length,width);
        double area=rectangle1.calculateArea();
        System.out.println("the area is : "+area);


scanner.close();
    }
}
