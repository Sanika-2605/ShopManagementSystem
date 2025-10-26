package ui;

import util.AutoSaveThread;
import util.DataStore;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainFrame extends Frame {
private DataStore store = DataStore.getInstance();
private AutoSaveThread autoSave;


public MainFrame() {
super("Shop Management System - AWT");
setSize(600, 400);
setLayout(new GridLayout(2, 2, 10, 10));


Button btnProducts = new Button("Manage Products");
Button btnBilling = new Button("Billing");
Button btnCustomers = new Button("Customers");
Button btnExit = new Button("Exit");


add(btnProducts);
add(btnBilling);
add(btnCustomers);
add(btnExit);


btnProducts.addActionListener(e -> new ProductFrame(this));
btnBilling.addActionListener(e -> new BillingFrame(this));
btnCustomers.addActionListener(e -> new CustomerFrame(this));
btnExit.addActionListener(e -> {
try { store.saveAll(); } catch (Exception ex) { ex.printStackTrace(); }
if (autoSave != null) autoSave.shutdown();
dispose();
System.exit(0);
});


addWindowListener(new WindowAdapter() {
@Override
public void windowClosing(WindowEvent e) {
try { store.saveAll(); } catch (Exception ex) { ex.printStackTrace(); }
if (autoSave != null) autoSave.shutdown();
dispose();
System.exit(0);
}
});


setVisible(true);


// start auto-save every 60 sec
autoSave = new AutoSaveThread(60);
autoSave.start();


// load existing data
try { store.loadAll(); } catch (Exception e) { System.out.println("No existing data: " + e.getMessage()); }
}
}