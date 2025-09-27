package question2;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RegisterForm {
    JFrame frame;
    JLabel nameLabel = new JLabel("Names");
    JLabel userLabel = new JLabel("UserName");
    JLabel passLabel = new JLabel("Password");

    JTextField nameTF = new JTextField();
    JTextField userTF = new JTextField();
    JPasswordField passTF = new JPasswordField();

    JButton registerBtn = new JButton("Register");
    JButton resetBtn = new JButton("Reset");

    public RegisterForm() {
        createWindow();
        setLocationAndSize();
        addComponentsToFrame();
    }

    private void createWindow() {
        frame = new JFrame("---- User Registration ----");
        frame.setBounds(100, 50, 400, 300);
        frame.getContentPane().setBackground(new Color(70, 140, 200));
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void setLocationAndSize() {
        Border blackline = BorderFactory.createLineBorder(Color.BLACK);

        nameLabel.setBounds(30, 30, 120, 30);
        nameLabel.setBorder(blackline);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameTF.setBounds(170, 30, 180, 30);

        userLabel.setBounds(30, 80, 120, 30);
        userLabel.setBorder(blackline);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userTF.setBounds(170, 80, 180, 30);

        passLabel.setBounds(30, 130, 120, 30);
        passLabel.setBorder(blackline);
        passLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passTF.setBounds(170, 130, 180, 30);

        registerBtn.setBounds(60, 190, 120, 40);
        resetBtn.setBounds(200, 190, 120, 40);
    }

    private void addComponentsToFrame() {
        frame.add(nameLabel);
        frame.add(nameTF);
        frame.add(userLabel);
        frame.add(userTF);
        frame.add(passLabel);
        frame.add(passTF);
        frame.add(registerBtn);
        frame.add(resetBtn);
    }

    public static void main(String[] args) {
        new RegisterForm();
    }
}
