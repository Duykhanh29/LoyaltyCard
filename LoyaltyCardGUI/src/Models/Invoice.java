package Models;

import java.sql.Timestamp;
import java.util.List;

public class Invoice {
    private int id; // ID hóa đơn
    private String invoiceCode; // Mã hóa đơn duy nhất
    private int userId; // Liên kết với người dùng
    private double totalAmount; // Tổng giá trị hóa đơn
    private String paymentMethod; // Phương thức thanh toán
    private String description; // Mô tả hóa đơn
    private Timestamp createdAt; // Thời gian tạo hóa đơn
    private Timestamp updatedAt; // Thời gian cập nhật hóa đơn\
    private List<Order> orders;

    // Constructor không tham số
    public Invoice() {
    }

    // Constructor đầy đủ
    public Invoice(int id, String invoiceCode, int userId, double totalAmount,
            String paymentMethod, String description,
            Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.invoiceCode = invoiceCode;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Invoice(int id, String invoiceCode, int userId, double totalAmount,
            String paymentMethod, String description,
            Timestamp createdAt, Timestamp updatedAt, List<Order> orders) {
        this.id = id;
        this.invoiceCode = invoiceCode;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orders = orders;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
