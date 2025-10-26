package ui;


import java.awt.*;
import model.User;


public class MainFrameWithAuth extends Frame {
private User user;
public MainFrameWithAuth(User user) {
super("Shop Management - Logged in as: " + user.getUsername());
this.user = user;
setSize(600,400); setLayout(new GridLayout(2,2,10,10));


Button btnProducts = new Button("Manage Products");
Button btnBilling = new Button("Billing");
Button btnCustomers = new Button("Customers");
Button btnExit = new Button("Exit");


add(btnProducts); add(btnBilling); add(btnCustomers); add(btnExit);


// Access control: only ADMIN can access Product management
btnProducts.addActionListener(e -> {
if (user.getRole().equals("ADMIN")) new ProductFrame(this); else showAccessDenied();
});
// CASHIER and ADMIN can access billing
btnBilling.addActionListener(e -> { if (user.getRole().equals("CASHIER") || user.getRole().equals("ADMIN")) new BillingFrame(this); else showAccessDenied(); });
// ADMIN can view/add customers; CASHIER can view customers
btnCustomers.addActionListener(e -> { if (user.getRole().equals("ADMIN")||user.getRole().equals("CASHIER")) new CustomerFrame(this); else showAccessDenied(); });


btnExit.addActionListener(e -> { dispose(); System.exit(0); });


setVisible(true);
}


private void showAccessDenied() {
Dialog d = new Dialog(this, "Access Denied", true);
d.setSize(200,100); d.setLayout(new FlowLayout()); d.add(new Label("You do not have permission.")); d.add(new Button("OK") {{ addActionListener(e -> d.dispose()); }});
d.setVisible(true);
}
}