package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "insert into myusers (id, firstname, lastname, age) values ((?), (?), (?), (?)) returning id";
    private static final String updateUserSQL = "update myusers set id = (?), firstname = (?), lastname = (?), age = (?) where id = (?) returning id, firstname, lastname, age";
    private static final String deleteUser = "delete from myusers where id = (?)";
    private static final String findUserByIdSQL = "select * from myusers where id = (?)";
    private static final String findUserByNameSQL = "select * from myusers where firstname = (?)";
    private static final String findAllUserSQL = "select * from myusers";

    public Long createUser(User user) {
        long result = -1;
        try {
            ps = connection.prepareStatement(createUserSQL);
            ps.setLong(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setInt(4, user.getAge());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public User findUserById(Long userId) {
        User resultUser = null;
        try {
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultUser = new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    public User findUserByName(String userName) {
        User resultUser = null;
        try {
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultUser = new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    public List<User> findAllUser() {
        List<User> resultList = new ArrayList<>();
        try {
            ps = connection.prepareStatement(findAllUserSQL);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultList.add(new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public User updateUser(User user) {
        User resultUser = null;
        try {
            ps = connection.prepareStatement(updateUserSQL);
            ps.setLong(1, user.getId());
            ps.setLong(5, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setInt(4, user.getAge());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultUser = new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    private void deleteUser(Long userId) {
        try {
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.executeUpdate();
            System.out.println("User with id " + userId + " successfully deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
