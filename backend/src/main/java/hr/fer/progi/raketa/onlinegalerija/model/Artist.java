package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name="visitor")
public class Artist extends Visitor {
    @Column(name ="portfolio")
    private byte[] portfolio;

    //private ArrayList<Collection> collections = new ArrayList<>();

    public Artist() {
        super();
    }

    public Artist(String name, String surname, String email,
                  String password, String paypalMail, byte[] portfolio) {
        super(name, surname, email, password, paypalMail);
        this.portfolio = portfolio;
    }

    public byte[] getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(byte[] portfolio) {
        this.portfolio = portfolio;
    }

    //public ArrayList<Collection> getCollections() {
    //    return collections;
    //}
}

//    /** Method to add a new collection,
//     * should check if a collection with the same name is already contained
//     * within the list of collections and adds if it is not contained
//     */
//    //public boolean addCollection(Collection col){}
//
//    /**
//     * Method to remove a collection with a given name from the list
//     */
//    //public boolean removeCollection(collection col){}
//
//
//}
