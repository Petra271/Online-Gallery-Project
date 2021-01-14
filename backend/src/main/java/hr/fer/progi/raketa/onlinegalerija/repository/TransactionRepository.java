package hr.fer.progi.raketa.onlinegalerija.repository;

import hr.fer.progi.raketa.onlinegalerija.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT * FROM transaction  WHERE payer = :uuid OR receiver = :uuid",
            nativeQuery = true)
    List<Transaction> findTransactionsByUser(@Param("uuid") UUID uuid);
}
