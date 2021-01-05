package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="collection")
public class Collection {
    @Id
    @Column(name ="id")
    private final UUID id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="style", nullable = false)
    private Style style;

    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)//MERGE
    private Set<Artwork> artworks;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "contest_application_id", nullable = true)
    private ContestApplication contestApplication;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "exhibition_id", nullable = true)
    private Exhibition exhibition;

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

    public UUID getId() {
        return id;
    }

    public Set<Artwork> getArtworks() {
        return artworks;
    }

    public Artist getArtist() {
        return artist;
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

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setArtworks(Set<Artwork> artworks) {
        this.artworks = artworks;

    }

    public ContestApplication getContestApplication() {
        return contestApplication;
    }

    public void setContestApplication(ContestApplication contestApplication) {
        this.contestApplication = contestApplication;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return name.equals(that.name) &&
                artist.equals(that.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist);
    }
}
