package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Models.Voucher;
import utils.JDBCUtil;

public class VoucherDao {

    // SQL query để lấy danh sách voucher có start_time >= CURRENT_DATE và end_time
    // >= CURRENT_DATE
    private static final String GET_ACTIVE_VOUCHERS_SQL = "SELECT * FROM voucher WHERE start_time >= CURRENT_DATE AND end_time >= CURRENT_DATE AND status = 1";

    // SQL query để lấy danh sách voucher của người dùng theo user_id
    private static final String GET_VOUCHERS_BY_USER_ID_SQL = 
            "SELECT v.id, v.code, v.name, v.description, v.discount_value, v.discount_percent, v.start_time, v.end_time, v.points_value, v.status " +
            "FROM users_voucher uv " +
            "JOIN voucher v ON uv.voucher_id = v.id " +
            "WHERE uv.user_id = ?";

    // Hàm lấy danh sách voucher chưa hết hạn
    public List<Voucher> getActiveVouchers() throws SQLException, ClassNotFoundException {
        List<Voucher> vouchers = new ArrayList<>();

        // Kết nối cơ sở dữ liệu
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(GET_ACTIVE_VOUCHERS_SQL);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            // Duyệt qua kết quả trả về và tạo danh sách voucher
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double discountValue = resultSet.getDouble("discount_value");
                double discountPercent = resultSet.getDouble("discount_percent");
                Date startTime = resultSet.getDate("start_time");
                Date endTime = resultSet.getDate("end_time");
                int pointsValue = resultSet.getInt("points_value");
                int status = resultSet.getInt("status");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Timestamp updatedAt = resultSet.getTimestamp("update_at");

                // Tạo đối tượng Voucher và thêm vào danh sách
                Voucher voucher = new Voucher(id, code, name, description, discountValue, discountPercent,
                        startTime, endTime, pointsValue, status, createdAt, updatedAt);
                vouchers.add(voucher);
            }
        }

        return vouchers;
    }

    // Hàm lấy danh sách voucher của người dùng theo user_id
    public List<Voucher> getVouchersByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<Voucher> vouchers = new ArrayList<>();

        // Kết nối cơ sở dữ liệu
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(GET_VOUCHERS_BY_USER_ID_SQL)) {

            // Set giá trị user_id vào PreparedStatement
            preparedStatement.setInt(1, userId);

            // Thực thi câu lệnh SQL
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Tạo đối tượng Voucher từ kết quả trả về
                    int id = resultSet.getInt("id");
                    String code = resultSet.getString("code");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double discountValue = resultSet.getDouble("discount_value");
                    double discountPercent = resultSet.getDouble("discount_percent");
                    Date startTime = resultSet.getDate("start_time");
                    Date endTime = resultSet.getDate("end_time");
                    int pointsValue = resultSet.getInt("points_value");
                    int status = resultSet.getInt("status");
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    Timestamp updatedAt = resultSet.getTimestamp("updated_at");

                    // Tạo đối tượng Voucher sử dụng constructor có đầy đủ tham số
                    Voucher voucher = new Voucher(id, code, name, description, discountValue, discountPercent, startTime, endTime, pointsValue, status, createdAt, updatedAt);
                    vouchers.add(voucher);
                }
            }
        }

        return vouchers;
    }
}
