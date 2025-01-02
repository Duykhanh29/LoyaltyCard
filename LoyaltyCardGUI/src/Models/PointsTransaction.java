package Models;

import java.sql.Timestamp;

public class PointsTransaction {
    private int id; // ID giao dịch
    private int userId; // Liên kết với người dùng
    private String transactionType; // Loại giao dịch ('add' hoặc 'subtract')
    private int points; // Số điểm được cộng hoặc trừ
    private String description; // Mô tả giao dịch
    private int resourceId; // ID tài nguyên (invoice, voucher hoặc gift)
    private String resourceType; // Loại tài nguyên ('invoice', 'voucher', 'gift')
    private Timestamp createdAt; // Thời gian thực hiện giao dịch

    // Constructor không tham số
    public PointsTransaction() {
    }

    // Constructor đầy đủ
    public PointsTransaction(int id, int userId, String transactionType, int points,
            String description, int resourceId,
            String resourceType, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.transactionType = transactionType;
        this.points = points;
        this.description = description;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PointsTransaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", transactionType='" + transactionType + '\'' +
                ", points=" + points +
                ", description='" + description + '\'' +
                ", resourceId=" + resourceId +
                ", resourceType='" + resourceType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
