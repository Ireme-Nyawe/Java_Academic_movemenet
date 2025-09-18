package casestudy4;

public class Circle {
private double radius;

// Constructor
public Circle(double radius) {
    this.radius = radius;
}

// Method to calculate area
public double calculateArea() {
    return Math.PI * radius * radius;
}
}
