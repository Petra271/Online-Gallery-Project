package hr.fer.progi.raketa.onlinegalerija.repository;

import hr.fer.progi.raketa.onlinegalerija.model.ContestApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<ContestApplication, UUID> {

}
