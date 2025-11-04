package ui;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    public DashboardFrame(String role) {
        setTitle("Dashboard - " + role);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Products", new ProductPanel());
        tabs.add("Customers", new Customer());
        tabs.add("Billing", new BillingPanel());
if(role.equalsIgnoreCase("ADMIN")) tabs.add("Users", new UserPanel());

if(role.equalsIgnoreCase("ADMIN")) tabs.add("Users", new UserPanel());


        add(tabs);
    }
}
