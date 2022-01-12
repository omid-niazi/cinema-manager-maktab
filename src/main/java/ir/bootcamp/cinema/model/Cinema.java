package ir.bootcamp.cinema.model;

public class Cinema extends User{
    private String address;
    private String status;

    public Cinema(int id,  String username, String password, String name, String phone, String email, String address, String status) {
        super(id, username, password, name, phone, email);
        this.address = address;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", phone='" + getPhone()+ '\'' +
                ", email='" + getEmail() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
