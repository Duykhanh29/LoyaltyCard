package DAO;

import Models.Invoice;
import Models.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.JDBCUtil;

public class InvoiceDao {
    
    public static InvoiceDao getInstance() {
        return new InvoiceDao();
    }

    // SQL query để lấy hóa đơn và danh sách đơn hàng theo invoice_code
    private static final String GET_INVOICE_BY_CODE_SQL = 
            "SELECT invoices.*, orders.id AS order_id, orders.product_name, orders.quantity, orders.price, orders.description AS order_description, orders.created_at AS order_created_at, orders.invoice_id " +
            "FROM invoices " +
            "LEFT JOIN orders ON invoices.id = orders.invoice_id " +
            "WHERE invoices.invoice_code = ?";

    // Hàm lấy hóa đơn và danh sách đơn hàng theo mã hóa đơn
    public Invoice getInvoiceByCode(String invoiceCode) throws SQLException, ClassNotFoundException {
        Invoice invoice = null;
        List<Order> orders = new ArrayList<>();

        // Kết nối cơ sở dữ liệu
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(GET_INVOICE_BY_CODE_SQL)) {

            // Set giá trị invoice_code vào PreparedStatement
            preparedStatement.setString(1, invoiceCode);

            // Thực thi câu lệnh SQL
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Nếu hóa đơn chưa được tạo, tạo một đối tượng Invoice
                    if (invoice == null) {
                        int id = resultSet.getInt("id");
                        String code = resultSet.getString("invoice_code");
                        int userId = resultSet.getInt("user_id");
                        double totalAmount = resultSet.getDouble("total_amount");
                        String paymentMethod = resultSet.getString("payment_method");
                        String description = resultSet.getString("description");
                        Timestamp createdAt = resultSet.getTimestamp("created_at");
                        Timestamp updatedAt = resultSet.getTimestamp("updated_at");

                        // Tạo đối tượng Invoice
                        invoice = new Invoice(id, code, userId, totalAmount, paymentMethod, description, createdAt, updatedAt);
                    }

                    // Lấy thông tin đơn hàng và thêm vào danh sách orders
                    int orderId = resultSet.getInt("order_id");
                    if (orderId > 0) {
                        String productName = resultSet.getString("product_name");
                        int quantity = resultSet.getInt("quantity");
                        double price = resultSet.getDouble("price");
                        String orderDescription = resultSet.getString("order_description");
                        Timestamp orderCreatedAt = resultSet.getTimestamp("order_created_at");
                        int invoiceId = resultSet.getInt("invoice_id");

                        // Tạo đối tượng Order và thêm vào danh sách
                        Order order = new Order(orderId, invoiceId, productName, quantity, price, orderDescription, orderCreatedAt);
                        orders.add(order);
                    }
                }
            }
        }

        // Gán danh sách orders vào hóa đơn
        if (invoice != null) {
            invoice.setOrders(orders);
        }

        return invoice;
    }
}
