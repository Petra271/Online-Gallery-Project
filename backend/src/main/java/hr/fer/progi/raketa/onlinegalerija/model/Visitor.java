package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id")
    private final UUID id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="pay_pal_mail")
    private String paypalMail;

    @Column(name ="role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Transaction> transactionListAsPayer;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Transaction> transactionListAsReceiver;

    public Visitor(){
        this.id = UUID.randomUUID();
    }
    public Visitor(String name, String surname, String email,
                   String password, String paypalMail) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.paypalMail = paypalMail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public void addTransactionAsPayer(Transaction transaction){
        transactionListAsPayer.add(transaction);
    }

    public void addTransactionAsReceiver(Transaction transaction){
        transactionListAsReceiver.add(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visitor)) return false;
        Visitor visitor = (Visitor) o;
        return id.equals(visitor.id) &&
                email.equals(visitor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
