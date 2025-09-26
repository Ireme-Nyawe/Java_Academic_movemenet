package crs.general;

import crs.util.DB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CasePanel extends JPanel implements ActionListener {
    JComboBox<String> crimeCombo = new JComboBox<>();
    JComboBox<String> criminalCombo = new JComboBox<>();
    JComboBox<String> officerCombo = new JComboBox<>();
    JComboBox<String> statusCombo = new JComboBox<>(new String[]{"active","resolved","pending"});
    JTextArea remarksTxt = new JTextArea();
    JTextField idTxt = new JTextField();

    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    JTable table;
    DefaultTableModel model;

    // Maps to hold ID -> Name mapping
    Map<String, Integer> crimeMap = new HashMap<>();
    Map<String, Integer> criminalMap = new HashMap<>();
    Map<String, Integer> officerMap = new HashMap<>();

    public CasePanel() {
        setLayout(null);
        String[] cols = {"Case ID", "Crime", "Criminal", "Officer", "Status", "Remarks"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 300, 800, 230);

        int y = 20;
        addField("Crime", crimeCombo, y); y += 30;
        addField("Criminal", criminalCombo, y); y += 30;
        addField("Officer", officerCombo, y); y += 30;
        addField("Case ID", idTxt, y);
        idTxt.setEditable(false);

        JLabel statusLbl = new JLabel("Status");
        statusLbl.setBounds(20, 150, 100, 25);
        statusCombo.setBounds(115, 150, 150, 25);
        add(statusLbl);
        add(statusCombo);

        JLabel remarksLbl = new JLabel("Remarks");
        remarksLbl.setBounds(20, 200, 100, 25);
        remarksTxt.setBounds(115, 200, 300, 80);
        add(remarksLbl);
        add(remarksTxt);

        addButtons();
        add(sp);

        loadCombos();
        loadCases();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idTxt.setText(model.getValueAt(row,0).toString());
                    crimeCombo.setSelectedItem(model.getValueAt(row,1).toString());
                    criminalCombo.setSelectedItem(model.getValueAt(row,2).toString());
                    officerCombo.setSelectedItem(model.getValueAt(row,3).toString());
                    statusCombo.setSelectedItem(model.getValueAt(row,4).toString());
                    remarksTxt.setText(model.getValueAt(row,5).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent comp, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 100, 25);
        comp.setBounds(130, y, 150, 25);
        add(l); add(comp);
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

    private void loadCombos() {
        try (Connection con = DB.getConnection()) {
            // Load Crimes
            crimeCombo.removeAllItems();
            crimeMap.clear();
            ResultSet rs = con.prepareStatement("SELECT crime_id, type FROM crime").executeQuery();
            while (rs.next()) {
                String name = rs.getString("type");
                crimeCombo.addItem(name);
                crimeMap.put(name, rs.getInt("crime_id"));
            }

            // Load Criminals
            criminalCombo.removeAllItems();
            criminalMap.clear();
            rs = con.prepareStatement("SELECT criminal_id, name FROM criminal").executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                criminalCombo.addItem(name);
                criminalMap.put(name, rs.getInt("criminal_id"));
            }

            // Load Officers
            officerCombo.removeAllItems();
            officerMap.clear();
            rs = con.prepareStatement("SELECT officer_id, name FROM police_officer").executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                officerCombo.addItem(name);
                officerMap.put(name, rs.getInt("officer_id"));
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                if (crimeCombo.getSelectedItem()==null || criminalCombo.getSelectedItem()==null || officerCombo.getSelectedItem()==null) {
                    JOptionPane.showMessageDialog(this,"Select Crime, Criminal, and Officer!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO case_record(crime_id, criminal_id, officer_id, case_status, remarks) VALUES(?,?,?,?,?)"
                );
                ps.setInt(1, crimeMap.get(crimeCombo.getSelectedItem()));
                ps.setInt(2, criminalMap.get(criminalCombo.getSelectedItem()));
                ps.setInt(3, officerMap.get(officerCombo.getSelectedItem()));
                ps.setString(4, statusCombo.getSelectedItem().toString());
                ps.setString(5, remarksTxt.getText().trim());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Case Added!");
                clear();
                loadCases();

            } else if (e.getSource() == updateBtn) {
                if (idTxt.getText().isEmpty()) return;
                int caseId = Integer.parseInt(idTxt.getText());
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE case_record SET crime_id=?, criminal_id=?, officer_id=?, case_status=?, remarks=? WHERE case_id=?"
                );
                ps.setInt(1, crimeMap.get(crimeCombo.getSelectedItem()));
                ps.setInt(2, criminalMap.get(criminalCombo.getSelectedItem()));
                ps.setInt(3, officerMap.get(officerCombo.getSelectedItem()));
                ps.setString(4, statusCombo.getSelectedItem().toString());
                ps.setString(5, remarksTxt.getText().trim());
                ps.setInt(6, caseId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Case Updated!");
                clear();
                loadCases();

            } else if (e.getSource() == deleteBtn) {
                if (idTxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM case_record WHERE case_id=?");
                ps.setInt(1,Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Case Deleted!");
                clear();
                loadCases();

            } else { // loadBtn
                clear();
                loadCases();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCases() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            String sql = "SELECT c.case_id, cr.type, crl.name AS criminal_name, po.name AS officer_name, c.case_status, c.remarks " +
                    "FROM case_record c " +
                    "LEFT JOIN crime cr ON c.crime_id = cr.crime_id " +
                    "LEFT JOIN criminal crl ON c.criminal_id = crl.criminal_id " +
                    "LEFT JOIN police_officer po ON c.officer_id = po.officer_id";
            ResultSet rs = con.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("case_id"),
                        rs.getString("type"),
                        rs.getString("criminal_name"),
                        rs.getString("officer_name"),
                        rs.getString("case_status"),
                        rs.getString("remarks")
                });
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clear() {
        idTxt.setText("");
        remarksTxt.setText("");
        statusCombo.setSelectedIndex(0);
        loadCombos();
    }
}
