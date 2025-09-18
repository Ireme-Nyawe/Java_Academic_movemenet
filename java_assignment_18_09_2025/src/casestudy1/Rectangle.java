package casestudy1;

public class Rectangle {
    private double length;
    private double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    private void    checkSquare(){
        if(this.length==this.width){
            System.out.println("the shape is Square,");
        }
        else {
            System.out.println("The shape is not square");
        }
    }
    public double calculateArea(){
        this.checkSquare();
        return (this.length*this.width);
    }

}
