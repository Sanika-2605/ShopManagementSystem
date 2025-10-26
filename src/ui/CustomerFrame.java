package ui;

import util.DataStore;
import model.Customer;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerFrame extends Frame {
	private DataStore store = DataStore.getInstance();
	private TextField tfId, tfName, tfPhone;
	private TextArea ta;

	public CustomerFrame(Frame parent) {
		super("Customer Management");
		setSize(600, 450);
		setLayout(new BorderLayout());

		Panel in = new Panel(new GridLayout(4, 2));
		in.add(new Label("Customer ID:")); tfId = new TextField(); in.add(tfId);
		in.add(new Label("Name:")); tfName = new TextField(); in.add(tfName);
		in.add(new Label("Phone:")); tfPhone = new TextField(); in.add(tfPhone);
		Button btnAdd = new Button("Add"); Button btnShow = new Button("Show All"); in.add(btnAdd); in.add(btnShow);

		ta = new TextArea(); ta.setEditable(false);

		add(in, BorderLayout.NORTH); add(ta, BorderLayout.CENTER);

		btnAdd.addActionListener(e -> {
			try {
				String id = tfId.getText().trim();
				String name = tfName.getText().trim();
				String phone = tfPhone.getText().trim();
				if (id.isEmpty() || name.isEmpty()) { ta.setText("ID and name required"); return; }
				if (store.findCustomerById(id).isPresent()) { ta.setText("Customer already exists"); return; }
				store.addCustomer(new Customer(id, name, phone));
				ta.setText("Customer added.");
				tfId.setText(""); tfName.setText(""); tfPhone.setText("");
			} catch (Exception ex) { ta.setText("Error: " + ex.getMessage()); }
		});

		btnShow.addActionListener(e -> displayAll());

		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); } });
		setVisible(true); displayAll();
	}

	private void displayAll() {
		List<Customer> list = store.getCustomers();
		StringBuilder sb = new StringBuilder();
		for (Customer c : list) sb.append(c.toString()).append("\n");
		ta.setText(sb.toString());
	}
}