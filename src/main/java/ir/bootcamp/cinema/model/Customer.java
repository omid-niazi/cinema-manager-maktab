package ir.bootcamp.cinema.model;

public class Customer extends User{
    long balance;

    public Customer(int id, String username, String password, String name, String phone, String email, long balance) {
        super(id, username, password, name, phone, email);
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", phone='" + getPhone()+ '\'' +
                ", email='" + getEmail() + '\'' +
                ", balance='" + getBalance() + '\'' +
                '}';
    }
}
