package ir.bootcamp.cinema.service;

import ir.bootcamp.cinema.exceptions.CinemaNotFoundException;
import ir.bootcamp.cinema.exceptions.InvalidPasswordException;
import ir.bootcamp.cinema.exceptions.UserExistsException;
import ir.bootcamp.cinema.exceptions.UserNotFoundException;
import ir.bootcamp.cinema.model.Admin;
import ir.bootcamp.cinema.model.Cinema;
import ir.bootcamp.cinema.repositories.AdminRepository;
import ir.bootcamp.cinema.repositories.CinemaRepository;
import ir.bootcamp.cinema.repositories.TransactionRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import static ir.bootcamp.cinema.util.ConsoleMessageType.info;
import static ir.bootcamp.cinema.util.ConsoleMessageType.success;
import static ir.bootcamp.cinema.util.ConsoleUtil.print;

public class AdminService {
    private CinemaRepository cinemaRepository;
    private AdminRepository adminRepository;
    private TransactionRepository transactionRepository;
    private Admin loggedInAdmin;

    public AdminService(Connection connection) throws SQLException {
        cinemaRepository = new CinemaRepository(connection);
        adminRepository = new AdminRepository(connection);
        transactionRepository = new TransactionRepository(connection);
    }

    public void login(String username, String password) throws SQLException, UserNotFoundException, InvalidPasswordException {
        Admin admin = adminRepository.find(username);
        if (admin == null) {
            throw new UserNotFoundException("user not found");
        }

        if (!admin.getPassword().equals(password)) {
            throw new InvalidPasswordException("wrong password");
        }

        loggedInAdmin = admin;
        print("logged in successfully", success);
    }

    public void createAdminAccount(String username, String password, String name, String phone, String email) throws SQLException, UserExistsException {
        Admin admin = adminRepository.find(username);
        if (admin != null) {
            throw new UserExistsException("username exists");
        }
        adminRepository.add(new Admin(0, username, password, name, phone, email));
        print("account created", success);
    }

    public void changeCinemaStatus(int cinemaId, String status) throws SQLException, CinemaNotFoundException {
        Cinema cinema = cinemaRepository.find(cinemaId);
        if (cinema == null) {
            throw new CinemaNotFoundException("there is no cinema with this id");
        }

        cinema.setStatus(status);
        cinemaRepository.update(cinema);
        print("cinema " + cinemaId + " status change to " + status, success);
    }

    public void showAllTransactions() throws SQLException {
        print(transactionRepository.findAll().toString(), info);
    }

    public void showTransactionsByDate(Date date) throws SQLException {
        print(transactionRepository.find(date).toString(), info);
    }
}
