package hr.fer.progi.raketa.onlinegalerija.model;

import java.util.UUID;

public class User {
    private final UUID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String paypalMail;
    private String paypalPass;

    public User(String name, String surname, String email,
                String password, String paypalMail, String paypalPass) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.paypalMail = paypalMail;
        this.paypalPass = paypalPass;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPaypalMail() {
        return paypalMail;
    }

    public String getPaypalPass() {
        return paypalPass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPaypalMail(String paypalMail) {
        this.paypalMail = paypalMail;
    }

    public void setPaypalPass(String paypalPass) {
        this.paypalPass = paypalPass;
    }

    public boolean changePassword(String pass){
        if(pass == null)
            throw new IllegalArgumentException("Password cannot be null");
        if(pass.equals(this.password)) {
            setPassword(pass);
            return true;
        }
        return false;
    }
}
