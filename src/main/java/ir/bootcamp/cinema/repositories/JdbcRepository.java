package ir.bootcamp.cinema.repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class JdbcRepository<T> implements Repository<T> {

    protected Connection connection;

    public JdbcRepository(Connection connection) throws SQLException {
        this.connection = connection;
        createTables();
    }

    protected abstract T mapTo(ResultSet resultSet) throws SQLException;

    protected abstract List<T> mapToList(ResultSet resultSet) throws SQLException;

    private void createTables() throws SQLException {
        String adminCreateQuery = "create table if not exists admin" +
                "(" +
                "    id       serial primary key," +
                "    name     varchar(255) not null ," +
                "    username varchar(255) unique not null ," +
                "    password varchar(255) not null ," +
                "    phone    varchar(20) not null ," +
                "    email    varchar(255) not null" +
                "" +
                ");";
        String cinemaCreateQuery = "create table if not exists cinema" +
                "(" +
                "    id       serial primary key," +
                "    name     varchar(255) not null ," +
                "    username varchar(255) unique ," +
                "    password varchar(255) not null ," +
                "    phone    varchar(20) not null ," +
                "    email    varchar(255) not null ," +
                "    address  varchar(255) not null ," +
                "    status   varchar(10)" +
                ");";
        String customerCreateQuery = "create table if not exists customer" +
                "(" +
                "    id       serial primary key," +
                "    name     varchar(255) not null ," +
                "    username varchar(255) unique not null ," +
                "    password varchar(255) not null ," +
                "    phone    varchar(20) not null ," +
                "    email    varchar(255) not null ," +
                "    balance  bigint" +
                ");";
        String sessionsCreateQuery = "create table if not exists scheduled_session" +
                "(" +
                "    id         serial primary key," +
                "    cinema_id  int ," +
                "    movie_name varchar(100) not null ," +
                "    date date not null, " +
                "    start_time time not null ," +
                "    end_time   time not null ," +
                "    capacity   smallint not null ," +
                "    sold_tickets smallint not null ," +
                "    price      int not null ," +
                "    foreign key (cinema_id) references cinema (id)" +
                ");";
        String ticketsCreateQuery = "create table if not exists ticket" +
                "(" +
                "    id          serial primary key," +
                "    scheduled_session_id   int," +
                "    customer_id int," +
                "    seat_number smallserial," +
                "    foreign key (scheduled_session_id) references scheduled_session(id)," +
                "    foreign key (customer_id) references customer (id)" +
                ");";
        String transactionsCreateQuery = "create table if not exists transaction" +
                "(" +
                "    id          serial primary key," +
                "    customer_id   int," +
                "    date date," +
                "    time time," +
                "    amount bigint, " +
                "    foreign key (customer_id) references customer (id)" +
                ");";
        ;
        connection.createStatement().execute(adminCreateQuery);
        connection.createStatement().execute(cinemaCreateQuery);
        connection.createStatement().execute(customerCreateQuery);
        connection.createStatement().execute(sessionsCreateQuery);
        connection.createStatement().execute(ticketsCreateQuery);
        connection.createStatement().execute(transactionsCreateQuery);

    }
}
