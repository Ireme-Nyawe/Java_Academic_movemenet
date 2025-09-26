package crs.general;

import crs.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportPanel extends JPanel implements ActionListener {
    JComboBox<String> officerCombo = new JComboBox<>();
    JComboBox<String> crimeCombo = new JComboBox<>();
    JComboBox<String> statusCombo = new JComboBox<>(new String[]{"All","active","resolved","pending"});
    JButton filterBtn = new JButton("Filter"), loadBtn = new JButton("Load All");

    JTable table;
    DefaultTableModel model;

    Map<String, Integer> officerMap = new HashMap<>();
    Map<String, Integer> crimeMap = new HashMap<>();

    public ReportPanel() {
        setLayout(null);
        String[] cols = {"Case ID", "Crime", "Criminal", "Officer", "Status", "Remarks"};
        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20,150,800,300);
        add(sp);

        int y = 20;
        addField("Officer", officerCombo, y); y+=30;
        addField("Crime", crimeCombo, y); y+=30;
        addField("Status", statusCombo, y); y+=30;

        filterBtn.setBounds(400, 20, 100, 30);
        loadBtn.setBounds(400, 60, 100, 30);
        add(filterBtn); add(loadBtn);

        filterBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        loadCombos();
        loadCases();
    }

    private void addField(String lbl, JComponent comp, int y){
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 100, 25);
        comp.setBounds(130, y, 150, 25);
        add(l); add(comp);
    }

    private void loadCombos() {
        try(Connection con = DB.getConnection()){
            // Officers
            officerCombo.removeAllItems();
            officerMap.clear();
            officerCombo.addItem("All");
            ResultSet rs = con.prepareStatement("SELECT officer_id, name FROM police_officer").executeQuery();
            while(rs.next()){
                String name = rs.getString("name");
                officerCombo.addItem(name);
                officerMap.put(name, rs.getInt("officer_id"));
            }

            // Crimes
            crimeCombo.removeAllItems();
            crimeMap.clear();
            crimeCombo.addItem("All");
            rs = con.prepareStatement("SELECT crime_id, type FROM crime").executeQuery();
            while(rs.next()){
                String type = rs.getString("type");
                crimeCombo.addItem(type);
                crimeMap.put(type, rs.getInt("crime_id"));
            }

        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private void loadCases(){
        try(Connection con = DB.getConnection()){
            model.setRowCount(0);
            String sql = "SELECT c.case_id, cr.type, crl.name AS criminal_name, po.name AS officer_name, c.case_status, c.remarks " +
                    "FROM case_record c " +
                    "LEFT JOIN crime cr ON c.crime_id = cr.crime_id " +
                    "LEFT JOIN criminal crl ON c.criminal_id = crl.criminal_id " +
                    "LEFT JOIN police_officer po ON c.officer_id = po.officer_id";
            ResultSet rs = con.prepareStatement(sql).executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("case_id"),
                        rs.getString("type"),
                        rs.getString("criminal_name"),
                        rs.getString("officer_name"),
                        rs.getString("case_status"),
                        rs.getString("remarks")
                });
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent e){
        try(Connection con = DB.getConnection()){
            if(e.getSource()==loadBtn){
                loadCases();
            } else {
                StringBuilder sql = new StringBuilder("SELECT c.case_id, cr.type, crl.name AS criminal_name, po.name AS officer_name, c.case_status, c.remarks " +
                        "FROM case_record c " +
                        "LEFT JOIN crime cr ON c.crime_id = cr.crime_id " +
                        "LEFT JOIN criminal crl ON c.criminal_id = crl.criminal_id " +
                        "LEFT JOIN police_officer po ON c.officer_id = po.officer_id WHERE 1=1");

                if(!"All".equals(officerCombo.getSelectedItem())){
                    sql.append(" AND c.officer_id=").append(officerMap.get(officerCombo.getSelectedItem()));
                }
                if(!"All".equals(crimeCombo.getSelectedItem())){
                    sql.append(" AND c.crime_id=").append(crimeMap.get(crimeCombo.getSelectedItem()));
                }
                if(!"All".equals(statusCombo.getSelectedItem())){
                    sql.append(" AND c.case_status='").append(statusCombo.getSelectedItem()).append("'");
                }

                model.setRowCount(0);
                ResultSet rs = con.prepareStatement(sql.toString()).executeQuery();
                while(rs.next()){
                    model.addRow(new Object[]{
                            rs.getInt("case_id"),
                            rs.getString("type"),
                            rs.getString("criminal_name"),
                            rs.getString("officer_name"),
                            rs.getString("case_status"),
                            rs.getString("remarks")
                    });
                }
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }
}
