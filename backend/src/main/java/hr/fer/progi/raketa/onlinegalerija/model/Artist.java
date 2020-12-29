package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="artist")
public class Artist extends Visitor {
    @Column(name ="portfolio")
    private byte[] portfolio;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Collection> collections;

    public Artist() {
        super();
    }

    public Artist(String name, String surname, String email,
                  String password, String paypalMail, byte[] portfolio) {
        super(name, surname, email, password, paypalMail);
        this.portfolio = portfolio;
        collections = new ArrayList<>();
    }

    public byte[] getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(byte[] portfolio) {
        this.portfolio = portfolio;
    }

    public List<Collection> getCollections() {
        return collections;
    }


    /** Method to add a new collection,
     * should check if a collection with the same name is already contained
     * within the list of collections and adds if it is not contained
     */
    //public boolean addCollection(Collection col){}

    /**
     * Method to remove a collection with a given name from the list
     */
    //public boolean removeCollection(collection col){}


}
