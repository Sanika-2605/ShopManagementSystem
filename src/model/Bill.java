package model;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int billId;
    private int customerId;
    private double totalAmount;
    private String billDate;
    private List<BillItem> items;

    public Bill(int billId, int customerId, double totalAmount, String billDate) {
        this.billId = billId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
        this.items = new ArrayList<>();
    }

    public Bill() {
        this.items = new ArrayList<>();
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getBillDate() { return billDate; }
    public void setBillDate(String billDate) { this.billDate = billDate; }

    public List<BillItem> getItems() { return items; }
    public void addItem(BillItem item) { this.items.add(item); }
}
