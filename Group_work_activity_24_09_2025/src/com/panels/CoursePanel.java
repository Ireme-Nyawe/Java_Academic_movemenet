package com.panels;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CoursePanel extends JPanel implements ActionListener {
    JTextField idTxt = new JTextField(), nameTxt = new JTextField();
    JComboBox<String> teacherCmb = new JComboBox<>();
    JButton addBtn = new JButton("Add"), updateBtn = new JButton("Update"), deleteBtn = new JButton("Delete"), loadBtn = new JButton("Load");
    JTable table;
    DefaultTableModel model;

    public CoursePanel() {
        setLayout(null);
        String[] labels = {"ID", "Name", "Teacher"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 180, 500, 300);

        int y = 20;
        addField("ID", idTxt, y);
        y += 30;
        addField("Name", nameTxt, y);
        y += 30;
        addComboField("Teacher", teacherCmb, y);

        idTxt.setEditable(false);
        addButtons();
        add(sp);

        loadTeachers();
        loadCourses();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idTxt.setText(model.getValueAt(row, 0).toString());
                    nameTxt.setText(model.getValueAt(row, 1).toString());
                    teacherCmb.setSelectedItem(model.getValueAt(row, 2).toString());
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

    private void addComboField(String lbl, JComboBox<String> cmb, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 80, 25);
        cmb.setBounds(100, y, 150, 25);
        add(l);
        add(cmb);
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

    private void loadTeachers() {
        try (Connection con = DB.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM teacher");
            teacherCmb.removeAllItems();
            while (rs.next()) teacherCmb.addItem(rs.getString("name"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getTeacherId(String name, Connection con) throws Exception {
        ResultSet rs = con.createStatement().executeQuery("SELECT teacherid FROM teacher WHERE name='" + name + "'");
        if (rs.next()) return rs.getInt("teacherid");
        return 0;
    }

    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                int teacherId = getTeacherId(teacherCmb.getSelectedItem().toString(), con);
                PreparedStatement ps = con.prepareStatement("INSERT INTO course(name,teacherid) VALUES(?,?)");
                ps.setString(1, nameTxt.getText());
                ps.setInt(2, teacherId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course Added!");
                loadTeachers();
                loadCourses();
            } else if (e.getSource() == updateBtn) {
                int teacherId = getTeacherId(teacherCmb.getSelectedItem().toString(), con);
                PreparedStatement ps = con.prepareStatement("UPDATE course SET name=?, teacherid=? WHERE courseid=?");
                ps.setString(1, nameTxt.getText());
                ps.setInt(2, teacherId);
                ps.setInt(3, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course Updated!");
                loadTeachers();
                loadCourses();
            } else if (e.getSource() == deleteBtn) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM course WHERE courseid=?");
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course Deleted!");
                loadCourses();
            } else if (e.getSource() == loadBtn) {
                loadCourses();
                loadTeachers();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCourses() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT c.courseid, c.name, t.name as teacher FROM course c LEFT JOIN teacher t ON c.teacherid=t.teacherid"
            );
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("courseid"), rs.getString("name"), rs.getString("teacher")});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
