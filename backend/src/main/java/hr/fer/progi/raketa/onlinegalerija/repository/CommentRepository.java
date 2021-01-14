package hr.fer.progi.raketa.onlinegalerija.repository;

import hr.fer.progi.raketa.onlinegalerija.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findById(UUID id);
}
