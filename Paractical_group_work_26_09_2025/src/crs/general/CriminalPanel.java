package crs.general;

import crs.util.DB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class CriminalPanel extends JPanel implements ActionListener {
    JTextField nameTxt = new JTextField(),
            ageTxt = new JTextField(),
            addressTxt = new JTextField(),
            phoneTxt = new JTextField(),
            idTxt = new JTextField();

    JComboBox<String> genderCombo = new JComboBox<>(new String[]{"male", "female", "other"});
    JTextArea crimeHistoryTxt = new JTextArea();

    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    JTable table;
    DefaultTableModel model;

    public CriminalPanel() {
        setLayout(null);
        String[] cols = {"ID", "Name", "Age", "Gender", "Address", "Phone", "Crime History"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 300, 800, 230);

        int y = 20;
        addField("Name", nameTxt, y); y += 30;
        addField("Age", ageTxt, y); y += 30;
        addField("Gender", genderCombo, y); y += 30;
        addField("Address", addressTxt, y); y += 30;
        addField("Phone", phoneTxt, y); y += 30;
        addField("Criminal ID", idTxt, y);
        idTxt.setEditable(false);

        // Crime History TextArea
        JLabel crimeLbl = new JLabel("Crime History");
        crimeLbl.setBounds(300, 200, 100, 25);
        crimeHistoryTxt.setBounds(400, 200, 300, 80);
        add(crimeLbl);
        add(crimeHistoryTxt);

        addButtons();
        add(sp);

        loadCriminals();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idTxt.setText(model.getValueAt(row, 0).toString());
                    nameTxt.setText(model.getValueAt(row, 1).toString());
                    ageTxt.setText(model.getValueAt(row, 2).toString());
                    genderCombo.setSelectedItem(model.getValueAt(row, 3).toString());
                    addressTxt.setText(model.getValueAt(row, 4).toString());
                    phoneTxt.setText(model.getValueAt(row, 5).toString());
                    crimeHistoryTxt.setText(model.getValueAt(row, 6).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent txt, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 100, 25);
        txt.setBounds(130, y, 150, 25);
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
                if (nameTxt.getText().trim().isEmpty() || phoneTxt.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and Phone are required!");
                    return;
                }

                if (isDuplicateCriminal(nameTxt.getText(), phoneTxt.getText(), null)) {
                    JOptionPane.showMessageDialog(this, "Criminal with this Name and Phone already exists!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO criminal(name, age, gender, address, phone, crime_history) VALUES(?,?,?,?,?,?)"
                );
                ps.setString(1, nameTxt.getText().trim());
                ps.setInt(2, ageTxt.getText().isEmpty() ? 0 : Integer.parseInt(ageTxt.getText().trim()));
                ps.setString(3, genderCombo.getSelectedItem().toString());
                ps.setString(4, addressTxt.getText().trim());
                ps.setString(5, phoneTxt.getText().trim());
                ps.setString(6, crimeHistoryTxt.getText().trim());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Criminal Added Successfully!");
                clear();
                loadCriminals();

            } else if (e.getSource() == updateBtn) {
                if (idTxt.getText().isEmpty()) return;

                int criminalId = Integer.parseInt(idTxt.getText());
                if (isDuplicateCriminal(nameTxt.getText(), phoneTxt.getText(), criminalId)) {
                    JOptionPane.showMessageDialog(this, "Another criminal with this Name and Phone exists!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE criminal SET name=?, age=?, gender=?, address=?, phone=?, crime_history=? WHERE criminal_id=?"
                );
                ps.setString(1, nameTxt.getText().trim());
                ps.setInt(2, ageTxt.getText().isEmpty() ? 0 : Integer.parseInt(ageTxt.getText().trim()));
                ps.setString(3, genderCombo.getSelectedItem().toString());
                ps.setString(4, addressTxt.getText().trim());
                ps.setString(5, phoneTxt.getText().trim());
                ps.setString(6, crimeHistoryTxt.getText().trim());
                ps.setInt(7, criminalId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Criminal Updated Successfully!");
                clear();
                loadCriminals();

            } else if (e.getSource() == deleteBtn) {
                if (idTxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM criminal WHERE criminal_id=?");
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Criminal Deleted!");
                clear();
                loadCriminals();

            } else { // loadBtn
                clear();
                loadCriminals();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCriminals() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM criminal");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("criminal_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("crime_history")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clear() {
        nameTxt.setText(""); ageTxt.setText(""); addressTxt.setText("");
        phoneTxt.setText(""); idTxt.setText(""); crimeHistoryTxt.setText("");
        genderCombo.setSelectedIndex(0);
    }

    private boolean isDuplicateCriminal(String name, String phone, Integer excludeId) {
        try (Connection con = DB.getConnection()) {
            String sql = "SELECT * FROM criminal WHERE name=? AND phone=?";
            if (excludeId != null) sql += " AND criminal_id<>?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, phone.trim());
            if (excludeId != null) ps.setInt(3, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
