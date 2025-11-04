package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import util.DBConnection;
import java.text.SimpleDateFormat;

public class BillingPanel extends JPanel {
    private JComboBox<ProductItem> productBox;
    private JTextField qtyField;
    private JTextArea billArea;
    private JButton addBtn, saveBtn, printBtn, newBtn;
    private java.util.List<BillLine> currentLines = new ArrayList<>();
    private double total = 0;
    private int currentUserId = 1; // TODO: set logged-in user id from login flow (pass it)

    public BillingPanel() {
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel();
        productBox = new JComboBox<>();
        loadProducts();
        top.add(new JLabel("Product:")); top.add(productBox);

        top.add(new JLabel("Qty:")); qtyField = new JTextField(4); top.add(qtyField);

        addBtn = new JButton("Add"); saveBtn = new JButton("Save Bill"); printBtn = new JButton("Print"); newBtn = new JButton("New Bill");
        top.add(addBtn); top.add(saveBtn); top.add(printBtn); top.add(newBtn);

        add(top, BorderLayout.NORTH);

        billArea = new JTextArea(); billArea.setEditable(false); add(new JScrollPane(billArea), BorderLayout.CENTER);

        addBtn.addActionListener(e -> addLine());
        saveBtn.addActionListener(e -> saveBill());
        printBtn.addActionListener(e -> printBill());
        newBtn.addActionListener(e -> newBill());
        newBill();
    }

    private void newBill(){
        currentLines.clear();
        total = 0;
        billArea.setText("---- New Bill ----\n");
    }

    private void loadProducts(){
        productBox.removeAllItems();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT product_id, name, price, quantity FROM products")) {
            while(rs.next()){
                productBox.addItem(new ProductItem(rs.getInt("product_id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity")));
            }
        } catch(SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error loading products"); }
    }

    private void addLine(){
        ProductItem pi = (ProductItem) productBox.getSelectedItem();
        if(pi==null){ JOptionPane.showMessageDialog(this,"No product selected"); return; }
        int qty;
        try{ qty = Integer.parseInt(qtyField.getText().trim()); if(qty<=0){ JOptionPane.showMessageDialog(this,"Qty>0"); return; } }
        catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid qty"); return; }
        if(qty > pi.stock){ JOptionPane.showMessageDialog(this,"Not enough stock"); return; }
        double subtotal = qty * pi.price;
        currentLines.add(new BillLine(pi.productId, pi.name, qty, pi.price, subtotal));
        total += subtotal;
        billArea.append(pi.name + " x " + qty + " = " + subtotal + "\n");
        billArea.append("TOTAL: " + total + "\n");
    }

    private void saveBill(){
        if(currentLines.isEmpty()){ JOptionPane.showMessageDialog(this,"Add items first"); return; }
        String custIdStr = JOptionPane.showInputDialog(this, "Enter customer id (leave blank for guest)");
        Integer custId = null;
        if(custIdStr!=null && !custIdStr.trim().isEmpty()){
            try{ custId = Integer.parseInt(custIdStr.trim()); } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid customer id"); return; }
        }
        Connection c = null;
        try{
            c = DBConnection.getConnection();
            c.setAutoCommit(false);
            PreparedStatement ps = c.prepareStatement("INSERT INTO bills (customer_id, user_id, total) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            if(custId==null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, custId);
            ps.setInt(2, currentUserId);
            ps.setDouble(3, total);
            ps.executeUpdate();
            ResultSet gk = ps.getGeneratedKeys();
            if(gk.next()){
                int billId = gk.getInt(1);
                PreparedStatement psi = c.prepareStatement("INSERT INTO bill_items (bill_id, product_id, quantity, subtotal) VALUES (?,?,?,?)");
                PreparedStatement pUpd = c.prepareStatement("UPDATE products SET quantity = quantity - ? WHERE product_id = ?");
                for(BillLine bl : currentLines){
                    psi.setInt(1, billId); psi.setInt(2, bl.productId); psi.setInt(3, bl.qty); psi.setDouble(4, bl.subtotal);
                    psi.addBatch();
                    pUpd.setInt(1, bl.qty); pUpd.setInt(2, bl.productId); pUpd.addBatch();
                }
                psi.executeBatch();
                pUpd.executeBatch();
                c.commit();
                JOptionPane.showMessageDialog(this, "Bill saved. ID: " + billId);
                newBill();
                loadProducts();
            } else {
                c.rollback();
                JOptionPane.showMessageDialog(this, "Failed to retrieve bill id");
            }
        } catch(Exception ex){
            ex.printStackTrace();
            try{ if(c!=null) c.rollback(); }catch(Exception e){}
            JOptionPane.showMessageDialog(this,"Error saving bill: "+ex.getMessage());
        } finally {
            try { if(c!=null) c.setAutoCommit(true); } catch(Exception e){}
        }
    }

    private void printBill(){
        try {
            boolean ok = billArea.print();
            if(ok) JOptionPane.showMessageDialog(this,"Print job completed");
        } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Print failed: "+ex.getMessage()); }
    }

    // small helper classes
    private static class ProductItem {
        int productId; String name; double price; int stock;
        ProductItem(int id,String name,double price,int stock){ this.productId=id; this.name=name; this.price=price; this.stock=stock; }
        public String toString(){ return name + " (" + price + ")"; }
    }

    private static class BillLine {
        int productId; String name; int qty; double unit; double subtotal;
        BillLine(int productId,String name,int qty,double unit,double subtotal){ this.productId=productId; this.name=name; this.qty=qty; this.unit=unit; this.subtotal=subtotal; }
    }
}
