package com.form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperationWithTable implements ActionListener{
    JFrame frame;
    JLabel firstNumber = new JLabel("First number");
    JLabel secondNumber = new JLabel("Second  number");
    JLabel result = new JLabel("Result");

    JTextField fnTF = new JTextField();
    JTextField snTF = new JTextField();
    JTextField resTF= new JTextField();

    JButton add = new JButton("+");
    JButton sub = new JButton("-");
    JButton mul = new JButton("x");
    JButton div = new JButton("/");
    JButton mod = new JButton("%");

    JTable table;
    DefaultTableModel tableModel;
    JScrollPane scrollPane;

    public OperationWithTable(){
        createFrame();
        addComponentToFrame();
        addLocationAndSize();
        addEventAction();
    }
    public void createFrame() {
        frame = new JFrame("\t Operations");
        frame.setBounds(10,10,300,500);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        String[] colums = {"First Number","Operator","Second Number","Result"};
        tableModel = new DefaultTableModel(colums,0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

    }
    public void addLocationAndSize() {
        firstNumber.setBounds(10, 10, 100, 30);
        fnTF.setBounds(120, 10, 100, 30);

        secondNumber.setBounds(10, 50, 100, 30);
        snTF.setBounds(120, 50, 100, 30);

        result.setBounds(10, 90, 100, 30);
        resTF.setBounds(120, 90, 100, 30);

        int x = 10, y = 130, w = 50, h = 30;
        add.setBounds(x, y, w, h);
        x += 50;
        sub.setBounds(x, y, w, h);
        x += 50;
        mul.setBounds(x, y, w, h);
        x += 50;
        div.setBounds(x, y, w, h);
        x += 50;
        mod.setBounds(x, y, w, h);

        scrollPane.setBounds(10,170,250,250);
    }
    public void addComponentToFrame(){
        frame.add(firstNumber);
        frame.add(secondNumber);
        frame.add(result);
        frame.add(fnTF);
        frame.add(snTF);
        frame.add(resTF);
        frame.add(add);
        frame.add(sub);
        frame.add(mul);
        frame.add(div);
        frame.add(mod);
        frame.add(scrollPane);
    }

    public void addEventAction(){
        add.addActionListener(this);
        sub.addActionListener(this);
        mul.addActionListener(this);
        div.addActionListener(this);
        mod.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int f_n = Integer.parseInt(fnTF.getText());
            int s_n = Integer.parseInt(snTF.getText());
            String operator ="";
            int result =0;

            if (e.getSource()==add){
                result = f_n+s_n;
                operator ="+";

            }
            else if (e.getSource()==sub){
                result = f_n-s_n;
                operator ="-";

            }
            else if (e.getSource()==mul){
                result = f_n*s_n;
                operator ="*";

            }
            else if (e.getSource()==div){
                result = f_n/s_n;
                operator ="/";

            }
            else if (e.getSource()==mod){
                result = f_n%s_n;
                operator ="%";

            }
            else {
                System.out.println("Invalid Application!");
            }

            resTF.setText(String.valueOf(result));
            tableModel.addRow(new Object[] {f_n,operator,s_n,result});

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,"Please enter valid number !");
        }
    }

    public static void main(String[] args) {
       new OperationWithTable();
    }
}
