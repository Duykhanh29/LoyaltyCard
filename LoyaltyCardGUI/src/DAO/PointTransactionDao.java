package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Models.PointsTransaction;
import utils.JDBCUtil;

public class PointTransactionDao {
    
    public static PointTransactionDao getInstance() {
        return new PointTransactionDao();
    }

    // SQL query để lấy danh sách giao dịch điểm theo user_id
    private static final String GET_TRANSACTIONS_BY_USER_SQL = "SELECT * FROM point_transactions WHERE user_id = ?";

    private static final String INSERT_POINT_TRANSACTION_SQL = "INSERT INTO point_transactions (user_id, transaction_type, points, description, resource_id, resource_type) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    // Hàm lấy danh sách giao dịch điểm theo user_id
    public List<PointsTransaction> getTransactionsByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<PointsTransaction> transactions = new ArrayList<>();

        // Kết nối cơ sở dữ liệu
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(GET_TRANSACTIONS_BY_USER_SQL)) {

            // Set giá trị user_id vào PreparedStatement
            preparedStatement.setInt(1, userId);

            // Thực thi câu lệnh SQL
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Duyệt qua kết quả trả về và tạo danh sách giao dịch điểm
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String transactionType = resultSet.getString("transaction_type");
                    int points = resultSet.getInt("points");
                    String description = resultSet.getString("description");
                    int resourceId = resultSet.getInt("resource_id");
                    String resourceType = resultSet.getString("resource_type");
                    Timestamp createdAt = resultSet.getTimestamp("created_at");

                    // Tạo đối tượng PointTransaction và thêm vào danh sách
                    PointsTransaction transaction = new PointsTransaction(id, userId, transactionType, points,
                            description, resourceId, resourceType, createdAt);
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    public boolean insertPointTransaction(PointsTransaction pointTransaction) throws ClassNotFoundException {
        try (Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_POINT_TRANSACTION_SQL)) {

            preparedStatement.setInt(1, pointTransaction.getUserId());
            preparedStatement.setString(2, pointTransaction.getTransactionType());
            preparedStatement.setInt(3, pointTransaction.getPoints());
            preparedStatement.setString(4, pointTransaction.getDescription());
            preparedStatement.setInt(5, pointTransaction.getResourceId());
            preparedStatement.setString(6, pointTransaction.getResourceType());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
