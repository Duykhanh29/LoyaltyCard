package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    public static Connection getConnection() throws ClassNotFoundException {
        Connection conn = null;
        String url = "jdbc:mysql://162.248.102.236:6606/loyaltycard?useSSL=false";
        String username = "loyaltycard";
        String password = "loyaltycard";

        try {
            conn = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close(); // Đóng kết nối
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Connection con = null;
        try {
            con = getConnection();
            if (con != null) {
                System.out.println("oke");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con);
        }

    }
}
