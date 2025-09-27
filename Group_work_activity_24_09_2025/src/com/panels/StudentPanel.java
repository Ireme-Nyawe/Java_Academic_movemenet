package com.panels;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentPanel extends JPanel implements ActionListener {
    JTextField idTxt = new JTextField(), nameTxt = new JTextField(), emailTxt = new JTextField();
    JButton addBtn = new JButton("Add"), updateBtn = new JButton("Update"), deleteBtn = new JButton("Delete"), loadBtn = new JButton("Load");
    JTable table; DefaultTableModel model;

    public StudentPanel() {
        setLayout(null);
        String[] labels = {"ID", "Name", "Email"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 180, 500, 300);

        int y = 20;
        addField("ID", idTxt, y); y+=30;
        addField("Name", nameTxt, y); y+=30;
        addField("Email", emailTxt, y);

        idTxt.setEditable(false);
        addButtons();
        add(sp);
        loadStudents();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row>=0){
                    idTxt.setText(model.getValueAt(row,0).toString());
                    nameTxt.setText(model.getValueAt(row,1).toString());
                    emailTxt.setText(model.getValueAt(row,2).toString());
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

    public void actionPerformed(ActionEvent e){
        try(Connection con = DB.getConnection()){
            if(e.getSource()==addBtn){
                PreparedStatement ps = con.prepareStatement("INSERT INTO student(name,email) VALUES(?,?)");
                ps.setString(1, nameTxt.getText());
                ps.setString(2, emailTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Student Added!");
                loadStudents();
            } else if(e.getSource()==updateBtn){
                PreparedStatement ps = con.prepareStatement("UPDATE student SET name=?, email=? WHERE studentid=?");
                ps.setString(1, nameTxt.getText());
                ps.setString(2, emailTxt.getText());
                ps.setInt(3, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Student Updated!");
                loadStudents();
            } else if(e.getSource()==deleteBtn){
                PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE studentid=?");
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Student Deleted!");
                loadStudents();
            } else if(e.getSource()==loadBtn){
                loadStudents();
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private void loadStudents(){
        try(Connection con = DB.getConnection()){
            model.setRowCount(0);
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM student");
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("studentid"),
                        rs.getString("name"),
                        rs.getString("email")
                });
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }
}
