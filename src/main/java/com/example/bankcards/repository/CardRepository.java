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
    Optional<Card> findByNumberCard(String numberCard);

    Optional<CardProjections> findNumberCardByUserIdAndNumberCard(UUID userId,String numberCard);

    @Modifying
    @Query(value = "Update cards Set is_active = :isActive WHERE number_card = :numberCard",nativeQuery = true)
    void IsActive(String numberCard,boolean isActive);

    @Modifying
    @Query(value = "UPDATE cards SET score = score + :amount WHERE number_card = :cardNumber", nativeQuery = true)
    void addToScore(String cardNumber, BigDecimal amount);

    @Modifying
    @Query(value = "UPDATE cards SET score = score - :amount WHERE number_card = :cardNumber", nativeQuery = true)
    void subtractFromScore(String cardNumber, BigDecimal amount);
}
