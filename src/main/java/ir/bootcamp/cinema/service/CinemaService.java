package ir.bootcamp.cinema.service;

import ir.bootcamp.cinema.model.Cinema;
import ir.bootcamp.cinema.model.ScheduledSession;
import ir.bootcamp.cinema.model.Ticket;
import ir.bootcamp.cinema.repositories.CinemaRepository;
import ir.bootcamp.cinema.repositories.CustomerRepository;
import ir.bootcamp.cinema.repositories.ScheduledSessionRepository;
import ir.bootcamp.cinema.repositories.TicketRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import static ir.bootcamp.cinema.util.ConsoleMessageType.*;
import static ir.bootcamp.cinema.util.ConsoleUtil.*;

public class CinemaService {

    private CinemaRepository cinemaRepository;
    private ScheduledSessionRepository scheduledSessionRepository;
    private TicketRepository ticketRepository;
    private Cinema loggedInCinema;

    public CinemaService(Connection connection) throws SQLException {
        cinemaRepository = new CinemaRepository(connection);
        scheduledSessionRepository = new ScheduledSessionRepository(connection);
        ticketRepository = new TicketRepository(connection);
    }

    public boolean login(String username, String password) throws SQLException {
        Cinema cinema = cinemaRepository.find(username);
        if (cinema == null) {
            print("user not found", error);
            return false;
        }
        if (!cinema.getPassword().equals(password)) {
            print("wrong password", error);
            return false;
        }

        loggedInCinema = cinema;
        print("logged in successfully", success);
        return true;
    }

    public void createCinemaAccount(String username, String password, String name, String phone, String email, String address) throws SQLException {
        Cinema cinema = cinemaRepository.find(username);
        if (cinema != null) {
            print("username exists", error);
            return;
        }
        cinemaRepository.add(new Cinema(0, username, password, name, phone, email, address, "pending"));
        print("account created", success);
    }

    public void showAll() throws SQLException {
        List<Cinema> cinemaList = cinemaRepository.findAll();
        if (cinemaList.isEmpty()){
            print("no one registered yet", info);
            return;
        }
        for (Cinema cinema : cinemaList) {
            print(cinema.toString(), info);
        }
    }

    public void scheduleSession(String movieName, Date date, Time startTime, Time endTime, short capacity, long price) throws SQLException {
        if (!loggedInCinema.getStatus().equals("verified")) {
            print("you can't schedule a session until your account verified", error);
            return;
        }

        List<ScheduledSession> scheduled = scheduledSessionRepository.findOverlappedSessions(loggedInCinema.getId(), date, startTime, endTime);
        if (!scheduled.isEmpty()) {
            print("another session is scheduled at this time", error);
            return;
        }

        ScheduledSession scheduledSession = new ScheduledSession(loggedInCinema, movieName, date, startTime, endTime, capacity, price);
        scheduledSessionRepository.add(scheduledSession);
        print("the session scheduled", success);
    }

    public void showAvailableSessions() throws SQLException {
        Date today = new Date(Calendar.getInstance().getTimeInMillis());
        List<ScheduledSession> availableSessions = scheduledSessionRepository.findAvailableSessions(today);
        if (availableSessions.isEmpty()){
            print("not session is available", info);
            return;
        }
        for (ScheduledSession scheduledSession : availableSessions) {
            print(scheduledSession.toString(), info);
        }
    }

    public void searchSessions(Date date) throws SQLException {
        List<ScheduledSession> sessions = scheduledSessionRepository.findSessionsByDate(date);
        print(sessions.toString(), info);
    }

    public void searchSessions(String movieName) throws SQLException {
        List<ScheduledSession> sessions = scheduledSessionRepository.findSessionsByMovieName(movieName);
        print(sessions.toString(), info);
    }

    public String getCinemaStatus() throws SQLException {
        if (loggedInCinema == null)
            return "";

        return loggedInCinema.getStatus();
    }

    public void showIncome() throws SQLException {
        List<ScheduledSession> scheduledSessions = scheduledSessionRepository.findSessionsByCinemaId(loggedInCinema.getId());
        int sum = 0;
        for (ScheduledSession scheduledSession : scheduledSessions) {
            sum += scheduledSession.getPrice() * scheduledSession.getSoldTickets();
        }
        print("your total income is " + sum + " $", info);
    }

    public void cancelSession(int scheduledSessionId) throws SQLException {
        ScheduledSession scheduledSession = scheduledSessionRepository.find(scheduledSessionId);
        if (scheduledSession == null) {
            print("session not found", error);
            return;
        }

        if (scheduledSession.getCinema().getId() != loggedInCinema.getId()) {
            print("this session doesn't belong to your company", error);
            return;
        }

        if (scheduledSession.getDate().getTime() < Calendar.getInstance().getTimeInMillis()){
            print("this session has already been finished", error);
            return;
        }

        scheduledSessionRepository.delete(scheduledSessionId);
        print("session canceled", success);
    }
}
