package crs.general;

import crs.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class OfficerPanel extends JPanel implements ActionListener {
    JTextField nameTxt = new JTextField(),
            rankTxt = new JTextField(),
            stationTxt = new JTextField(),
            phoneTxt = new JTextField(),
            idTxt = new JTextField();

    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    JTable table;
    DefaultTableModel model;

    public OfficerPanel() {
        setLayout(null);
        String[] cols = {"ID", "Name", "Rank", "Station", "Phone"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 230, 800, 300);

        int y = 20;
        addField("Name", nameTxt, y);
        y += 30;
        addField("Rank", rankTxt, y);
        y += 30;
        addField("Station", stationTxt, y);
        y += 30;
        addField("Phone", phoneTxt, y);
        y += 30;
        addField("Officer ID", idTxt, y);
        idTxt.setEditable(false);

        addButtons();
        add(sp);

        loadOfficers();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idTxt.setText(model.getValueAt(row, 0).toString());
                    nameTxt.setText(model.getValueAt(row, 1).toString());
                    rankTxt.setText(model.getValueAt(row, 2).toString());
                    stationTxt.setText(model.getValueAt(row, 3).toString());
                    phoneTxt.setText(model.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent txt, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 80, 25);
        txt.setBounds(100, y, 150, 25);
        add(l);
        add(txt);
    }

    private void addButtons() {
        addBtn.setBounds(300, 20, 100, 30);
        updateBtn.setBounds(300, 60, 100, 30);
        deleteBtn.setBounds(300, 100, 100, 30);
        loadBtn.setBounds(300, 140, 100, 30);
        add(addBtn);
        add(updateBtn);
        add(deleteBtn);
        add(loadBtn);
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

                if (isDuplicateOfficer(nameTxt.getText(), phoneTxt.getText(), null)) {
                    JOptionPane.showMessageDialog(this, "Officer with this Name and Phone already exists!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO police_officer(name, officer_rank, station, phone) VALUES(?,?,?,?)"
                );
                ps.setString(1, nameTxt.getText().trim());
                ps.setString(2, rankTxt.getText().trim());
                ps.setString(3, stationTxt.getText().trim());
                ps.setString(4, phoneTxt.getText().trim());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Officer Added Successfully!");
                clear();
                loadOfficers();
            } else if (e.getSource() == addBtn) {
                if (nameTxt.getText().trim().isEmpty() || phoneTxt.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and Phone are required!");
                    return;
                }

                if (isDuplicateOfficer(nameTxt.getText(), phoneTxt.getText(), null)) {
                    JOptionPane.showMessageDialog(this, "Officer with this Name and Phone already exists!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO police_officer(name, officer_rank, station, phone) VALUES(?,?,?,?)"
                );
                ps.setString(1, nameTxt.getText().trim());
                ps.setString(2, rankTxt.getText().trim());
                ps.setString(3, stationTxt.getText().trim());
                ps.setString(4, phoneTxt.getText().trim());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Officer Added Successfully!");
                clear();
                loadOfficers();
            } else if (e.getSource() == deleteBtn) {
                if (idTxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM police_officer WHERE officer_id=?");
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Officer Deleted!");
                clear();
                loadOfficers();
            } else {
                clear();
                loadOfficers();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadOfficers() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM police_officer");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("officer_id"),
                        rs.getString("name"),
                        rs.getString("officer_rank"),
                        rs.getString("station"),
                        rs.getString("phone")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clear() {
        nameTxt.setText("");
        rankTxt.setText("");
        stationTxt.setText("");
        phoneTxt.setText("");
        idTxt.setText("");
    }

    private boolean isDuplicateOfficer(String name, String phone, Integer excludeId) {
        try (Connection con = DB.getConnection()) {
            String sql = "SELECT * FROM police_officer WHERE name=? AND phone=?";
            if (excludeId != null) {
                sql += " AND officer_id<>?";
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, phone.trim());
            if (excludeId != null) {
                ps.setInt(3, excludeId);
            }
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a row exists
        } catch (Exception ex) {
            ex.printStackTrace();
            return true; // safest to assume duplicate on error
        }
    }
}
