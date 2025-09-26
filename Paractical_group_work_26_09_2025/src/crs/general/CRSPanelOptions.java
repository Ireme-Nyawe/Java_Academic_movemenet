package crs.general;

import javax.swing.*;
import java.awt.*;

public class CRSPanelOptions extends JFrame {
    JTabbedPane tabs = new JTabbedPane();

    public CRSPanelOptions(String role, int userId) {
        setTitle("Crime Record Management System");
        setSize(1000, 650);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        if (role.equalsIgnoreCase("admin")) {
            // Admin can manage everything
            tabs.add("Users", new UserPanel());
            tabs.add("Officers", new OfficerPanel());
            tabs.add("Criminals", new CriminalPanel());
            tabs.add("Crimes", new CrimePanel());
            tabs.add("Cases", new CasePanel());
            tabs.add("Reports", new ReportPanel());
        } else {
            // Officer has limited options
            tabs.add("Criminals", new CriminalPanel());
            tabs.add("Crimes", new CrimePanel());
            tabs.add("Cases", new CasePanel());
            tabs.add("Reports", new ReportPanel());
        }

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
