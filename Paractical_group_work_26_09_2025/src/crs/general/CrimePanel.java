package crs.general;

import crs.util.DB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class CrimePanel extends JPanel implements ActionListener {
    JTextField typeTxt = new JTextField(),
            locationTxt = new JTextField(),
            idTxt = new JTextField();

    JTextArea descriptionTxt = new JTextArea();
    JComboBox<String> statusCombo = new JComboBox<>(new String[]{"open", "investigating", "closed"});
    JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));

    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    JTable table;
    DefaultTableModel model;

    public CrimePanel() {
        setLayout(null);
        String[] cols = {"ID", "Type", "Date", "Location", "Description", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 300, 800, 230);

        int y = 20;
        addField("Type", typeTxt, y); y += 30;
        addField("Date (YYYY-MM-DD)", dateField, y); y += 30;
        addField("Location", locationTxt, y); y += 30;
        addField("Crime ID", idTxt, y);
        idTxt.setEditable(false);

        // Description TextArea
        JLabel descLbl = new JLabel("Description");
        descLbl.setBounds(20, 150, 100, 25);
        descriptionTxt.setBounds(115, 150, 300, 80);
        add(descLbl);
        add(descriptionTxt);

        // Status Combo
        JLabel statusLbl = new JLabel("Status");
        statusLbl.setBounds(20, 250, 100, 25);
        statusCombo.setBounds(115, 250, 150, 25);
        add(statusLbl);
        add(statusCombo);

        addButtons();
        add(sp);

        loadCrimes();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idTxt.setText(model.getValueAt(row, 0).toString());
                    typeTxt.setText(model.getValueAt(row, 1).toString());
                    dateField.setText(model.getValueAt(row, 2).toString());
                    locationTxt.setText(model.getValueAt(row, 3).toString());
                    descriptionTxt.setText(model.getValueAt(row, 4).toString());
                    statusCombo.setSelectedItem(model.getValueAt(row, 5).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent txt, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 150, 25);
        txt.setBounds(180, y, 150, 25);
        add(l); add(txt);
    }

    private void addButtons() {
        addBtn.setBounds(500, 20, 100, 30);
        updateBtn.setBounds(500, 60, 100, 30);
        deleteBtn.setBounds(500, 100, 100, 30);
        loadBtn.setBounds(500, 140, 100, 30);
        add(addBtn); add(updateBtn); add(deleteBtn); add(loadBtn);
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                if (typeTxt.getText().trim().isEmpty() || dateField.getText().trim().isEmpty() || locationTxt.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Type, Date, and Location are required!");
                    return;
                }

                if (isDuplicateCrime(typeTxt.getText(), dateField.getText(), locationTxt.getText(), null)) {
                    JOptionPane.showMessageDialog(this, "Crime of same Type, Date, and Location already exists!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO crime(type, date, location, description, status) VALUES(?,?,?,?,?)"
                );
                ps.setString(1, typeTxt.getText().trim());
                ps.setDate(2, java.sql.Date.valueOf(dateField.getText().trim()));
                ps.setString(3, locationTxt.getText().trim());
                ps.setString(4, descriptionTxt.getText().trim());
                ps.setString(5, statusCombo.getSelectedItem().toString());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Crime Added Successfully!");
                clear();
                loadCrimes();

            } else if (e.getSource() == updateBtn) {
                if (idTxt.getText().isEmpty()) return;
                int crimeId = Integer.parseInt(idTxt.getText());
                if (isDuplicateCrime(typeTxt.getText(), dateField.getText(), locationTxt.getText(), crimeId)) {
                    JOptionPane.showMessageDialog(this, "Another crime with same Type, Date, Location exists!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE crime SET type=?, date=?, location=?, description=?, status=? WHERE crime_id=?"
                );
                ps.setString(1, typeTxt.getText().trim());
                ps.setDate(2, java.sql.Date.valueOf(dateField.getText().trim()));
                ps.setString(3, locationTxt.getText().trim());
                ps.setString(4, descriptionTxt.getText().trim());
                ps.setString(5, statusCombo.getSelectedItem().toString());
                ps.setInt(6, crimeId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Crime Updated Successfully!");
                clear();
                loadCrimes();

            } else if (e.getSource() == deleteBtn) {
                if (idTxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM crime WHERE crime_id=?");
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Crime Deleted!");
                clear();
                loadCrimes();

            } else { // loadBtn
                clear();
                loadCrimes();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCrimes() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM crime");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("crime_id"),
                        rs.getString("type"),
                        rs.getDate("date"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getString("status")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clear() {
        typeTxt.setText(""); locationTxt.setText(""); idTxt.setText("");
        descriptionTxt.setText(""); dateField.setText("");
        statusCombo.setSelectedIndex(0);
    }

    private boolean isDuplicateCrime(String type, String date, String location, Integer excludeId) {
        try (Connection con = DB.getConnection()) {
            String sql = "SELECT * FROM crime WHERE type=? AND date=? AND location=?";
            if (excludeId != null) sql += " AND crime_id<>?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, type.trim());
            ps.setDate(2, java.sql.Date.valueOf(date.trim()));
            ps.setString(3, location.trim());
            if (excludeId != null) ps.setInt(4, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
