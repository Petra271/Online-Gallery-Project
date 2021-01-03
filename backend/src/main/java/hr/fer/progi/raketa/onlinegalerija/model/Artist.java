package hr.fer.progi.raketa.onlinegalerija.model;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="artist")
public class Artist extends Visitor {
    @Column(name ="portfolio")
    @NotNull
    private byte[] portfolio;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Collection> collections;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<ContestApplication> applications;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "artist_exhibition",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "exhibition_id", referencedColumnName = "id"))
    private Set<Exhibition> exhibitions;

    public Artist() {
        super();
    }

    public Artist(String name, String surname, String email,
                  String password, String paypalMail, byte[] portfolio) {
        super(name, surname, email, password, paypalMail);
        this.portfolio = portfolio;
        collections = new HashSet<>();
    }

    public byte[] getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(byte[] portfolio) {
        this.portfolio = portfolio;
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }

    public Collection getCollectionByName(String name){
        for(Collection c : collections)
            if(c.getName().equals(name))
                return c;
        return null;
    }

    public List<ContestApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<ContestApplication> applications) {
        this.applications = applications;
    }

    public Set<Exhibition> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(Set<Exhibition> exhibitions) {
        this.exhibitions = exhibitions;
    }

    public void addExhibition(Exhibition exhibition){
        exhibitions.add(exhibition);
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
