package com.entities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Operations implements ActionListener {
    JFrame frame;
    JLabel firstNumber = new JLabel("First number");
    JLabel secondNumber = new JLabel("Second  number");
    JLabel result = new JLabel("Result");

    JTextField fnTextField = new JTextField();
    JTextField snTextField = new JTextField();
    JTextField resTextField = new JTextField();

    JButton add = new JButton("+");
    JButton sub = new JButton("-");
    JButton mul = new JButton("x");
    JButton div = new JButton("/");
    JButton mod = new JButton("%");

    public Operations() {
        createWindow();
        setLocationAndSize();
        addComponentsToFrame();
        addActionEvent();
    }

    private void createWindow() {
        frame = new JFrame();
        frame.setTitle("----Arthemetic-Operations----");
        frame.setBounds(100, 10, 400, 300);
        frame.getContentPane().setBackground(Color.pink);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    public void addActionEvent() {
        add.addActionListener(this);
        sub.addActionListener(this);
        mul.addActionListener(this);
        div.addActionListener(this);
        mod.addActionListener(this);
    }

    public void setLocationAndSize() {
        firstNumber.setBounds(10, 10, 100, 30);
        fnTextField.setBounds(120, 10, 100, 30);

        secondNumber.setBounds(10, 50, 100, 30);
        snTextField.setBounds(120, 50, 100, 30);

        result.setBounds(10, 90, 100, 30);
        resTextField.setBounds(120, 90, 100, 30);

        int x = 10, y = 130, w = 50, h = 30;
        add.setBounds(x, y, w, h);
        x += 60;
        sub.setBounds(x, y, w, h);
        x += 60;
        mul.setBounds(x, y, w, h);
        x += 60;
        div.setBounds(x, y, w, h);
        x += 60;
        mod.setBounds(x, y, w, h);
    }

    public void addComponentsToFrame() {
        frame.add(firstNumber);
        frame.add(secondNumber);
        frame.add(fnTextField);
        frame.add(snTextField);
        frame.add(resTextField);
        frame.add(result);
        frame.add(add);
        frame.add(mul);
        frame.add(sub);
        frame.add(div);
        frame.add(mod);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int num1 = Integer.parseInt(fnTextField.getText());
        int num2 = Integer.parseInt(snTextField.getText());
        if (e.getSource() == add) {
            int res = num1 + num2;
            resTextField.setText(String.valueOf(res));
        } else if ((e.getSource() == sub)) {
            int res = num1 - num2;
            resTextField.setText(String.valueOf(res));
        } else if ((e.getSource() == mul)) {
            int res = num1 * num2;
            resTextField.setText(String.valueOf(res));
        } else if ((e.getSource() == div)) {
            int res = num1 / num2;
            resTextField.setText(String.valueOf(res));
        } else if ((e.getSource() == mod)) {
            int res = num1 % num2;
            resTextField.setText(String.valueOf(res));
        } else {
            System.out.println("invalid");
        }

    }
}
