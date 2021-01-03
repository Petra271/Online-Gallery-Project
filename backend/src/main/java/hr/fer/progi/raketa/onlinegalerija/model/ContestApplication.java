package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="contestApplication")
public class ContestApplication {
    @Id
    @Column(name ="id", nullable = true)
    private final UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "contestApplication", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Collection> collections;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="contest_id")
    private Contest contest;

    public ContestApplication(){
        this.id = UUID.randomUUID();
    }

    public ContestApplication(Artist artist, Contest contest) {
        this.id = UUID.randomUUID();
        this.artist = artist;
        this.collections = new HashSet<>();
        this.contest = contest;
    }

    public UUID getId() {
        return id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
        for(Collection c : this.collections)
            c.setContestApplication(this);
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
