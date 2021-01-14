package hr.fer.progi.raketa.onlinegalerija.repository;

import hr.fer.progi.raketa.onlinegalerija.model.Artist;
import hr.fer.progi.raketa.onlinegalerija.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    List<Collection> findByArtist(Artist artist);
}
