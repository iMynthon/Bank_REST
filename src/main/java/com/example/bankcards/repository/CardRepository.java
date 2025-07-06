package com.example.bankcards.repository;

import com.example.bankcards.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    @EntityGraph(attributePaths = "user")
    Page<Card> findCardByUserId(UUID userId, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Optional<Card> findByNumberCard(String numberCard);

    @Modifying
    @Query(value = "Update cards Set is_active = :isActive WHERE number_card = :numberCard",nativeQuery = true)
    void IsActive(String numberCard,boolean isActive);
}
