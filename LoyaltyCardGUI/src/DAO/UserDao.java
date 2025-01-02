/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.UserData;
import utils.JDBCUtil;

import java.sql.*;

/**
 *
 * @author MSI 15
 */
public class UserDao {

    public static UserDao getInstance() {
        return new UserDao();
    }

    private static final String INSERT_USER_SQL = "INSERT INTO users (firstname, last_name, phone_number, identification, birthday, gender, point, public_key, avatar) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
     private static final String UPDATE_PUBLIC_KEY_USER_SQL = "UPDATE users "
            + "SET public_key = ? "
            + "WHERE id = ?";

    private static final String UPDATE_USER_SQL = "UPDATE users "
            + "SET firstname = ?, last_name = ?, phone_number = ?, identification = ?, "
            + "birthday = ?, gender = ?, point = ?, public_key = ?, avatar = ? "
            + "WHERE id = ?";

    private static final String GET_USER_BY_ID_SQL = "SELECT * "
            + "FROM users WHERE id = ?";

    private static final String CHECK_EXIST_USER_SQL = "SELECT * FROM users WHERE phone_number = ? OR identification = ?";

    public UserData checkExistUser(String phone, String identification) throws ClassNotFoundException {
        UserData user = null;

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(CHECK_EXIST_USER_SQL)) {

            // Set giá trị cho PreparedStatement
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, identification);

            // Thực thi câu lệnh SQL
            ResultSet resultSet = preparedStatement.executeQuery();

            // Xử lý kết quả trả về
            if (resultSet.next()) {
                user = new UserData();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPhone(resultSet.getString("phone_number"));
                user.setIdentification(resultSet.getString("identification"));
                user.setBirthday(resultSet.getString("birthday"));
                user.setGender(resultSet.getInt("gender"));
                user.setPoints((short)resultSet.getInt("point"));
                user.setPublicKey(resultSet.getBytes("public_key"));
                user.setImagePath(resultSet.getString("avatar"));
                user.setCreatedAt(resultSet.getTimestamp("created_at"));
                user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user; // Trả về user hoặc null nếu không tìm thấy
    }

    public UserData getUserById(int id) throws ClassNotFoundException {
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(GET_USER_BY_ID_SQL)) {

            // Set giá trị vào PreparedStatement
            preparedStatement.setInt(1, id);

            // Thực hiện câu lệnh SQL
            ResultSet resultSet = preparedStatement.executeQuery();

            // Kiểm tra và lấy dữ liệu nếu tồn tại
            if (resultSet.next()) {
                UserData user = new UserData();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPhone(resultSet.getString("phone_number"));
                user.setIdentification(resultSet.getString("identification"));
                user.setBirthday(resultSet.getString("birthday"));
                user.setGender(resultSet.getInt("gender"));
                user.setPoints((short) resultSet.getInt("point"));
                user.setPublicKey(resultSet.getBytes("public_key"));
                user.setImagePath(resultSet.getString("avatar"));
                user.setCreatedAt(resultSet.getTimestamp("created_at"));
                user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy user hoặc có lỗi
    }

    public Integer insertUser(UserData user) throws ClassNotFoundException {
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            // Set giá trị vào PreparedStatement
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getIdentification());
            preparedStatement.setString(5, user.getBirthday());
            preparedStatement.setInt(6, user.getGender());
            preparedStatement.setInt(7, user.getPoints());
            preparedStatement.setBytes(8, user.getPublicKey());
            
            // image
//            if(user.getImage()!=null)
//            {
//                preparedStatement.setBytes(9, user.getImage());
//            }else{
//                preparedStatement.setNull(9, java.sql.Types.BLOB); 
//            }

             preparedStatement.setString(9, user.getImagePath());
            
            // Thực hiện câu lệnh SQL
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Lấy ID của người dùng mới chèn
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(UserData user) throws ClassNotFoundException {
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_SQL)) {

            // Set giá trị vào PreparedStatement
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getIdentification());
            preparedStatement.setString(5, user.getBirthday());
            preparedStatement.setInt(6, user.getGender());
            preparedStatement.setInt(7, user.getPoints());
            preparedStatement.setBytes(8, user.getPublicKey());
            preparedStatement.setString(9, user.getImagePath()); 
            preparedStatement.setInt(10, user.getId()); // Điều kiện WHERE

            // Thực hiện câu lệnh SQL
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi
    }
    
    public boolean updatePublicKey(UserData user) throws ClassNotFoundException {
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(UPDATE_PUBLIC_KEY_USER_SQL)) {

            // Set giá trị vào PreparedStatement
            preparedStatement.setBytes(1, user.getPublicKey());
            preparedStatement.setInt(2, user.getId()); // Điều kiện WHERE

            // Thực hiện câu lệnh SQL
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi
    }

    public static void main(String[] args) throws ClassNotFoundException {
        int res = UserDao.getInstance().insertUser(new UserData());
        System.out.println("DAO.UserDao.main()" + res);
    }

}
