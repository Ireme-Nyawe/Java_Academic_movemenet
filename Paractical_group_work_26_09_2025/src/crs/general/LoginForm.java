package crs.general;
import crs.util.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm extends JFrame implements ActionListener {
    // Define components
    JLabel userLb = new JLabel("User username");
    JLabel passLb = new JLabel("Password");

    JTextField usertxt = new JTextField();
    JPasswordField passtxt = new JPasswordField();

    JButton loginbtn = new JButton("Login");
    JButton cancelbtn = new JButton("Cancel");

    public LoginForm() {
        setTitle("Login - Fleet MS");
        setBounds(100, 100, 300, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        userLb.setBounds(30, 30, 100, 25);
        usertxt.setBounds(140, 30, 140, 25);

        passLb.setBounds(30, 70, 100, 25);
        passtxt.setBounds(140, 70, 140, 25);

        loginbtn.setBounds(40, 120, 90, 30);
        cancelbtn.setBounds(150, 120, 90, 30);

        add(userLb);
        add(passLb);
        add(usertxt);
        add(passtxt);
        add(loginbtn);
        add(cancelbtn);

        loginbtn.addActionListener(this);
        cancelbtn.addActionListener(this);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginbtn) {
            String user = usertxt.getText();
            String password = new String(passtxt.getPassword());

            if(user.equals("")||password.equals("")){
                JOptionPane.showMessageDialog(this,"User username and Password are required to login!");
            }
            else {
                try (Connection con = DB.getConnection()) {
                    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, user);
                    pst.setString(2, password);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        String role = rs.getNString("role");
                        int userId = rs.getInt("user_id");
                        new CRSPanelOptions(role,userId);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (e.getSource() == cancelbtn) {
            int confirm = JOptionPane.showConfirmDialog(this, "Exit Application?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
