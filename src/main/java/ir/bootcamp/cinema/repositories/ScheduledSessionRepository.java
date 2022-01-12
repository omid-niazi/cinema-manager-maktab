package ir.bootcamp.cinema.repositories;

import ir.bootcamp.cinema.model.Cinema;
import ir.bootcamp.cinema.model.ScheduledSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduledSessionRepository extends JdbcRepository<ScheduledSession> {

    public ScheduledSessionRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public int add(ScheduledSession scheduledSession) throws SQLException {
        String sql = "insert into scheduled_session values (DEFAULT, ?,?,?, ?, ?, ?,?, ?) returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, scheduledSession.getCinema().getId());
        preparedStatement.setString(2, scheduledSession.getMovieName());
        preparedStatement.setDate(3, scheduledSession.getDate());
        preparedStatement.setTime(4, scheduledSession.getStartTime());
        preparedStatement.setTime(5, scheduledSession.getEndTime());
        preparedStatement.setShort(6, scheduledSession.getCapacity());
        preparedStatement.setShort(7, scheduledSession.getSoldTickets());
        preparedStatement.setLong(8, scheduledSession.getPrice());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    @Override
    public ScheduledSession find(int id) throws SQLException {
        String sql = "select c.*, s.*, c.id as cinema_id, s.id as scheduled_session_id  from scheduled_session s inner join cinema c on s.cinema_id = c.id where s.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<ScheduledSession> findAll() throws SQLException {
        String sql = "select c.*, s.*, c.id as cinema_id, s.id as scheduled_session_id " +
                "from scheduled_session s " +
                "inner join cinema c on s.cinema_id = c.id";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(ScheduledSession scheduledSession) throws SQLException {
        String sql = "update scheduled_session set cinema_id = ?, movie_name = ?, date = ?,  start_time = ?, end_time = ?, capacity = ?, sold_tickets = ?, price = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, scheduledSession.getCinema().getId());
        preparedStatement.setString(2, scheduledSession.getMovieName());
        preparedStatement.setDate(3, scheduledSession.getDate());
        preparedStatement.setTime(4, scheduledSession.getStartTime());
        preparedStatement.setTime(5, scheduledSession.getEndTime());
        preparedStatement.setShort(6, scheduledSession.getCapacity());
        preparedStatement.setShort(7, scheduledSession.getSoldTickets());
        preparedStatement.setLong(8, scheduledSession.getPrice());
        preparedStatement.setInt(9, scheduledSession.getId());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from scheduled_session where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    public List<ScheduledSession> findSessionsByDate(Date date) throws SQLException {
        String sql = "select c.*, s.*, c.id as cinema_id, s.id as scheduled_session_id from scheduled_session s " +
                "inner join cinema c on s.cinema_id = c.id where s.date =?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    public List<ScheduledSession> findOverlappedSessions(int cinemaId, Date date, Time beginTime, Time endTime) throws SQLException {
        String sql = "select c.*, s.*, c.id as cinema_id, s.id as scheduled_session_id from scheduled_session s " +
                "inner join cinema c on s.cinema_id = c.id " +
                "where c.id = ? and s.date =? and " +
                "(s.start_time, s.end_time) overlaps (cast(? as time) , cast( ? as time))";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cinemaId);

        preparedStatement.setDate(2, date);
        preparedStatement.setTime(3, beginTime);
        preparedStatement.setTime(4, endTime);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    public List<ScheduledSession> findSessionsByMovieName(String movieName) throws SQLException {
        String sql = "select c.*, s.*, c.id as cinema_id, s.id as scheduled_session_id  from scheduled_session s " +
                "inner join cinema c on s.cinema_id = c.id " +
                "where s.movie_name =?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, movieName);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    public List<ScheduledSession> findSessionsByCinemaId(int cinemaId) throws SQLException {
        String sql = "select c.*, s.*, c.id as cinema_id, s.id as scheduled_session_id  from scheduled_session s " +
                "inner join cinema c on s.cinema_id = c.id " +
                "where s.cinema_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, cinemaId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    public List<ScheduledSession> findAvailableSessions(Date date) throws SQLException {
        String sql = "select cinema.*, cinema.id as cinema_id, scheduled_session.*, scheduled_session.id as scheduled_session_id from scheduled_session " +
                "inner join cinema on scheduled_session.cinema_id = cinema.id " +
                "where scheduled_session.date >=? " +
                "and scheduled_session.capacity > scheduled_session.sold_tickets";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    protected ScheduledSession mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        Cinema cinema = new Cinema(
                resultSet.getInt("cinema_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getString("status")
        );
        return new ScheduledSession(
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
    }

    @Override
    protected List<ScheduledSession> mapToList(ResultSet resultSet) throws SQLException {
        List<ScheduledSession> result = new ArrayList<>();
        while (resultSet.next()) {
            Cinema cinema = new Cinema(
                    resultSet.getInt("cinema_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getString("address"),
                    resultSet.getString("status")
            );
            result.add(new ScheduledSession(
                    resultSet.getInt("scheduled_session_id"),
                    cinema,
                    resultSet.getString("movie_name"),
                    resultSet.getDate("date"),
                    resultSet.getTime("start_time"),
                    resultSet.getTime("end_time"),
                    resultSet.getShort("capacity"),
                    resultSet.getShort("sold_tickets"),
                    resultSet.getLong("price")
            ));
        }
        return result;
    }
}
