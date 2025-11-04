package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import util.DBConnection;

public class UserPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JComboBox<String> cbRole;
    private JButton btnAdd, btnRefresh;

    public UserPanel(){
        setLayout(new BorderLayout(8,8));
        model = new DefaultTableModel(new Object[]{"ID","Username","Role"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(1,6,8,8));
        tfUsername = new JTextField(); pfPassword = new JPasswordField();
        cbRole = new JComboBox<>(new String[]{"ADMIN","CASHIER"});
        btnAdd = new JButton("Create User"); btnRefresh = new JButton("Refresh");
        form.add(new JLabel("Username:")); form.add(tfUsername);
        form.add(new JLabel("Password:")); form.add(pfPassword);
        form.add(cbRole); form.add(btnAdd);
        add(form, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> createUser());
        btnRefresh.addActionListener(e -> loadUsers());

        loadUsers();
    }

    private void loadUsers(){
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT user_id, username, role FROM users")){
            while(rs.next()) model.addRow(new Object[]{ rs.getInt("user_id"), rs.getString("username"), rs.getString("role") });
        } catch(SQLException ex){ showError(ex); }
    }

    private void createUser(){
        String uname = tfUsername.getText().trim();
        String pwd = new String(pfPassword.getPassword());
        String role = (String) cbRole.getSelectedItem();
        if(uname.isEmpty() || pwd.isEmpty()){ JOptionPane.showMessageDialog(this,"Username & Password required"); return; }
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users (username, password_hash, role) VALUES (?, SHA2(?,256), ?)")){
            ps.setString(1, uname); ps.setString(2, pwd); ps.setString(3, role); ps.executeUpdate();
            loadUsers(); tfUsername.setText(""); pfPassword.setText("");
            JOptionPane.showMessageDialog(this,"User created");
        } catch(SQLException ex){ showError(ex); }
    }

    private void showError(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"DB error: "+ex.getMessage()); }
}
