package model;

public class BillItem {
    private int billItemId;
    private int billId;       // foreign key from Bill
    private int productId;    // foreign key from Product
    private int quantity;
    private double subtotal;

    public BillItem(int billItemId, int billId, int productId, int quantity, double subtotal) {
        this.billItemId = billItemId;
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public BillItem() {}

    public int getBillItemId() { return billItemId; }
    public void setBillItemId(int billItemId) { this.billItemId = billItemId; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
