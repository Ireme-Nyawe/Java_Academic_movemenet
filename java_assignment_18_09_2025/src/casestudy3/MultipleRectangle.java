package casestudy3;

public class MultipleRectangle {
    private double length;
    private double width;

    public MultipleRectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
    public double calculateArea(){
        return (this.length*this.width);
    }

}
