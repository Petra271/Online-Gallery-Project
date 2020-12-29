package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="collection")
public class Collection {
    @Id
    private final UUID id;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="description", nullable = false)
    private String description;
    @Column(name="style", nullable = false)
    private Style style;
    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Artwork> artworks;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public Collection(){
        this.id = UUID.randomUUID();
    }
    public Collection(String name, String description, Style style, Artist artist) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.style = style;
        this.artworks = new HashSet<Artwork>();
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public boolean addArtwork(Artwork artwork){
        return artworks.add(artwork);
    }
}
