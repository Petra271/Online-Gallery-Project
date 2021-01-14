package hr.fer.progi.raketa.onlinegalerija.model;
import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="transaction")
public class Transaction {
    @Id
    @Column(name="transaction_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payer_id", nullable = false)
    private Visitor payer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Visitor receiver;

    @Column(name="total_amount", nullable = false)
    private double totalAmount;

    @Column(name="amount_to_artist", nullable = false)
    private double amountToArtist;

    @Column(name="provision", nullable = false)
    private double provision;

    @Column(name="provision_amount", nullable = false)
    private double provisionAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artwork_id", referencedColumnName = "id")
    private Artwork artwork;

    public Transaction() {}

    public Transaction(Visitor payer, Visitor receiver,
                       double totalAmount, double amountToArtist,
                       double provision, double provisionAmount, Artwork artwork) {
        this.id = UUID.randomUUID();
        this.payer = payer;
        this.receiver = receiver;
        this.totalAmount = totalAmount;
        this.amountToArtist = amountToArtist;
        this.provision = provision;
        this.provisionAmount = provisionAmount;
        this.artwork = artwork;
    }

    public UUID getId() {
        return id;
    }

    public Visitor getPayer() {
        return payer;
    }

    public Visitor getReceiver() {
        return receiver;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getAmountToArtist() {
        return amountToArtist;
    }

    public double getProvision() {
        return provision;
    }

    public double getProvisionAmount() {
        return provisionAmount;
    }

    public Artwork getArtwork(){return artwork;}
}
