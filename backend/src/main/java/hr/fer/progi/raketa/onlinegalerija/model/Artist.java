package hr.fer.progi.raketa.onlinegalerija.model;

import java.util.ArrayList;
import java.util.UUID;

public class Artist extends User {
    private ArrayList<Collection> collections = new ArrayList<>();

    public Artist(UUID id, String name, String surname, String email,
                  String password, String paypalMail, String paypalPass) {
        super(name, surname, email, password, paypalMail, paypalPass);
    }

    public ArrayList<Collection> getCollections() {
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
