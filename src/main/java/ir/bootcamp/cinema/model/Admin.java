package ir.bootcamp.cinema.model;

public class Admin extends User {
    public Admin(int id,String username, String password,  String name, String phone, String email) {
        super(id, username, password, name, phone, email);
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
                '}';
    }
}
