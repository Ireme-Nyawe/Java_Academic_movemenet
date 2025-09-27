package question1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanReturnCalculator implements ActionListener {
    JFrame card;
    JLabel amountLabel = new JLabel("Amount Requested");
    JLabel durationLabel = new JLabel("Duration (Year)");
    JLabel returnLabel = new JLabel("Total to Return");

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
        card = new JFrame();
        card.setTitle("----Total Loan Return Calculator----");
        card.setBounds(100, 10, 400, 300);
        card.getContentPane().setBackground(new Color(70, 140, 200));
        card.getContentPane().setLayout(null);
        card.setVisible(true);
        card.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        card.setResizable(false);
    }

    public void setLocationAndSize() {
        amountLabel.setBounds(20, 20, 150, 30);
        amountTF.setBounds(180, 20, 150, 30);

        durationLabel.setBounds(20, 70, 150, 30);
        durationTF.setBounds(180, 70, 150, 30);

        returnLabel.setBounds(20, 120, 150, 30);
        returnTF.setBounds(180, 120, 150, 30);
        returnLabel.setHorizontalAlignment(1);
        returnTF.setEditable(false);

        calculateBtn.setBounds(120, 180, 120, 40);
    }

    public void addComponentsToFrame() {
        card.add(amountLabel);
        card.add(amountTF);
        card.add(durationLabel);
        card.add(durationTF);
        card.add(returnLabel);
        card.add(returnTF);
        card.add(calculateBtn);
    }

    public void addActionEvent() {
        calculateBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double amount = Double.parseDouble(amountTF.getText());
            int years = Integer.parseInt(durationTF.getText());

            double interestRate = 0.006;
            double totalReturn = amount + (amount * interestRate * years);

            returnTF.setText(String.format("%.0f", totalReturn));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(card, "Please enter valid numbers!");
        }
    }

}
