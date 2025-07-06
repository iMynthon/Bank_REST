package com.example.bankcards.repository;

import com.example.bankcards.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = "roles")
    @Override
    Optional<User> findById(UUID uuid);
}
