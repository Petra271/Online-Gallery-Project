package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="exhibition")
public class Exhibition {
    @Id
    @Column(name ="id", nullable = true)
    private final UUID id;

    @Column(name="beginDateTime", nullable = false)
    private LocalDateTime beginDateTime;

    @Column(name="duration", nullable = false)
    private Duration duration;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="style", nullable = false)
    private Style style;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="provision", nullable = false)
    private double provision;

    @ManyToMany(mappedBy = "exhibitions")
    private Set<Artist> artists;

    @OneToMany(mappedBy = "exhibition", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Collection> collections;

    public Exhibition(){
        id = UUID.randomUUID();
    }

    public Exhibition(LocalDateTime beginDateTime, Duration duration, String name, Style style, String description, double provision) {
        this.id = UUID.randomUUID();
        this.beginDateTime = beginDateTime;
        this.duration = duration;
        this.name = name;
        this.style = style;
        this.description = description;
        this.provision = provision;
        this.artists = new HashSet<>();
        this.collections = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(LocalDateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProvision() {
        return provision;
    }

    public void setProvision(double provision) {
        this.provision = provision;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public void addArtist(Artist artist){
        artists.add(artist);
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }
}
