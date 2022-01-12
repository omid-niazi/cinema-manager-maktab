package ir.bootcamp.cinema.service;

import ir.bootcamp.cinema.model.Customer;
import ir.bootcamp.cinema.model.ScheduledSession;
import ir.bootcamp.cinema.model.Ticket;
import ir.bootcamp.cinema.model.Transaction;
import ir.bootcamp.cinema.repositories.CustomerRepository;
import ir.bootcamp.cinema.repositories.ScheduledSessionRepository;
import ir.bootcamp.cinema.repositories.TicketRepository;
import ir.bootcamp.cinema.repositories.TransactionRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

import static ir.bootcamp.cinema.util.ConsoleUtil.*;
import static ir.bootcamp.cinema.util.ConsoleMessageType.*;

public class CustomerService {

    private CustomerRepository customerRepository;
    private TicketRepository ticketRepository;
    private ScheduledSessionRepository scheduledSessionRepository;
    private TransactionRepository transactionRepository;
    private Customer loggedInCustomer = null;


    public CustomerService(Connection connection) throws SQLException {
        customerRepository = new CustomerRepository(connection);
        ticketRepository = new TicketRepository(connection);
        scheduledSessionRepository = new ScheduledSessionRepository(connection);
        transactionRepository = new TransactionRepository(connection);
    }

    public boolean login(String username, String password) throws SQLException {
        Customer customer = customerRepository.find(username);
        if (customer == null) {
            print("user not fount", error);
            return false;
        }
        if (!customer.getPassword().equals(password)) {
            print("wrong password", error);
            return false;

        }

        loggedInCustomer = customer;
        print("logged in successfully", success);
        return true;
    }

    public void createCustomerAccount(String username, String password, String name, String phone, String email) throws SQLException {
        Customer customer = customerRepository.find(username);
        if (customer != null) {
            print("username already exists", error);
            return;
        }
        customerRepository.add(new Customer(0, username, password, name, phone, email, 0));
        print("account created", info);
    }

    public void buyTicket(int scheduledSessionId) throws SQLException {
        ScheduledSession scheduledSession = scheduledSessionRepository.find(scheduledSessionId);
        if (scheduledSession == null) {
            print("session not found", error);
            return;
        }
        if (scheduledSession.getCapacity() <= scheduledSession.getSoldTickets()) {
            print("all session seats are sold", error);
            return;
        }

        if (loggedInCustomer.getBalance() < scheduledSession.getPrice()) {
            print("your balance is not enough", error);
            return;
        }

        Ticket ticket = new Ticket(scheduledSession, loggedInCustomer);
        int ticketId = ticketRepository.add(ticket);
        scheduledSession.setSoldTickets(((short) (scheduledSession.getSoldTickets() + 1)));
        scheduledSessionRepository.update(scheduledSession);
        loggedInCustomer.setBalance(loggedInCustomer.getBalance() - scheduledSession.getPrice());
        customerRepository.update(loggedInCustomer);
        printTicket(ticketId);
    }

    public void makeTransaction(Date date, Time time, long amount) throws SQLException {
        Transaction transaction = new Transaction(loggedInCustomer, date, time, amount);
        transactionRepository.add(transaction);
        loggedInCustomer.setBalance(loggedInCustomer.getBalance() + amount);
        customerRepository.update(loggedInCustomer);

        print("your account balance is " + loggedInCustomer.getBalance() + " $", info);
    }

    public void showTransactions() throws SQLException {
        print(transactionRepository.findUserTransactions(loggedInCustomer.getId()).toString(), info);
    }

    private void printTicket(int ticketId) throws SQLException {
        Ticket ticket = ticketRepository.find(ticketId);
        String result = "ticket id: " + ticket.getId() + "   **  name : " + ticket.getCustomer().getName() + "   **  movie name: " + ticket.getScheduledSession().getMovieName() + "   **  cinema name: " + ticket.getScheduledSession().getCinema().getName() + "   **  date: " + ticket.getScheduledSession().getDate() + " " + ticket.getScheduledSession().getStartTime() + "   **  seat number: " + ticket.getSeatNumber();
        print(result, info);
    }
}
