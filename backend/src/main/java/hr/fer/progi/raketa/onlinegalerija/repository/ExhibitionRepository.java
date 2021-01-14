package hr.fer.progi.raketa.onlinegalerija.repository;

import hr.fer.progi.raketa.onlinegalerija.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("exhibition")
public interface ExhibitionRepository extends JpaRepository<Exhibition, UUID> {
    Exhibition findByName(String name);
    boolean existsByName(String name);
}
