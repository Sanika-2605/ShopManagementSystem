package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import util.DBConnection;

public class ProductPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfName, tfQty, tfPrice;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public ProductPanel() {
        setLayout(new BorderLayout(8,8));

        model = new DefaultTableModel(new Object[]{"ID","Name","Quantity","Price"}, 0) {
            public boolean isCellEditable(int row, int col){ return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(2,4,8,8));
        tfName = new JTextField(); tfQty = new JTextField(); tfPrice = new JTextField();
        form.add(new JLabel("Name:")); form.add(tfName);
        form.add(new JLabel("Qty:")); form.add(tfQty);
        form.add(new JLabel("Price:")); form.add(tfPrice);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8));
        buttons.add(btnAdd); buttons.add(btnUpdate); buttons.add(btnDelete); buttons.add(btnRefresh);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(buttons, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnRefresh.addActionListener(e -> loadProducts());

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int r = table.getSelectedRow();
                if(r>=0){
                    tfName.setText(model.getValueAt(r,1).toString());
                    tfQty.setText(model.getValueAt(r,2).toString());
                    tfPrice.setText(model.getValueAt(r,3).toString());
                }
            }
        });

        loadProducts();
    }

    private void loadProducts(){
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT product_id, name, quantity, price FROM products")) {
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getBigDecimal("price")
                });
            }
        } catch(SQLException ex){ showError(ex); }
    }

    private void addProduct(){
        String name = tfName.getText().trim();
        String sQty = tfQty.getText().trim();
        String sPrice = tfPrice.getText().trim();
        if(name.isEmpty() || sQty.isEmpty() || sPrice.isEmpty()){ JOptionPane.showMessageDialog(this,"Fill all fields"); return; }
        try {
            int q = Integer.parseInt(sQty);
            double p = Double.parseDouble(sPrice);
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO products (name, quantity, price) VALUES (?,?,?)")) {
                ps.setString(1, name); ps.setInt(2, q); ps.setDouble(3, p);
                ps.executeUpdate();
                loadProducts();
                clearInputs();
            }
        } catch(NumberFormatException nfe){ JOptionPane.showMessageDialog(this,"Invalid number for qty/price"); }
        catch(SQLException ex){ showError(ex); }
    }

    private void updateProduct(){
        int r = table.getSelectedRow();
        if(r<0){ JOptionPane.showMessageDialog(this,"Select a row to update"); return; }
        int id = (int) model.getValueAt(r,0);
        String name = tfName.getText().trim();
        String sQty = tfQty.getText().trim();
        String sPrice = tfPrice.getText().trim();
        if(name.isEmpty() || sQty.isEmpty() || sPrice.isEmpty()){ JOptionPane.showMessageDialog(this,"Fill all fields"); return; }
        try {
            int q = Integer.parseInt(sQty);
            double p = Double.parseDouble(sPrice);
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("UPDATE products SET name=?, quantity=?, price=? WHERE product_id=?")) {
                ps.setString(1, name); ps.setInt(2, q); ps.setDouble(3, p); ps.setInt(4, id);
                ps.executeUpdate();
                loadProducts();
                clearInputs();
            }
        } catch(NumberFormatException nfe){ JOptionPane.showMessageDialog(this,"Invalid number for qty/price"); }
        catch(SQLException ex){ showError(ex); }
    }

    private void deleteProduct(){
        int r = table.getSelectedRow();
        if(r<0){ JOptionPane.showMessageDialog(this,"Select a row to delete"); return; }
        int id = (int) model.getValueAt(r,0);
        int yn = JOptionPane.showConfirmDialog(this, "Delete product id "+id+"?","Confirm",JOptionPane.YES_NO_OPTION);
        if(yn!=JOptionPane.YES_OPTION) return;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE product_id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
            loadProducts();
            clearInputs();
        } catch(SQLException ex){ showError(ex); }
    }

    private void clearInputs(){ tfName.setText(""); tfQty.setText(""); tfPrice.setText(""); }

    private void showError(Exception ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: "+ex.getMessage());
    }
}
