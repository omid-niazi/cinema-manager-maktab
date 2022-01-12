package ir.bootcamp.cinema.model;

import java.sql.Date;
import java.sql.Time;

public class Transaction {
    private int id;
    private Customer customer;
    private Date date;
    private Time time;
    private long amount;

    public Transaction(Customer customer, Date date, Time time, long amount) {
        this.customer = customer;
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    public Transaction(int id, Customer customer, Date date, Time time, long amount) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", payed by =" + customer.getUsername() +
                ", date=" + date +
                ", time=" + time +
                ", amount=" + amount +
                '}';
    }
}
