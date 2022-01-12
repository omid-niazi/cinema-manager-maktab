package ir.bootcamp.cinema.service;

import ir.bootcamp.cinema.model.Admin;
import ir.bootcamp.cinema.model.Cinema;
import ir.bootcamp.cinema.repositories.AdminRepository;
import ir.bootcamp.cinema.repositories.CinemaRepository;
import ir.bootcamp.cinema.repositories.TransactionRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import static ir.bootcamp.cinema.util.ConsoleMessageType.*;
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

    public boolean login(String username, String password) throws SQLException {
        Admin admin = adminRepository.find(username);
        if (admin == null) {
            print("user not found", error);
            return false;
        }

        if (!admin.getPassword().equals(password)) {
            print("wrong password", error);
            return false;
        }

        loggedInAdmin = admin;
        print("logged in successfully", success);
        return true;
    }

    public void createAdminAccount(String username, String password, String name, String phone, String email) throws SQLException {
        Admin admin = adminRepository.find(username);
        if (admin != null) {
            print("username exists", error);
            return;
        }
        adminRepository.add(new Admin(0, username, password,  name,phone, email));
        print("account created", success);
    }

    public void changeCinemaStatus(int cinemaId, String status) throws SQLException {
        Cinema cinema = cinemaRepository.find(cinemaId);
        if (cinema == null) {
            print("cinema not found", error);
            return;
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
