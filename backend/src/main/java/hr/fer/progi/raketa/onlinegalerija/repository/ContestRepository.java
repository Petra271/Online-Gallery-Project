package hr.fer.progi.raketa.onlinegalerija.repository;

import hr.fer.progi.raketa.onlinegalerija.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContestRepository extends JpaRepository<Contest, UUID> {
    Contest findByWorkingName(String workingName);
    boolean existsByWorkingName(String workingName);
}
