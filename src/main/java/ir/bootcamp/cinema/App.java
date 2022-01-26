package ir.bootcamp.cinema;

import ir.bootcamp.cinema.exceptions.*;
import ir.bootcamp.cinema.model.UserType;
import ir.bootcamp.cinema.service.AdminService;
import ir.bootcamp.cinema.service.CinemaService;
import ir.bootcamp.cinema.service.CustomerService;
import ir.bootcamp.cinema.util.PropertiesHelper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;

import static ir.bootcamp.cinema.util.ConsoleMessageType.*;
import static ir.bootcamp.cinema.util.ConsoleUtil.*;

public class App {
    private static Scanner scanner;
    private static AdminService adminService;
    private static CustomerService customerService;
    private static CinemaService cinemaService;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);


        Properties properties = null;
        try {
            properties = PropertiesHelper.loadPropertiesFile("database-config.txt");
        } catch (IOException e) {
            print("create database-config.txt in resources folder", error);
            return;
        }

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));) {
            customerService = new CustomerService(connection);
            adminService = new AdminService(connection);
            cinemaService = new CinemaService(connection);
            mainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("password") || e.getMessage().contains("user")) {
                System.out.println("please check the database-config.txt file in resources folder");
                return;
            }
            throw new RuntimeException();
        }
    }

    private static void mainMenu() throws SQLException {
        while (true) {
            System.out.println("1- admin login");
            System.out.println("2- customer login");
            System.out.println("3- customer sign up");
            System.out.println("4- cinema login");
            System.out.println("5- cinema sign up");
            System.out.println("6- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    login(UserType.admin);
                    break;
                case 2:
                    login(UserType.customer);
                    break;
                case 3:
                    try {
                        signUp(UserType.customer);
                    } catch (UserExistsException e) {
                        print(e.getMessage(), error);
                        return;
                    }
                    break;
                case 4:
                    login(UserType.cinema);
                    break;
                case 5:
                    try {
                        signUp(UserType.cinema);
                    } catch (UserExistsException e) {
                        print(e.getMessage(), error);
                        return;
                    }
                    break;
                case 6:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void adminMenu() throws SQLException {
        while (true) {
            System.out.println("1- create another employee account");
            System.out.println("2- show all cinema accounts");
            System.out.println("3- change status of a cinema account");
            System.out.println("4- show all transactions");
            System.out.println("5- show today transactions");
            System.out.println("6- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    try {
                        signUp(UserType.admin);
                    } catch (UserExistsException e) {
                        print(e.getMessage(), error);
                        return;
                    }
                    break;
                case 2:
                    showRegisteredCinemas();
                    break;
                case 3:
                    changeCinemaStatus();
                    break;
                case 4:
                    showAllTransactions();
                    break;
                case 5:
                    showTodayTransactions();
                    break;
                case 6:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void customerMenu() throws SQLException {
        while (true) {
            System.out.println("1- show available sessions");
            System.out.println("2- buy a ticket");
            System.out.println("3- search sessions by date");
            System.out.println("4- search sessions by movie name");
            System.out.println("5- show my transactions");
            System.out.println("6- increase account balance");
            System.out.println("7- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    showAvailableSessions();
                    break;
                case 2:
                    buyTicket();
                    break;
                case 3:
                    searchSessionsByDate();
                    break;
                case 4:
                    searchSessionsByMoviesName();
                    break;
                case 5:
                    showUserTransactions();
                    break;
                case 6:
                    makeTransactions();
                    break;
                case 7:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void cinemaMenu() throws SQLException {
        if (!cinemaService.getCinemaStatus().equals("verified")) {
            print("your menu disabled until your account get verified", error);
            return;
        }
        while (true) {
            System.out.println("1- schedule a session");
            System.out.println("2- cancel a session");
            System.out.println("3- show my income");
            System.out.println("4- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    scheduleSession();
                    break;
                case 2:
                    cancelSession();
                    break;
                case 3:
                    showCinemaIncome();
                    break;
                case 4:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void login(UserType userType) throws SQLException {
        System.out.println("enter your username: ");
        String username = scanner.nextLine();
        System.out.println("enter your password: ");
        String password = scanner.nextLine();
        switch (userType) {
            case admin:
                try {
                    adminService.login(username, password);
                } catch (UserNotFoundException | InvalidPasswordException e) {
                    print(e.getMessage(), error);
                    return;
                }
                adminMenu();
                break;
            case customer:
                try {
                    customerService.login(username, password);
                } catch (UserNotFoundException | InvalidPasswordException e) {
                    print(e.getMessage(), error);
                    return;
                }
                customerMenu();
                break;
            case cinema:
                try {
                    cinemaService.login(username, password);
                } catch (UserNotFoundException | InvalidPasswordException e) {
                    print(e.getMessage(), error);
                    return;
                }
                cinemaMenu();
                break;
        }
    }

    private static void signUp(UserType userType) throws SQLException, UserExistsException {
        System.out.println("enter your username: ");
        String username = scanner.nextLine();
        System.out.println("enter your password: ");
        String password = scanner.nextLine();
        System.out.println("enter your name");
        String name = scanner.nextLine();
        System.out.println("enter your phone");
        String phone = scanner.nextLine();
        System.out.println("enter your email");
        String email = scanner.nextLine();
        switch (userType) {
            case admin:
                adminService.createAdminAccount(username, password, name, phone, email);
                break;
            case customer:
                customerService.createCustomerAccount(username, password, name, phone, email);
                break;
            case cinema:
                System.out.println("enter your address");
                String address = scanner.nextLine();
                cinemaService.createCinemaAccount(username, password, name, phone, email, address);
                break;
        }
    }

    private static void scheduleSession() throws SQLException {
        System.out.print("enter movie name: ");
        String movieName = scanner.nextLine();
        System.out.print("enter date (example: 2021-01-01): ");
        String dateString = scanner.nextLine();
        Date date = null;
        try {
            date = Date.valueOf(dateString);
        } catch (Exception e) {
            print("wrong input", error);
            return;
        }
        System.out.print("enter start time (example : 06:00): ");
        String startTimeString = scanner.nextLine();
        Time startTime = null;
        try {
            startTime = new Time(
                    Integer.parseInt(startTimeString.split(":")[0]),
                    Integer.parseInt(startTimeString.split(":")[1]),
                    0);
        } catch (Exception e) {
            print("wrong input", error);
            return;
        }
        System.out.print("enter start time (example : 06:00):");
        String endTimeString = scanner.nextLine();
        Time endTime = null;
        try {
            endTime = new Time(
                    Integer.parseInt(endTimeString.split(":")[0]),
                    Integer.parseInt(endTimeString.split(":")[1]),
                    0);
        } catch (Exception e) {
            print("wrong input", error);
            return;
        }

        System.out.print("enter the capacity: ");
        short capacity = scanner.nextShort();
        System.out.print("enter the price: ");
        long price = scanner.nextLong();
        try {
            cinemaService.scheduleSession(movieName, date, startTime, endTime, capacity, price);
        } catch (AccessDeniedException | CinemaIsOccupyException e) {
            print(e.getMessage(), error);
        }
    }

    private static void cancelSession() throws SQLException {
        System.out.print("enter session id: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();
        try {
            cinemaService.cancelSession(sessionId);
        } catch (SessionNotFoundException | SessionFinishedException | AccessDeniedException e) {
            print(e.getMessage(), error);
        }
    }

    private static void showRegisteredCinemas() throws SQLException {
        cinemaService.showAll();
    }

    private static void changeCinemaStatus() throws SQLException {
        System.out.print("enter cinema id: ");
        int cinemaId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("enter new status (pending / verified / rejected): ");
        String status = scanner.nextLine();
        if (!status.equals("pending") && !status.equals("verified") && !status.equals("rejected")) {
            print("wrong input", error);
            return;
        }
        try {
            adminService.changeCinemaStatus(cinemaId, status);
        } catch (CinemaNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void showAvailableSessions() throws SQLException {
        cinemaService.showAvailableSessions();
    }

    private static void buyTicket() throws SQLException {
        System.out.print("enter session id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            customerService.buyTicket(id);
        } catch (SessionNotFoundException | SessionIsFullException | NotEnoughMoneyException e) {
            print(e.getMessage(), error);
        }
    }

    private static void searchSessionsByDate() throws SQLException {
        System.out.print("enter date (example: 2021-01-01): ");
        String dateString = scanner.nextLine();
        Date date = null;
        try {
            date = new Date(new SimpleDateFormat("yyy-mm-dd").parse(dateString).getTime());
        } catch (ParseException e) {
            print("wrong input", error);
            return;
        }
        cinemaService.searchSessions(date);
    }

    private static void searchSessionsByMoviesName() throws SQLException {
        System.out.print("enter movie name: ");
        String movieName = scanner.nextLine();
        cinemaService.searchSessions(movieName);
    }

    private static void showAllTransactions() throws SQLException {
        adminService.showAllTransactions();
    }

    private static void showTodayTransactions() throws SQLException {
        adminService.showTransactionsByDate(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    private static void showUserTransactions() throws SQLException {
        customerService.showTransactions();
    }

    private static void makeTransactions() throws SQLException {
        Calendar calendar = Calendar.getInstance();
        System.out.print("enter the amount: ");
        long amount = scanner.nextLong();
        scanner.nextLine();
        customerService.makeTransaction(new Date(calendar.getTimeInMillis()), new Time(calendar.getTimeInMillis()), amount);
    }

    private static void showCinemaIncome() throws SQLException {
        cinemaService.showIncome();
    }

    private static int getCommandKey() {
        int key = -1;
        try {
            key = scanner.nextInt();
        } catch (Exception ignored) {

        }
        return key;
    }
}
