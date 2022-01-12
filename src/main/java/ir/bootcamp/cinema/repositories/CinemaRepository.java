package ir.bootcamp.cinema.repositories;

import ir.bootcamp.cinema.model.Cinema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CinemaRepository extends JdbcRepository<Cinema> {

    public CinemaRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public int add(Cinema cinema) throws SQLException {
        String sql = "insert into cinema values (DEFAULT, ?, ?, ?, ?, ?, ?, ?) returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, cinema.getName());
        preparedStatement.setString(2, cinema.getUsername());
        preparedStatement.setString(3, cinema.getPassword());
        preparedStatement.setString(4, cinema.getPhone());
        preparedStatement.setString(5, cinema.getEmail());
        preparedStatement.setString(6, cinema.getAddress());
        preparedStatement.setString(7, cinema.getStatus());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public Cinema find(int id) throws SQLException {
        String sql = "select * from cinema where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Cinema> findAll() throws SQLException {
        String sql = "select * from cinema";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Cinema cinema) throws SQLException {
        String sql = "update cinema set name = ?, username = ?, password = ?, phone = ?, email = ?, address = ?, status = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, cinema.getName());
        preparedStatement.setString(2, cinema.getUsername());
        preparedStatement.setString(3, cinema.getPassword());
        preparedStatement.setString(4, cinema.getPhone());
        preparedStatement.setString(5, cinema.getEmail());
        preparedStatement.setString(6, cinema.getAddress());
        preparedStatement.setString(7, cinema.getStatus());
        preparedStatement.setInt(8, cinema.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from cinema where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    public Cinema find(String username) throws SQLException {
        String sql = "select * from cinema where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    protected Cinema mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Cinema(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getString("status")
        );
    }

    @Override
    protected List<Cinema> mapToList(ResultSet resultSet) throws SQLException {
        List<Cinema> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Cinema(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getString("address"),
                    resultSet.getString("status")
            ));
        }
        return result;
    }
}
