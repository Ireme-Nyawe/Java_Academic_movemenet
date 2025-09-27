package question1;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanReturnCalculator implements ActionListener {
    JFrame frame;
    JLabel amountLabel = new JLabel("Amount requested");
    JLabel durationLabel = new JLabel("Duration(year)");
    JLabel returnLabel = new JLabel("Total to return");

    JTextField amountTF = new JTextField();
    JTextField durationTF = new JTextField();
    JTextField returnTF = new JTextField();

    JButton calculateBtn = new JButton("Calculate");

    public LoanReturnCalculator() {
        createWindow();
        setLocationAndSize();
        addComponentsToFrame();
        addActionEvent();
    }

    private void createWindow() {
        frame = new JFrame("---- Total Loan Return Calculator ----");
        frame.setBounds(100, 50, 400, 300);
        frame.getContentPane().setBackground(new Color(70, 140, 200));
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void setLocationAndSize() {
        Border blackline = BorderFactory.createLineBorder(Color.BLACK);

        amountLabel.setBounds(30, 30, 150, 30);
        amountLabel.setBorder(blackline);
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        amountTF.setBounds(200, 30, 150, 30);

        durationLabel.setBounds(30, 80, 150, 30);
        durationLabel.setBorder(blackline);
        durationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        durationTF.setBounds(200, 80, 150, 30);

        returnLabel.setBounds(30, 130, 150, 30);
        returnLabel.setBorder(blackline);
        returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        returnTF.setBounds(200, 130, 150, 30);
        returnTF.setEditable(false);

        calculateBtn.setBounds(120, 190, 150, 40);
    }

    private void addComponentsToFrame() {
        frame.add(amountLabel);
        frame.add(amountTF);
        frame.add(durationLabel);
        frame.add(durationTF);
        frame.add(returnLabel);
        frame.add(returnTF);
        frame.add(calculateBtn);
    }

    private void addActionEvent() {
        calculateBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double amount = Double.parseDouble(amountTF.getText());
            int years = Integer.parseInt(durationTF.getText());

            double interestRate = 0.006; // 0.6% yearly
            double totalReturn = amount + (amount * interestRate * years);

            returnTF.setText(String.format("%,.0f", totalReturn));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers!");
        }
    }

    public static void main(String[] args) {
        new LoanReturnCalculator();
    }
}
