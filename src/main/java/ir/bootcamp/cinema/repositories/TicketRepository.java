package ir.bootcamp.cinema.repositories;

import ir.bootcamp.cinema.model.Cinema;
import ir.bootcamp.cinema.model.Customer;
import ir.bootcamp.cinema.model.ScheduledSession;
import ir.bootcamp.cinema.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository extends JdbcRepository<Ticket> {

    public TicketRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public int add(Ticket ticket) throws SQLException {
        String sql = "insert into ticket values (DEFAULT, ?, ?) returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, ticket.getScheduledSession().getId());
        preparedStatement.setInt(2, ticket.getCustomer().getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public Ticket find(int id) throws SQLException {
        String sql = "select ticket.*, customer.*, scheduled_session.*, cinema.*, ticket.id as ticket_id, cinema.id as cinema_id, scheduled_session.id as scheduled_session_id , customer.id as customer_id, customer.name as customer_name, cinema.name as cinema_name from ticket " +
                "inner join scheduled_session on ticket.scheduled_session_id = scheduled_session.id " +
                "inner join customer on ticket.customer_id = customer.id " +
                "inner join cinema  on scheduled_session.cinema_id = cinema.id " +
                "where ticket.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Ticket> findAll() throws SQLException {
        String sql = "select ticket.*, customer.*, scheduled_session.*, cinema.*, ticket.id as ticket_id, cinema.id as cinema_id, scheduled_session.id as scheduled_session_id , customer.id as customer_id,customer.name as customer_name, cinema.name as cinema_name from ticket inner join scheduled_session on ticket.scheduled_session_id = scheduled_session.id inner join customer on ticket.customer_id = customer.id inner join cinema on scheduled_session.cinema_id = cinema.id";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Ticket ticket) throws SQLException {
        String sql = "update ticket set scheduled_session_id = ?, customer_id = ?, seat_number = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, ticket.getScheduledSession().getId());
        preparedStatement.setInt(2, ticket.getCustomer().getId());
        preparedStatement.setInt(3, ticket.getSeatNumber());
        preparedStatement.setInt(4, ticket.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from ticket where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    public List<Ticket> findByDate(Date date) throws SQLException {
        String sql = "select ticket.*, customer.*, scheduled_session.*, cinema.*, ticket.id as ticket_id, cinema.id as cinema_id, scheduled_session.id as scheduled_session_id , customer.id as customer_id, customer.name as customer_name, cinema.name as cinema_name from ticket " +
                "inner join scheduled_session on ticket.scheduled_session_id = scheduled_session.id " +
                "inner join customer on ticket.customer_id = customer.id " +
                "inner join cinema on scheduled_session.cinema_id = cinema.id where scheduled_session.date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }


    public List<Ticket> findBySessionId(int sessionId) throws SQLException {
        String sql = "select ticket.*, customer.*, scheduled_session.*, cinema.*, ticket.id as ticket_id, cinema.id as cinema_id, scheduled_session.id as scheduled_session_id , customer.id as customer_id, customer.name as customer_name, cinema.name as cinema_name from ticket " +
                "inner join scheduled_session on ticket.scheduled_session_id = scheduled_session.id " +
                "inner join customer on ticket.customer_id = customer.id " +
                "inner join cinema on scheduled_session.cinema_id = cinema.id where " +
                "scheduled_session.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sessionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    protected Ticket mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        Cinema cinema = new Cinema(
                resultSet.getInt("cinema_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("cinema_name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getString("status")
        );
        ScheduledSession scheduledSession = new ScheduledSession(
                resultSet.getInt("scheduled_session_id"),
                cinema,
                resultSet.getString("movie_name"),
                resultSet.getDate("date"),
                resultSet.getTime("start_time"),
                resultSet.getTime("end_time"),
                resultSet.getShort("capacity"),
                resultSet.getShort("sold_tickets"),
                resultSet.getLong("price")
        );
        Customer customer = new Customer(
                resultSet.getInt("customer_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("customer_name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getLong("balance"));
        return new Ticket(resultSet.getInt("ticket_id"),
                scheduledSession, customer, resultSet.getShort("seat_number"));
    }

    @Override
    protected List<Ticket> mapToList(ResultSet resultSet) throws SQLException {
        List<Ticket> result = new ArrayList<>();
        while (resultSet.next()) {
            Cinema cinema = new Cinema(
                    resultSet.getInt("cinema_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("cinema_name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getString("address"),
                    resultSet.getString("status")
            );
            ScheduledSession scheduledSession = new ScheduledSession(
                    resultSet.getInt("scheduled_session_id"),
                    cinema,
                    resultSet.getString("movie_name"),
                    resultSet.getDate("date"),
                    resultSet.getTime("start_time"),
                    resultSet.getTime("end_time"),
                    resultSet.getShort("capacity"),
                    resultSet.getShort("sold_tickets"),
                    resultSet.getLong("price")
            );
            Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("customer_name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getLong("balance"));
            result.add(new Ticket(resultSet.getInt("ticket_id"),
                    scheduledSession, customer, resultSet.getShort("seat_number")));
        }
        return result;
    }
}
