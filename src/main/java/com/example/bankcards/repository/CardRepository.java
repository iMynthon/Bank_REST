package com.example.bankcards.repository;

import com.example.bankcards.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    @EntityGraph("user")
    Page<Card> findCardByUserId(UUID userId);
}
