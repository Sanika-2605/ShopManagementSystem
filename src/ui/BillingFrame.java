package ui;

import java.awt.*;
import java.awt.event.*;
import util.DataStore;
import model.Bill;
import model.Product;

public class BillingFrame extends Frame {
    private Panel top;
    private Choice choiceProducts;
    private TextField tfQty, tfCustId;
    private Button btnNewBill, btnAdd, btnGenerate;
    private TextArea taBill;
    
    private Bill currentBill;
    private DataStore store;

    public BillingFrame(Frame parent) {
        this.store = DataStore.getInstance();

        top = new Panel();
        choiceProducts = new Choice();
        tfQty = new TextField(5);
        tfCustId = new TextField(10);
        btnNewBill = new Button("New Bill");
        btnAdd = new Button("Add");
        btnGenerate = new Button("Generate");

        top.add(new Label("Product:")); top.add(choiceProducts);
        top.add(new Label("Qty:")); top.add(tfQty);
        top.add(new Label("Customer ID (opt):")); top.add(tfCustId);
        top.add(btnNewBill); top.add(btnAdd); top.add(btnGenerate);

        taBill = new TextArea();
        taBill.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(taBill, BorderLayout.CENTER);

        // Example listener
        btnNewBill.addActionListener(e -> {
            String custId = tfCustId.getText().trim();
            currentBill = new Bill("B001", custId.isEmpty()?"NA":custId);
            taBill.setText("New bill created: B001\n");
        });

        // add product to current bill
        btnAdd.addActionListener(e -> {
            if (currentBill == null) { taBill.setText("Create a new bill first.\n"); return; }
            String pid = choiceProducts.getSelectedItem();
            if (pid == null || pid.trim().isEmpty()) { taBill.setText("Product not selected.\n"); return; }
            try {
                int qty = Integer.parseInt(tfQty.getText().trim());
                var opt = store.findProductById(pid);
                if (opt.isEmpty()) { taBill.setText("Product not found.\n"); return; }
                Product p = opt.get();
                currentBill.addItem(pid, qty, p.getPrice());
                taBill.append("Added " + qty + " x " + p.getName() + "\n");
            } catch (Exception ex) { taBill.append("Error: " + ex.getMessage() + "\n"); }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        setSize(500, 400);
        setVisible(true);
    }
}
