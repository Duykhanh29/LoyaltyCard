package Models;

import java.sql.Timestamp;

public class Voucher {
    private int id; // ID voucher
    private String code; // Mã voucher
    private String name; // Tên voucher
    private String description; // Mô tả
    private double discountValue; // Giá trị giảm giá
    private double discountPercent; // Phần trăm giảm giá
    private java.sql.Date startTime; // Thời gian bắt đầu
    private java.sql.Date endTime; // Thời gian kết thúc
    private int pointsValue; // Số điểm cần để đổi
    private int status; // Trạng thái voucher
    private Timestamp createdAt; // Thời gian tạo
    private Timestamp updatedAt; // Thời gian cập nhật

    // Constructor không tham số
    public Voucher() {
    }

    // Constructor đầy đủ
    public Voucher(int id, String code, String name, String description, double discountValue, double discountPercent,
            java.sql.Date startTime, java.sql.Date endTime, int pointsValue, int status,
            Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.discountValue = discountValue;
        this.discountPercent = discountPercent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pointsValue = pointsValue;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public java.sql.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.sql.Date startTime) {
        this.startTime = startTime;
    }

    public java.sql.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.sql.Date endTime) {
        this.endTime = endTime;
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", discountValue=" + discountValue +
                ", discountPercent=" + discountPercent +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pointsValue=" + pointsValue +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
