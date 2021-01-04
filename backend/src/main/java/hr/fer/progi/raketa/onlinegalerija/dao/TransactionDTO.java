package hr.fer.progi.raketa.onlinegalerija.dao;

import java.util.UUID;

public class TransactionDTO {

    private double totalAmount;
    private UUID payerId;
    private UUID receiverId;
    private double provision;

    public double getTotalAmount() {
        return totalAmount;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public double getProvision() {
        return provision;
    }
}
