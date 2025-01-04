package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.JDBCUtil;

public class UserVoucherDao {

    private static final String INSERT_USER_VOUCHER_SQL = "INSERT INTO users_voucher (user_id, voucher_id, status, created_at, update_at) VALUES (?, ?, ?, ?, ?)";

    public boolean insertUserVoucher(int userId, int voucherId, String status, java.sql.Timestamp createdAt,
            java.sql.Timestamp updatedAt) throws ClassNotFoundException {
        try (Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_VOUCHER_SQL)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, voucherId);
            preparedStatement.setString(3, status);
            preparedStatement.setTimestamp(4, createdAt);
            preparedStatement.setTimestamp(5, updatedAt);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
