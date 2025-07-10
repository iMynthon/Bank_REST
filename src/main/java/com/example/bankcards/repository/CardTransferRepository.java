package com.example.bankcards.repository;
import com.example.bankcards.model.CardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardTransferRepository extends JpaRepository<CardTransfer, UUID> {
    List<CardTransfer> findByUserId(UUID userId);
}
