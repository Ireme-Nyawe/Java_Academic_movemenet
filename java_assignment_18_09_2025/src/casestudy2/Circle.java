package casestudy2;

public class Circle {
    private double radius;

    private static double pi=3.14;

    public Circle(double radius) {
        this.radius = radius;
    }
    public double calculateArea(){
        return (pi*Math.pow(radius,2));

    }
    public double calculateCircumference(){
        return (2*pi*radius);

    }



}
