package ir.bootcamp.cinema.repositories;

import ir.bootcamp.cinema.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository extends JdbcRepository<Customer> {

    public CustomerRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public int add(Customer customer) throws SQLException {
        String sql = "insert into customer values (DEFAULT, ?, ?, ?, ?, ?,?) returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getUsername());
        preparedStatement.setString(3, customer.getPassword());
        preparedStatement.setString(4, customer.getPhone());
        preparedStatement.setString(5, customer.getEmail());
        preparedStatement.setLong(6, customer.getBalance());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public Customer find(int id) throws SQLException {
        String sql = "select * from customer where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        String sql = "select * from admin";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Customer customer) throws SQLException {
        String sql = "update customer set name = ?, username = ?, password = ?, phone = ?, email = ?, balance = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getUsername());
        preparedStatement.setString(3, customer.getPassword());
        preparedStatement.setString(4, customer.getPhone());
        preparedStatement.setString(5, customer.getEmail());
        preparedStatement.setLong(6, customer.getBalance());
        preparedStatement.setInt(7, customer.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from customer where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    public Customer find(String username) throws SQLException {
        String sql = "select * from customer where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    protected Customer mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Customer(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getLong("balance")
        );
    }

    @Override
    protected List<Customer> mapToList(ResultSet resultSet) throws SQLException {
        List<Customer> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Customer(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getLong("balance")
            ));
        }
        return result;
    }
}
