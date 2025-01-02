package Models;

import java.sql.Timestamp;

public class Order {
    private int id; // ID đơn hàng
    private int invoiceId; // Liên kết hóa đơn
    private String productName; // Tên sản phẩm
    private int quantity; // Số lượng sản phẩm
    private double price; // Giá của sản phẩm
    private String description; // Mô tả sản phẩm
    private Timestamp createdAt; // Thời gian tạo đơn hàng

    // Constructor không tham số
    public Order() {
    }

    // Constructor đầy đủ
    public Order(int id, int invoiceId, String productName, int quantity,
            double price, String description, Timestamp createdAt) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
