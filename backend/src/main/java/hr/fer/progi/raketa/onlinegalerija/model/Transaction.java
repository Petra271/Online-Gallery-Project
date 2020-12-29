package hr.fer.progi.raketa.onlinegalerija.model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="transaction")
public class Transaction {
    @Id
    private final UUID id;
    private final UUID payerId;
    private final UUID receiverId;
    private final double amount;

    public Transaction(UUID payerId, UUID receiverId, double amount) {
        this.id = UUID.randomUUID();
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
