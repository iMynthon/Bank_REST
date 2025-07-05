package com.example.bankcards.repository;
import com.example.bankcards.model.CardTransfer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CardTransferRepository extends CrudRepository<CardTransfer, UUID> {
}
