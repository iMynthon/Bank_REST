package com.example.bankcards.repository;

import com.example.bankcards.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {
}
