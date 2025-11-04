package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import util.DBConnection;

public class Customer extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfName, tfPhone;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public Customer(){
        setLayout(new BorderLayout(8,8));
        model = new DefaultTableModel(new Object[]{"ID","Name","Phone"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(1,4,8,8));
        tfName = new JTextField(); tfPhone = new JTextField();
        form.add(new JLabel("Name:")); form.add(tfName);
        form.add(new JLabel("Phone:")); form.add(tfPhone);

        btnAdd = new JButton("Add"); btnUpdate = new JButton("Update"); btnDelete = new JButton("Delete"); btnRefresh = new JButton("Refresh");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8));
        buttons.add(btnAdd); buttons.add(btnUpdate); buttons.add(btnDelete); buttons.add(btnRefresh);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER); south.add(buttons, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnRefresh.addActionListener(e -> loadCustomers());

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int r = table.getSelectedRow();
                if(r>=0){
                    tfName.setText(model.getValueAt(r,1).toString());
                    tfPhone.setText(model.getValueAt(r,2).toString());
                }
            }
        });

        loadCustomers();
    }

    private void loadCustomers(){
        model.setRowCount(0);
        Connection c = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            c = DBConnection.getConnection();
            if (!DBConnection.isConnectionValid(c)) {
                throw new SQLException("Database connection is not valid");
            }
            st = c.createStatement();
            rs = st.executeQuery("SELECT customer_id, name, phone FROM customers");
            while(rs.next()){
                model.addRow(new Object[]{ rs.getInt("customer_id"), rs.getString("name"), rs.getString("phone") });
            }
        } catch(SQLException ex){ 
            showError(ex); 
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {}
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignored) {}
            }
            if (c != null) {
                DBConnection.returnConnection(c);
            }
        }
    }

    private void addCustomer(){
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Name required"); return; }
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = DBConnection.getConnection();
            if (!DBConnection.isConnectionValid(c)) {
                throw new SQLException("Database connection is not valid");
            }
            ps = c.prepareStatement("INSERT INTO customers (name, phone) VALUES (?,?)");
            ps.setString(1, name); ps.setString(2, phone.isEmpty()?null:phone); ps.executeUpdate();
            loadCustomers(); clearInputs();
        } catch(SQLException ex){ showError(ex); }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {}
            }
            if (c != null) {
                DBConnection.returnConnection(c);
            }
        }
    }

    private void updateCustomer(){
        int r = table.getSelectedRow();
        if(r<0){ JOptionPane.showMessageDialog(this,"Select a customer"); return; }
        int id = (int) model.getValueAt(r,0);
        String name = tfName.getText().trim(); String phone = tfPhone.getText().trim();
        if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Name required"); return; }
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = DBConnection.getConnection();
            if (!DBConnection.isConnectionValid(c)) {
                throw new SQLException("Database connection is not valid");
            }
            ps = c.prepareStatement("UPDATE customers SET name=?, phone=? WHERE customer_id=?");
            ps.setString(1,name); ps.setString(2, phone.isEmpty()?null:phone); ps.setInt(3, id);
            ps.executeUpdate(); loadCustomers(); clearInputs();
        } catch(SQLException ex){ showError(ex); }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {}
            }
            if (c != null) {
                DBConnection.returnConnection(c);
            }
        }
    }

    private void deleteCustomer(){
        int r = table.getSelectedRow();
        if(r<0){ JOptionPane.showMessageDialog(this,"Select a customer"); return; }
        int id = (int) model.getValueAt(r,0);
        int yn = JOptionPane.showConfirmDialog(this, "Delete customer "+id+"?","Confirm",JOptionPane.YES_NO_OPTION);
        if(yn!=JOptionPane.YES_OPTION) return;
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = DBConnection.getConnection();
            if (!DBConnection.isConnectionValid(c)) {
                throw new SQLException("Database connection is not valid");
            }
            ps = c.prepareStatement("DELETE FROM customers WHERE customer_id=?");
            ps.setInt(1,id); ps.executeUpdate(); loadCustomers(); clearInputs();
        } catch(SQLException ex){ showError(ex); }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {}
            }
            if (c != null) {
                DBConnection.returnConnection(c);
            }
        }
    }

    private void clearInputs(){ tfName.setText(""); tfPhone.setText(""); }

    private void showError(SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"DB error: "+ex.getMessage()); }
}
