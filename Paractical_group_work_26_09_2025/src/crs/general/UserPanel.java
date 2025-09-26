package crs.general;

import crs.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class UserPanel extends JPanel implements ActionListener {
    JTextField
            usernameTxt = new JTextField(),
            idTxt = new JTextField();
    JPasswordField passTxt = new JPasswordField();
    JComboBox<String> roleBox = new JComboBox<>(new String[]{"admin", "officer"});

    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    JTable table;
    DefaultTableModel model;

    public UserPanel() {
        setLayout(null);
        String[] labels = {"ID", "Username", "Role"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 200, 800, 300);

        int y = 20;
        addField("Username", usernameTxt, y); y += 30;
        addField("Password", passTxt, y); y += 30;

        JLabel roleLbl = new JLabel("Role");
        roleLbl.setBounds(20, y, 80, 25);
        roleBox.setBounds(100, y, 150, 25);
        add(roleLbl); add(roleBox);
        y += 30;

        addField("User ID", idTxt, y);
        idTxt.setEditable(false);

        addButtons();
        add(sp);

        // Load users automatically
        loadUsers();

        // Fill form on row click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idTxt.setText(model.getValueAt(row, 0).toString());
                    usernameTxt.setText(model.getValueAt(row, 1).toString());
                    roleBox.setSelectedItem(model.getValueAt(row, 2).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent txt, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 80, 25);
        txt.setBounds(100, y, 150, 25);
        add(l); add(txt);
    }

    private void addButtons() {
        addBtn.setBounds(300, 20, 100, 30);
        updateBtn.setBounds(300, 60, 100, 30);
        deleteBtn.setBounds(300, 100, 100, 30);
        loadBtn.setBounds(300, 140, 100, 30);
        add(addBtn); add(updateBtn); add(deleteBtn); add(loadBtn);

        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                if (!validateFields()) return;

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users(username, password, role) VALUES(?,?,?)"
                );
                ps.setString(1, usernameTxt.getText());
                ps.setString(2, new String(passTxt.getPassword())); // plain for now
                ps.setString(3, roleBox.getSelectedItem().toString());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User Added!");
                clearFields();
                loadUsers();
            }
            else if (e.getSource() == updateBtn) {
                if (idTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select a user to update!");
                    return;
                }
                if (!validateFields()) return;

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE users SET username=?, password=?, role=? WHERE user_id=?"
                );
                ps.setString(1, usernameTxt.getText());
                ps.setString(2, new String(passTxt.getPassword()));
                ps.setString(3, roleBox.getSelectedItem().toString());
                ps.setInt(4, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User Updated!");
                clearFields();
                loadUsers();
            }
            else if (e.getSource() == deleteBtn) {
                if (idTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select a user to delete!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE user_id=?");
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User Deleted!");
                clearFields();
                loadUsers();
            }
            else if (e.getSource() == loadBtn) {
                clearFields();
                loadUsers();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Load table data
    private void loadUsers() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            PreparedStatement ps = con.prepareStatement("SELECT user_id, username, role FROM users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Validation
    private boolean validateFields() {
        if (usernameTxt.getText().trim().isEmpty() || passTxt.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Username and Password are required!");
            return false;
        }
        return true;
    }

    private void clearFields() {
        usernameTxt.setText("");
        passTxt.setText("");
        idTxt.setText("");
        roleBox.setSelectedIndex(0);
    }
}
