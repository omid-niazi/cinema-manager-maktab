package ir.bootcamp.cinema.model;

public class Ticket {
    private int id;
    private ScheduledSession scheduledSession;
    private Customer customer;
    private short seatNumber;

    public Ticket(ScheduledSession scheduledSession, Customer customer) {
        this.id = id;
        this.scheduledSession = scheduledSession;
        this.customer = customer;
    }
    public Ticket(int id, ScheduledSession scheduledSession, Customer customer, short seatNumber) {
        this.id = id;
        this.scheduledSession = scheduledSession;
        this.customer = customer;
        this.seatNumber = seatNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ScheduledSession getScheduledSession() {
        return scheduledSession;
    }

    public void setScheduledSession(ScheduledSession scheduledSession) {
        this.scheduledSession = scheduledSession;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public short getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(short seatNumber) {
        this.seatNumber = seatNumber;
    }
}
