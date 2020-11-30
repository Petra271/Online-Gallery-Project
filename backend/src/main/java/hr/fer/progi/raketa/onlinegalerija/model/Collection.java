//package hr.fer.progi.raketa.onlinegalerija.model;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//@Entity
//@Table(name="category")
//public class Collection {
//    @Id
//    private final UUID id;
//    private String name;
//    private String description;
//    private Style style;
//    @OneToMany
//    private Set<Artwork> artworks;
//
//    public Collection(String name, String description, Style style) {
//        this.id = UUID.randomUUID();
//        this.name = name;
//        this.description = description;
//        this.style = style;
//        this.artworks = new HashSet<Artwork>();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Style getStyle() {
//        return style;
//    }
//
//    public void setStyle(Style style) {
//        this.style = style;
//    }
//
//    public boolean addArtwork(Artwork artwork){
//        return artworks.add(artwork);
//    }
//}
