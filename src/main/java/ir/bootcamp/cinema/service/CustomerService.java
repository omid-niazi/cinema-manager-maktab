package ir.bootcamp.cinema.service;

import ir.bootcamp.cinema.exceptions.*;
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

import static ir.bootcamp.cinema.util.ConsoleMessageType.info;
import static ir.bootcamp.cinema.util.ConsoleMessageType.success;
import static ir.bootcamp.cinema.util.ConsoleUtil.print;

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

    public void login(String username, String password) throws SQLException, UserNotFoundException, InvalidPasswordException {
        Customer customer = customerRepository.find(username);
        if (customer == null) {
            throw new UserNotFoundException("user not fount");
        }
        if (!customer.getPassword().equals(password)) {
            throw new InvalidPasswordException("wrong password");

        }

        loggedInCustomer = customer;
        print("logged in successfully", success);
    }

    public void createCustomerAccount(String username, String password, String name, String phone, String email) throws SQLException, UserExistsException {
        Customer customer = customerRepository.find(username);
        if (customer != null) {
            throw new UserExistsException("username already exists");
        }
        customerRepository.add(new Customer(0, username, password, name, phone, email, 0));
        print("account created", info);
    }

    public void buyTicket(int scheduledSessionId) throws SQLException, SessionNotFoundException, SessionIsFullException, NotEnoughMoneyException {
        ScheduledSession scheduledSession = scheduledSessionRepository.find(scheduledSessionId);
        if (scheduledSession == null) {
            throw new SessionNotFoundException("session not found");
        }
        if (scheduledSession.getCapacity() <= scheduledSession.getSoldTickets()) {
            throw new SessionIsFullException("all tickets are sold");
        }

        if (loggedInCustomer.getBalance() < scheduledSession.getPrice()) {
            throw new NotEnoughMoneyException("your balance is not enough");
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
