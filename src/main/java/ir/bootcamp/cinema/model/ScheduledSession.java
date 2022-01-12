package ir.bootcamp.cinema.model;

import java.sql.Date;
import java.sql.Time;

public class ScheduledSession {
    private int id;
    private Cinema cinema;
    private String movieName;
    private Date date;
    private Time startTime;
    private Time endTime;
    private Short capacity;
    private short soldTickets = 0;
    private long price;

    public ScheduledSession(Cinema cinema, String movieName, Date date, Time startTime, Time endTime, Short capacity, long price) {
        this.cinema = cinema;
        this.movieName = movieName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.price = price;
    }

    public ScheduledSession(int id, Cinema cinema, String movieName, Date date, Time startTime, Time endTime, Short capacity, short soldTickets, long price) {
        this.id = id;
        this.cinema = cinema;
        this.movieName = movieName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.soldTickets = soldTickets;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Short getCapacity() {
        return capacity;
    }

    public void setCapacity(Short capacity) {
        this.capacity = capacity;
    }

    public short getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(short soldTickets) {
        this.soldTickets = soldTickets;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ScheduledSession{" +
                "id=" + id +
                ", cinema=" + cinema.getName() +
                ", movieName='" + movieName + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", capacity=" + capacity +
                ", soldTickets=" + soldTickets +
                ", price=" + price +
                '}';
    }
}
