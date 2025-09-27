package com.panels;

import com.util.DB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MarkPanel extends JPanel implements ActionListener {
    JComboBox<String> studentCmb = new JComboBox<>();
    JComboBox<String> courseCmb = new JComboBox<>();
    JTextField markTxt = new JTextField();
    JButton addBtn = new JButton("Add"), updateBtn = new JButton("Update"), deleteBtn = new JButton("Delete"), loadBtn = new JButton("Load");
    JTable table; DefaultTableModel model;

    public MarkPanel() {
        setLayout(null);
        String[] labels = {"ID","Student","Course","Mark"};
        model = new DefaultTableModel(labels,0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20,180,500,300);

        int y = 20;
        addComboField("Student", studentCmb, y); y+=30;
        addComboField("Course", courseCmb, y); y+=30;
        addField("Mark", markTxt, y);

        addButtons();
        add(sp);
        loadStudents(); loadCourses(); loadMarks();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                int row = table.getSelectedRow();
                if(row>=0){
                    studentCmb.setSelectedItem(model.getValueAt(row,1).toString());
                    courseCmb.setSelectedItem(model.getValueAt(row,2).toString());
                    markTxt.setText(model.getValueAt(row,3).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent txt, int y){
        JLabel l = new JLabel(lbl); l.setBounds(20,y,80,25); txt.setBounds(100,y,150,25); add(l); add(txt);
    }
    private void addComboField(String lbl, JComboBox<String> cmb, int y){
        JLabel l = new JLabel(lbl); l.setBounds(20,y,80,25); cmb.setBounds(100,y,150,25); add(l); add(cmb);
    }
    private void addButtons(){
        addBtn.setBounds(300,20,100,30);
        updateBtn.setBounds(300,60,100,30);
        deleteBtn.setBounds(300,100,100,30);
        loadBtn.setBounds(300,140,100,30);
        add(addBtn); add(updateBtn); add(deleteBtn); add(loadBtn);
        addBtn.addActionListener(this); updateBtn.addActionListener(this); deleteBtn.addActionListener(this); loadBtn.addActionListener(this);
    }

    // ... same imports and class declaration
    private void loadStudents(){
        try(Connection con = DB.getConnection()){
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM student");
            studentCmb.removeAllItems();
            while(rs.next()) studentCmb.addItem(rs.getString("name"));
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private void loadCourses(){
        try(Connection con = DB.getConnection()){
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT c.name FROM course c"
            );
            courseCmb.removeAllItems();
            while(rs.next()) courseCmb.addItem(rs.getString("name"));
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {

            // Get selected student and course safely
            Object selectedStudent = studentCmb.getSelectedItem();
            Object selectedCourse = courseCmb.getSelectedItem();

            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a student and a course.");
                return;
            }

            int studentId = getId("student", "studentid", "name", selectedStudent.toString(), con);
            int courseId = getId("course", "courseid", "name", selectedCourse.toString(), con);

            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO marks(studentid, courseid, mark) VALUES(?, ?, ?)"
                );
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDouble(3, Double.parseDouble(markTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mark Added!");

                // Reload combo boxes in case new students/courses were added
                loadStudents();
                loadCourses();
                loadMarks();

            } else if (e.getSource() == updateBtn) {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Please select a mark to update.");
                    return;
                }
                int markId = getIdFromTable(row);

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE marks SET studentid=?, courseid=?, mark=? WHERE markid=?"
                );
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDouble(3, Double.parseDouble(markTxt.getText()));
                ps.setInt(4, markId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mark Updated!");

                loadStudents();
                loadCourses();
                loadMarks();

            } else if (e.getSource() == deleteBtn) {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Please select a mark to delete.");
                    return;
                }
                int markId = getIdFromTable(row);

                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM marks WHERE markid=?"
                );
                ps.setInt(1, markId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mark Deleted!");

                loadMarks();

            } else if (e.getSource() == loadBtn) {
                loadStudents();
                loadCourses();
                loadMarks();
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid mark value. Please enter a valid number.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private int getId(String table,String idCol,String nameCol,String name, Connection con) throws Exception{
        ResultSet rs = con.createStatement().executeQuery("SELECT "+idCol+" FROM "+table+" WHERE "+nameCol+"='"+name+"'");
        if(rs.next()) return rs.getInt(idCol);
        return 0;
    }

    private int getIdFromTable(int row){
        return Integer.parseInt(model.getValueAt(row,0).toString());
    }

    private void loadMarks(){
        try(Connection con = DB.getConnection()){
            model.setRowCount(0);
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT m.markid, s.name as student, c.name as course, m.mark FROM marks m " +
                            "LEFT JOIN student s ON m.studentid=s.studentid " +
                            "LEFT JOIN course c ON m.courseid=c.courseid"
            );
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("markid"), rs.getString("student"), rs.getString("course"), rs.getDouble("mark")
                });
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }
}
