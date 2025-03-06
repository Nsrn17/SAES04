package iut.dam.sae_s04;

public class User {
    private int id;
    private String username;
    private String email;
    private String name;

    public User(int id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return name; }
}
