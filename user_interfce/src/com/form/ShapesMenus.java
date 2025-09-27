//package com.form;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class ShapesMenus implements ActionListener {
//    JFrame frame;
//    JButton circleBtn;
//    JButton rectangleBtn;
//    JButton squareBtn;
//    JButton triangleBtn;
//
//    public ShapesMenus(){
//        createFrame();
//        addComponentToFrame();
//        addSizeAndLocation();
//        addActionListener();
//
//
//    }
//    private void createFrame(){
//        frame = new JFrame("-----Shapes Operations-----");
//        frame.setBounds(100, 10, 400, 300);
//        frame.getContentPane().setBackground(Color.GRAY);
//        frame.getContentPane().setLayout(null);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//
//    }
//    private void  addComponentToFrame(){
//        frame.add(circleBtn);
//        frame.add(rectangleBtn);
//        frame.add(squareBtn);
//        frame.add(triangleBtn);
//    }
//    private void addSizeAndLocation(){
//        int x=100,y=30,h=30,w=100;
//        circleBtn.setBounds(x,y,w,h);
//        y+=40;
//        rectangleBtn.setBounds(x,y,w,h);
//        y+=40;
//        squareBtn.setBounds(x,y,w,h);
//        y+=40;
//        triangleBtn.setBounds(x,y,w,h);
//
//    }
//    private void addActionListener(){
//        circleBtn.addActionListener(this);
//        rectangleBtn.addActionListener(this);
//        squareBtn.addActionListener(this);
//        triangleBtn.addActionListener(this);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//    }
//
//    public static void main(String[] args) {
//        new ShapesMenus();
//    }
//}

package com.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// === Main Menu Frame ===
public class ShapesMenus implements ActionListener {
    JFrame frame;
    JButton circleBtn = new JButton("Circle");
    JButton rectangleBtn = new JButton("Rectangle");
    JButton squareBtn = new JButton("Square");
    JButton triangleBtn = new JButton("Triangle");
    JButton exitBtn = new JButton("Exit");

    public ShapesMenus() {
        createWindow();
        setLocationAndSize();
        addComponentsToFrame();
        addActionEvent();
    }

    private void createWindow() {
        frame = new JFrame("=== 2D Shapes Calculator ===");
        frame.setBounds(100, 100, 350, 300);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.lightGray);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    private void setLocationAndSize() {
        circleBtn.setBounds(100, 30, 120, 30);
        rectangleBtn.setBounds(100, 70, 120, 30);
        squareBtn.setBounds(100, 110, 120, 30);
        triangleBtn.setBounds(100, 150, 120, 30);
        exitBtn.setBounds(100, 190, 120, 30);
    }

    private void addComponentsToFrame() {
        frame.add(circleBtn);
        frame.add(rectangleBtn);
        frame.add(squareBtn);
        frame.add(triangleBtn);
        frame.add(exitBtn);
    }

    private void addActionEvent() {
        circleBtn.addActionListener(this);
        rectangleBtn.addActionListener(this);
        squareBtn.addActionListener(this);
        triangleBtn.addActionListener(this);
        exitBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == circleBtn) {
            new CircleShape();
        } else if (e.getSource() == rectangleBtn) {
            new RectangleShape();
        } else if (e.getSource() == squareBtn) {
            new SquareShape();
        } else if (e.getSource() == triangleBtn) {
            new TriangleShape();
        } else if (e.getSource() == exitBtn) {
            JOptionPane.showMessageDialog(frame, "Exiting... Goodbye!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new ShapesMenus();
    }
}

// === Circle Class ===
class CircleShape {
    public CircleShape() {
        String radiusStr = JOptionPane.showInputDialog(null, "Enter Radius of Circle:");
        if (radiusStr != null) {
            double r = Double.parseDouble(radiusStr);
            double area = Math.PI * r * r;
            double perimeter = 2 * Math.PI * r;
            JOptionPane.showMessageDialog(null,
                    "Circle:\nArea = " + area + "\nPerimeter = " + perimeter);
        }
    }
}

// === Rectangle Class ===
class RectangleShape {
    public RectangleShape() {
        String lengthStr = JOptionPane.showInputDialog(null, "Enter Length:");
        String widthStr = JOptionPane.showInputDialog(null, "Enter Width:");
        if (lengthStr != null && widthStr != null) {
            double l = Double.parseDouble(lengthStr);
            double w = Double.parseDouble(widthStr);
            double area = l * w;
            double perimeter = 2 * (l + w);
            JOptionPane.showMessageDialog(null,
                    "Rectangle:\nArea = " + area + "\nPerimeter = " + perimeter);
        }
    }
}

// === Square Class ===
class SquareShape {
    public SquareShape() {
        String sideStr = JOptionPane.showInputDialog(null, "Enter Side of Square:");
        if (sideStr != null) {
            double s = Double.parseDouble(sideStr);
            double area = s * s;
            double perimeter = 4 * s;
            JOptionPane.showMessageDialog(null,
                    "Square:\nArea = " + area + "\nPerimeter = " + perimeter);
        }
    }
}

// === Triangle Class ===
class TriangleShape {
    public TriangleShape() {
        String aStr = JOptionPane.showInputDialog(null, "Enter Side A:");
        String bStr = JOptionPane.showInputDialog(null, "Enter Side B:");
        String cStr = JOptionPane.showInputDialog(null, "Enter Side C:");

        if (aStr != null && bStr != null && cStr != null) {
            double a = Double.parseDouble(aStr);
            double b = Double.parseDouble(bStr);
            double c = Double.parseDouble(cStr);

            double perimeter = a + b + c;
            double s = perimeter / 2;
            double area = Math.sqrt(s * (s - a) * (s - b) * (s - c)); // Heron's formula

            JOptionPane.showMessageDialog(null,
                    "Triangle:\nArea = " + area + "\nPerimeter = " + perimeter);
        }
    }
}

