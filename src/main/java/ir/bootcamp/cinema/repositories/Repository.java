package ir.bootcamp.cinema.repositories;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
    int add(T t) throws SQLException;

    T find(int id) throws SQLException;

    List<T> findAll() throws SQLException;

    int update(T t) throws SQLException;

    int delete(int id) throws SQLException;
}
