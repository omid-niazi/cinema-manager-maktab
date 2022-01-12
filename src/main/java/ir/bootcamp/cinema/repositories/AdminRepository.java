package ir.bootcamp.cinema.repositories;

import ir.bootcamp.cinema.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository extends JdbcRepository<Admin> {

    public AdminRepository(Connection connection) throws SQLException {
        super(connection);
        createDefaultAdmin();
    }

    @Override
    public int add(Admin admin) throws SQLException {
        String sql = "insert into admin values (DEFAULT, ?, ?, ?, ?, ?) returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, admin.getName());
        preparedStatement.setString(2, admin.getUsername());
        preparedStatement.setString(3, admin.getPassword());
        preparedStatement.setString(4, admin.getPhone());
        preparedStatement.setString(5, admin.getEmail());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public Admin find(int id) throws SQLException {
        String sql = "select * from admin where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Admin> findAll() throws SQLException {
        String sql = "select * from admin";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Admin admin) throws SQLException {
        String sql = "update admin set name = ?, username = ?, password = ?, phone = ?, email = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, admin.getName());
        preparedStatement.setString(2, admin.getUsername());
        preparedStatement.setString(3, admin.getPassword());
        preparedStatement.setString(4, admin.getPhone());
        preparedStatement.setString(5, admin.getEmail());
        preparedStatement.setInt(6, admin.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from admin where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    public Admin find(String username) throws SQLException {
        String sql = "select * from admin where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    private void createDefaultAdmin() throws SQLException {
        String sql = "insert into admin values(DEFAULT, 'admin', 'admin', 'admin', 'admin', 'admin') on conflict (username) do nothing ";
        connection.createStatement().execute(sql);
    }

    @Override
    protected Admin mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Admin(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email")
        );
    }

    @Override
    protected List<Admin> mapToList(ResultSet resultSet) throws SQLException {
        List<Admin> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Admin(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email")
            ));
        }
        return result;
    }
}
