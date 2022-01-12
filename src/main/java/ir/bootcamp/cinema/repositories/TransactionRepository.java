package ir.bootcamp.cinema.repositories;

import ir.bootcamp.cinema.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository extends JdbcRepository<Transaction> {

    public TransactionRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public int add(Transaction transaction) throws SQLException {
        String sql = "insert into transaction values (DEFAULT, ?, ?, ?,?) returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transaction.getCustomer().getId());
        preparedStatement.setDate(2, transaction.getDate());
        preparedStatement.setTime(3, transaction.getTime());
        preparedStatement.setLong(4, transaction.getAmount());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public Transaction find(int id) throws SQLException {
        String sql = "select transaction.*, customer.*, transaction.id as transaction_id, customer.id as customer_id from transaction " +
                "inner join customer on customer.id = transaction.customer_id where transaction.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        String sql = "select transaction.*, customer.*, transaction.id as transaction_id, customer.id as customer_id from transaction " +
                "inner join customer on customer.id = transaction.customer_id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    public int update(Transaction transaction) throws SQLException {
        return -1;
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from transaction where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    public List<Transaction> find(Date date) throws SQLException {
        String sql = "select transaction.*, customer.*, transaction.id as transaction_id, customer.id as customer_id from transaction " +
                "inner join customer on customer.id = transaction.customer_id where transaction.date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    public List<Transaction> findUserTransactions(int userId) throws SQLException {

        String sql = "select transaction.*, customer.*, transaction.id as transaction_id, customer.id as customer_id from transaction " +
                "inner join customer on customer.id = transaction.customer_id where transaction.customer_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    protected Transaction mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        Customer customer = new Customer(
                resultSet.getInt("customer_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getLong("balance"));
        return new Transaction(
                resultSet.getInt("transaction_id"),
                customer,
                resultSet.getDate("date"),
                resultSet.getTime("time"),
                resultSet.getLong("amount"));
    }

    @Override
    protected List<Transaction> mapToList(ResultSet resultSet) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        while (resultSet.next()) {
            Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getLong("balance"));
            result.add(new Transaction(
                    resultSet.getInt("transaction_id"),
                    customer,
                    resultSet.getDate("date"),
                    resultSet.getTime("time"),
                    resultSet.getLong("amount")));
        }
        return result;
    }
}
