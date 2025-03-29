package iut.dam.sae_s04.models;

public class Admin {
    private int id;
    private String name;
    private String email;
    private String username;
    private String associationName;

    public Admin(int id, String name, String email, String username, String associationName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.associationName = associationName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getAssociation() {
        return associationName;
    }
}