package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.JDBCUtil;

public class UserVoucherDao {
    
    public static UserVoucherDao getInstance() {
        return new UserVoucherDao();
    }

    private static final String INSERT_USER_VOUCHER_SQL = "INSERT INTO users_voucher (user_id, voucher_id, status) VALUES (?, ?, ?)";

    public boolean insertUserVoucher(int userId, int voucherId, int status) throws ClassNotFoundException {
        try (Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_VOUCHER_SQL)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, voucherId);
            preparedStatement.setInt(3, status);
            

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
