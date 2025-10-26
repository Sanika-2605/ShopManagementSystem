package util;


import model.Bill;
import model.Customer;
import model.Product;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DataStore implements Storable {
private static final String DATA_DIR = "data";
private final File productFile = new File(DATA_DIR, "products.ser");
private final File customerFile = new File(DATA_DIR, "customers.ser");
private final File billFile = new File(DATA_DIR, "bills.ser");


private List<Product> products = new ArrayList<>();
private List<Customer> customers = new ArrayList<>();
private List<Bill> bills = new ArrayList<>();


// singleton
private static DataStore instance;


private DataStore() { }


public static DataStore getInstance() {
if (instance == null) instance = new DataStore();
return instance;
}


@Override
public void saveAll() throws Exception {
new File(DATA_DIR).mkdirs();
FileHandler.saveList(productFile, products);
FileHandler.saveList(customerFile, customers);
FileHandler.saveList(billFile, bills);
}


@Override
public void loadAll() throws Exception {
products = FileHandler.loadList(productFile);
customers = FileHandler.loadList(customerFile);
bills = FileHandler.loadList(billFile);
}


// product ops
public void addProduct(Product p) { products.add(p); }
public List<Product> getProducts() { return products; }
public Optional<Product> findProductById(String id) {
return products.stream().filter(p -> p.getId().equals(id)).findFirst();
}


public void removeProduct(String id) { products.removeIf(p -> p.getId().equals(id)); }


// customer ops
public void addCustomer(Customer c) { customers.add(c); }
public List<Customer> getCustomers() { return customers; }
public Optional<Customer> findCustomerById(String id) {
	return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
}

// bill ops
public void addBill(Bill b) { bills.add(b); }
public List<Bill> getBills() { return bills; }
}