package Models;

import java.sql.Timestamp;

public class UserVoucher {
    private int id; // ID user voucher
    private int userId; // Liên kết với người dùng
    private int voucherId; // Liên kết với voucher
    private String status; // Trạng thái voucher
    private Timestamp createdAt; // Thời gian tạo
    private Timestamp updatedAt; // Thời gian cập nhật

    // Constructor không tham số
    public UserVoucher() {
    }

    // Constructor đầy đủ
    public UserVoucher(int id, int userId, int voucherId, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.voucherId = voucherId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        return "UserVoucher{" +
                "id=" + id +
                ", userId=" + userId +
                ", voucherId=" + voucherId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
