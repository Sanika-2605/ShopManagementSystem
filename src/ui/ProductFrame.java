package ui;

import util.DataStore;
import model.Product;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProductFrame extends Frame {
	private DataStore store = DataStore.getInstance();
	private TextField tfId, tfName, tfQty, tfPrice;
	private TextArea taDisplay;

	public ProductFrame(Frame parent) {
		super("Product Management");
		setSize(600, 450);
		setLayout(new BorderLayout());

		Panel in = new Panel(new GridLayout(5, 2));
		in.add(new Label("Product ID:")); tfId = new TextField(); in.add(tfId);
		in.add(new Label("Name:")); tfName = new TextField(); in.add(tfName);
		in.add(new Label("Quantity:")); tfQty = new TextField(); in.add(tfQty);
		in.add(new Label("Price:")); tfPrice = new TextField(); in.add(tfPrice);
		Button btnAdd = new Button("Add"); Button btnUpdate = new Button("Update"); in.add(btnAdd); in.add(btnUpdate);
		Button btnDelete = new Button("Delete"); Button btnShow = new Button("Show All"); in.add(btnDelete); in.add(btnShow);

		taDisplay = new TextArea(); taDisplay.setEditable(false);

		add(in, BorderLayout.NORTH);
		add(taDisplay, BorderLayout.CENTER);

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String id = tfId.getText().trim();
					String name = tfName.getText().trim();
					String qtyS = tfQty.getText().trim();
					String priceS = tfPrice.getText().trim();

					if (id.isEmpty() || name.isEmpty()) {
						showMsg("ID and Name are required.");
						return;
					}
					if (qtyS.isEmpty() || priceS.isEmpty()) {
						showMsg("Quantity and Price are required.");
						return;
					}

					int qty;
					double price;
					try {
						qty = Integer.parseInt(qtyS);
					} catch (NumberFormatException nfe) {
						showMsg("Invalid quantity. Enter a whole number.");
						return;
					}
					try {
						price = Double.parseDouble(priceS);
					} catch (NumberFormatException nfe) {
						showMsg("Invalid price. Enter a number (e.g. 99.99).");
						return;
					}

					if (store.findProductById(id).isPresent()) {
						showMsg("Product with this ID already exists.");
						return;
					}
					Product p = new Product(id, name, qty, price);
					store.addProduct(p);
					showMsg("Product added.");
					clearFields();
				} catch (Exception ex) { showMsg("Error: " + ex.getMessage()); }
			}
		});

		btnUpdate.addActionListener(e -> {
			try {
				String id = tfId.getText().trim();
				var opt = store.findProductById(id);
				if (opt.isEmpty()) { showMsg("Product not found"); return; }
				Product p = opt.get();
				if (!tfName.getText().trim().isEmpty()) p.setName(tfName.getText().trim());
				if (!tfQty.getText().trim().isEmpty()) p.setQuantity(Integer.parseInt(tfQty.getText().trim()));
				if (!tfPrice.getText().trim().isEmpty()) p.setPrice(Double.parseDouble(tfPrice.getText().trim()));
				showMsg("Product updated.");
				clearFields();
			} catch (Exception ex) { showMsg("Error: " + ex.getMessage()); }
		});

		btnDelete.addActionListener(e -> {
			try {
				String id = tfId.getText().trim();
				store.removeProduct(id);
				showMsg("Product removed (if existed).");
				clearFields();
			} catch (Exception ex) { showMsg("Error: " + ex.getMessage()); }
		});

		btnShow.addActionListener(e -> displayAll());

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) { dispose(); }
		});

		setVisible(true);
		displayAll();
	}

	private void displayAll() {
		List<Product> list = store.getProducts();
		StringBuilder sb = new StringBuilder();
		for (Product p : list) sb.append(p.toString()).append("\n");
		taDisplay.setText(sb.toString());
	}

	private void showMsg(String m) { taDisplay.setText(m + "\n" + taDisplay.getText()); }
	private void clearFields() { tfId.setText(""); tfName.setText(""); tfQty.setText(""); tfPrice.setText(""); }
}
