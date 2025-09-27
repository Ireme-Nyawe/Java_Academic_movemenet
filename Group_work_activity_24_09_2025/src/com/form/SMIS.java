package com.form;

import com.panels.*;

import javax.swing.*;
import java.awt.*;

public class SMIS extends JFrame{
    JTabbedPane tabs = new JTabbedPane();
    public  SMIS(String role, int userId){
        setTitle("School Management System");
        setSize(900,600);
        setLayout(new BorderLayout());
        
        if (role.equalsIgnoreCase("admin")){
            tabs.add("users", new UserPanel());
            tabs.add("teachers", new TeacherPanel());
            tabs.add("courses", new CoursePanel());
            tabs.add("Students", new StudentPanel());
            tabs.add("Marks", new MarkPanel());
        }
        else if (role.equalsIgnoreCase("teacher")){
            tabs.add("courses", new CoursePanel());
            tabs.add("Marks", new MarkPanel());
        }
        else if (role.equalsIgnoreCase("student")){
            tabs.add("Students", new StudentPanel());
            tabs.add("courses", new CoursePanel());
            tabs.add("Marks", new MarkPanel());
        }
        setVisible(true);
        add(tabs,BorderLayout.CENTER);
    }
}
