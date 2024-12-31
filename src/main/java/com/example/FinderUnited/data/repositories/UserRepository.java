package com.example.FinderUnited.data.repositories;

import com.example.FinderUnited.data.entities.User;
import com.example.FinderUnited.data.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRole(Role role);
}
