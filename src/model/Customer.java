package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Customer implements Serializable {
private static final long serialVersionUID = 1L;


private String id;
private String name;
private String phone;
private List<String> purchaseHistory; // store bill IDs or summaries


public Customer(String id, String name, String phone) {
this.id = id;
this.name = name;
this.phone = phone;
this.purchaseHistory = new ArrayList<>();
}


public String getId() { return id; }
public String getName() { return name; }
public String getPhone() { return phone; }
public List<String> getPurchaseHistory() { return purchaseHistory; }


public void addPurchase(String billSummary) { purchaseHistory.add(billSummary); }


@Override
public String toString() {
return id + " | " + name + " | " + phone;
}
}