package com.example.bankcards.repository;

import com.example.bankcards.model.Card;
import com.example.bankcards.repository.projections.CardProjections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    @EntityGraph(attributePaths = "user")
    Page<Card> findCardByUserId(UUID userId, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Optional<Card> findByHashCardNumber(String numberCard);

    Optional<CardProjections> findNumberCardByUserIdAndHashCardNumber(UUID userId,String hashCardNumber);

    @Modifying
    @Query(value = "UPDATE cards SET is_active = :isActive WHERE hash_card_number = :hashNumberCard",nativeQuery = true)
    void IsActive(String hashNumberCard,boolean isActive);

    @Modifying
    @Query(value = "UPDATE cards SET score = score + :amount WHERE hash_card_number = :hashNumberCard", nativeQuery = true)
    void addToScore(String hashNumberCard, BigDecimal amount);

    @Modifying
    @Query(value = "UPDATE cards SET score = score - :amount WHERE hash_card_number = :hashNumberCard", nativeQuery = true)
    void subtractFromScore(String hashNumberCard, BigDecimal amount);
}
