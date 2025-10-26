package model;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class Bill implements Serializable {
private static final long serialVersionUID = 1L;


private String billId;
private String customerId; // optional
private Date date;
private Map<String, Integer> items; // productId -> quantity
private double total;


public Bill(String billId, String customerId) {
this.billId = billId;
this.customerId = customerId;
this.date = new Date();
this.items = new LinkedHashMap<>();
this.total = 0.0;
}


public void addItem(String productId, int qty, double unitPrice) {
items.put(productId, items.getOrDefault(productId, 0) + qty);
total += unitPrice * qty;
}


public String getBillId() { return billId; }
public String getCustomerId() { return customerId; }
public Date getDate() { return date; }
public Map<String, Integer> getItems() { return items; }
public double getTotal() { return total; }


@Override
public String toString() {
return "Bill#" + billId + " | Cust:" + customerId + " | Date:" + date + " | Total:Rs " + total;
}
}