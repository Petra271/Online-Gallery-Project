package hr.fer.progi.raketa.onlinegalerija.model;


import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final UUID payerId;
    private final UUID receiverId;
    private final double amount;

    public Transaction(UUID id, UUID payerId, UUID receiverId, double amount) {
        this.id = id;
        this.payerId = payerId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

}
